import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * This class shows a GUI and draws the fractal, with click-magnify and
 * reset functionalities. What's more, it now supports three different
 * fractals display, and can save the current image to disk.
 * Another update is that this class now uses multi-thread strategy to
 * draw fractals so you can see it runs faster. Also, it prevents all
 * interactions when it is drawing the background.
 * @author BOTAO
 *
 */
public class FractalExplorer {
	
	private int displaySize;
	private FractalGenerator generator;
	private Rectangle2D.Double range;
	private JImageDisplay display;
	private JComboBox<FractalGenerator> box;
	private JFrame frame;
	private JButton reset;
	private JButton save;
	private int rowsRemain;
	
	/**
	 * The constructor initializes the display screen with displaySize
	 * (the screen is a square). It also initializes a fractal generator
	 * object and the range in complex space.
	 * @param displaySize
	 */
	public FractalExplorer(int displaySize) {
		this.displaySize = displaySize;
		this.range = new Rectangle2D.Double(0, 0, 0, 0);
		generator = new Mandelbrot();
		generator.getInitialRange(this.range);
	}
	
	/**
	 * This method initialize the GUI with display screen at the center,
	 * a list of three fractals to choose from on the upper side, and save
	 * and reset button on the lower side. It also registers event
	 * handler to read in button and mouse events.
	 */
	public void createAndShowGUI() {
		frame = new JFrame("Master Piece");
		
		//save and reset button initialize
		reset = new JButton("Reset Display");
		save = new JButton("Save Image");
		reset.setActionCommand("reset");
		save.setActionCommand("save");
		JPanel panelDown = new JPanel();
		panelDown.add(save);
		panelDown.add(reset);
		
		//combo-box initialize
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Fractal: ");
		box = new JComboBox<FractalGenerator>();
		box.addItem(new Mandelbrot());
		box.addItem(new Tricorn());
		box.addItem(new BurningShip());
		panel.add(label);
		panel.add(box);
		
		//register action listeners
		display = new JImageDisplay(displaySize, displaySize);
		ActionHandler handler = new ActionHandler();
		MouseListener listener = new MouseListener();
		reset.addActionListener(handler);
		display.addMouseListener(listener);
		box.addActionListener(handler);
		save.addActionListener(handler);
		
		//put them altogether in the frame
		frame.add(display, BorderLayout.CENTER);
		frame.add(panelDown, BorderLayout.SOUTH);
		frame.add(panel, BorderLayout.NORTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	/**
	 * This method now controls the drawing of fractal pixels.
	 * It first disables all UIs, and for every row of image, it creates
	 * a FractalWorker object and let that object handle the calculation
	 * and painting staff. Also, it set the rowsRemain to the initial
	 * value before each painting process.
	 * 
	 */
	private void drawFractal() {
		this.enableUI(false);
		rowsRemain = displaySize;
		for (int y = 0; y < displaySize; y++) {
			FractalWorker worker = new FractalWorker(y);
			worker.execute();
		}
	}
	
	/**
	 * This is an inner class to handle button actions.
	 * For action happens in the combo-box:
	 * initializes a new generator in the selected fractal type;
	 * redraws the fractal image.
	 * For save button clicked:
	 * opens up a file chooser for user to save current png image of fractal;
	 * if IO Exception occurs, opens up an error window.
	 * For reset button clicked:
	 * initializes a new generator in the same fractal type;
	 * redraws the fractal image.
	 */
	public class ActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//combo-box action
			if (e.getSource() == box) {
				generator = (FractalGenerator) box.getSelectedItem();
				generator.getInitialRange(range);
				drawFractal();
			}
			//save action
			else if (e.getActionCommand() == "save") {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
				chooser.setFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);
				//opens up a file choosing window
				int status = chooser.showOpenDialog(frame);
				//saves image to disk and handles IO Exception
				if (status == JFileChooser.APPROVE_OPTION) {
					try {
						ImageIO.write(display.getImage(), "png", chooser.getSelectedFile());
					}
					catch (IOException error) {
						JOptionPane.showMessageDialog(frame, error.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			//reset action
			else if (e.getActionCommand() == "reset") {
				generator.getInitialRange(range);
				drawFractal();
			}
		} 
	}
	
	/**
	 * This is an inner class to handle mouse actions.
	 * If a click happens on the display screen, this class will magnify
	 * the image with the center at the location where the click takes place.
	 * 
	 */
	public class MouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (rowsRemain != 0) return; 
			//System.out.println(e.getX());
			//System.out.println(e.getY());
			double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());
			double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, displaySize, e.getY());
			generator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
			drawFractal();
		} 
	}
	
	/**
	 * This method enables and disables UIs (buttons and combo-box) 
	 * according to the input
	 * @param val.
	 */
	void enableUI(boolean val) {
		reset.setEnabled(val);
		save.setEnabled(val);
		box.setEnabled(val);
	}
	
	/**
	 * This class is a subclass of SwingWorker class. It's responsibility is 
	 * to perform multi-thread calculation and drawing of fractal images. It
	 * forces all operations done in background, and thus improves the speed
	 * of drawing and efficiency of the program. Also, it keeps track of 
	 * rowsRemain variable, if all rows are successfully drawn, it enables
	 * UI interaction.
	 * @author ALBERT
	 *
	 */
	private class FractalWorker extends SwingWorker<Object, Object> {
		int y;
		int[] rgbColor;
		
		public FractalWorker(int y) {
			this.y = y;
		}
		
		/**
		 * This method calculate each row of the fractal, and store the corresponding
		 * rgb result into an int array.
		 */
		protected Object doInBackground() {
			rgbColor = new int[displaySize];
			for (int x = 0; x < displaySize; x++) {
				double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, x);
				double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, displaySize, y);
				int numIters = generator.numIterations(xCoord, yCoord);
				if (numIters == -1) rgbColor[x] = 0;
				else {
					float hue = 0.7f + (float) numIters / 200f;
					rgbColor[x] = Color.HSBtoRGB(hue, 1f, 1f);
				}
			}
			return null;
		}
		
		/**
		 * This method draws a row of image according to the rgb array calculated by
		 * doInBackground. After all rows are done, it enables UI interaction.
		 */
		protected void done() {
			for (int x = 0; x < displaySize; x++) {
				display.drawPixel(x, y, rgbColor[x]);
			}
			display.repaint(0, 0, y, displaySize, 1);
			rowsRemain--;
			if (rowsRemain == 0) enableUI(true);
		}
	}
	
	/**
	 * Run the main function to open the frame and enjoy!
	 * (This time it's quite fast.)
	 */
	public static void main(String[] args) {
		FractalExplorer explorer = new FractalExplorer(800);
		explorer.createAndShowGUI();
		explorer.drawFractal();
	}
}
