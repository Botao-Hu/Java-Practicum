/**
 * This class represents a specific location in a 2D map.  Coordinates are
 * integer values.
 **/
public class Location
{
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }
    
    //Judge two Locations are equal or not by comparing their coordinates
    public boolean equals(Object obj) {
    	if (obj instanceof Location) {
    		Location other = (Location) obj;
    		if (xCoord == other.xCoord && 
    			yCoord == other.yCoord) {
    			return true;
    		}
    	}
    	return false;
    }
    
    //Override hashCode() since equals() is overridden
    public int hashCode() {
    	int result = 17;
    	
    	result = 37 * result + xCoord;
    	result = 37 * result + yCoord;
    	
    	return result;
    }
}