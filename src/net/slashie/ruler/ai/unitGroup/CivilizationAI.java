package net.slashie.ruler.ai.unitGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.slashie.ruler.action.player.AddUnitToGroup;
import net.slashie.ruler.action.player.AssembleUnit;
import net.slashie.ruler.action.player.Settle;
import net.slashie.ruler.action.player.Walk;
import net.slashie.ruler.ai.CivSelector;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.factory.ItemFactory;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ai.SimpleAI;
import net.slashie.utils.Position;
import net.slashie.utils.Util;

public class CivilizationAI extends SimpleAI implements CivSelector{
	private UnitGroup unitGroup;
	private final static int SETTLER = 0;
	private final static int GARRISON = 1;
	private final static int RECRUIT = 2;
	private final static int WAIT = 3;
	
	private int mode;
	private int preWaitMode;
	private int GARRISON_SIZE = 3;
	private String preferredUnit;
	
	public CivilizationAI(UnitGroup unitGroup, Actor mainTarget, Action mainWalk) {
		super(mainTarget, mainWalk);
		this.unitGroup = unitGroup;
	}
	
	@Override
	public Action selectAction(Actor who) {
		switch (mode){
		case SETTLER:
			return settlerMode(who);
		case RECRUIT:
			return recruitMode(who);
		case GARRISON:
			return garrisonMode(who);
		case WAIT:
			return waitMode(who);
		}
		return null;
	}
	
	
	
	private Action garrisonMode(Actor who) {
		UnitGroup u = (UnitGroup) who;
		WorldLevel w = (WorldLevel) u.getLevel();
		City c = w.getCityAt(u.getPosition());
		
		if (c == null){
			//This should never happen
			mode = SETTLER;
			return waitMode(u);
		}
		
		// Is our garrison ready?
		if (c.getUnits().size() >= GARRISON_SIZE){
			mode = RECRUIT;
			return recruitMode(who);
		}
		
		// Can we build units here?
		if (c.getCurrentAssemblyUnit() != null){
			preWaitMode = mode;
			mode = WAIT;
			return waitMode(u);
		}
		
		//Add some defenders to our group
		return createUnit(c, u, false);
		
	}

	private Action waitMode(Actor who) {
		if (Util.chance(10)){
			mode = preWaitMode;
		}
		Action ret = new Walk();
		ret.setDirection(Action.SELF);
		return ret;
	}

	private Action recruitMode(Actor who) {
		UnitGroup u = (UnitGroup) who;
		WorldLevel w = (WorldLevel) u.getLevel();
		City c = w.getCityAt(u.getPosition());
		
		if (c == null){
			//This should never happen
			//This should never happen
			mode = SETTLER;
			return waitMode(u);
		}
		
		// Is our assault group ready?
		if (u.getSize() == UnitGroup.MAX_SIZE){
			mode = SETTLER;
			return settlerMode(who);
		}
		
		// Are there units to enlist in our group?
		if (c.getUnits().size() > GARRISON_SIZE ){
			//Add strongest attack unit to group
			Action ret = new AddUnitToGroup();
			return ret;
		}
		
		// Can we build units here?
		if (c.getCurrentAssemblyUnit() != null){
			preWaitMode = mode;
			mode = WAIT;
			return waitMode(u);
		}
		
		// Do we have a settler already?
		if (!hasSettler(u)){
			// Can we create settlers? if not, let's wait
			if (c.canCreateSettler()){
				AssembleUnit ret = new AssembleUnit();
				preferredUnit = Unit.SETTLER_ID;
				return ret;
			}
			preWaitMode = mode;
			mode = WAIT;
			return waitMode(u);
				
		}
		

		
		// Add some warriors to our group
		return createUnit(c, u, true);
	}
	

	private Action createUnit(City c, UnitGroup u, boolean attack) {
		List<Unit> availableUnits = c.getAvailableUnitsForAssembly();
		Unit selectedUnit = pickAUnitToCreate(availableUnits);
		if (selectedUnit == null){
			// Can't create any unit, either wait or change mode to settler
			if (Util.chance(50)){
				preWaitMode = mode;
				mode = WAIT;
				return waitMode(u);
			} else {
				mode = SETTLER;
				return settlerMode(u);
			}
		} else {
			AssembleUnit ret = new AssembleUnit();
			return ret;
		}
	}

