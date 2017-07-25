package GridGUI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import BattleCommands.BattleCommand;
import Heros.Hero;
import PartyContainers.AI;
import PartyContainers.Player;

public class BattleViewBackup extends JFrame{

	/**
	 * Added so compiler doesn't complain.
	 */
	private static final long serialVersionUID = -570089313612736620L;
	
	// Declare constants for positioning
	// Y for row positioning
	public static final int STATUS_Y = 0;
	public static final int HEALTH_BAR_Y = 1;
	public static final int AP_BAR_Y = 2;
	public static final int MOVE_TEXT_Y = 3;
	public static final int INDICATOR_Y = 4;
	public static final int CHARACTER_Y = 5;
	public static final int ABILITY_Y = 6;
	
	// Position to indicate grid height and grid width in makeGbc function 
	public static final int BARS_POS = 1;
	public static final int INDICATOR_POS = 0;
	public static final int STATUS_POS = 0;
	public static final int CHAR_POS = 2;
	public static final int MOVE_TEXT_POS = 0;
	public static final int ABILITY_POS = 4;
	
	// Label to show the image
	private BackGroundPane pane;

	// Declare buttons to be made
	JButton ability1;
	JButton ability2;
	JButton ability3;
	JButton ability4;
	JButton item;
	
	// Declare health bars / AP bars
	public ArrayList<HealthBar> healthBars = new ArrayList<HealthBar>();
	public ArrayList<AbilityPointsBar> apBars = new ArrayList<AbilityPointsBar>();
	
	// Keep track of character position for targeting
	public HashMap<Integer,Hero> charPos = new HashMap<Integer, Hero>();
	
	// Keep track of current acting character for indicator arrow
	public HashMap<Integer, JLabel> indicatorPos = new HashMap<Integer, JLabel>();
	
	// HashMap for targeted character position
	public HashMap<Integer, JLabel> targetPos = new HashMap<Integer, JLabel>();
	
	// HashMap for healing character position
	public HashMap<Integer, JLabel> healPos = new HashMap<Integer, JLabel>();
	
	// HashMap for notifying which Ability was used
	public HashMap<Integer, AbilityNotifier> abilityNotifiers = new HashMap<Integer, AbilityNotifier>();
	
	// ArrayList for status effect positions 
	public ArrayList<StatusBar> statusBars = new ArrayList<StatusBar>();
	
	// Declare targetOptionPanel
	JPanel targetOptionPanel;
	
	// Declare action command names
    static final private String ABILITY1 = "ability1";
    static final private String ABILITY2 = "ability2";
    static final private String ABILITY3 = "ability3";
    static final private String ABILITY4 = "ability4";
    static final private String ITEM = "Item";
    
	public BattleViewBackup() throws IOException
	{
		// 1. Create the frame
		// Using PaintPane to set background
		
		String filename = "/FFIV_PSP_Final_Dungeon_Background_2.png";
		pane = new BackGroundPane(ImageIO.read(this.getClass().getResource(filename)));
		
		// 2. Choose what happens when the frame closes
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 3. Create components and put them in the frame
		pane.setLayout(new GridBagLayout());
		add(pane);

		// 4. Size the frame
		// Done in BackGroundPane JPanel 
		super.pack();
		
		// 5. Show it
		this.setVisible(true);
	}
	
	
	public void addAbilityButtons()
	{
	    // Create buttons
		this.ability1 = makeButton("Ability1", ABILITY1, "Ability1");
		this.ability2 = makeButton("Ability2", ABILITY2, "Ability2");
		this.ability3 = makeButton("Ability3", ABILITY3, "Ability3");
		this.ability4 = makeButton("Ability4", ABILITY4, "Ability4");
		this.item = makeButton("Item", ITEM, "Item");
		
		// Add buttons to layout
		GridBagConstraints c = makeGbc(0,ABILITY_Y,4);
		pane.add(ability1, c);
	    c = makeGbc(2,ABILITY_Y,ABILITY_POS);
		pane.add(ability2, c);
		c = makeGbc(4,ABILITY_Y,ABILITY_POS);
		pane.add(ability3, c);
		c = makeGbc(6,ABILITY_Y,ABILITY_POS);
		pane.add(ability4, c);
		c = makeGbc(8,ABILITY_Y,ABILITY_POS);
		pane.add(item, c);

		// Add panel for target option pane
		targetOptionPanel = new JPanel();
		targetOptionPanel.setVisible(false);
		c = makeGbc(2,0,0);
		pane.add(targetOptionPanel,c);
	}
	
