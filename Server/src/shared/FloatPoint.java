package shared;

import java.io.Serializable;

public class FloatPoint implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // variables
    private float x;
    private float y;
    
    // initializes point object
    public FloatPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    // Sets x and y to values of x and y in p
    public void set(FloatPoint p) {
        this.x = p.getX();
        this.y = p.getY();
    }
    
    // Sets x and y to given values
    public void setXandY(float x, float y) {
        setX(x);
        setY(y);
    }
    
    // Sets x to the given value
    public void setX(float x) {
        this.x = x;
    }
    
    // Sets Y to the given value
    public void setY(float y) {
        this.y = y;
    }
    
    // Returns X
    public float getX() {
        return x;
    }
    
    // Returns Y
    public float getY() {
        return y;
    }
    
    public static final double EPSILON = 0.001;
    
    @Override
    public boolean equals(Object o) {
    	if (!(o instanceof FloatPoint))
    		return false;
    	FloatPoint fp = (FloatPoint) o;
    	return (Math.abs(this.x - fp.x) < EPSILON) && (Math.abs(this.y - fp.y) < EPSILON);
    }
    
    @Override
    public int hashCode() {
    	return (int) (x * x * 11 + y * y * 13);
    }

    public void increment() {
        ++x;
        ++y;
    }
    
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
