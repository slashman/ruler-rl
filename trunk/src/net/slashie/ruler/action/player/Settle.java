package net.slashie.ruler.action.player;

import net.slashie.ruler.ai.CivSelector;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ui.UserInterface;

public class Settle extends Action{
	private boolean actionCancelled;
	@Override
	public String getID() {
		return "SETTLE";
	}
	
	@Override
	public void execute() {
		actionCancelled = false;
		UnitGroup u = (UnitGroup) performer;
		if (u.canSettle()){
			// Check if there's a settlement here already
			if (((WorldLevel)u.getLevel()).getCityAt(u.getPosition()) != null){
				youMessage("There's a settlement here already");
				return;
			}
			if (((CivSelector)performer.getSelector()).doYouWantToCreateASettlement()){
				//Choose a name for the settlement
				String name = ((CivSelector)performer.getSelector()).chooseNameForSettlement();
				((WorldLevel)u.getLevel()).addSettlement(name, u.getPosition(), u.getCivilization());
				//Reduce one settler from the group
				u.reduceUnitOfClassifier(Unit.SETTLER_ID);
				youMessage(name+" has been founded!");
			} else {
				actionCancelled = true;
				return;
			}
		}
	}
	
	@Override
	public int getCost() {
		if (actionCancelled)
			return 0;
		return 30 * 4;
	}
	
	@Override
	public boolean canPerform(Actor a) {
		UnitGroup u = (UnitGroup) a;
		if (!u.canSettle()){
			invalidationMessage = "You can't settle!";
			return false;
		}
		if (((WorldLevel)u.getLevel()).getCityAt(u.getPosition()) != null){
			invalidationMessage = "There's a city here already";
			return false;
		}
		return true;
	}
}
