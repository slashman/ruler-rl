package net.slashie.ruler.domain.world;

import net.slashie.ruler.domain.entities.Civilization;

public class BarbarianSettlement extends City{
	public BarbarianSettlement(Civilization civ) {
		super("Barbarian Settlement ("+civ.getCivDefinition().getCivilizationName()+")", 1, civ);
	}
	
	@Override
	public boolean isBarbarian() {
		return true;
	}

}
