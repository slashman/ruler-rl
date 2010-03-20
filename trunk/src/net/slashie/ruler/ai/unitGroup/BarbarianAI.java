package net.slashie.ruler.ai.unitGroup;

import java.util.ArrayList;
import java.util.List;

import net.slashie.ruler.ai.CivSelector;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.world.City;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ai.RangedActionSpec;
import net.slashie.serf.ai.SimpleAI;
import net.slashie.utils.Util;

public class BarbarianAI extends SimpleAI implements CivSelector{
	public BarbarianAI(Actor mainTarget, Action mainWalk) {
		super(mainTarget, mainWalk);
		ArrayList<RangedActionSpec> rangedActions = new ArrayList<RangedActionSpec>();
		rangedActions.add(new RangedActionSpec("ATTACK_UNIT_GROUP",1,100,"",""));
		setRangedActions(rangedActions);
	}
	
	@Override
	public boolean isCanWalkOverActors() {
		return false;
	}

	public String chooseNewNameForSettlement(City c) {
		return Util.randomElementOf(names);
	}
	
	private String[] names = new String[]{
		"Ulabumba",
		"Guachapanga",
		"Akarumba",
		"Matanga",
		"Gomga",
		"Epageta",
		"Soria",
		"Nimido",
		"Babakama",
		"Uguruima"
	};
	
	public boolean doYouWantToCreateASettlement() {
		return false;
	}
	
	public String chooseNameForSettlement() {
		return null;
	}
	
	public Unit selectUnitToCreate(List<Unit> availableUnits) {
		return null;
	}
	
	public Unit selectUnitToEnlist(List<Unit> units) {
		return null;
	}
}
