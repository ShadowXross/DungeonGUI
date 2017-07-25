package GridGUI;

import java.io.IOException;

import PartyContainers.AI;
import PartyContainers.HumanPlayer;

public class Battle_MVC {
	public boolean state;

	public Battle_MVC() throws IOException
	{
		BattleView view = new BattleView();
		BattleController controller = new BattleController(view);
		BattleModel model = new BattleModel(controller, new HumanPlayer(),new AI());
		controller.setModel(model);
		state = model.battleCheck();
	}
	
	
	public static void main(String[] args) throws IOException
	{
		Battle_MVC start = new Battle_MVC();
	}
}
