package GridGUI;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Class to prep images for use by the view. Why make this class: images are made in many packages and not just the view.
 * For example Status and Hero classes have images. 
 * @author Kevin
 *
 */
public class ImagePreparation {

	private static final ImagePreparation instance = new ImagePreparation();
	
	/** 
	 * Private constructor, empty by intention as no fields necessary. 
	 */
	private ImagePreparation()
	{
		
	}
	
	/**
	 * Get the singleton instance of this class. 
	 * @return ImagePreparation instance
	 */
	public static ImagePreparation getInstance()
	{
		return ImagePreparation.instance;
	}
	
	/** 
	 * Prepare an image for use by the view.
	 * @param path path to the image
	 * @param width image width
	 * @param height image height
	 * @return the image
	 */
	public Image prepImage(String path, int width, int height)
	{
		ImageIcon currentIcon = new ImageIcon(this.getClass().getResource(path));
        Image image = currentIcon.getImage(); // transform it 
		Image scaledImage = image.getScaledInstance( width, height, java.awt.Image.SCALE_DEFAULT); // scale it the smooth way  
		return scaledImage;
	}

	public JLabel attachImageIconToJLabel(Image image)
	{
		ImageIcon imageIcon = new ImageIcon(image);
		JLabel imageLbl = new JLabel(imageIcon);
		return imageLbl;
	}
	
	public JLabel attachImageIconToJLabel(String path, int width, int height)
	{
		ImageIcon ii = new ImageIcon(this.getClass().getResource(path));
		Image image = ii.getImage(); // transform it 
		Image newimg = image.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT); // scale it the smooth way  
		ii = new ImageIcon(newimg);  // transform it back
		JLabel imageLbl = new JLabel(ii);
		return imageLbl;
	}
	
}


