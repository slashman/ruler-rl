package net.slashie.ruler.ui;

import java.io.File;

import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.ruler.domain.world.Age;
import net.slashie.utils.OutParameter;

public abstract class Display {
	public static Display thus;
	
	public abstract void createPlayer(OutParameter name, OutParameter civilization);
	public abstract int showTitleScreen();
	public abstract int showSavedGames(File[] saves);
	public abstract void showIntro(Civilization civ);
	public abstract void showWin(Civilization civ);
	public abstract void showHelp();
	
	public abstract void showAgeUp(Civilization civ, Age currentAge);
}
