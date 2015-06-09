package Server;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import shared.PixelColor;
import shared.Point;

public class DrawingBoardManager {

	private int[][] board;
	private int boardWidth;
	private int boardHeight;
	private PixelColor pc;

	private HashMap<Integer, HashMap<Point, Integer>> clientsOldPixels;
	private HashSet<Point> allPoints;

	public DrawingBoardManager(int width, int height) {
		board = new int[width][height];
		boardWidth = width;
		boardHeight = height;
		pc = PixelColor.getInstance();
		clientsOldPixels = new HashMap<Integer, HashMap<Point, Integer>>();
		allPoints = new HashSet<Point>();
		initBoard();
	}

	// Initialize the board
	private void initBoard() {
		// Should initialize with a packet from the server
		for (int x = 0; x < boardWidth; x++) {
			for (int y = 0; y < boardHeight; y++) {
				setPixel(new Point(x, y), PixelColor.WHITE);
			}
		}
	}

	public int[][] getBoard() {
		return board;
	}

	// color corresponds to a PixelColor static int
	public void setPixel(Point p, int c) {
		setPixel(p, c, false, -1);
	};

	public void setPixelWithThickness(Point p, int thickness, int c) {
		setPixelWithThickness(p, thickness, c, false, -1);
	}

	// Sets a pixel and the surrounding pixels to the current color
	public void setPixelWithThickness(Point p, int thickness, int c,
			boolean isTemp, int clientNum) {
		setPixel(p, c, isTemp, clientNum);
		int r = thickness - 1;
		for (int h = 0; h <= r; h++) {
			int w = (int) Math.round(Math.sqrt(r * r - h * h));
			for (int i = 0; i <= w; i++) {
				setPixel(new Point(p.getX() + i, p.getY() + h), c, isTemp,
						clientNum);
				setPixel(new Point(p.getX() + i, p.getY() - h), c, isTemp,
						clientNum);
				setPixel(new Point(p.getX() - i, p.getY() + h), c, isTemp,
						clientNum);
				setPixel(new Point(p.getX() - i, p.getY() - h), c, isTemp,
						clientNum);
			}
		}
	}

	public void setPixel(Point p, int c, boolean isTemp, int clientNum) {
		int x = p.getX();
		int y = p.getY();
		
		if (withinBounds(p)) {
			HashMap<Point, Integer> oldPixels = clientsOldPixels.get(clientNum);
			if (oldPixels == null) {
				clientsOldPixels.put(clientNum, new HashMap<Point, Integer>());
				oldPixels = clientsOldPixels.get(clientNum);
			}
			if (!isTemp) {
				oldPixels.remove(p);
			} else if (isTemp && !allPoints.contains(p)) {
				allPoints.add(p);
				oldPixels.put(p, board[x][y]);
			}
			board[x][y] = c;
		}
	}

	// Returns the color at a given pixel
	public Color getPixel(Point p) {
		if (!withinBounds(p)) {
			return null;
		}
		return pc.getColorFromID(board[p.getX()][p.getY()]);
	};

	public void erase(Point old, Point current, int thickness) {
		drawLine(old, current, thickness, PixelColor.WHITE, false, -1);
	}

	public void drawLine(Point old, Point current, int thickness, int color,
			boolean isTemp, int clientNum) {
		drawLineHelper(old, current, thickness, color, isTemp, clientNum);
	}

	public void drawBox(Point old, Point current, int thickness, int color,
			boolean isTemp, int clientNum) {

		Point cornerOne = new Point(old.getX(), current.getY());
		Point cornerTwo = new Point(current.getX(), old.getY());
		drawLineHelper(old, cornerOne, thickness, color, isTemp, clientNum);
		drawLineHelper(old, cornerTwo, thickness, color, isTemp, clientNum);
		drawLineHelper(current, cornerOne, thickness, color, isTemp, clientNum);
		drawLineHelper(current, cornerTwo, thickness, color, isTemp, clientNum);
	}

