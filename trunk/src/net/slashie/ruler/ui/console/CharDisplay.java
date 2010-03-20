package net.slashie.ruler.ui.console;

import java.io.File;
import java.util.List;

import net.slashie.libjcsi.CSIColor;
import net.slashie.libjcsi.CharKey;
import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.serf.sound.STMusicManagerNew;
import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.ruler.domain.entities.CivilizationColors;
import net.slashie.ruler.domain.entities.CivilizationDefinition;
import net.slashie.ruler.domain.world.Age;
import net.slashie.ruler.factory.CivilizationGenerator;
import net.slashie.ruler.ui.Display;
import net.slashie.utils.OutParameter;

public class CharDisplay extends Display{
	private ConsoleSystemInterface csi;
	public CharDisplay(ConsoleSystemInterface si) {
		this.csi = si;
	}
	
	@Override
	public void createPlayer(OutParameter nameOut, OutParameter civilizationOut) {
		csi.cls();
		List<CivilizationDefinition> civilizations = CivilizationGenerator.getCivilizationDefs();
		csi.print(2, 2, "Choose a Civilization");
		int i = 0;
		for (CivilizationDefinition civilization: civilizations){
			csi.print(4, 4+i, (char)('a'+i)+". "+civilization.getCivilizationName(), getCharColor(civilization.getColor()));
			i++;
		}
		csi.refresh();
    	CharKey x = new CharKey(CharKey.NONE);
		while (x.code < CharKey.a || x.code > CharKey.a+civilizations.size())
			x = csi.inkey();
		CivilizationDefinition choosen = civilizations.get(x.code - CharKey.a);
		civilizationOut.setObject(choosen.getCivilizationId());
		csi.print(30, 2, "By what name shall you be remembered?");
		csi.print(32, 4, "_");
		csi.locateCaret(32, 4);
		csi.refresh();
		String name = csi.input(10);
		nameOut.setObject(name);
	}
	
	private CSIColor getCharColor(CivilizationColors color) {
		try {
			return (CSIColor) CSIColor.class.getField(color.name()).get(null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int showTitleScreen() {
		csi.cls();
		csi.print(2, 2, "RULER", ConsoleSystemInterface.GRAY);
		csi.print(4, 4, "a. New Earth");
		csi.print(4, 5, "b. Resume");
		csi.print(4, 6, "c. Exit");
		csi.print(2, 8, "Slashie - 2010 7DRL Challenge", ConsoleSystemInterface.GRAY);
		csi.refresh();
    	STMusicManagerNew.thus.playKey("TITLE");
    	CharKey x = new CharKey(CharKey.NONE);
		while (x.code != CharKey.A && x.code != CharKey.a &&
				x.code != CharKey.B && x.code != CharKey.b &&
				x.code != CharKey.C && x.code != CharKey.c)
			x = csi.inkey();
		csi.cls();
		switch (x.code){
		case CharKey.A: case CharKey.a:
			return 0;
		case CharKey.B: case CharKey.b:
			return 1;
		case CharKey.C: case CharKey.c:
			return 2;
		}
		return 0;
	}
	
	@Override
	public int showSavedGames(File[] saveFiles) {
		csi.cls();
		if (saveFiles == null || saveFiles.length == 0){
			csi.print(3,6, "No save files");
			csi.print(4,8, "[Space to Cancel]");
			csi.refresh();
			csi.waitKey(CharKey.SPACE);
			return -1;
		}
			
		csi.print(3,6, "Pick an save file");
		for (int i = 0; i < saveFiles.length; i++){
			csi.print(5,7+i, (char)(CharKey.a+i+1)+ " - "+ saveFiles[i].getName());
		}
		csi.print(3,9+saveFiles.length, "[Space to Cancel]");
		csi.refresh();
		CharKey x = csi.inkey();
		while ((x.code < CharKey.a || x.code > CharKey.a+saveFiles.length) && x.code != CharKey.SPACE){
			x = csi.inkey();
		}
		csi.cls();
		if (x.code == CharKey.SPACE)
			return -1;
		else
			return x.code - CharKey.a;
	}
	
	@Override
	public void showIntro(Civilization civ) {
		csi.cls();
		csi.print(2,2, civ.getCivDefinition().getCivilizationName()+" was one of those tribes...");
		csi.print(2,4, "Press space to continue", ConsoleSystemInterface.WHITE);
		csi.refresh();
		csi.waitKey(CharKey.SPACE);
		csi.cls();
	}
	
	@Override
	public void showWin(Civilization civ) {
		csi.cls();
		csi.print(2,2, civ.getCivDefinition().getCivilizationName()+" is the most powerful civilization.");
		csi.print(2,4, "Press space to continue", ConsoleSystemInterface.WHITE);
		csi.refresh();
		csi.waitKey(CharKey.SPACE);
	}
	
	@Override
	public void showHelp() {
		csi.saveBuffer();
		csi.cls();
		csi.print(2,4, "Just run around, dont bump into spikes. Find the Amulet of Vendor", ConsoleSystemInterface.RED);
		csi.refresh();
		csi.waitKey(CharKey.SPACE);
		csi.restore();
	}
	
	@Override
	public void showAgeUp(Civilization civ, Age currentAge) {
		csi.saveBuffer();
		csi.cls();
		csi.print(2,2, civ.getCivDefinition().getCivilizationName()+" has reached the "+currentAge.getDescription()+"!");
		csi.print(2,4, "Press space to continue", ConsoleSystemInterface.WHITE);
		csi.refresh();
		csi.waitKey(CharKey.SPACE);
		csi.restore();
		csi.refresh();
	}
}
