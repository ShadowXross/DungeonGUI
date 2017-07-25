package GridGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import BattleCommands.Ability;
import BattleCommands.BattleCommand;
import BattleCommands.Item;
import Heros.Hero;
import PartyContainers.AI;
import PartyContainers.AiBattleReturnType;
import PartyContainers.Player;
import RPG_Exceptions.MaximumStatException;

public class BattleModel {
	private BattleController controller; 
	private Player human;
	private AI AI;
	private Queue<Hero> gameQueue = new LinkedList<Hero>();
	private final BattleState gameState = new BattleState(false);
	private Hero currentHero; 
	private Hero targetedChar;
	private boolean gameOver = false;
	private boolean firstGo = true;
	public static int DISPLAY_SLEEP_TIME = 750;

	/*********************************************************************************************************/
	/*********************************************************************************************************/
	/*********************************************************************************************************/
  
	public BattleModel(BattleController controller, Player human, AI AI){
        this.controller = controller;
        this.human = human;
        this.AI = AI;
        AI.setController(controller);
        // Begin battle 
        // Tell controller to send signal to view to add all components
        controller.sendInitializeSignal(human, AI);
        // Fill the initial battle queue
        this.fillInitialQueue();
        // Start the battle
        this.battle();
    }
	
	/*********************************************************************************************************/
    
	/**
	 * Starts the game state thread. 
	 */
    private void battle()
    {
    	gameState.start();
    }
    
    /*********************************************************************************************************/
    
    /**
     * Fills the initial battle queue when the gameState starts. 
     */
    private void fillInitialQueue()
    {
    	// Add human party to array list to be sorted
    	TreeMap<String, Hero> party = human.getParty();
		Collection<Hero> p = party.values();
		ArrayList<Hero> sortMe = new ArrayList<Hero>(p);

		// Add AI party to array list to be sorted
		TreeMap<String, Hero> aiParty = AI.getParty();
		Collection<Hero> aiP = aiParty.values();
		sortMe.addAll(aiP);
		
		// Sort the complete list
		Collections.sort(sortMe);
		// Since it sorts in ascending order 
		// i.e lower speed first 
		// Need to reverse the order 
		Collections.reverse(sortMe);
		
		// Add to queue in sorted order 
		for(Hero hero : sortMe)
		{
			gameQueue.add(hero);
		}
    }
	
	/*********************************************************************************************************/
	
    /**
     * Checks the battle status to determine if the battle is over or not. If all characters in either party are dead the battle is over. 
     */
	public void checkBattleStatus()
	{
		// Will remove characters from party when they die
		// So if either party is empty, end battle 
		int deadCountHuman = partyDead(human.getParty());
		if(deadCountHuman == 4)
		{
			gameOver = true;
			controller.theEnd();
		}
		int deadCountAI = partyDead(AI.getParty());
		if(deadCountAI == 4)
		{
			gameOver = true;
			controller.theEnd();
		}
	}
	
	/*********************************************************************************************************/
	
	/**
	 * Computes the dead count in a party. If the dead count is 4 then the battle is over.
	 * @param party the party whose dead you wish to count
	 * @return deadCount an integer of how many characters have 0 or less health 
	 */
	private int partyDead(TreeMap<String,Hero> party)
	{
		Collection<Hero> p = party.values();
		int deadCount = 0;
		for(Hero currentClass : p)
		{
			if(currentClass.getHealth() <= 0)
			{
				deadCount ++;
			}
		}
		return deadCount;
	}
	
	/*********************************************************************************************************/
	
	/**
	 * Updates statuses, and all display bars on the view on successful turn completion. Also readds the character whose turn has 
	 * just finished to the queue. 
	 * @param actingHero the hero whose bars and statuses must be updated. 
	 */
    private void updateOnSuccessfulEvent(Hero actingHero)
    {
    	controller.sendUpdateEndOfTurnSignal(actingHero);
		gameQueue.add(actingHero);
		checkBattleStatus(); 	
    }
    
    /*********************************************************************************************************/
	
	//=========================================================================================================
    //=========================================== GETTERS AND SETTERS =========================================
	/**
	 * Gets the human player from the model
	 * @return the human player
	 */
	public Player getHuman() {
		return human;
	}

	/**
	 * Gets the AI player from the model
	 * @return the AI player 
	 */
	public AI getAI() {
		return AI;
	}

