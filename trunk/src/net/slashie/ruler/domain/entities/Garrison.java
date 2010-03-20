package net.slashie.ruler.domain.entities;

import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.Resource;

public class Garrison extends UnitGroup{
	private City city;
	public Garrison(City city) {
		super(city.getCivilization());
		this.city = city;
	}
	
	@Override
	public boolean isInvisible() {
		return true;
	}
	
	@Override
	public String getDescription() {
		return city.getDescription()+ " Garrison";
	}
	
	@Override
	public void removeUnit(Unit u) {
		super.removeUnit(u);
		city.releaseResource(Resource.FOOD, 1, "GARRISON_UNIT_"+u.getFullID());
	}
	
	

}
