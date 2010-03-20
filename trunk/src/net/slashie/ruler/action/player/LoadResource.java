package net.slashie.ruler.action.player;

import java.util.ArrayList;
import java.util.List;

import net.slashie.libjcsi.CSIColor;
import net.slashie.ruler.action.ActionException;
import net.slashie.ruler.domain.entities.ResourceCarrier;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.ui.RulerUserInterface;
import net.slashie.serf.action.Action;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.ui.UserInterface;
import net.slashie.util.Pair;

/**
 * Makes a caravan carry a resource
 * @author Slash
 *
 */
public class LoadResource extends Action{
	
	@Override
	public void execute() {
		UnitGroup u = (UnitGroup) performer;

		//
		City c = ((WorldLevel)u.getLevel()).getCityAt(u.getPosition());
		if (c == null){
			youMessage("You can only load resources in one of your settlements");
			return;
		}
		
		
		// Looks for a caravan in the unitGroup
		ResourceCarrier caravan = null;
		List<Equipment> unitsE = u.getInventory();
		for (Equipment unitE: unitsE){
			if (unitE.getItem() instanceof ResourceCarrier){
				caravan = (ResourceCarrier) unitE.getItem();
				if (caravan.getCarriedResource() != null){
					youMessage("One of your caravans is already carrying a resource");
					return;
				}
			}
		}
		
		if (caravan == null){
			youMessage("None of your units can carry goods");
			return;
		}
		
		List<Pair<Resource, Integer>> availableResources = new ArrayList<Pair<Resource,Integer>>();
		for (Resource res: Resource.values()){
			if (c.getAvailableResources(res) > 0){
				availableResources.add(new Pair<Resource, Integer>(res, c.getAvailableResources(res)));
			}
		}
		
		if (availableResources.size() == 0){
			youMessage("There are no resources available at "+c.getDescription());
			return;
		}
		
		Resource chosenResource = null;
		try {
			chosenResource = ((RulerUserInterface)UserInterface.getUI()).selectResourceFrom("Select an resource to load", availableResources);
		} catch (ActionException ae){
			youMessage("There are no resources available at "+c.getDescription());
			return;
		}
		if (chosenResource == null){
			return;
		}
		youMessage("Your "+caravan.getDescription()+" carries the "+chosenResource.getDescription());
		c.commitResource(chosenResource, 1, "Carried by Caravan", "CARAVAN_"+caravan.getFullID());
		caravan.carryResource(chosenResource, c);
	}
	
	@Override
	public String getID() {
		return "LOAD_RESOURCE";
	}
	
	@Override
	public int getCost() {
		return 0;
	}

}
