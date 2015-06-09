package Server;

import java.awt.Color;
import shared.DrawingTool;
import shared.PixelColor;
import shared.Point;

public class DrawingBoardModel {

	// Change these values to connect to the server

	private static final double multiplier = 1.3;
	
	// Should be set with call to server
	private static final int WIDTH = (int) (800 * multiplier); // Panel Width
	private static final int HEIGHT = (int) (600 * multiplier); // Panel Height
	
	
	

	private PixelColor pc; // Pixel Color object
	private DrawingTool currentTool; // current selected drawing tool
	private int currentColor; // current color selected
	private DrawingBoardManager dbm; // Used to manage the board

	// Initializes the drawing board, Pixel Color object, current tool, and
	// current color
	public DrawingBoardModel() {
		dbm = new DrawingBoardManager(WIDTH, HEIGHT);
		pc = PixelColor.getInstance();
		currentColor = PixelColor.BLACK;
		currentTool = new DrawingTool();
	}

	// Returns the color at a given pixel
	public Color getPixel(Point p) {
		return dbm.getPixel(p);
	}
	
	public void restoreOld(int clientNum) {
		dbm.restoreOld(clientNum);
	}

	// Color methods

	// Set the color given a color name
	public void setColor(String color) {
		currentColor = pc.getIdFromName(color);
	}

	// Returns an array of all the color names
	public String[] getAllColorNames() {
		return PixelColor.getAllColors();
	}

	public Color getColorFromName(String name) {
		return pc.getColorFromID(pc.getIdFromName(name));
	}

	public String getNameFromColor(Color color) {
		return pc.getNameFromColor(color);
	}

	// Changing tool method

	// Sets the tool the the given tool name
	public void setTool(String tool) {
		currentTool.setTool(tool);
	}

	// Returns the current tool
	public DrawingTool getTool() {
		return currentTool;
	}

	// Sets the thickness to the given thickness
	public void setThickness(int thickness) {
		currentTool.setThickness(thickness);
	}

	public int getWidth() {
		return WIDTH;
	};

	public int getHeight() {
		return HEIGHT;
	}

	// DBM accessor methods

	public void fillArea(Point p) {
		dbm.fillArea(p, currentColor);
	}

	public void drawLine(Point old, Point current, int clientNum) {
		drawLine(old, current, false, clientNum);
	}

	// Draws a temp line on the board from (oldX, oldY) to (currentX, currentY)
	public void drawLine(Point old, Point current, boolean isTemp, int clientNum) {
		dbm.drawLine(old, current, currentTool.getThickness(), currentColor,
				isTemp, clientNum);
	}

	public void drawBox(Point old, Point current, boolean isTemp, int clientNum) {
		dbm.drawBox(old, current, currentTool.getThickness(), currentColor,
				isTemp, clientNum);
	}

	public void drawCircle(Point old, Point current, boolean isTemp, int clientNum) {
		dbm.drawCircle(old, current, currentTool.getThickness(), currentColor,
				isTemp, clientNum);
	}

	public void erase(Point old, Point current) {
		dbm.erase(old, current, currentTool.getThickness());
	}

	public void clearBoard() {
		dbm.clearBoard();
	}
}
