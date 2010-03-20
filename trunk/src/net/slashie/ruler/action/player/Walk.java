package net.slashie.ruler.action.player;

import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.serf.level.AbstractFeature;
import net.slashie.ruler.domain.entities.Garrison;
import net.slashie.ruler.domain.entities.MovementType;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.AnchoredShip;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.WorldLevel;
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
    	if (actor != null && !(actor instanceof Garrison)){
    		invalidationMessage = "You can't walk there";
    		return false;
    	}
        WorldTile cell = (WorldTile) a.getLevel().getMapCell(destinationPoint);
        
        if (cell == null){
        	invalidationMessage = "You can't walk there";
        	return false;
        }
        
        switch (p.getMovementType()){
        case LAND:
        	if (cell.isWater()){
        		AbstractFeature feature = p.getLevel().getFeatureAt(destinationPoint);
        		if (feature instanceof AnchoredShip){
        			
        		} else {
        			invalidationMessage = "Your group can't transverse water";
        			return false;
        		}
            	
            }
        	break;
        case WATER:
        	City c = ((WorldLevel)p.getLevel()).getCityAt(destinationPoint);
        	if (!cell.isWater() && c == null){
            	UnitGroup u = (UnitGroup) a;
        		Unit seaUnit = u.getSeaUnit();
        		if (seaUnit == null){
        			invalidationMessage = "You can't disembark";
        			return false;
        		}
            }
        }
        
        
		return true;
	}

	
	
	@Override
	public void execute() {
		actionCancelled = false;
		UnitGroup p = (UnitGroup) performer;
		WorldLevel w = (WorldLevel) p.getLevel();
		
		if (targetDirection == Action.SELF){
			youMessage("You camp.");
			int supplyCost = getCost()* 2;;
        	p.useSupplies(supplyCost);
			return;
		}
		
        Position var = directionToVariation(targetDirection);
        Position destinationPoint = Position.add(performer.getPosition(), var);
        
		//Check if is leaving a city
        City c = w.getCityAt(p.getPosition()); 
        City destinationCity = w.getCityAt(destinationPoint);
        if (c != null && destinationCity == null){
        	//Restock group with supplies for the travel
			youMessage("You restock.");
        	p.restock();
        }
        
        //Expend supplies
        if (destinationCity == null){
        	int supplyCost = getCost()* 2;;
        	p.useSupplies(supplyCost);
        }
        
        WorldTile cell = (WorldTile) p.getLevel().getMapCell(destinationPoint);

        // Try to disembark and run
        if (p.getMovementType() == MovementType.WATER && !cell.isWater()){
        	Unit seaUnit = p.getSeaUnit();
        	AnchoredShip anchoredShip = new AnchoredShip(seaUnit);
        	anchoredShip.setPosition(new Position(p.getPosition()));
        	p.getLevel().addFeature(anchoredShip);
        	youMessage("You leave your "+seaUnit.getDescription()+" behind");
        	p.removeUnit(seaUnit);
        }
        
        
        //Drop unit on destination
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
