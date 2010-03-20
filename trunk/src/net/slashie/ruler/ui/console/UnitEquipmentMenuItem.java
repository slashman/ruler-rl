package net.slashie.ruler.ui.console;

import net.slashie.ruler.domain.entities.Unit;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.ui.consoleUI.EquipmentMenuItem;

public class UnitEquipmentMenuItem extends EquipmentMenuItem{
	public UnitEquipmentMenuItem(Equipment e) {
		super(e);
	}

	public String getMenuDescription(){
 		return ((Unit)e.getItem()).getMenuDescription();
 	}
 	
}
