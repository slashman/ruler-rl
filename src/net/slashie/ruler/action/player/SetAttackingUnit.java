package net.slashie.ruler.action.player;

import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.serf.action.Action;

public class SetAttackingUnit extends Action{
	@Override
	public String getID() {
		return "SET_ATTACKING_UNIT";
	}
	
	@Override
	public boolean needsItem() {
		return true;
	}
	
	@Override
	public String getPromptItem() {
		return "Select an Unit to lead the attack";
	}
	
	@Override
	public void execute() {
		((UnitGroup) performer).setAttackingUnit((Unit)targetItem);
		youMessage("Your "+((Unit)targetItem).getDescription()+" lead the attack");
	}
	
	@Override
	public int getCost() {
		return 0;
	}

}
