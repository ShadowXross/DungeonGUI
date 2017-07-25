package GridGUI;

import java.awt.Color;

import javax.swing.JProgressBar;

import Heros.Hero;

public class AbilityPointsBar extends JProgressBar{
	private static final long serialVersionUID = 1L;
	private Hero hero;
	
	public AbilityPointsBar(Hero hero)
	{
		super();
		super.setForeground(Color.BLUE);
        super.setMaximum(hero.getAbilityPoints());
        super.setMinimum(0);
        this.hero = hero;
        updateAPBar();
	}
	
	public void updateAPBar()
	{
		super.setValue(hero.getAbilityPoints());
		super.setString(String.format("%d",hero.getAbilityPoints()));
      	super.setStringPainted(true);
	}
}
