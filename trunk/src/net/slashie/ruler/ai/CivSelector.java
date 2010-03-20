package net.slashie.ruler.ai;

import java.util.List;

import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.world.City;

public interface CivSelector {
	public String chooseNewNameForSettlement(City c);
	public boolean doYouWantToCreateASettlement();
	public String chooseNameForSettlement();
	public Unit selectUnitToCreate(List<Unit> availableUnits);
	public Unit selectUnitToEnlist(List<Unit> units);
}