	/**
	 * Gets the current acting hero from the model
	 * @return the current acting hero 
	 */
	public Hero getCurrentHero() {
		return currentHero;
	}

	/**
	 * Get the targeted hero from the model
	 * @return the targetedChar
	 */
	public Hero getTargetedChar() {
		return targetedChar;
	}
	
	/**
	 * Get the game state.
	 * @return the gameState
	 */
	public BattleState getGameState() {
		return gameState;
	}

	/**
	 * Is it the first go?
	 * @return the whether or no it's the first turn 
	 */
	public boolean isFirstGo() {
		return firstGo;
	}
	
	/**
	 * Set the targeted character in the model
	 * @param targetedChar the targetedChar to set
	 */
	public void setTargetedChar(Hero targetedChar) {
		this.targetedChar = targetedChar;
	}
	
	public boolean battleCheck() {
		return this.gameOver;
	}
	
	/*********************************************************************************************************/
	/*********************************************************************************************************/
	/*********************************************************************************************************/
  
    public class BattleState extends Thread{
    	
   	  private final Object GUI_INITIALIZATION_MONITOR = new Object();
      private boolean pauseThreadFlag = false;
    
   	  public BattleState(boolean pauseThreadFlag) { 
   		  super("BattleState");
   		  this.pauseThreadFlag = pauseThreadFlag; 
   	  }
   	  
   	  public boolean getpauseThreadFlag() { 
   		  return pauseThreadFlag; 
   	  }
   	  
   	  public void setPauseThreadFlag(boolean pauseThreadFlag) { 
   		  this.pauseThreadFlag = pauseThreadFlag; 
   	  }
   	  
