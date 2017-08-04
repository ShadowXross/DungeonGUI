package DungeonMVC;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

/**Controls the movement of the Hero and tests for collision instances, implements ActionListener Class for keyboard controls
 * 
 * @author Anton Thomas, Kevin Joseph
 *
 */
public class DungeonModel implements ActionListener
{
	private final static String PRESSED = "pressed ";
	private final static String RELEASED = "released ";
	private JComponent component;
	private Timer timer;
	private HashMap<String, Point> pressedKeys = new HashMap<String, Point>();
	private static boolean battleOver = false;
	private DungeonController controller;
	private boolean looted = false;
	
	/**The Constructor which initializes the model and gives control over the hero object
	 * 
	 * @param component The Hero object that gets passed from the View
	 * @param delay
	 * @param controller sets the controller
	 */
	public DungeonModel(JComponent component, int delay, DungeonController controller)
	{
		this.component = component;
		timer = new Timer(delay, this);
		timer.setInitialDelay( 0 );
		this.controller = controller;
		DungeonModel.battleOver = false;
	}

	/**Adds actions to the keystrokes makes use of Hidden Inner class
	 * 
	*  @param keyStroke - see KeyStroke.getKeyStroke(String) for the format of
	*                     of the String. Except the "pressed|released" keywords
	*                     are not to be included in the string.
	*/
	public void addAction(String keyStroke, int deltaX, int deltaY)
	{
		//  Separate the key identifier from the modifiers of the KeyStroke

		int offset = keyStroke.lastIndexOf(" ");
		String key = offset == -1 ? keyStroke :  keyStroke.substring( offset + 1 );
		String modifiers = keyStroke.replace(key, "");

		//  Get the InputMap and ActionMap of the component

		InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = component.getActionMap();

		//  Create Action and add binding for the pressed key

		Action pressedAction = new AnimationAction(key, new Point(deltaX, deltaY));
		String pressedKey = modifiers + PRESSED + key;
		KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(pressedKey);
		inputMap.put(pressedKeyStroke, pressedKey);
		actionMap.put(pressedKey, pressedAction);

		//  Create Action and add binding for the released key

		Action releasedAction = new AnimationAction(key, null);
		String releasedKey = modifiers + RELEASED + key;
		KeyStroke releasedKeyStroke = KeyStroke.getKeyStroke(releasedKey);
		inputMap.put(releasedKeyStroke, releasedKey);
		actionMap.put(releasedKey, releasedAction);
	}

	//  Invoked whenever a key is pressed or released

	/**Handles the event from the pressing of the arrow keys to control the component
	 * 
	 * @param key
	 * @param moveDelta
	 */
	private void handleKeyEvent(String key, Point moveDelta)
	{
		//  Keep track of which keys are pressed

		if (moveDelta == null)
			pressedKeys.remove( key );
		else
			pressedKeys.put(key, moveDelta);

		//  Start the Timer when the first key is pressed

   		if (pressedKeys.size() == 1)
   		{
   			timer.start();
   		}

		//  Stop the Timer when all keys have been released

   		if (pressedKeys.size() == 0)
   		{
   			timer.stop();
   		}
	}

	//  Invoked when the Timer fires

	public void actionPerformed(ActionEvent e)
	{
		int[] newCoords = this.computeNewCoordinates();
		controller.signalMoveComponent(this.component, newCoords[0], newCoords[1]);
		if(!looted)
			checkChestCollision();
		if(!battleOver)
			checkEnemyCollisions();
		if(battleOver)
			checkDoorCollisions();
	}

