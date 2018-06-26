/**
 * A three dimensional point class
 * @author BOTAO HU
 */
public class Point3d {
	
	//X coordinate of the point
    private double xCoord;
    
    //Y coordinate of the point
    private double yCoord;
    
    //Z coordinate of the point
    private double zCoord;
    
    //Constructor to initialize point to (x, y, z) value.
    public Point3d(double x, double y, double z) {
    	xCoord = x;
    	yCoord = y;
    	zCoord = z;
    }
    
    //No-argument constructor: defaults to a point at the origin.
    public Point3d() {
    	this(0, 0, 0);
    }
    
    //Return the X coordinate of the point.
    public double getX() {
        return xCoord;
    }

    //Return the Y coordinate of the point.
    public double getY() {
        return yCoord;
    }

    //Return the Z coordinate of the point.
    public double getZ() {
        return zCoord;
    }
    
    //Set the X coordinate of the point.
    public void setX(double val) {
        xCoord = val;
    }
    
    //Set the Y coordinate of the point.
    public void setY(double val) {
        yCoord = val;
    }
    
    //Set the Z coordinate of the point.
    public void setZ(double val) {
        zCoord = val;
    }
    
    //Judge two points are equal or not by comparing their coordinates
    public boolean equals(Object obj) {
    	if (obj instanceof Point3d) {
    		Point3d other = (Point3d) obj;
    		if (xCoord == other.getX() && 
    			yCoord == other.getY() &&
    			zCoord == other.getZ()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    //Calculate the double distance between two points
    public double distanceTo(Point3d pt) {
    	double dx = xCoord - pt.getX();
    	double dy = yCoord - pt.getY();
    	double dz = zCoord - pt.getZ();
    	double distance = Math.sqrt(dx * dx + dy * dy + dz *dz);
    	return distance;
    }
}
