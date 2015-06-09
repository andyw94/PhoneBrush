package shared;
import java.awt.Color;
import java.util.HashMap;

public class PixelColor {
	private static PixelColor pc;
	
	// Color ID's
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    public static final int DARK_GRAY = 2;
    public static final int GRAY = 3;
    public static final int LIGHT_GRAY = 4;
    public static final int BLUE = 5;
    public static final int GREEN = 6;
    public static final int RED = 7;
    public static final int CYAN = 8;
    public static final int MAGENTA = 9;
    public static final int ORANGE = 10;
    public static final int YELLOW = 11; // No clue why this and white are getting mixed up when not using eclipse
    public static final int PINK = 12;
    public static final int PURPLE = 13;
    public static final int VIOLET = 14;
    public static final int[] colors = {BLACK, WHITE, DARK_GRAY, GRAY, LIGHT_GRAY, BLUE, GREEN, RED, 
    	CYAN, MAGENTA, ORANGE, YELLOW, PINK, PURPLE, VIOLET};
    
    // Maps
    private static HashMap<Integer, Color> idToColorMap;
    private static HashMap<Color, Integer> colorToIdMap;
    private static HashMap<Integer, String> idToNameMap;
    private static HashMap<String, Integer> nameToIdMap;
    
    // Initialize Maps
    private PixelColor() {
        idToColorMap = new HashMap<Integer, Color>();
        colorToIdMap = new HashMap<Color, Integer>();
        idToNameMap = new HashMap<Integer, String>();
        nameToIdMap = new HashMap<String, Integer>();
    	addColorsToMaps();
    }
    
    // Adds info to all the maps
    private void addColorsToMaps() {
        addDataToMaps(PixelColor.BLACK, Color.BLACK, "Black");
        addDataToMaps(PixelColor.DARK_GRAY, Color.DARK_GRAY, "Dark Gray");
        addDataToMaps(PixelColor.GRAY, Color.GRAY, "Gray");
        addDataToMaps(PixelColor.LIGHT_GRAY, Color.LIGHT_GRAY, "Light Gray");
        addDataToMaps(PixelColor.BLUE, new Color(0, 109, 255), "Blue");
        addDataToMaps(PixelColor.CYAN, new Color(0, 232, 255), "Cyan");
        addDataToMaps(PixelColor.GREEN, new Color(0, 225, 0), "Green");
        addDataToMaps(PixelColor.MAGENTA, new Color(255, 0 ,220), "Magenta");
        addDataToMaps(PixelColor.ORANGE, new Color(255, 178, 0), "Orange");
        addDataToMaps(PixelColor.PINK, new Color(255, 159, 237), "Pink");
        addDataToMaps(PixelColor.RED, new Color(245, 40, 40), "Red");
        addDataToMaps(PixelColor.WHITE, Color.WHITE, "White");
        addDataToMaps(PixelColor.YELLOW, new Color(255, 235, 0), "Yellow");
        addDataToMaps(PixelColor.PURPLE, new Color(178, 0, 255), "Purple");
        addDataToMaps(PixelColor.VIOLET, new Color(72, 0, 255), "Violet");
    }
    
    // Adds data to one map
    private void addDataToMaps(Integer id, Color color, String name) {
        colorToIdMap.put(color, id);
        idToColorMap.put(id, color);
        idToNameMap.put(id, name);
        nameToIdMap.put(name, id);        
    }
    
    // Gets the instance of the the Pixel Color object
    public static PixelColor getInstance() {
    	if (pc == null)
    		pc = new PixelColor();
    	return pc;
    }
    
    // Returns an array of available colors
    public static String[] getAllColors() {
        String[] namesArray = new String[colors.length];
        for (int i = 0; i < namesArray.length; i++) {
            namesArray[i] = getNameFromID(colors[i]);
        }
        return namesArray;
    }
    
    // Returns a color given an id
    public Color getColorFromID(int id) {
    	return idToColorMap.get(id);
    }
    
    // Returns the name of the color given the id
    public static String getNameFromID(int id) {
        return idToNameMap.get(id);
    }

    // Returns the id given a color name
    public int getIdFromName(String name) {
        return nameToIdMap.get(name);
    }

    // Returns the Id given a color
    public int getIdFromColor(Color color) {
        return colorToIdMap.get(color);
    }
    
    public String getNameFromColor(Color color) {
    	return idToNameMap.get(colorToIdMap.get(color));
    }
     
}
