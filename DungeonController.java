package GridGUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;

public class DungeonController {

	private DungeonView view;
	boolean battleOver;
	
	public DungeonController(DungeonView view)
	{
		this.view = view;
	}
	
	public void signalSwitchView()
	{
		view.switchView();
	}
	
	public void signalMoveComponent(JComponent component, int nextX, int nextY)
	{
		//  Move the component
		view.moveComponent(component, nextX, nextY);
	}
	
	public void hideChest()
	{
		
		GridBagLayout lay = (GridBagLayout) view.getLayout();
		GridBagConstraints che = lay.getConstraints(view.chest.Sprite);
		view.hideChest(true);
		view.remove(DungeonView.chest.Sprite);
		view.remove(DungeonView.hero.Sprite);
		GridBagConstraints gbc = view.makeGbc(0,0);
		view.add(view.chestHidden.Sprite,gbc);
		view.add(view.hero.Sprite, che);
		
		
		view.repaint();
		view.revalidate();
	}
	
}
