package GridGUI;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class makeChar {
	
	protected JLabel Sprite;
	
	public makeChar(ImageIcon file)
	{
		Image fileImage = file.getImage();
		Image newFileImg = fileImage.getScaledInstance((int) (1024 / 9.0), 250, java.awt.Image.SCALE_DEFAULT);
		file = new ImageIcon(newFileImg);
		this.Sprite = new JLabel(file);
		
	}
	
	public Rectangle dimen(){
		
		return new Rectangle (Sprite.getX(), Sprite.getY(), Sprite.getWidth(), Sprite.getHeight());
	}
}
