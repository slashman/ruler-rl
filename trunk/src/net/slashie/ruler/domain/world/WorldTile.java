package net.slashie.ruler.domain.world;

import java.util.Hashtable;
import java.util.Map;

import net.slashie.serf.level.AbstractCell;
import net.slashie.serf.ui.AppearanceFactory;

public class WorldTile extends AbstractCell{
	private boolean isWater;
	private Map<Resource, Integer> resources = new Hashtable<Resource, Integer>();
	public WorldTile(String pID, String sdes, boolean isWater,int goldOre, int horses, int ironOre, int oil, int plutonium, int steelOre, int wood, int food){
		super(pID, sdes, sdes, AppearanceFactory.getAppearanceFactory().getAppearance(pID), false, false);
		resources.put(Resource.GOLD_ORE, goldOre);
		resources.put(Resource.HORSES,horses);
		resources.put(Resource.IRON_ORE,ironOre);
		resources.put(Resource.OIL,oil);
		resources.put(Resource.PLUTONIUM,plutonium);
		resources.put(Resource.STEEL_ORE,steelOre);
		resources.put(Resource.WOOD,wood);
		resources.put(Resource.FOOD,food);
		this.isWater = isWater;
	}
	
	public boolean isWater() {
		return isWater;
	}
	
	public int getResource(Resource r){
		return resources.get(r);
	}
}
