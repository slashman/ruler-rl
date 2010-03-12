package net.slashie.ruler.ui.console;

import net.slashie.ruler.controller.Game;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ui.consoleUI.ConsoleUISelector;

public class RulerConsoleUISelector extends ConsoleUISelector{
	@Override
	public int onActorStumble(Actor actor) {
		if (actor instanceof UnitGroup){
			UnitGroup u = (UnitGroup) actor;
			if (Game.getCurrentGame().isEnemy(u)){
				return STUMBLE_ATTACK;
			} else 
				return STUMBLE_NOTHING;
		}
		return 0;
	}

}
