package net.slashie.ruler.action.player;

import java.util.List;

import net.slashie.ruler.action.ActionException;
import net.slashie.ruler.ai.CivSelector;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.ui.RulerUserInterface;
import net.slashie.serf.action.Action;
import net.slashie.serf.ui.UserInterface;

public class AssembleUnit extends Action{
	@Override
	public void execute() {
		UnitGroup u = (UnitGroup) performer;
		City c = ((WorldLevel)u.getLevel()).getCityAt(u.getPosition());
		if (c == null){
			youMessage("You can only create units in one of your settlements");
			return;
		}
		Unit currentUnit = c.getCurrentAssemblyUnit(); 
		if (currentUnit != null){
			youMessage("A "+currentUnit.getDescription()+" is being assembled already at "+c.getDescription());
			return;
		}
		List<Unit> availableUnits = c.getAvailableUnitsForAssembly();
		Unit selectedUnit = ((CivSelector)performer.getSelector()).selectUnitToCreate(availableUnits);
		if (selectedUnit == null)
			return;
		c.spendSpecialistsFor(selectedUnit);
		c.startCreation(selectedUnit);
		youMessage("A "+selectedUnit.getDescription()+" is being assembled at "+c.getDescription());
		
		// TODO Auto-generated method stub
	}
	@Override
	public String getID() {
		return "ASSEMBLE_UNIT";
	}
	
	@Override
	public int getCost() {
		return 0;
	}
}
