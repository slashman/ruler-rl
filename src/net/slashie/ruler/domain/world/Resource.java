package net.slashie.ruler.domain.world;

public enum Resource {
	GOLD_ORE,
	HORSES,
	IRON_ORE,
	OIL,
	PLUTONIUM,
	STEEL_ORE,
	WOOD,
	FOOD;

	public String getDescription() {
		switch (this){
		case FOOD:
			return "Food";
		case GOLD_ORE:
			return "Gold Ore";
		case HORSES:
			return "Horses";
		case IRON_ORE:
			return "Iron Ore";
		case OIL:
			return "Oil";
		case PLUTONIUM:
			return "Plutonium";
		case STEEL_ORE:
			return "Steel Ore";
		case WOOD:
			return "Wood";
		}
		return null;
	}
}
