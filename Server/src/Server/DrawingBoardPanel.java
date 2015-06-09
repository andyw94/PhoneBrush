package Server;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import shared.Point;

public class DrawingBoardPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;    // Needed for Eclipse
    
    private DrawingBoardModel model;

    // Initializes the panel
    DrawingBoardPanel(DrawingBoardModel model) {
        this.model = model;
        setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
    }
    
    // Redraw the board
    public void drawBoard() {
        repaint();
    }
    
    // Draws every pixel
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);
        for (int x = 0; x < model.getWidth(); x++) {
            for (int y = 0; y < model.getHeight(); y++) {
                g2.setColor(model.getPixel(new Point(x, y)));
                g2.drawLine(x, y, x, y);
            }
        }
    }
}
