package net.slashie.ruler.action.player;

import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.Road;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.serf.level.AbstractFeature;

public class Buildroad extends Action{
	@Override
	public void execute() {
		UnitGroup u = (UnitGroup) performer;
		AbstractFeature f = u.getLevel().getFeatureAt(u.getPosition());
		if (f != null){
			youMessage("You can't lay down a road here");
			return;
		}
		Road road = new Road();
		road.setPosition(u.getPosition());
		u.getLevel().addFeature(road);
		youMessage("You lay down a road.");
	}
	
	@Override
	public String getID() {
		return "BuildRoad";
	}
	
	@Override
	public int getCost() {
		return 4 * 20;
	}
	
	@Override
	public boolean canPerform(Actor a) {
		UnitGroup u = (UnitGroup) a;
		if (!u.canBuildRoads()){
			invalidationMessage = "You can't build roads.";
			return false;
		}
		return true;
	}

}
