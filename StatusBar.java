package GridGUI;

import java.awt.Color;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import BattleCommands.PaladinAbility.Defend;
import Heros.Hero;

public class StatusBar extends JLabel{
	private Hero hero;
	
	public StatusBar(Hero hero, String text)
	{
		super(text, SwingConstants.CENTER);
        super.setForeground(Color.WHITE);
        super.setBackground(Color.BLACK);
        super.setOpaque(true);
		this.hero = hero;
	}
	
	public void updateStatus()
	{
		String updatedStatusText = "<html><center>";
		for(Map.Entry entry : hero.getStatuses().entrySet())
		{
			updatedStatusText += entry.getValue().toString() + "<br>";
		}
		if(updatedStatusText.equals("<html><center>"))
		{
			updatedStatusText = "None";
		}
		else
		{
			updatedStatusText += "</center></html>";
		}
		super.setText(updatedStatusText);
	}
}
