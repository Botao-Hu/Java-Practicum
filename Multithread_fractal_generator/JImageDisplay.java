import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * This class creates a graphics widget which allows for display, and also
 * includes painting method for either entire image or a specific pixel.
 * @author BOTAO
 *
 */
public class JImageDisplay extends JComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	
	/**
	 * The constructor takes in an integer width and height, and initialize its 
	 * BufferedImage.
	 * @param width
	 * @param height
	 */
	JImageDisplay(int width, int height) {
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Dimension dim = new Dimension(width, height);
		super.setPreferredSize(dim);
	}
	
	/**
	 * This method is the overridden version. After calling the superclass'
	 * method, this method draws image into the component.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}
	
	/**
	 * This method sets all the pixels into black.
	 */
	public void clearImage() {
		image.setRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, 1);
	}
	
	/**
	 * This method allows to draw certain pixel with certain color.
	 * @param x 
	 * @param y specifies the coordination of the pixel
	 * @param rgbColor the color
	 */
	public void drawPixel(int x, int y, int rgbColor) {
		image.setRGB(x, y, rgbColor);
	}
	
	/**
	 * This method returns the current image as BufferedImage type,
	 * for saving purpose.
	 * @return
	 */
	public BufferedImage getImage() {
		return image;
	}
}
