package com.example.testapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

import orientationProvider.CalibratedGyroscopeProvider;
import orientationProvider.EulerAngles;
import orientationProvider.GravityCompassProvider;
import orientationProvider.ImprovedOrientationSensor1Provider;
import orientationProvider.ImprovedOrientationSensor2Provider;
import orientationProvider.OrientationProvider;
import orientationProvider.RotationVectorProvider;
import shared.ClickObject;
import android.provider.Settings.Secure;

public class MainActivity extends Activity {

	String HOST = "173.250.159.183";
	
	int PORT = 9000;

	private Socket socket;
	private SensorManager mSensorManager;
	private ObjectOutputStream oos;
	private OrientationProvider op;

	private int thickness = 5;

	// Starts as black - look at PixelColor
	private int colorSelected = 0;

	private boolean drawing = false;
	private boolean is_clear = false;

	int configPoints = 0;
		
	// For calibration with two points
	//String[] corners = {"top left", "bottom right"};
	
	// For calibration with three points
	//String[] corners = {"bottom left", "top left", "top right"};
	
	// For calibration with 4 points
	String[] corners = {"left", "right", "top", "bottom"};


	TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.popup);

		// Different types of sensor data providers
		
		// op = new ImprovedOrientationSensor1Provider((SensorManager)
		// getSystemService(Context.SENSOR_SERVICE));
		// op = new ImprovedOrientationSensor2Provider((SensorManager)
		// getSystemService(Context.SENSOR_SERVICE));
		// op = new GravityCompassProvider((SensorManager)
		// getSystemService(Context.SENSOR_SERVICE));
		op = new CalibratedGyroscopeProvider(
				(SensorManager) getSystemService(Context.SENSOR_SERVICE));
		// op = new RotationVectorProvider((SensorManager)
		// getSystemService(Context.SENSOR_SERVICE));

		op.start();

		connectToServer();
		configPopup();
	}

	// Configure the screen size
	private void configPopup() {		
		// Set instruction text
		text = (TextView) findViewById(R.id.pop_up_text);
		text.setText("Please point your device at the " + corners[configPoints] + " of the drawing board, and press the button");
			
		// To send the info, just call sendClick() for each corner
		final Button config = (Button) findViewById(R.id.config_button);
		config.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						try {
							sendClick();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();

				configPoints++;

				// 2 point calibration
				//if (configPoints == 2) {
				// 3 point calibration
				//if (configPoints == 3) {
				// 4 point calibration
				if (configPoints == 4) {
					// Change view to the main drawing activity
					setContentView(R.layout.activity_main);

					// Create all the drawing buttons
					createDrawButton();
					createClearButton();
					createColorSpinner();
					createThicknessSlider();

					// Start sending
					startSending();
				} else {
					text.setText("Please point your device at the " + corners[configPoints] + " of the drawing board, and press the button");
				}
			}
		});
	}

	// Continuously send sensor data to the server
	private void startSending() {

		new Thread(new Runnable() {
			public void run() {
				try {
					while (true)
						sendClick();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// Reads and sends sensor data to the server
	private void sendClick() throws IOException {

		EulerAngles ea = op.getEulerAngles();
		float[] angles = new float[4];
		angles[0] = ea.getYaw();
		angles[1] = ea.getPitch();
		angles[2] = ea.getRoll();

		oos = new ObjectOutputStream(socket.getOutputStream());

		int type;
		if (is_clear) {
			type = ClickObject.CLEAR_BOARD;
			is_clear = false;
		} else {
			if (drawing) {
				type = ClickObject.DRAWING;
			} else {
				type = ClickObject.CURSOR;
			}
		}

		ClickObject co = new ClickObject(angles, colorSelected, type, thickness);
		oos.writeObject(co);
	}

	// Connect to the server from the predefined host and port
	private void connectToServer() {
		new Thread(new Runnable() {
			public void run() {
				try {
					Log.d("app", " entered");

					System.out.print("********* Connecting to socket");
					socket = new Socket(HOST, PORT);
					System.out.println("- we connected *************");
					Log.d("app", " connected");

				} catch (Exception e) {
					System.out.println("- There was an exception!");
					e.printStackTrace();
				}
			}
		}).start();
	}

	
	/*
	 * Buttons
	 */

	private void createThicknessSlider() {
		final SeekBar thicknessSlider = (SeekBar) findViewById(R.id.thickness_slider);
		thicknessSlider
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						progress++;
						TextView currentThickness = (TextView) findViewById(R.id.currentThickness);
						currentThickness.setText("Thickness: " + progress);
						thickness = progress;
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void createClearButton() {
		final Button clear = (Button) findViewById(R.id.clear_button);
		
		clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						is_clear = true;
						
						// need to stop drawing otherwise causes server to crash
						drawing = false;
					}
				}).start();
			}
		});
	}

	private void createDrawButton() {
		final Button draw = (Button) findViewById(R.id.draw_button);

		draw.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				drawing = !drawing;
			}

		});

		// Hold to draw instead of one press to turn on/off drawing
		/*
		draw.setOnTouchListener(new View.OnTouchListener() {
		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					drawing = true;
				} else if (event.getAction() == MotionEvent.ACTION_UP){
					drawing = false;
				}
				return true;
			}
			
		});
		*/

	}

	private void createColorSpinner() {
		final Spinner spinner = (Spinner) findViewById(R.id.color_chooser);
		spinner.setAdapter(new ColorSpinnerAdapter(this));
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				colorSelected = pos;
			}

			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}
}
