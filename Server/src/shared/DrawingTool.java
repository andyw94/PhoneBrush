package shared;
import java.util.HashMap;
import java.util.Set;


public class DrawingTool {
    
    // toolnum and current thickness
    private int toolNum;
    private int thickness;
    
    public static final int numTools = 6;
    
    // 0 = pencil, 1 = line, 2 = eraser, 3 = fill
    private static HashMap<String, Integer> tools;

    // initializes a tool object
    public DrawingTool() {
        toolNum = 0;
        thickness = 2;
        
        tools = new HashMap<String, Integer>();
        tools.put("pencil", 0);
        tools.put("line", 1);
        tools.put("eraser", 2);
        tools.put("fill", 3);
        tools.put("box", 4);
        tools.put("circle", 5);
    }
    
    // Returns a set of all tools
    public static Set<String> getAllTools() {
        return tools.keySet();
    }
    
    // Sets the current tool to the given String
    public void setTool(String t) {
        if (tools.containsKey(t))
            setTool(tools.get(t));
    }
    
    // Sets the tool to the given ID
    public void setTool(int t) {
        if (t >= 0 && t < numTools) {
            toolNum = t;   
        }
    }
    
    // Returns the current tool name
    public String getName() {
        for (String toolName : getAllTools()) {
            if (tools.get(toolName) == toolNum) {
                return toolName;
            }
        }
        return null;
    }
    
    // Sets the thickness for the tool
    public void setThickness(int thickness) {
        // Check for invalid input
        this.thickness = thickness;
    }
    
    // Returns the thickness of the tool
    public int getThickness() {
        return thickness;
    }
}
