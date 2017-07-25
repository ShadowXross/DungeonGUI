package GridGUI;

import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import BattleCommands.BattleCommand;
import Heros.Hero;
import PartyContainers.*;
import RPG_Exceptions.MaximumStatException;


public class BattleController{
	private BattleView view;
	private BattleModel model;
	private int[][] layoutDim;
	private int attackAnimationStartY;
	private int characterColumnWidth;
	
	/**********************************************************************/
	
    public BattleController(BattleView view){
    	this.view = view;
    }
    
    /**********************************************************************/
	
    public void setModel(BattleModel model)
    {
    	this.model = model;
    	getAnimationConstants();
    }
    
    /**********************************************************************/
    
    private void getAnimationConstants()
    {
    	// Get the layout
    	// Done in battle as the layout is now made
        GridBagLayout gridBagLayout = (GridBagLayout) view.getPane().getLayout();
        
        // Get the layout dimensions
    	// Returns an array of arrays. The first array is the COLUMN widths. The second is the row WIDTHS.
    	layoutDim = gridBagLayout.getLayoutDimensions();
     		
   		// Need to add up to the row -1 distance from the top of the view
    	attackAnimationStartY = 0;
    	
    	for(int rowCount = 0; rowCount <= BattleViewBackup.CHARACTER_Y-1; rowCount++)
    	{
    		attackAnimationStartY += layoutDim[1][rowCount];
    	}
    	
    	// Get the character column width, it's a constant so only want to get it once
    	characterColumnWidth = layoutDim[0][0];
    }
	
    /**********************************************************************/
    
    public void sendInitializeSignal(Player human, AI ai)
    {
    	// Send the signal to the view to add everything 
    	view.initializeInitialDisplay(human, ai);
    }
    
    /**********************************************************************/

    public void sendUpdateEndOfTurnSignal(Hero actingHero)
    {
    	view.updateHealthBars();
		view.updateAbilityPointBars();
		view.updateStatuses();
		view.removeIndicator(actingHero);
		// Game didn't end properly
		// Perhaps the AbilityListener should be in the model
		// Seems to do quite a bit of data management 
		model.checkBattleStatus();
    }
    
    /**********************************************************************/

    public void signalRemoveAbilityUsed(Hero actingHero)
    {
    	view.removeAbilityUsed(actingHero);
    }
    
    /**********************************************************************/

    public void signalDisplayArrow(Hero actingHero)
    {
    	view.displayIndicator(actingHero);
    }
    
    public void signalDisplayAbilityUsed(Hero actingHero, BattleCommand ability)
    {
    	view.displayAbilityUsed(actingHero, ability);
    }
    
    public void signalRemoveArrow(Hero actingHero)
    {
    	view.removeIndicator(actingHero);
    }
    
    public void signalUpdateStatuses()
    {
    	view.updateStatuses();
    }
    
    public void sendNotEnoughAbilityPointsSignal()
    {
    	JOptionPane.showMessageDialog(view,"Not Enough AP.", "Not Enough AP.", JOptionPane.ERROR_MESSAGE);
    }
    
    public void sendMaxStatExceptionSignal()
    {
    	JOptionPane.showMessageDialog(view,"Stat at Maximum Value.", "Stat at Maximum Value.", JOptionPane.ERROR_MESSAGE);
    }

    public void signalItemPopUp(JPopupMenu itemMenu)
    {
    	JButton itemButton = view.getButtons().get(4); 
	    itemButton.setComponentPopupMenu(itemMenu);
	    itemMenu.show(itemButton, itemButton.getWidth()/2, itemButton.getHeight()/2);
    }
    
    /**********************************************************************/
    
    public void animateBattleCommand(Hero targetedChar, Image animationImage, boolean down)
    {
    	int startX = getAnimationStartingX(targetedChar);
    	if(down)
    	{
    		view.getPane().startDownAnimation(startX, attackAnimationStartY,animationImage);
    	}
    	else
    	{
    		view.getPane().startUpAnimation(startX, BackGroundPane.B_HEIGHT,animationImage);
    	}
    }
    
    public void animateHeal(Hero targetedChar)
    {
    	int startX = getAnimationStartingX(targetedChar);
		view.getPane().startHealAnimation(startX, BackGroundPane.B_HEIGHT);
    }
    
    public int getAnimationStartingX(Hero targetedChar)
    {
    	// Determine where to start the animation
    	int targetPosition = targetedChar.getPosition();
    	int startX;
    
    	// If targeting an AI must include the offset of the middle column
    	// Since the first 4 columns are of equal width can just multiply the 
    	// Column width by 4 then add the offset
    	if(targetPosition > 4)
    	{
    		startX = 4 * characterColumnWidth + layoutDim[0][4] +  characterColumnWidth*(targetPosition - 5);
    	}
    	else
    	{
    		startX = targetPosition * characterColumnWidth;
    	}
    	return startX;
    }
    
    /**********************************************************************/
    
    public void addActionListeners(Hero addHero)
    {
    	if(model.isFirstGo())
    	{
    		for(int i = 0; i < 4; i ++)
    		{
    			view.addAbilityListener(i, model.new AbilityListener(addHero));
    		}
            view.addAbilityListener(4, model.new ItemListener());
    	}
    	else
    	{
    		for(int i = 0; i < 4; i ++)
    		{
    			view.setAbilityListener(i, model.new AbilityListener(addHero));
    		}
    	}
    }
    
    public void AI_Turn_Error()
    {
    	JOptionPane.showMessageDialog(view,"Wait your turn!", "Not Your Turn", JOptionPane.ERROR_MESSAGE);
    }
    
    /**********************************************************************/
    
    public void addActionListenersAI_Turn()
    {
    	for(int i = 0; i < 4; i ++)
		{
    		view.setAbilityListener(i, model.new AI_Turn_Listener());
		}
    }
    
    /**********************************************************************/
    
    public void changeButtonNamesAndCmds(Hero hero)
    {
    	String[] abNames = hero.getAbilitiesNames();
    	ArrayList<JButton> buttons = view.getButtons();
    	for(int i = 0; i < 4; i ++)
		{
			buttons.get(i).setText(abNames[i]);
			buttons.get(i).setActionCommand(abNames[i]);
		}
    }
    
    /**********************************************************************/
    
    public Hero signalShowTargetOptions()
    {
    	Object[] options = {"1","2","3","4","Cancel"};   
    	int target = JOptionPane.showOptionDialog(view,"Pick a Target", "Target Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    	if(target == 4)
    	{
    		return null;
    	}
    	target = target + 5;
    	System.out.println(target);
    	Hero charTarget = view.charPos.get(target);
    	// If target is dead, send error message 
    	if(charTarget.getHealth() <= 0)
    	{
    		JOptionPane.showMessageDialog(view,"Please select a target that isn't dead.", "Invalid Target Error", JOptionPane.ERROR_MESSAGE);
    		return null;
    	}
    	return charTarget;
    }
    
    /**********************************************************************/
    
    public void theEnd ()
    {
        view.setVisible(false);
        view.dispose();
        System.out.println("The battle is over");
    }
    
    /**********************************************************************/
}
