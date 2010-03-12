package net.slashie.ruler.ai.barbarian;

import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ai.SimpleAI;
import net.slashie.utils.Position;

public class BarbarianAI extends SimpleAI{
	public BarbarianAI(Actor mainTarget, Action mainWalk) {
		super(mainTarget, mainWalk);
	}

	
	public boolean canWalkTowards(Actor aMonster, int direction){
		Position destination = Position.add(aMonster.getPosition(), Action.directionToVariation(direction));
		if (!aMonster.getLevel().isWalkable(destination)){
			return false;
		} else
			return true;
	}
}
