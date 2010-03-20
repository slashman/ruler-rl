package net.slashie.ruler.ui.console;

import net.slashie.libjcsi.textcomponents.BasicListItem;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.serf.ui.consoleUI.CharAppearance;

public class UnitListItem extends BasicListItem{
	private String description;
	public UnitListItem(Unit unit, String description) {
		super( ((CharAppearance)unit.getAppearance()).getChar(), ((CharAppearance)unit.getAppearance()).getColor(), description);
		this.description = description;
		this.unit = unit;
		/*if (this.description.length() > trimSize)
			this.description = this.description.substring(0, trimSize);*/
	}
	
	@Override
	public String getRow() {
		return description;
	}
	
	private Unit unit;
	public Unit getUnit() {
		return unit;
	}
	

}
