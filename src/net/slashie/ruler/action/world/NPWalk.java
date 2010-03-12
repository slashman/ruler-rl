package net.slashie.ruler.action.world;

import net.slashie.ruler.controller.Game;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.WorldTile;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.utils.Position;

public class NPWalk extends Action{
	private int actionCost;
	
	@Override
	public boolean canPerform(Actor a) {
        Position var = directionToVariation(targetDirection);
        Position destinationPoint = Position.add(a.getPosition(), var);
    	
        WorldTile cell = (WorldTile) a.getLevel().getMapCell(destinationPoint);
        if (cell == null){
        	return false;
        }
        
        if (cell.isWater()){
        	return false;
        }
        
		return true;
	}

	
	@Override
	public void execute() {
		UnitGroup p = (UnitGroup) performer;
		
		if (targetDirection == Action.SELF){
			return;
		}
		
        Position var = directionToVariation(targetDirection);
        Position destinationPoint = Position.add(performer.getPosition(), var);
        
        UnitGroup destinationGroup = (UnitGroup) performer.getLevel().getActorAt(destinationPoint);
        if (destinationGroup != null){
        	if (destinationGroup == Game.getCurrentGame().getPlayer())
        		performer.getLevel().addMessage("The "+performer.getDescription()+" looks at you curiously.");
        	actionCost = p.getSpeed();
        } else {
        	p.landOn(destinationPoint);
        	actionCost = p.getSpeed();
        }
	}
	

	@Override
	public String getID() {
		return "NP_WALK";
	}
	
	
	@Override
	public int getCost() {
		return actionCost;
	}
}