	/**********************************************************************/
	
	public void addHealthBars(Player player, AI ai)
	{
		// Add beating heart place holder
		// Place holder for the Status Effect bar
		// So the Components don't constantly resize 
		GridBagConstraints placeHolder = makeGbc(4,HEALTH_BAR_Y,BARS_POS);
		JLabel beatingHeart = prepImageBarPlaceHolder("/AnimationHeart.gif");
		pane.add(beatingHeart,placeHolder);
		
		TreeMap<String, Hero> party = player.getParty();
		Collection<Hero> p = party.values();
		int gridCount = 0;
		for(Hero hero : p)
		{
			GridBagConstraints c = makeGbc(gridCount,HEALTH_BAR_Y,BARS_POS);
			HealthBar currentHealthBar = new HealthBar(hero);
			this.healthBars.add(currentHealthBar);
			pane.add(currentHealthBar, c);
			gridCount ++;
		}
		
		TreeMap<String, Hero> aiParty = ai.getParty();
		Collection<Hero> aiP = aiParty.values();
		gridCount = 5;
		for(Hero hero : aiP)
		{
			GridBagConstraints c = makeGbc(gridCount,HEALTH_BAR_Y,BARS_POS);
			HealthBar currentHealthBar = new HealthBar(hero);
			this.healthBars.add(currentHealthBar);
			pane.add(currentHealthBar, c);
			gridCount ++;
		}
		
		super.pack();
	}
	
	/**********************************************************************/
	
	public void addAPBars(Player player, AI ai)
	{
		// Add beating heart place holder
		// Place holder for the Status Effect bar
		// So the Components don't constantly resize 
		GridBagConstraints placeHolder = makeGbc(4,AP_BAR_Y,BARS_POS);
		JLabel mana = prepImageBarPlaceHolder("/Mana.png");
		pane.add(mana,placeHolder);
		
		TreeMap<String, Hero> party = player.getParty();
		Collection<Hero> p = party.values();
		int gridCount = 0;
		for(Hero hero : p)
		{
			GridBagConstraints c = makeGbc(gridCount,AP_BAR_Y,BARS_POS);
			AbilityPointsBar currentAP_Bar = new AbilityPointsBar(hero);
			this.apBars.add(currentAP_Bar);
			pane.add(currentAP_Bar, c);
			gridCount ++;
		}
		
		TreeMap<String, Hero> aiParty = ai.getParty();
		Collection<Hero> aiP = aiParty.values();
		gridCount = 5;
		for(Hero hero : aiP)
		{
			GridBagConstraints c = makeGbc(gridCount,AP_BAR_Y,BARS_POS);
			AbilityPointsBar currentAP_Bar = new AbilityPointsBar(hero);
			this.apBars.add(currentAP_Bar);
			pane.add(currentAP_Bar, c);
			gridCount ++;
		}
		
		super.pack();
	}
	
	/**********************************************************************/
	
	public void updateHealthBars()
	{
		for(HealthBar hb : this.healthBars)
		{
			hb.updateHealthBar();
		}
		super.pack();
	}
	
	public void updateAPbars()
	{
		for(AbilityPointsBar apb : this.apBars)
		{
			apb.updateAPBar();
		}
		super.pack();
	}
	
	/**********************************************************************/
	
	public void updateStatuses()
	{
		for(StatusBar statBar : this.statusBars)
		{
			statBar.updateStatus();
		}
		super.pack();
	}
	
