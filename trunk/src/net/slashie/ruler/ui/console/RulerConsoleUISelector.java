package net.slashie.ruler.ui.console;

import java.util.List;

import net.slashie.ruler.action.ActionException;
import net.slashie.ruler.ai.CivSelector;
import net.slashie.ruler.controller.Game;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.ui.RulerUserInterface;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ui.UserInterface;
import net.slashie.serf.ui.consoleUI.ConsoleUISelector;
import net.slashie.utils.Util;

public class RulerConsoleUISelector extends ConsoleUISelector implements CivSelector{
	@Override
	public int onActorStumble(Actor actor) {
		if (actor instanceof UnitGroup){
			UnitGroup u = (UnitGroup) actor;
			if (Game.getCurrentGame().isEnemy(u)){
				return STUMBLE_ATTACK;
			} else 
				return STUMBLE_WALK;
		}
		return 0;
	}
	
	public String chooseNewNameForSettlement(City c) {
		if (c.getOriginalCivilization() == ((UnitGroup)Game.getCurrentGame().getPlayer()).getCivilization()){
			return c.getOriginalName();
		}
		String newName = null;
		boolean barbarian = c.isBarbarian();
		if ( barbarian || 
			 UserInterface.getUI().promptChat("You have invaded "+c.getDescription()+", do you want to change its name? (Y/N)")){
			//Choose a name for the settlement
			newName = UserInterface.getUI().inputBox("How should we call this settlement?");
		}
		return newName;
	}
	
	public String chooseNameForSettlement() {
		String ret = UserInterface.getUI().inputBox("How should we call this settlement?");
		if (ret.equals(""))
			return ((UnitGroup)Game.getCurrentGame().getPlayer()).getCivilization().getCivDefinition().getACityName();
		else
			return ret;
	}
	
	public boolean doYouWantToCreateASettlement() {
		return UserInterface.getUI().promptChat("Do you want to create a settlement? (Y/N)");
	}
	
	public Unit selectUnitToCreate(List<Unit> availableUnits) {
		try {
			return ((RulerUserInterface)UserInterface.getUI()).selectUnitFrom("Select an unit to create", availableUnits);
		} catch (ActionException e) {
			return null;
		}
	}
	
	public Unit selectUnitToEnlist(List<Unit> units) {
		try {
			return ((RulerUserInterface)UserInterface.getUI()).selectUnitFrom("Select an unit to enlist", units);
		} catch (ActionException ae){
			return null;
		}
	}

}
