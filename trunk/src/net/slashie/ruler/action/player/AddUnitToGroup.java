package net.slashie.ruler.action.player;

import java.util.List;

import net.slashie.ruler.action.ActionException;
import net.slashie.ruler.ai.CivSelector;
import net.slashie.ruler.domain.entities.Garrison;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.ui.RulerUserInterface;
import net.slashie.serf.action.Action;
import net.slashie.serf.ui.UserInterface;

public class AddUnitToGroup extends Action{
	@Override
	public void execute() {
		UnitGroup u = (UnitGroup) performer;
		
		if (u.getSize() == UnitGroup.MAX_SIZE){
			youMessage("Your group is too large. You can't add more units");
			return;
		}
		
		City c = ((WorldLevel)u.getLevel()).getCityAt(u.getPosition());
		if (c == null){
			youMessage("You can only enlist in one of your settlements");
			return;
		}
		List<Unit> units = c.getUnits();
		Unit selectedUnit = ((CivSelector)performer.getSelector()).selectUnitToEnlist(units);
		if (selectedUnit == null)
			return;
		// If selected unit is maritime, only one at a time
		Unit maritimeUnit = u.getSeaUnit();
		if (!(u instanceof Garrison)&&maritimeUnit != null && selectedUnit.isWaterUnit()){
			youMessage("You already have a "+maritimeUnit.getDescription()+" in your group");
			return;
		}
		
		youMessage("The "+selectedUnit.getDescription()+" joins your group");
		u.addUnit(selectedUnit);
		c.removeUnit(selectedUnit);
	}
	
	@Override
	public String getID() {
		return "ADD_UNIT_TO_GROUP";
	}
	
	@Override
	public int getCost() {
		return 0;
	}

}