	/**Computes and returns the current and next co-ordinates of the component
	 * 
	 * @return new Co-ordinates of the component
	 */
	public int[] computeNewCoordinates()
	{
		int componentWidth = component.getSize().width;
		int componentHeight = component.getSize().height;

		Dimension parentSize = component.getParent().getSize();
		int parentWidth  = parentSize.width;
		int parentHeight = parentSize.height;

		//  Calculate new move

		int deltaX = 0;
		int deltaY = 0;

		for (Point delta : pressedKeys.values())
		{
			deltaX += delta.x;
			deltaY += delta.y;
		}

		//  Determine next X position

		int nextX = Math.max(component.getLocation().x + deltaX, 0);

		if ( nextX + componentWidth > parentWidth)
		{
			nextX = parentWidth - componentWidth;
		}

		//  Determine next Y position

		int nextY = Math.max(component.getLocation().y + deltaY, 0);

		if ( nextY + componentHeight > parentHeight)
		{
			nextY = parentHeight - componentHeight;
		}
		
		int[] newCoords = new int[] {nextX,nextY};
		return newCoords;
	}
	
	/**Test collision between the Hero and the enemy
	 * Stops the timer and informs controller to change views
	 * 
	 */
	private void checkEnemyCollisions() 
	{
		Rectangle r1 = new Rectangle(component.getBounds());
		Rectangle r2 = new Rectangle(DungeonView.getEnemy().dimen());
		if((r2.getX() - 82) < r1.getX() && r1.getX() < (r2.getX() + 82))
		{
			timer.stop();
			// to set background to disappear you would need an instance therefore would require the menu
			controller.signalStartBattle();
			return;
		}
	}
	
	/**Check for collision with chest, 
	 * on collision cause popup with the gained items and hide chest
	 * 
	 */
	protected void checkChestCollision()
	{
		Rectangle r1 = new Rectangle(component.getBounds());
		Rectangle r2 = new Rectangle(DungeonView.getChest().dimen());
		if((r1.getX() > r2.getX() - 45))
		{
			timer.stop();
			JOptionPane.showMessageDialog(null, "You Found \nAbilityPoints Potion x1\nAntidote x1\nHealth Potion x1", "Golden Chest", JOptionPane.WARNING_MESSAGE);
			soundEffectControl.SoundClipPlayer.playSound("/Inventory.wav");
			looted  = true;
			controller.signalHideChest();
		}
	}
	
	/**Check collision with door,
	 * On collision cause a popup to inform player of choice to leave and act on the chosen option
	 */
	private void checkDoorCollisions()
	{
		Rectangle r1 = new Rectangle(component.getBounds());
		Rectangle r3 = new Rectangle(DungeonView.getDoor().getBounds());
		if(r1.getX() > (r3.getX()+136))
		{
			timer.stop();
			String message = "Thanks for playing \n Would you like to stay for more?";
			int reply = JOptionPane.showConfirmDialog(null, message, "Exit", JOptionPane.YES_NO_OPTION);
	        if (reply == JOptionPane.YES_OPTION) {
	          JOptionPane.showMessageDialog(null, "Freedom of choice is an illusion");
	          controller.signalSendToMainMenu();
	        }
	        else {
	           JOptionPane.showMessageDialog(null, "It was nice while it lasted");
	           controller.signalSendToMainMenu();
	        }	
		}
	}
	
	//  Action to keep track of the key and a Point to represent the movement
	//  of the component. A null Point is specified when the key is released.

	/**Hidden inner Helper class to recognize and act on events fired by key-press */
	@SuppressWarnings("serial")
	private class AnimationAction extends AbstractAction implements ActionListener
	{
		private Point moveDelta;

		/**Constructor calls to super from implementation of Action Listener
		 * @param key
		 * @param moveDelta
		 */
		public AnimationAction(String key, Point moveDelta)
		{
			super(key);

			this.moveDelta = moveDelta;
		}

		public void actionPerformed(ActionEvent e)
		{
			handleKeyEvent((String)getValue(NAME), moveDelta);
		}
	}
	
	/**Signify to controller the battle has ended
	 * @param battleOver
	 */
	public static void setBattleOver(boolean battleOver)
	{
		DungeonModel.battleOver = battleOver;
	}

}