	/**********************************************************************/
	
	public GridBagConstraints makeGbc(int x, int y, int pos) {
	    GridBagConstraints gbc = new GridBagConstraints();
	
	    gbc.gridx = x;
	    gbc.gridy = y;
	    gbc.weightx = 0.5;
	    gbc.weighty = 0.5;
	    
	    if(pos == 0)
	    {
	    	gbc.gridwidth=1;
	    	gbc.gridheight=1;
	    	gbc.fill = GridBagConstraints.BOTH;
	    }
	    if(pos == 1)
	    {
	    	gbc.gridwidth = 1;
	   	    gbc.gridheight = 1;
	   	    gbc.fill = GridBagConstraints.BOTH;
	    }
	    if(pos == 2)
	    {
	    	gbc.gridwidth = 1;
	   	    gbc.gridheight = 1;
	   	    gbc.ipady = 250;  
	   	    gbc.fill = GridBagConstraints.BOTH;
	    }
	    if(pos == 4)
	    {
    	    gbc.gridwidth = 2;
	   	    gbc.gridheight = 1;
	   	    gbc.ipadx = 80;
	   	    gbc.fill = GridBagConstraints.BOTH;
	    	gbc.anchor = GridBagConstraints.PAGE_END; //bottom of space
	    }

	    return gbc;
	}
	
	/**********************************************************************/
	
