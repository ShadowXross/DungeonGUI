package DungeonMVC;

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

import GridGUI.GameView;
import GridGUI.GameViewPanel;
import HallOfBonesMain.HallOfBonesMain;

/**Dungeon View class to create and maintain the Dungeon View of the game
 * Also interacts with Main Menu and Battle to ensure seamless interaction
 * @author Anton Thomas, Kevin Joseph
 */
@SuppressWarnings("serial")
public class DungeonView extends JPanel implements GameView{

	//===========================================================================================
	public static final String ACTIVITY_MUSIC = "/JonCJG_TheDemonstration.wav"; 
	
	GameViewPanel contentPane;
	public DungeonChar hero;
	public static DungeonChar enemy;
	private static DungeonChar chest;
	static JLabel door;
	protected DungeonChar chestHidden;
	static DungeonChar deadEnemy;
	
	private Image background;
	private final int B_WIDTH = 1400;
    public static final int B_HEIGHT = 920;
    private boolean displayedOnce;
    private DungeonController controller;
    private HallOfBonesMain mainJFrame;
	
    
	/**Create and initialize the dungeon view
	 * 
	 * @param background
	 * @param mainJFrame
	 * @throws IOException
	 */
	public DungeonView(Image background, HallOfBonesMain mainJFrame) throws IOException
	{
		super.setLayout(new GridBagLayout());
		this.background = background;    
		this.mainJFrame = mainJFrame;
		displayedOnce = false;
	}
	
	//===========================================================================================
	
	/**Create animation pane to hold all aspects of the dungeon view(All the JPanels)
	 * @throws IOException
	 */
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
		door.setVisible(true);
		
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
		
		revalidate();
		repaint();
	}
	
	/**Class to create uniform Gridbag layout constraints for the dungeon view
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
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
	
	/**Getter for the enemy sprite
	 * Returns deep copy of enemy object
	 * @return enemyCopy
	 */
	public static DungeonChar getEnemy()
	{
		DungeonChar enemyCopy = enemy;
		return enemyCopy;
	}
	
	/**Getter for the chest sprite
	 * Returns deep copy of the chest object
	 * @return chestCopy
	 */
	public static DungeonChar getChest() {
		
		DungeonChar chestCopy = chest;
		return chestCopy;
	}
	
	/**Setter to control visibility of enemy and replace with dead enemy sprite
	 * @param flag
	 */
	static void hideEnemy(Boolean flag)
	{
		enemy.Sprite.setVisible(!flag);
		deadEnemy.Sprite.setVisible(flag);
	}
	
	/**Setter to control visibility of chest and replace with hidden sprite
	 * @param flag
	 */
	protected void hideChest(Boolean flag)
	{
		chest.Sprite.setVisible(!flag);
		chestHidden.Sprite.setVisible(flag);
	}
	
	/**Getter for door sprite
	 * Returns deep copy of the door object
	 * @return
	 */
	public static JLabel getDoor()
	{
		JLabel doorCopy = door;
		return doorCopy;
	}
	
	/**Setter for the visibility of the door
	 * @param flag
	 */
	public void revealDoor(Boolean flag) {
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
	
    /** Control the location of the Hero Sprite from the controller
     * 
     * @param component
     * @param x
     * @param y
     */
    public void moveComponent(JComponent component, int x, int y)
    {
    	component.setLocation(x, y);
    }
    
    /**Signal for Main frame to switch to battle view
     * 
     */
    public void startBattleView()
    {
    	mainJFrame.switchViews(this, "battle");
    }
    
    /** Signal for Main frame to switch to Main Menu */
    public void switchToMainMenu()
    {
    	try {
			mainJFrame.resetOnLoss();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	mainJFrame.switchViews(this, "mainMenu");
    }
    
    /**Repaint the view once chest has been accessed,
     * ensuring the current positions of Sprites on screen other than chest
     * 
     */
    public void chestOpened()
    {
    	GridBagLayout lay = (GridBagLayout) this.getLayout();
		GridBagConstraints che = lay.getConstraints(chest.Sprite);
		this.hideChest(true);
		this.remove(chest.Sprite);
		this.remove(hero.Sprite);
		GridBagConstraints gbc = this.makeGbc(0,0);
		this.add(chestHidden.Sprite,gbc);
		this.add(hero.Sprite, che);
	
		repaint();
		revalidate();
    }
    public void startView()
    {
    	soundEffectControl.SoundClipPlayer.shutUp();
    	soundEffectControl.SoundClipPlayer.playLoopingSound(ACTIVITY_MUSIC);
    	if(displayedOnce)
		{
    		GridBagLayout lay = (GridBagLayout) mainJFrame.dungeonView.getLayout();
    		GridBagConstraints ene = lay.getConstraints(DungeonView.deadEnemy.Sprite);
    		mainJFrame.dungeonView.remove(enemy.Sprite);
    		mainJFrame.dungeonView.remove(hero.Sprite);
    		GridBagConstraints gbc = makeGbc(0,0);
    		mainJFrame.dungeonView.add(chestHidden.Sprite,gbc);
    		mainJFrame.dungeonView.add(hero.Sprite, ene);
    		mainJFrame.dungeonView.repaint();
    		mainJFrame.dungeonView.revalidate();
    		DungeonView.hideEnemy(true);
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


	
