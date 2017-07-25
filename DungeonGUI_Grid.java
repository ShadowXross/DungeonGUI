package GridGUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DungeonGUI_Grid extends JFrame {

	//===========================================================================================
	
	BackGroundPane contentPane;
	static makeChar hero;
	static makeChar enemy;
	static JLabel door;
	private final static GameState gamestate = new GameState(false);
	private static boolean state = true;
	
	public DungeonGUI_Grid() throws IOException
	{
		// 1. Construct JFrame
		super();
		String filename = "/Dungeon_Background_49.png";
		contentPane = new BackGroundPane(ImageIO.read(this.getClass().getResource(filename)));
		
		// 2. Choose what happens when the frame closes
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 3. Create components and put them in the frame
		contentPane.setLayout(new GridBagLayout());

		// 3. Create components and put them in the frame
		createAnimationPane();

		// 4. Size the frame
//		this.setSize(600, 600);
//		this.setLocationRelativeTo( null );
		this.pack();
		
		// 5. Show it
		this.setVisible(state);
		gamestate.start();

	}
	
	
	//===========================================================================================
	
	private void createAnimationPane() throws IOException
	{

		// Load background
	
		ImageIcon heroImg = new ImageIcon(this.getClass().getResource("/paladin.gif"));
		hero = new makeChar(heroImg);
		ImageIcon enemyImg = new ImageIcon(this.getClass().getResource("/boss_skele.gif"));
		enemy = new makeChar(enemyImg);
		
		//requires seperate scaling
		ImageIcon doorImg = new ImageIcon(this.getClass().getResource("/Door.png"));
		Image doorImage = doorImg.getImage();
		Image newFileImg = doorImage.getScaledInstance((int) (1024 / 3), 1000, java.awt.Image.SCALE_DEFAULT);
		doorImg = new ImageIcon(newFileImg);
		door = new JLabel(doorImg);
		
		
		// Add animation to JLabel 
		KeyboardAnimation animation = new KeyboardAnimation(hero.Sprite, 24);
		animation.addAction("LEFT", -3,  0);
		animation.addAction("RIGHT", 3,  0);

		animation.addAction("control LEFT", -5,  0);
		animation.addAction("V",  5,  5);
		
		// Add characters to layout
		GridBagConstraints c = makeGbc(0,0);
		contentPane.add(hero.Sprite, c);
	    c = makeGbc(4,0);
		contentPane.add(enemy.Sprite, c);
		c = makeGbc(5,0);
		contentPane.add(door, c);
		
		
		// Add to JPanel with back ground
		this.getContentPane().add(contentPane);
//			this.getContentPane().add(enemyLbl, BorderLayout.EAST);
		this.repaint();
		
	}
	
	public GridBagConstraints makeGbc(int x, int y)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
	    gbc.gridx = x;
	    gbc.gridy = y;
	    gbc.weightx = 0.5;
	    gbc.weighty = 0.5;
	    
    	gbc.gridwidth=1;
    	gbc.gridheight=1;
    	gbc.fill = GridBagConstraints.BOTH;
    	
    	return gbc;
	}
	
	protected static makeChar getEnemy()
	{
		makeChar enemyCopy = enemy;
		return enemyCopy;
	}
	protected static void hideEnemy(Boolean flag)
	{
		enemy.Sprite.setVisible(!flag);
	}
	
	protected static JLabel getDoor()
	{
		JLabel doorCopy = door;
		return doorCopy;
	}
	
	protected static void pauseState() 
	{
		try {
			gamestate.pauseThread();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static void continueThread()
	{
		gamestate.resumeThread();
		enemy.Sprite.setVisible(false);
	}
	
	protected static void disappear()
	{
		state = false;
	}
			
	//=================================================================================================
	
	public static void main(String[] args) throws IOException
	{
		new DungeonGUI_Grid();
	}
	
}