	public void drawCircle(Point old, Point current, int thickness,
			int currentColor, boolean isTemp, int clientNum) {
		Point center = old;

		int changeInX = Math.abs(current.getX() - old.getX());
		int changeInY = Math.abs(current.getY() - old.getY());
		int r = (int) Math.sqrt(changeInX * changeInX + changeInY * changeInY);

		if (r == 0) {
			setPixelWithThickness(center, thickness, currentColor, isTemp,
					clientNum);
		} else {
			for (double theta = 0; theta < (2 * Math.PI); theta = theta
					+ ((double) 1 / r)) {
				int x = (int) (center.getX() + r * Math.cos(theta));
				int y = (int) (center.getY() + r * Math.sin(theta));
				;

				setPixelWithThickness(new Point(x, y), thickness, currentColor,
						isTemp, clientNum);
			}
		}
	}

	// Draws a line on the board from (oldX, oldY) to (currentX, currentY)
	private void drawLineHelper(Point old, Point current, int thickness,
			int color, boolean isTemp, int clientNum) {

		// If any point is invalid, don't draw it
		if (!withinBounds(old) || !withinBounds(current)) {
			return;
		}

		// Draws the line
		double deltaX = current.getX() - old.getX();
		double deltaY = current.getY() - old.getY();
		int x = old.getX();
		int y = old.getY();

		if (deltaX != 0 && deltaY != 0) { // standard case

			Point mid = new Point((old.getX() + current.getX()) / 2,
					(old.getY() + current.getY()) / 2);

			if (deltaX > 0) {
				if (deltaY < 0) { // quadrant 1
					drawLineHelper(old, mid, thickness, color, isTemp,
							clientNum);
					drawLineHelper(mid, current, thickness, color, isTemp,
							clientNum);
				} else { // quadrant 2
					drawLineHelper(old, mid, thickness, color, isTemp,
							clientNum);
					mid.increment();
					drawLineHelper(mid, current, thickness, color, isTemp,
							clientNum);
				}
			} else {
				if (deltaY < 0) { // quadrant 4
					drawLineHelper(current, old, thickness, color, isTemp,
							clientNum);
				} else { // quadrant 3
					drawLineHelper(old, mid, thickness, color, isTemp,
							clientNum);
					drawLineHelper(mid, current, thickness, color, isTemp,
							clientNum);
				}
			}

		} else if (deltaX == 0 && deltaY != 0) { // vertical line
			int startY = Math.min(old.getY(), current.getY());
			int endY = Math.max(old.getY(), current.getY());
			for (int i = startY; i <= endY; i++)
				setPixelWithThickness(new Point(x, i), thickness, color,
						isTemp, clientNum);
		} else if (deltaX != 0 && deltaY == 0) { // horizontal line
			int startX = Math.min(old.getX(), current.getX());
			int endX = Math.max(old.getX(), current.getX());
			for (int i = startX; i <= endX; i++)
				setPixelWithThickness(new Point(i, y), thickness, color,
						isTemp, clientNum);
		} else { // point
			setPixelWithThickness(new Point(x, y), thickness, color, isTemp,
					clientNum);
		}
	}

	public void restoreOld(int clientNum) {
		HashMap<Point, Integer> oldPixels = clientsOldPixels.get(clientNum);
		if (oldPixels == null) {
			return;
		}

		for (Point p : oldPixels.keySet()) {
			board[p.getX()][p.getY()] = oldPixels.get(p);
			if (allPoints.contains(p))
				allPoints.remove(p);
		}
		oldPixels.clear();
	}

	// Fills the area that encloses the point (x, y)
	public void fillArea(Point point, int c) {
		// Determine the color of the source point
		Color source = getPixel(point);
		if (pc.getColorFromID(c) == source) {
			return;
		}
		// Get all the points in the enclosed area and change its value to the
		// current color
		LinkedList<Point> q = new LinkedList<Point>();
		q.push(point);
		while (!q.isEmpty()) {
			Point p = q.pop();
			if (getPixel(p) == source) {
				setPixel(p, c);
				q.push(new Point(p.getX() + 1, p.getY()));
				q.push(new Point(p.getX(), p.getY() + 1));
				q.push(new Point(p.getX() - 1, p.getY()));
				q.push(new Point(p.getX(), p.getY() - 1));
			}
		}
	}

	// Returns true if point is within the board, or it is null
	private boolean withinBounds(Point p) {
		return (p.getX() >= 0 && p.getX() < boardWidth && p.getY() >= 0 && p
				.getY() < boardHeight);
	}

	public void clearBoard() {
		clientsOldPixels = new HashMap<Integer, HashMap<Point, Integer>>();
		allPoints = new HashSet<Point>();
		initBoard();
	}
}
