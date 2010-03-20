package net.slashie.ruler.action.player;

import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;

public class GarrisonUnit extends Action {
	@Override
	public String getPromptItem() {
		return "Select an unit to garrison in the city";
	}
	
	@Override
	public boolean needsItem() {
		return true;
	}
	
	@Override
	public boolean canPerform(Actor a) {
		City c = ((WorldLevel)a.getLevel()).getCityAt(a.getPosition());
		if (c == null){
			invalidationMessage = "You can only garrison units in one of your settlements";
			return false;
		}
		return true;
	}
	
	@Override
	public void execute() {
		UnitGroup u = (UnitGroup) performer;
		City c = ((WorldLevel)u.getLevel()).getCityAt(u.getPosition());
		if (c == null){
			youMessage("You can only garrison units in one of your settlements");
			return;
		}
		
		/*if (c.getUnits().size() >= City.GARRISON_SIZE * c.getSize()){
			u.getLevel().addMessage(c.getDescription()+" can't be defended by more units");
			return;
		}*/
		
		Unit selectedUnit = (Unit) targetItem;
		c.addUnit(selectedUnit);
		u.removeUnit(selectedUnit);
		youMessage("Your "+selectedUnit.getDescription()+" are stationed at "+c.getDescription());

	}
	
	@Override
	public String getID() {
		return "GARRISON_UNIT";
	}

}
