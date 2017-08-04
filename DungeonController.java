package DungeonMVC;

import javax.swing.JComponent;

/**The controller for the Dungeon Veiw and Model
 * Passes signals between view and model enabling proper MVC architecture
 * 
 * @author Anton Thomas
 *
 */
public class DungeonController {

	/**The DungeonView is the view within the MVC */
	private DungeonView view;
	/**Signify the end of the battle encounter	 */
	boolean battleOver;
	
	/**Simple constructor that sets the Dungeon view
	 * @param view
	 */
	public DungeonController(DungeonView view)
	{
		this.view = view;
	}
	
	/**Begins the battle, Should trigger on collision of Hero with Enemy
	 * 
	 */
	public void signalStartBattle()
	{
		view.startBattleView();
	}
	
	/**
	 * Causes the view to switch to Main Menu, Should occur on loss in battle or collision with door
	 */
	public void signalSendToMainMenu()
	{
		view.switchToMainMenu();
	}
	
	/**Moves the Hero around the screen
	 * 
	 * @param component
	 * @param nextX
	 * @param nextY
	 */
	public void signalMoveComponent(JComponent component, int nextX, int nextY)
	{
		view.moveComponent(component, nextX, nextY);
	}
	
	/**Hides the chest once its been looted
	 * 
	 */
	public void signalHideChest()
	{
		view.chestOpened();
	}
	
}
