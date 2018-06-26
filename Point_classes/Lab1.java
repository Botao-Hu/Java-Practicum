import java.io.*;

/**
 * This class does the following functions:
 * reads in the coordinates of three points from the user;
 * computes the area of triangle bounded by these points;
 * some brief I/O and error handling.
 * @author BOTAO HU
 */
public class Lab1 {
	
	/**
     * Obtains one double-precision floating point number from
     * standard input.
     *
     * @return return the inputted double, or 0 on error.
     */
	public static double getDouble() {

        // There's potential for the input operation to "fail"; hard with a
        // keyboard, though!
        try {
            // Set up a reader tied to standard input.
            BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));

            // Read in a whole line of text.
            String s = br.readLine();

            // Conversion is more likely to fail, of course, if there's a typo.
            try {
                double d = Double.parseDouble(s);

                //Return the inputted double.
                return d;
            }
            catch (NumberFormatException e) {
                // Bail with a 0.  (Simple solution for now.)
                return 0.0;
            }
        }
        catch (IOException e) {
            // This should never happen with the keyboard, but we'll
            // conform to the other exception case and return 0 here,
            // too.
            return 0.0;
        }
    }
	
	/**
	 * Reads in the coordinates of one point
	 * and creates a Point3d object with the coordinates.
	 * @return returns the created Point3d object.
	 */
	public static Point3d getPoint() {
		
		//prompt for input
		System.out.println("Please type in the coordinates of the point, separate by Enter:");
		
		//read in coordinates
		double xCoord = getDouble();
		double yCoord = getDouble();
		double zCoord = getDouble();
		
		//create object
		Point3d pt = new Point3d(xCoord, yCoord, zCoord);
		
		return pt;
	}
	
	/**
	 * Reads in three inputted points
	 * and compute the area of triangle bounded by them
	 * using Heron's formula.
	 * @return returns the double area.
	 */
	public static double computeArea(Point3d pt1, Point3d pt2, Point3d pt3) {
		
		//compute length of three sides
		double a = pt1.distanceTo(pt2);
		double b = pt1.distanceTo(pt3);
		double c = pt2.distanceTo(pt3);
		
		//apply Heron's formula
		double s = (a + b + c) / 2;
		double area = Math.sqrt(s * (s - a) * (s - b) * (s - c));
		
		return area;
	}
	
	/**
	 * In main, we firstly read in three points with coordinates,
	 * and check if some points are equal.
	 * If so, we generate an error message and do not call computeArea.
	 * If not, we call computeArea and output the area of triangle.
	 */
	public static void main(String[] args) {
		
		//get three points
		Point3d pt1 = getPoint();
		Point3d pt2 = getPoint();
		Point3d pt3 = getPoint();
		
		//If some points are equal, this is an error.
		if (pt1.equals(pt2) || pt1.equals(pt3) || pt2.equals(pt3)) {
			System.out.println("INVALID INPUT: Some points are equal.");
		}
		else {
			//For a legal input, we calculate the size of triangle.
			double area = computeArea(pt1, pt2, pt3);
			System.out.println("The area of the input triangle is:" + area);
		}
	}

}
