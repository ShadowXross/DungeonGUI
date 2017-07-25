package GridGUI;

import java.awt.Color;

import javax.swing.JProgressBar;

import Heros.Hero;

public class HealthBar extends JProgressBar{
	private static final long serialVersionUID = 1L;
	private Hero hero;
	
	public HealthBar(Hero hero)
	{
		super();
		super.setForeground(Color.GREEN);
        super.setMaximum(hero.getHealth());
        super.setMinimum(0);
        this.hero = hero;
        updateHealthBar();
	}
	
	public void updateHealthBar()
	{
		super.setValue(hero.getHealth());
		super.setString(String.format("%d",hero.getHealth()));
      	super.setStringPainted(true);
	}
}
