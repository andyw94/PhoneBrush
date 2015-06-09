package Server;

/*
 * Andrew Wiggin
 * andyw94@cs
 * 1229403
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import shared.ClickObject;
import shared.FloatPoint;
import shared.Point;

public class ServerThread extends Thread {

	private Server master;
	private Socket socket;
	private int clientNum;

	// Calibration
	private FloatPoint topLeftPoint;
	private float width;
	private float height;

	public ServerThread(Server master, Socket socket, int clientNum) {
		this.master = master;
		this.socket = socket;
		this.clientNum = clientNum;
		System.out.println("clientNum = " + clientNum);
	}

	public void run() {
		System.out.println("Starting Thread");
		try {
			configureClient();
			while (true) {
				ClickObject co;
				co = getNextInput();
				drawDot(co);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private ClickObject getNextInput() throws IOException, ClassNotFoundException {
		try {
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			return (ClickObject) input.readObject();
		} catch (EOFException e) {
			System.out.println("We are breaking!");
			master.removeClient(clientNum);
		}
		return null;
	}

	private void configureClient() throws ClassNotFoundException, IOException {
		// Do the configuration process
		
		// Two point calibration
		/*
		System.out.println("Get the top left!");
		float[] topLeft = getNextInput().getAngles();
		System.out.println("top left = " + topLeft[0] + ", " + topLeft[1]);
		System.out.println();
		
		System.out.println("Get the bottom right!");
		float [] bottomRight = getNextInput().getAngles();
		System.out.println("bottom right = " +bottomRight[0] + ", " + bottomRight[1]);
		System.out.println();
		
		topLeftPoint = new FloatPoint(topLeft[0],  topLeft[1]);
		height = (float) (topLeft[1] - bottomRight[1]);
		width = (float) (bottomRight[0] - topLeft[0]);
		
		System.out.println("Top Left Point = " + topLeftPoint);
		System.out.println("Width = " + width);
		System.out.println("Height = " + height);
		System.out.println();
		*/
		
		// Three point calibration
		/*
		System.out.println("Get the bottom left!");
		float[] bottomLeft = getNextInput().getAngles();
		System.out.println("bottom left = " + bottomLeft[0] + ", " + bottomLeft[1]);
		System.out.println();
		
		System.out.println("Get the top left!");
		float[] topLeft = getNextInput().getAngles();
		System.out.println("top left = " + topLeft[0] + ", " + topLeft[1]);
		System.out.println();
		
		System.out.println("Get the top right!");
		float [] topRight = getNextInput().getAngles();
		System.out.println("top right = " + topRight[0] + ", " + topRight[1]);
		System.out.println();
		
		topLeftPoint = new FloatPoint(topLeft[0],  topLeft[1]);
		height = (float) (topLeft[1] - bottomLeft[1]);
		width = (float) (topRight[0] - topLeft[0]);
		
		System.out.println("Top Left Point = " + topLeftPoint);
		System.out.println("Width = " + width);
		System.out.println("Height = " + height);
		System.out.println();
		*/
		
		// 4 point calibration
		System.out.println("Get the left!");
		float[] left = getNextInput().getAngles();
		System.out.println("left = " + left[0] + ", " + left[1]);
		System.out.println();
		
		System.out.println("Get the right!");
		float [] right = getNextInput().getAngles();
		System.out.println("right = " + right[0] + ", " + right[1]);
		System.out.println();
		
		System.out.println("Get the top!");
		float [] top = getNextInput().getAngles();
		System.out.println("top = " + top[0] + ", " + top[1]);
		System.out.println();
		
		System.out.println("Get the bottom!");
		float [] bottom = getNextInput().getAngles();
		System.out.println("bottom = " + bottom[0] + ", " + bottom[1]);
		System.out.println();
		
		topLeftPoint = new FloatPoint(left[0],  top[1]);
		height = (float) (top[1] - bottom[1]);
		width = (float) (right[0] - left[0]);
		
		System.out.println("Top Left Point = " + topLeftPoint);
		System.out.println("Width = " + width);
		System.out.println("Height = " + height);
		System.out.println();
	}

	public void drawDot(ClickObject co) {
		
		if (co.getType() == ClickObject.CLEAR_BOARD) {
			master.clearBoard();
			return;
		}
		
		// Get the information about the request
		float[] angles = co.getAngles();
		int color = co.getColor();
		
		FloatPoint p = new FloatPoint(angles[0], angles[1]);
		
		int x = (int) (master.getScreenWidth() * (p.getX() - topLeftPoint.getX()) / width);
		int y = (int) (master.getScreenHeight() * (topLeftPoint.getY() - p.getY()) / height);
		Point drawPoint = new Point(x, y);

		if (co.getType() == ClickObject.DRAWING) {
			master.drawCursor(drawPoint, co.getThickness(), color, clientNum, true);
		} else if (co.getType() == ClickObject.CURSOR) {
			master.drawCursor(drawPoint, co.getThickness(), color, clientNum, false);
		}
	}
}