	public void addCharacters(Player player, AI ai)
	{
		// Place holder for the Indicators so
		// The components don't constantly resize
		GridBagConstraints placeHolder = makeGbc(4,INDICATOR_Y,INDICATOR_POS);
		JLabel swords = prepImageArrow("/swords.png");
		pane.add(swords,placeHolder);
		
		// Place holder for the Status Effect bar
		// So the Components don't constantly resize 
		placeHolder = makeGbc(4,STATUS_Y,STATUS_POS);
		JLabel statusEffectText = new JLabel("Statuses");
		statusEffectText.setHorizontalTextPosition(JLabel.CENTER);
        statusEffectText.setVerticalTextPosition(JLabel.CENTER);
        statusEffectText.setForeground(Color.WHITE);
        statusEffectText.setBackground(Color.BLACK);
        statusEffectText.setOpaque(true);
		pane.add(statusEffectText,placeHolder);
		
		// Place holder for ability used
		placeHolder = makeGbc(4,MOVE_TEXT_Y,MOVE_TEXT_POS);
		JLabel moveText = new JLabel("Ability Used");
		moveText.setHorizontalTextPosition(JLabel.CENTER);
        moveText.setVerticalTextPosition(JLabel.CENTER);
        moveText.setForeground(Color.WHITE);
        moveText.setBackground(Color.BLACK);
        moveText.setOpaque(true);
		pane.add(moveText,placeHolder);
		
		TreeMap<String, Hero> party = player.getParty();
		Collection<Hero> p = party.values();
		int gridX = 0;
		
		for(Hero hero : p)
		{
			// Create hero images
			GridBagConstraints c = makeGbc(gridX,CHARACTER_Y,CHAR_POS);
			JLabel imageLbl = prepImage(hero.getImage());
			imageLbl.setHorizontalAlignment(JLabel.CENTER);
			pane.add(imageLbl, c);
			this.charPos.put(gridX, hero);
			hero.setPosition(gridX);
			
			// Create the turn indicators 
			// NOTE: they go above the characters
			c = makeGbc(gridX,INDICATOR_Y,INDICATOR_POS);
			JLabel indicator = prepImageArrow("/indicator.gif");
			indicator.setVisible(false);
			pane.add(indicator,c);
			indicatorPos.put(gridX, indicator);	
			
			// Create target images
			// NOTE: they go in the same grid position as the character 
			JLabel targeted = prepImageArrow("/attacked.gif");
			targeted.setVisible(false);
			pane.add(targeted,c);
			targetPos.put(gridX, targeted);	
			
			// Create healing images
			// NOTE: they go in the same grid position as the arrow
			JLabel heal = prepImageArrow("/green_plus.png");
			heal.setVisible(false);
			pane.add(heal,c);
			healPos.put(gridX, heal);	
			
			// Create JLabels for status effect text
			c = makeGbc(gridX,STATUS_Y,STATUS_POS);
			StatusBar currentStatusBar = new StatusBar(hero,"None");
			this.statusBars.add(currentStatusBar);
			pane.add(currentStatusBar, c);
		
			// Create JLabels for the ability text
			c = makeGbc(gridX,MOVE_TEXT_Y,MOVE_TEXT_POS);
			AbilityNotifier currentNotifier = new AbilityNotifier(hero);
			this.abilityNotifiers.put(gridX, currentNotifier);
			pane.add(currentNotifier, c);
			
			gridX ++;
		}

		TreeMap<String, Hero> aiParty = ai.getParty();
		Collection<Hero> aiP = aiParty.values();
		gridX = 5;
		for(Hero hero : aiP)
		{
			// Create hero images
			GridBagConstraints c = makeGbc(gridX,CHARACTER_Y,CHAR_POS);
			JLabel imageLbl = prepImage(hero.getImage());
			imageLbl.setHorizontalAlignment(JLabel.CENTER);
			pane.add(imageLbl, c);
			this.charPos.put(gridX, hero);
			hero.setPosition(gridX);
			
			// Create the turn indicators 
			// NOTE: they go above the characters
			c = makeGbc(gridX,INDICATOR_Y,INDICATOR_POS);
			JLabel indicator = prepImageArrow("/indicator.gif");
			indicator.setVisible(false);
			pane.add(indicator,c);
			indicatorPos.put(gridX, indicator);	
		
			// Create target images
			// NOTE: they go in the same grid position as the arrow
			JLabel targeted = prepImageArrow("/attacked.gif");
			targeted.setVisible(false);
			pane.add(targeted,c);
			targetPos.put(gridX, targeted);	
			
			// Create healing images
			// NOTE: they go in the same grid position as the arrow
			JLabel heal = prepImageArrow("/green_plus.png");
			heal.setVisible(false);
			pane.add(heal,c);
			healPos.put(gridX, heal);	
			
			// Create JLabels for status effect text
			c = makeGbc(gridX,STATUS_Y,STATUS_POS);
			StatusBar currentStatusBar = new StatusBar(hero,"None");
			this.statusBars.add(currentStatusBar);
			pane.add(currentStatusBar, c);
			
			// Create JLabels for the ability text
			c = makeGbc(gridX,MOVE_TEXT_Y,MOVE_TEXT_POS);
			AbilityNotifier currentNotifier = new AbilityNotifier(hero);
			this.abilityNotifiers.put(gridX, currentNotifier);
			pane.add(currentNotifier, c);
			
			gridX ++;
		}
		
		super.pack();
	}
	
	/**********************************************************************/
	
	public void displayArrow(Hero actingChar)
	{
		int position = actingChar.getPosition();
		JLabel imageLbl = indicatorPos.get(position);
		imageLbl.setVisible(true);
		revalidate();
		repaint();
	}
	
	public void removeArrow(Hero actingChar)
	{
		int position = actingChar.getPosition();
		JLabel imageLbl = indicatorPos.get(position);
		imageLbl.setVisible(false);
	}
	
	public void displayTarget(Hero targetedChar)
	{
		int position = targetedChar.getPosition();
		JLabel imageLbl = targetPos.get(position);
		imageLbl.setVisible(true);
	}
	
	public void removeTarget(Hero targetedChar)
	{
		int position = targetedChar.getPosition();
		JLabel imageLbl = targetPos.get(position);
		imageLbl.setVisible(false);
	}
	
	public void displayHeal(Hero targetedChar)
	{
		int position = targetedChar.getPosition();
		JLabel imageLbl = healPos.get(position);
		imageLbl.setVisible(true);
	}
	
