package net.slashie.ruler.action.player;

import java.util.List;

import net.slashie.ruler.action.ActionException;
import net.slashie.ruler.domain.entities.Specialist;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.ui.RulerUserInterface;
import net.slashie.serf.action.Action;
import net.slashie.serf.ui.UserInterface;

public class InfluenceCity extends Action{
	@Override
	public void execute() {
		UnitGroup u = (UnitGroup) performer;
		City c = ((WorldLevel)u.getLevel()).getCityAt(u.getPosition());
		if (c == null){
			youMessage("There's no settlement to influence here");
			return;
		}
		List<Specialist> availableSpecialists = u.getCivilization().getAvailableSpecialists();
		Specialist selectedSpecialist = null;
		try {
			selectedSpecialist = ((RulerUserInterface)UserInterface.getUI()).selectSpecialistFrom("Select a specialist", availableSpecialists);
		} catch (ActionException ae){
			youMessage("No specialists...");
			return;
		}
		if (selectedSpecialist == null)
			return;
		
		c.setInfluencedSpecialist(selectedSpecialist);
		youMessage("You influence "+c.getDescription()+" to raise "+selectedSpecialist.getDescription());

		
	}
	
	@Override
	public String getID() {
		return "INFLUENCE_CITY";
	}
	
	@Override
	public int getCost() {
		return 0;
	}

}
