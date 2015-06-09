package shared;

import java.io.Serializable;

public class Point implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // variables
    private int x;
    private int y;
    
    // initializes point object
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Sets x and y to values of x and y in p
    public void set(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }
    
    // Sets x and y to given values
    public void setXandY(int x, int y) {
        setX(x);
        setY(y);
    }
    
    // Sets x to the given value
    public void setX(int x) {
        this.x = x;
    }
    
    // Sets Y to the given value
    public void setY(int y) {
        this.y = y;
    }
    
    // Returns X
    public int getX() {
        return x;
    }
    
    // Returns Y
    public int getY() {
        return y;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (!(o instanceof Point))
    		return false;
    	Point p = (Point) o;
    	return this.x == p.x && this.y == p.y;
    }
    
    @Override
    public int hashCode() {
    	return x * x * 11 + y * y * 13;
    }

    public void increment() {
        ++x;
        ++y;
    }
    
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
