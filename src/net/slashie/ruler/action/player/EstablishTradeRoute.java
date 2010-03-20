package net.slashie.ruler.action.player;

import java.util.ArrayList;
import java.util.List;

import net.slashie.ruler.action.ActionException;
import net.slashie.ruler.domain.entities.ResourceCarrier;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.ui.RulerUserInterface;
import net.slashie.serf.action.Action;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.ui.UserInterface;
import net.slashie.util.Pair;

public class EstablishTradeRoute extends Action{
	@Override
	public void execute() {
		UnitGroup u = (UnitGroup) performer;

		//
		City c = ((WorldLevel)u.getLevel()).getCityAt(u.getPosition());
		if (c == null){
			youMessage("You can only establish trade routes to one of your settlements (for now)");
			return;
		}
		
		
		// Looks for a caravan in the unitGroup
		ResourceCarrier caravan = null;
		List<Equipment> unitsE = u.getInventory();
		for (Equipment unitE: unitsE){
			if (unitE.getItem() instanceof ResourceCarrier){
				caravan = (ResourceCarrier) unitE.getItem();
				break;
			}
		}
		
		if (caravan == null || caravan.getCarriedResource() == null){
			youMessage("You are not carrying any goods to trade");
			return;
		}
		

		
		// Choose a resource to trade.
		List<Pair<Resource, Integer>> availableResources = new ArrayList<Pair<Resource,Integer>>();
		for (Resource res: Resource.values()){
			if (c.getAvailableResources(res) > 0){
				availableResources.add(new Pair<Resource, Integer>(res, c.getAvailableResources(res)));
			}
		}
		
		if (availableResources.size() == 0){
			youMessage("There are no resources to trade at "+c.getDescription());
			return;
		}
		
		Resource chosenResource = null;
		try {
			chosenResource = ((RulerUserInterface)UserInterface.getUI()).selectResourceFrom("Trade "+caravan.getCarriedResource().getDescription()+" for...", availableResources);
		} catch (ActionException ae){
			youMessage("There are no resources available at "+c.getDescription());
			return;
		}
		if (chosenResource == null)
			return;
		
		// Set trade agreement
		youMessage("Your "+caravan.getDescription()+" will trade "+caravan.getCarriedResource().getDescription()+" and "+chosenResource.getDescription()+" between "+caravan.getSourceSettlement().getDescription()+" and "+c.getDescription());
		caravan.getSourceSettlement().releaseResource(caravan.getCarriedResource(), 1, "CARAVAN_"+caravan.getFullID());
		
		caravan.getSourceSettlement().commitResource(caravan.getCarriedResource(), 1, "Trade to "+c.getDescription(), "TRADE_"+c.getClassifierID());
		caravan.getSourceSettlement().addTradeAgreement(c, chosenResource, 1, "TRADED_BY_CARAVAN_"+caravan.getFullID());
		c.commitResource(chosenResource, 1, "Trade to "+caravan.getSourceSettlement().getDescription(), "TRADE_"+caravan.getSourceSettlement().getClassifierID());
		c.addTradeAgreement(c, caravan.getCarriedResource(), 1, "TRADED_BY_CARAVAN_"+caravan.getFullID());
		
		u.removeUnit(caravan);
		
	}
	
	@Override
	public String getID() {
		return "ESTABLISH_TRADE_ROUTE";
	}

}
