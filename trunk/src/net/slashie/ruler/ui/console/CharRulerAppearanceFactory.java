package net.slashie.ruler.ui.console;

import net.slashie.libjcsi.CSIColor;
import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.ruler.domain.entities.CivilizationColors;
import net.slashie.ruler.ui.RulerAppearanceFactory;
import net.slashie.serf.ui.Appearance;
import net.slashie.serf.ui.consoleUI.CharAppearance;

public class CharRulerAppearanceFactory extends RulerAppearanceFactory{
	@Override
	public Appearance getSettlementAppearance(int size, Civilization civ) {
		CharAppearance ret = new CharAppearance("SETTLEMENT_"+size+"_"+civ.getCivDefinition().getCivilizationId(), getSizeChar(size), getCSIColor(civ.getCivDefinition().getColor()));
		return ret;
	}

	private char getSizeChar(int size) {
		if (size < 10)
			return (size+"").toCharArray()[0];
		else
			return (char)('A'+(size-10));
	}

	private int getCSIColor(CivilizationColors color) {
		try {
			return CSIColor.getCodeFromColor((CSIColor)CSIColor.class.getField(color.name()).get(null));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

}
