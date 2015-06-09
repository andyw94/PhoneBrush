package Server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import shared.PixelColor;
import shared.Point;

public class Server {

	private DrawingBoardModel model;
	private DrawingBoardPanel view;

	private static List<Integer> clients;
	private ServerSocket serverSocket;
	private int clientCounter = 0;
	private HashMap<Integer, Point> clientsLastPoint = new HashMap<Integer, Point>();

	private static final int INITIAL_PORT = 9000;
	private static final int WIDTH = 800; // Panel Width
	private static final int HEIGHT = 600; // Panel Height

	public int getScreenWidth() {
		return WIDTH;
	}
	
	public int getScreenHeight() {
		return HEIGHT;
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Server server = new Server(INITIAL_PORT, WIDTH, HEIGHT);
	}

	public Server(int initialPort, int width, int height) {
		serverSocket = setUpServer(initialPort);
		if (serverSocket == null) {
			System.out.println("Server initialization failed");
			return;
		}
		clients = new LinkedList<Integer>();
		System.out.println("Starting Server on port " + initialPort);
		listenForConnections();
	}
	
	private ServerSocket setUpServer(int initialPort) {
		try {
			ServerSocket serverSocket = new ServerSocket(INITIAL_PORT);

			// Initializes model, view and controller
			int minWidth = 960;
			int minHeight = 600;
			model = new DrawingBoardModel();
			view = new DrawingBoardPanel(model);

			// Creates the frame
			JFrame frame = new JFrame("Drawing");
			frame.setLayout(new BorderLayout());
			frame.add(view, BorderLayout.WEST);

			frame.setMinimumSize(new Dimension(minWidth, minHeight));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);

			return serverSocket;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void listenForConnections() {
		while (true) {

			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}

			clientCounter++;
			addClient(clientCounter);
			System.out.println("Handling init request");

			new ServerThread(this, socket, clientCounter).start();
		}
	}

	/*
	 * Client list access methods
	 */
	
	public void addClient(int clientNum) {
		clients.add(clientNum);
		clientsLastPoint.put(clientNum, null);
		System.out.println("Adding client to the list, we now have "
				+ clients.size() + " active clients");
	}

	public void removeClient(int clientNum) {
		for (Integer client : clients) {
			if (client == clientNum) {
				clients.remove(client);
				System.out.println("Removed client!");
				System.out.println("There are " + clients.size()
						+ " clients left");
			}
		}
	}
			
	public synchronized void drawCursor(Point p, int thickness, int color, int clientNum, boolean draw) {		
		// Set up
		model.restoreOld(clientNum);
		model.setColor(PixelColor.getNameFromID(color));
		Point lastPoint = clientsLastPoint.get(clientNum);
		if (draw) {
			// System.out.println("drawing - client num = " + clientNum);
			model.setThickness(thickness);	
			model.drawLine(lastPoint, p, false, clientNum);
			view.drawBoard();
		} else {
			int temp_thickness = 2;
			model.setThickness(temp_thickness);			
			// Draw the cross
			model.drawLine(new Point(p.getX() - (2 * temp_thickness), p.getY()), new Point(p.getX() + (2 * temp_thickness), p.getY()), true, clientNum);
			model.drawLine(new Point(p.getX(), p.getY() - (2 * temp_thickness)), new Point(p.getX(), p.getY() + (2 * temp_thickness)), true, clientNum);
			
			// Update the board
			view.drawBoard();
		} 
		
		clientsLastPoint.put(clientNum, p);
	}

	public void clearBoard() {
		model.clearBoard();
		clientsLastPoint.clear();
	}
}
