package net.slashie.ruler.action.player;

import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.WorldTile;
import net.slashie.utils.Position;

public class Walk extends Action{
	private boolean actionCancelled = false;
	
	@Override
	public boolean canPerform(Actor a) {
		UnitGroup p = (UnitGroup) a;
        Position var = directionToVariation(targetDirection);
        Position destinationPoint = Position.add(a.getPosition(), var);
    	Actor actor = a.getLevel().getActorAt(destinationPoint);
    	if (actor != null){
    		invalidationMessage = "You can't walk there";
    		return false;
    	}
        WorldTile cell = (WorldTile) a.getLevel().getMapCell(destinationPoint);
        
        if (cell == null){
        	invalidationMessage = "You can't walk there";
        	return false;
        }
        
        if (cell.isWater()){
        	//TODO: Check if the group can go in water
        	invalidationMessage = "Your group can't transverse water";
        	return false;
        }
        
		return true;
	}

	
	
	@Override
	public void execute() {
		actionCancelled = false;
		UnitGroup p = (UnitGroup) performer;
		
		
		if (targetDirection == Action.SELF){
			youMessage("You stand alert.");
			return;
		}
		
        Position var = directionToVariation(targetDirection);
        Position destinationPoint = Position.add(performer.getPosition(), var);
	    p.landOn(destinationPoint);
	}
	

	@Override
	public String getID() {
		return "WALK";
	}
	
	
	@Override
	public int getCost() {
		if (actionCancelled){
			actionCancelled = false;
			return 0;
		}
		UnitGroup p = (UnitGroup) performer;
		return p.getSpeed();
	}
	
	

}
