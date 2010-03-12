package net.slashie.ruler.ai.unitGroup;

import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ai.SimpleAI;

public class UnitGroupAI extends SimpleAI{
	public UnitGroupAI(Actor mainTarget, Action mainWalk) {
		super(mainTarget, mainWalk);
	}
	
	@Override
	public boolean isCanWalkOverActors() {
		return false;
	}

}
