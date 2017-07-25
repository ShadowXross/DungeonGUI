package GridGUI;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import BattleCommands.BattleCommand;
import Heros.Hero;

public class AbilityNotifier extends JLabel{
	
	/**
	 * Added so compiler doesn't complain
	 */
	private static final long serialVersionUID = 467004103231290586L;
	
	private Hero hero;
	
	public AbilityNotifier(Hero hero)
	{
		super("", SwingConstants.CENTER);
        super.setForeground(Color.WHITE);
        super.setBackground(Color.BLACK);
        super.setOpaque(true);
		this.setHero(hero);
	}
	
	public void updateAbilityNotifier(BattleCommand ability)
	{
		super.setText(ability.toString());
	}
	
	public void clearAbilityNotifier()
	{
		super.setText("");
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}
}
