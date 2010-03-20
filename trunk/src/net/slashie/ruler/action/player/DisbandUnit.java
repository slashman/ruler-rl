package net.slashie.ruler.action.player;

import net.slashie.ruler.domain.entities.MovementType;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;

public class DisbandUnit extends Action{
	
	@Override
	public boolean canPerform(Actor a) {
		UnitGroup u = (UnitGroup) a;
		if (u.getInventory().size() < 2){
			invalidationMessage = "Your group is too small to disband";
			return false;
		}
		if (u.getMovementType() == MovementType.WATER){
			invalidationMessage = "You can't disband units in the sea.";
			return false;
		}
		return true;
	}
	
	@Override
	public void execute() {
		UnitGroup u = (UnitGroup) performer;
		if (u.getInventory().size() < 2){
			youMessage("You can't your "+targetItem.getDescription());
			return;
		}
		u.removeUnit((Unit)targetItem);
		youMessage("You disband your "+targetItem.getDescription());
	}

	@Override
	public String getID() {
		return "DisbandUnit";
	}
	
	@Override
	public boolean needsItem() {
		return true;
	}
	
	@Override
	public String getPromptItem() {
		return "Select an unit to disband";
	}
	
	@Override
	public int getCost() {
		return 0;
	}
	
}
