package net.slashie.ruler.ui.console;

import net.slashie.libjcsi.textcomponents.MenuItem;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.serf.ui.consoleUI.CharAppearance;

public class UnitMenuItem implements MenuItem{
	private Unit u;
	public UnitMenuItem(Unit u) {
		this.u = u;
	}
	
	public char getMenuChar() {
		return ((CharAppearance)u.getAppearance()).getChar();
	}
	
	public int getMenuColor() {
		return ((CharAppearance)u.getAppearance()).getColor();
	}
	
	public String getMenuDescription() {
		return u.getMenuDescription();
	}

	public Unit getUnit() {
		return u;
	}

}
