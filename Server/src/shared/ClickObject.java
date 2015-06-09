package shared;

import java.io.Serializable;

public class ClickObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static int CURSOR = 0;
	public static int DRAWING = 1;
	public static int CLEAR_BOARD = 2;
	

	private float[] angles;
	private int color;
	private int type;
	private int thickness;
	
	public ClickObject(float[] angles, int color, int type, int thickness) {
		this.angles = angles;
		this.color = color;
		this.type = type;
		this.thickness = thickness;
	}
	
	public float[] getAngles() {
		return angles;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getType() {
		return type;
	}
	
	public int getThickness() {
		return thickness;
	}
}
