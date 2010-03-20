package net.slashie.ruler.domain.entities;

import java.util.List;

import net.slashie.ruler.domain.world.Age;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.util.Pair;
import net.slashie.utils.roll.Roll;

public class ResourceCarrier extends Unit{
	
	private Resource carriedResource;
	private City sourceSettlement;
	
	public ResourceCarrier(
			String classifierID, 
			String description, 
			int baseHP, 
			Roll attackRoll, 
			Roll defenseRoll, 
			int attackTurns,
			int movementCost,
			boolean isWaterUnit,
			Age availableAge,
			List<Pair<Resource, Integer>> resourceRequeriments,
			List<Pair<Specialist, Integer>> specialistRequirements
			) {
		super(classifierID, description, baseHP, attackRoll, defenseRoll, attackTurns, movementCost, isWaterUnit, false, availableAge, resourceRequeriments, specialistRequirements);
	}

	public Resource getCarriedResource() {
		return carriedResource;
	}

	public void carryResource(Resource carriedResource, City sourceSettlement) {
		this.carriedResource = carriedResource;
		this.sourceSettlement = sourceSettlement;
	}

	public City getSourceSettlement() {
		return sourceSettlement;
	}

}