      @Override
      public void run() {
    	  while(!gameOver)
    	  {
    		  // To display the targeted enemy
    		  if(targetedChar != null)
    		  {
    			  try {
					BattleState.sleep(DISPLAY_SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    			  
    		  }
    		  else if(!firstGo)
    		  {
    			  try {
  					BattleState.sleep(DISPLAY_SLEEP_TIME);
  				} catch (InterruptedException e) {
  					e.printStackTrace();
  				}
    			  controller.signalRemoveAbilityUsed(currentHero);
    		  }
    		  
    		  currentHero = gameQueue.poll();
    		  if(currentHero.getHealth() > 0)
    		  {
    			  // Display indicator arrow
        		  controller.signalDisplayArrow(currentHero);
	        	  if(currentHero.getControlledBy().equals("AI"))
	        	  {
	        		  try {
						BattleState.sleep(DISPLAY_SLEEP_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	        		  System.out.println("****************************************************");
	        		  System.out.println("Current AI health: " + currentHero.getHealth());
	        		  
	        		  AiBattleReturnType AI_Move; 
					  AI_Move = AI.aiTurn(currentHero, human);
					  
	        		  // Extract target and ability from AiBatt, leReturnType
	        		  Hero AI_target = AI_Move.getTarget();
	        		  BattleCommand AI_ability = AI_Move.getCmd();
	        		  
	        		  if(AI_target != null)
	        		  {
	        			  controller.signalDisplayAbilityUsed(currentHero, AI_ability);
	        		  }
	        		  else
	        		  {
	        			  if(AI_ability != null)
	        			  {
	        				  controller.signalDisplayAbilityUsed(currentHero, AI_ability);
	        			  }
	        			  controller.signalRemoveArrow(currentHero);
	        			  // TODO add animations to items, then good
//	        			  controller.animateHeal(currentHero);
	        		  }
	        		  try {
							BattleState.sleep(DISPLAY_SLEEP_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	        		  controller.sendUpdateEndOfTurnSignal(currentHero);
	        		  if(AI_target != null)
	        		  {
	        			  controller.signalRemoveAbilityUsed(currentHero);
	        		  }
	        		  checkBattleStatus();
	        		  gameQueue.add(currentHero);
	        	  }
	        	  else
	        	  {
	        		  // Only add action listeners ONCE
	        		  // If add more than once get many repeated actions 
        			  controller.addActionListeners(currentHero);
        			  controller.changeButtonNamesAndCmds(currentHero);
        			  firstGo = false;

	        		  System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	        		  System.out.println("Current hero health: " + currentHero.getHealth());
	        		  try {
	    				pauseThread();
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			}
	        		  System.out.println("Please input a command");
	        		  checkForPause();  
	        	  }
    		 }
    	  }
       	}

        private void checkForPause() {
            synchronized (GUI_INITIALIZATION_MONITOR) {
                while (pauseThreadFlag) {
                    try {
                        GUI_INITIALIZATION_MONITOR.wait();
                    } catch (Exception e) {}
                }
            }
        }

        public void pauseThread() throws InterruptedException {
            pauseThreadFlag = true;
        }

        public void resumeThread() {
            synchronized(GUI_INITIALIZATION_MONITOR) {
                pauseThreadFlag = false;
                GUI_INITIALIZATION_MONITOR.notify();
            }
        }

   }
    
    /*********************************************************************************************************/
    /*********************************************************************************************************/
    /*********************************************************************************************************/
  
    public class AbilityListener implements ActionListener {
    
    	private Hero watchingHero;
    	
    	public AbilityListener(Hero hero)
    	{
    		this.watchingHero = hero;
    	}
    	
        public void actionPerformed(ActionEvent e) {
        	// Retrieve Command
    		String cmd = e.getActionCommand();
    		
    		// Add thread that stops until a secondary command is input
    		// either provide a popup with numbers 1 to 4 or scroll through enemies with keypad
    		// Handle each button.
    		
    		Ability abilityToUse = Ability.getAbility(watchingHero, cmd);
    		// If not enough AP can't go, display error message and thread will loop around again 
    		// Until a successful action is made
    		try{
    			// Ability throws an exception if not enough AP
				// Attempt to use before resume
				// In case maximum stat exception thrown
				// Game won't resume
    			watchingHero.updateStatuses(controller);
    			abilityToUse.useBattleCommand(watchingHero, controller);
				// Update statuses
				gameState.resumeThread();
    			controller.signalDisplayAbilityUsed(watchingHero, abilityToUse);
    			controller.signalUpdateStatuses();
				// Animate Attack
				updateOnSuccessfulEvent(watchingHero);
    		}
    		catch(IllegalArgumentException notEnoughAP)
    		{
    			// Print the trace, good for debugging if necessary
    			notEnoughAP.printStackTrace();
    			controller.sendNotEnoughAbilityPointsSignal();
    		}
    		catch(MaximumStatException statsMax)
    		{
    			// Print the trace, good for debugging if necessary
    			statsMax.printStackTrace();
    			controller.sendMaxStatExceptionSignal();
    		}
    		catch(NullPointerException cancelledTarget)
    		{
    			
    		}
    	} 
    }
    
    /*********************************************************************************************************/
    /*********************************************************************************************************/
    /*********************************************************************************************************/
    
    /**
     * ItemListener adds the pop up menu when the Item button is clicked
     * @author Kevin
     *
     */
    public class ItemListener implements ActionListener{
    	public void actionPerformed(ActionEvent e) 
    	{
    		addPopUpMenu();
    	}
    }
    
	/**********************************************************************/
    
    /**
     * addPopUpMenu creates and adds the pop up menu to the item button when it is clicked.
     * It iterates through the player's inventory and adds each item to the pop up list. 
     * As the items are added to the pop up list and action listener is created for each item that calls
     * that item's useBattleCommand method on click.
     */
	private void addPopUpMenu()
	{
		 //Create the popup menu.
	    JPopupMenu itemPopUp = new JPopupMenu();
	    ArrayList<JMenuItem> menuItems = new ArrayList<JMenuItem>();
	    int itemCount = 0;
	    ArrayList<Item> items = human.inventoryItems();
	    for(Item currentItem : items)
	    {
	        menuItems.add(new JMenuItem(currentItem.toString()));
	        itemPopUp.add(menuItems.get(itemCount));
	        itemCount ++;
	    }
	    
	    for(int i = 0; i<menuItems.size(); i++)
	    {
	    	JMenuItem currentItem= menuItems.get(i);
	    	Item item = items.get(i);
	    	currentItem.addActionListener((ActionEvent e) -> {
	            try {
	            	// Try and use the item
	            	// It useItem throws MaximumStatException if your character is at the maximum of said stat 
					item.useBattleCommand(currentHero,controller);
		    		itemPopUp.setVisible(false);
		    		controller.signalDisplayAbilityUsed(currentHero, item);
		    		updateOnSuccessfulEvent(currentHero);
		    		gameState.resumeThread();
				} catch (MaximumStatException e1) {
					controller.sendMaxStatExceptionSignal();				
				} 
	        });
	    }
	    controller.signalItemPopUp(itemPopUp);
	}

}