	public void removeHeal(Hero targetedChar)
	{
		int position = targetedChar.getPosition();
		JLabel imageLbl = healPos.get(position);
		imageLbl.setVisible(false);
	}
	
	public void displayAbilityUsed(Hero actingChar, BattleCommand ability)
	{
		int position = actingChar.getPosition();
		AbilityNotifier abilityLbl = abilityNotifiers.get(position);
		abilityLbl.updateAbilityNotifier(ability);
	}
	
	public void removeAbilityUsed(Hero actingChar)
	{
		int position = actingChar.getPosition();
		AbilityNotifier abilityLbl = abilityNotifiers.get(position);
		abilityLbl.clearAbilityNotifier();
	}
	
	/**********************************************************************/
	
	private JLabel prepImage(String path)
	{
		ImageIcon ii = new ImageIcon(this.getClass().getResource(path));
		Image image = ii.getImage(); // transform it 
		Image newimg = image.getScaledInstance((int) (1024 / 9.0), 250, java.awt.Image.SCALE_DEFAULT); // scale it the smooth way  
		ii = new ImageIcon(newimg);  // transform it back
		JLabel imageLbl = new JLabel(ii);
		return imageLbl;
	}
	
	/**********************************************************************/
	
	private JLabel prepImageArrow(String path)
	{
		ImageIcon ii = new ImageIcon(this.getClass().getResource(path));
		Image image = ii.getImage(); // transform it 
		Image newimg = image.getScaledInstance(50,50, java.awt.Image.SCALE_DEFAULT); // scale it the smooth way  
		ii = new ImageIcon(newimg);  // transform it back
		JLabel imageLbl = new JLabel(ii);
		return imageLbl;
	}
	
	private JLabel prepImageBarPlaceHolder(String path)
	{
		ImageIcon ii = new ImageIcon(this.getClass().getResource(path));
		Image image = ii.getImage(); // transform it 
		Image newimg = image.getScaledInstance(35,35, java.awt.Image.SCALE_DEFAULT); // scale it the smooth way  
		ii = new ImageIcon(newimg);  // transform it back
		JLabel imageLbl = new JLabel(ii);
		return imageLbl;
	}
	
	/**********************************************************************/
	
	protected JButton makeButton(String buttonText, String actionCommand, String toolTipText) 
	{

		//Create and initialize the button.
		JButton button = new JButton(buttonText);
		button.setActionCommand(actionCommand);
		button.setToolTipText(toolTipText);
		return button;
	}
	
	/**********************************************************************/
	
	public BackGroundPane getPane()
	{
		return this.pane;
	}
	
	/**********************************************************************/
	
	public void addAbility1Listener(ActionListener action) {
       this.ability1.addActionListener(action);
    }
	
	public void addAbility2Listener(ActionListener action) {
	       this.ability2.addActionListener(action);
	}
		
	public void addAbility3Listener(ActionListener action) {
	       this.ability3.addActionListener(action);
	}
		
	public void addAbility4Listener(ActionListener action) {
	       this.ability4.addActionListener(action);
	}
	
	public void setAbility1Listener(ActionListener action)
	{
		for(ActionListener act : ability1.getActionListeners())
		{
			this.ability1.removeActionListener(act);
		}
		this.ability1.addActionListener(action);
	}
	
	public void setAbility2Listener(ActionListener action)
	{
		for(ActionListener act : ability2.getActionListeners())
		{
			this.ability2.removeActionListener(act);
		}
		this.ability2.addActionListener(action);
	}
	
	public void setAbility3Listener(ActionListener action)
	{
		for(ActionListener act : ability3.getActionListeners())
		{
			this.ability3.removeActionListener(act);
		}
		this.ability3.addActionListener(action);
	}
	
	public void setAbility4Listener(ActionListener action)
	{
		for(ActionListener act : ability4.getActionListeners())
		{
			this.ability4.removeActionListener(act);
		}
		this.ability4.addActionListener(action);
	}
	
	public void addItemListener(ActionListener action)
	{
		this.item.addActionListener(action);
	}
	
}
