package net.slashie.ruler.action.player;

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
				u.getLevel().addMessage("There's a city here already");
				return;
			}
			if (UserInterface.getUI().promptChat("Do you want to create a settlement? (Y/N)")){
				//Choose a name for the settlement
				String name = UserInterface.getUI().inputBox("How should we call this settlement?");
				((WorldLevel)u.getLevel()).addSettlement(name, u.getPosition(), u.getCivilization());
				//Reduce one settler from the group
				u.reduceUnitOfClassifier("SETTLER");
				u.getLevel().addMessage("After a month of work, you create a settlement!");
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