	private boolean hasSettler(UnitGroup u) {
		List<Unit> units = u.compileUnitList();
		for (Unit unit: units){
			if (unit.isSettler())
				return true;
		}
		return false;
	}

	/**
	 * Can we create cities? if not, lets look forward to create a settler
	 * if we can create, is current ground ok? if so create one and probably continue expanding or recruit units
	 * if not, let's be barbarian
	 * @param who
	 * @return
	 */
	private Action settlerMode(Actor who) {
		UnitGroup u = (UnitGroup) who;
		if (currentGroundOkForSettling(who)){
			Action ret = new Settle();
			if (Util.chance(80))
				mode = GARRISON;
			return ret;
		} else {
			// Delegate to simpleAI
			return super.selectAction(who);
		}
	}

	private boolean currentGroundOkForSettling(Actor who) {
		// Must ensure that a settler can be created
		WorldLevel w = (WorldLevel)who.getLevel();
		int food = w.getResourcesAround(who.getPosition(), Resource.FOOD, 1);
		boolean cityhere = w.getCityAt(who.getPosition()) != null;
		int nearbyCityDistance = 999;
		List<City> cities = w.getCities();
		for (City city: cities){
			int distance = Position.distance(who.getPosition(), city.getPosition()); 
			if (distance < nearbyCityDistance)
				nearbyCityDistance = distance; 
		}
		return cityhere == false && food > 1 && nearbyCityDistance > 5;
		
	}

	@Override
	public boolean isCanWalkOverActors() {
		return false;
	}

	public String chooseNewNameForSettlement(City c) {
		return null;
	}
	
	public boolean doYouWantToCreateASettlement() {
		return true;
	}
	
	public String chooseNameForSettlement() {
		return unitGroup.getCivilization().getCivDefinition().getACityName();
	}
	
	public Unit selectUnitToCreate(List<Unit> availableUnits) {
		return pickAUnitToCreate(availableUnits);
	}
	
	public Unit pickAUnitToCreate(List<Unit> availableUnits){
		if (preferredUnit != null){
			for (Unit u: availableUnits){
				if (u.getClassifierId().equals(preferredUnit))
					return u;
			}
			preferredUnit = null;
		}
		List<Unit> possibleUnits = ItemFactory.getUnitsByAge(unitGroup.getCivilization().getCurrentAge());
		if (mode == RECRUIT)
			// Get strongest attack units
			Collections.sort(possibleUnits, new Comparator<Unit>() {
				public int compare(Unit o1, Unit o2) {
					return o2.getAttackRoll().getMax() - o1.getAttackRoll().getMax();
				}
			});
		else
			// Get strongest defense units
			Collections.sort(possibleUnits, new Comparator<Unit>() {
				public int compare(Unit o1, Unit o2) {
					return o2.getDefenseRoll().getMax() - o1.getDefenseRoll().getMax();
				}
			});
			
		Unit selectedUnit = null;
		for (Unit unit: possibleUnits){
			if (unit.isWaterUnit())
				continue;
			if (availableUnits.contains(unit)){
				selectedUnit = unit;
				break;
			}
		}
		return selectedUnit;
	}
	
	public Unit selectUnitToEnlist(List<Unit> units) {
		if (mode == RECRUIT)
			// Get strongest attack units
			Collections.sort(units, new Comparator<Unit>() {
				public int compare(Unit o1, Unit o2) {
					return o2.getAttackRoll().getMax() - o1.getAttackRoll().getMax();
				}
			});
		else
			// Get strongest defense units
			Collections.sort(units, new Comparator<Unit>() {
				public int compare(Unit o1, Unit o2) {
					return o2.getDefenseRoll().getMax() - o1.getDefenseRoll().getMax();
				}
			});
		return units.get(0);
	}
}
