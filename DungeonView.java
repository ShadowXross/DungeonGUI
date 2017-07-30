package GridGUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import HallOfBonesMain.GameView;
import HallOfBonesMain.HallOfBonesMain;

@SuppressWarnings("serial")
public class DungeonView extends JPanel implements GameView{

	//===========================================================================================
	
	GameViewPanel contentPane;
	public static DungeonChar hero;
	public static DungeonChar enemy;
	protected static DungeonChar chest;
	static JLabel door;
	private static JLabel doorHidden;
	protected static DungeonChar chestHidden;
	static DungeonChar deadEnemy;
	
	private Image background;
	private final int B_WIDTH = 1400;
    public static final int B_HEIGHT = 920;
    private boolean displayedOnce;
    private DungeonController controller;
    private HallOfBonesMain mainJFrame;
	
    
	public DungeonView(Image background, HallOfBonesMain mainJFrame) throws IOException
	{
		super.setLayout(new GridBagLayout());
		this.background = background;    
		this.mainJFrame = mainJFrame;
	}
	
	//===========================================================================================
	
	public void createAnimationPane() throws IOException
	{

		// Load Hero Images
		ImageIcon heroImg = new ImageIcon(this.getClass().getResource("/paladin.gif"));
		hero = new DungeonChar(heroImg);
		
		ImageIcon enemyImg = new ImageIcon(this.getClass().getResource("/boss_skele.gif"));
		enemy = new DungeonChar(enemyImg);
		ImageIcon deadImg = new ImageIcon(this.getClass().getResource("/pileBones.png"));
		deadEnemy = new DungeonChar(deadImg);
		deadEnemy.Sprite.setVisible(false);
		ImageIcon chestImg = new ImageIcon(this.getClass().getResource("/Treasure_Chest.png"));
		chest = new DungeonChar(chestImg);
		ImageIcon chestHiddenImg = new ImageIcon(this.getClass().getResource("/Treasure_Chest_hidden.png"));
		chestHidden = new DungeonChar(chestHiddenImg);
		chestHidden.Sprite.setVisible(false);
		
		// Door requires separate scaling
		ImageIcon doorImg = new ImageIcon(this.getClass().getResource("/Door.png"));
		Image doorImage = doorImg.getImage();
		Image newDoorImg = doorImage.getScaledInstance((int) (1024 / 3), 1000, java.awt.Image.SCALE_DEFAULT);
		doorImg = new ImageIcon(newDoorImg);
		door = new JLabel(doorImg);
		door.setVisible(false);
		
		ImageIcon doorHiddenImg = new ImageIcon(this.getClass().getResource("/Door_hidden.png"));
		Image doorHiddenImage = doorHiddenImg.getImage();
		Image newDoorHiddenImg = doorHiddenImage.getScaledInstance((int) (1024 / 3), 1000, java.awt.Image.SCALE_DEFAULT);
		doorHiddenImg = new ImageIcon(newDoorHiddenImg);
		doorHidden = new JLabel(doorHiddenImg);
		
		// Add animation to JLabel 
		DungeonModel animation = new DungeonModel(hero.Sprite, 24, controller);
		animation.addAction("LEFT", -5,  0);
		animation.addAction("RIGHT", 5,  0);

		// Add characters to layout
		GridBagConstraints c = makeGbc(0,0);
		this.add(hero.Sprite, c);
		 c = makeGbc(1,0);
		 this.add(chest.Sprite, c);
		 this.add(chestHidden.Sprite, c);
	    c = makeGbc(2,0);
		this.add(enemy.Sprite, c);
		this.add(deadEnemy.Sprite, c);
		c = makeGbc(3,0);
		this.add(door, c);
		this.add(doorHidden, c);
		
		revalidate();
		repaint();
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
	
	public static DungeonChar getEnemy()
	{
		DungeonChar enemyCopy = enemy;
		return enemyCopy;
	}
	
	public static DungeonChar getChest() {
		
		DungeonChar chestCopy = chest;
		return chestCopy;
	}
	
	static void hideEnemy(Boolean flag)
	{
		enemy.Sprite.setVisible(!flag);
		deadEnemy.Sprite.setVisible(flag);
	}
	
	protected static void hideChest(Boolean flag)
	{
		chest.Sprite.setVisible(!flag);
		chestHidden.Sprite.setVisible(flag);
	}
	
	public static JLabel getDoor()
	{
		JLabel doorCopy = door;
		return doorCopy;
	}
	
	public static void revealDoor(Boolean flag) {
		doorHidden.setVisible(!flag);
		door.setVisible(flag);
		
	}
	
	 @Override
    public Dimension getPreferredSize() {
    	return background == null ? new Dimension(0, 0) : new Dimension(B_WIDTH, B_HEIGHT);   
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (background != null) {                
            g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
        }
   
    }
	
    public void moveComponent(JComponent component, int x, int y)
    {
    	component.setLocation(x, y);
    }
    
    public void switchView()
    {
    	mainJFrame.switchViews(this, "battle");
    }
    
    public void startView()
    {
    	if(displayedOnce)
		{
        	//DungeonView.hideChest(true);
    		GridBagLayout lay = (GridBagLayout) mainJFrame.dungeonView.getLayout();
    		GridBagConstraints ene = lay.getConstraints(DungeonView.deadEnemy.Sprite);
    		mainJFrame.dungeonView.remove(DungeonView.enemy.Sprite);
    		mainJFrame.dungeonView.remove(DungeonView.hero.Sprite);
    		GridBagConstraints gbc = makeGbc(0,0);
    		mainJFrame.dungeonView.add(DungeonView.chestHidden.Sprite,gbc);
    		mainJFrame.dungeonView.add(DungeonView.hero.Sprite, ene);
    		mainJFrame.dungeonView.repaint();
    		mainJFrame.dungeonView.revalidate();
    		DungeonView.hideEnemy(true);
    		DungeonView.revealDoor(true);
    		mainJFrame.dungeonView.setComponentZOrder(door, 1);
    		mainJFrame.dungeonView.setComponentZOrder(deadEnemy.Sprite, 1);
    		mainJFrame.dungeonView.setComponentZOrder(chestHidden.Sprite, 2);
    		mainJFrame.dungeonView.setComponentZOrder(hero.Sprite, 0);
    		
			repaint();
			revalidate();
		}
		else
		{
			try {
				this.controller = new DungeonController(this);
				this.createAnimationPane();
				displayedOnce = true;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	
    }

	
}


	
