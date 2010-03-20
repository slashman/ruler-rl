package net.slashie.ruler.data.dao;

import java.util.ArrayList;
import java.util.List;

import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.serf.level.AbstractCell;
import net.slashie.serf.ui.AppearanceFactory;
import net.slashie.serf.ui.consoleUI.CharAppearance;
import net.slashie.util.Pair;
import net.slashie.utils.roll.Roll;
import net.slashie.ruler.domain.entities.ResourceCarrier;
import net.slashie.ruler.domain.entities.Specialist;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.world.Age;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.ruler.domain.world.WorldTile;

public class EntitiesDAO {
	
	public static AbstractCell[] getCellDefinitions (AppearanceFactory appFactory){
		return new AbstractCell[]{
			//Overworld cells
			new WorldTile("PLAINS", "Plains", false, 0, 0, 0, 0, 0, 0, 0, 0),
			new WorldTile("GRASSLANDS", "Grasslands", false, 0, 0, 0, 0, 0, 0, 0, 1),
			new WorldTile("GRASSLANDS_HORSES", "Grasslands (Horses)", false, 0, 1, 0, 0, 0, 0, 0, 0),
			new WorldTile("MOUNTAINS", "Mountains", false, 0, 0, 0, 0, 0, 0, 0, 0),
			new WorldTile("MOUNTAINS_GOLD", "Mountains (Gold)", false, 1, 0, 0, 0, 0, 0, 0, 0),
			new WorldTile("MOUNTAINS_IRON", "Mountains (Iron)", false, 0, 0, 1, 0, 0, 0, 0, 0),
			new WorldTile("MOUNTAINS_PLUTONIUM", "Mountains (Plutonium)", false, 0, 0, 0, 0, 1, 0, 0, 0),
			new WorldTile("DESERT", "Desert", false, 0, 0, 0, 0, 0, 0, 0, 0),
			new WorldTile("DESERT_OIL", "Desert (Oil)", false, 0, 0, 0, 1, 0, 0, 0, 0),
			new WorldTile("WOODS", "Woods", false, 0, 0, 0, 0, 0, 0, 1, 0),
			new WorldTile("OCEAN", "Ocean", true, 0, 0, 0, 0, 0, 0, 0, 1),

		};
	}
	
	public static CharAppearance[] getCharAppearances(){
		return new CharAppearance[]{
			//Leader Groups
			new CharAppearance("LEADER_LIME", '@', ConsoleSystemInterface.LEMON),
			new CharAppearance("LEADER_BLUE", '@', ConsoleSystemInterface.BLUE),
			new CharAppearance("LEADER_RED", '@', ConsoleSystemInterface.RED),
			new CharAppearance("LEADER_YELLOW", '@', ConsoleSystemInterface.YELLOW),
			new CharAppearance("LEADER_CYAN", '@', ConsoleSystemInterface.CYAN),
			new CharAppearance("LEADER_GRAY", '@', ConsoleSystemInterface.GRAY),
			new CharAppearance("LEADER_MAGENTA", '@', ConsoleSystemInterface.MAGENTA),
			
			//Groups
			new CharAppearance("GROUP_LIME", 'B', ConsoleSystemInterface.GREEN),
			new CharAppearance("GROUP_BLUE", 'B', ConsoleSystemInterface.DARK_BLUE),
			new CharAppearance("GROUP_RED", 'B', ConsoleSystemInterface.DARK_RED),
			new CharAppearance("GROUP_YELLOW", 'B', ConsoleSystemInterface.BROWN),
			new CharAppearance("GROUP_CYAN", 'B', ConsoleSystemInterface.TEAL),
			new CharAppearance("GROUP_GRAY", 'B', ConsoleSystemInterface.LIGHT_GRAY),
			new CharAppearance("GROUP_MAGENTA", 'B', ConsoleSystemInterface.PURPLE),
			
			
			
			
			
			//Cells
			new CharAppearance("WOODS", '&', ConsoleSystemInterface.GREEN),
			new CharAppearance("PLAINS", '.', ConsoleSystemInterface.BROWN),
			new CharAppearance("GRASSLANDS", '=', ConsoleSystemInterface.GREEN),
			new CharAppearance("GRASSLANDS_HORSES", '%', ConsoleSystemInterface.GRAY),
			new CharAppearance("MOUNTAINS", '^', ConsoleSystemInterface.GRAY),
			new CharAppearance("MOUNTAINS_GOLD", '^', ConsoleSystemInterface.YELLOW),
			new CharAppearance("MOUNTAINS_IRON", '^', ConsoleSystemInterface.WHITE),
			new CharAppearance("MOUNTAINS_PLUTONIUM", '^', ConsoleSystemInterface.LEMON),
			new CharAppearance("DESERT", '~', ConsoleSystemInterface.YELLOW),
			new CharAppearance("DESERT_OIL", 'o', ConsoleSystemInterface.GRAY),
			new CharAppearance("OCEAN", '~', ConsoleSystemInterface.BLUE),
			
			// Features
			new CharAppearance("ANCHORED_SHIP", 'X', ConsoleSystemInterface.RED),
			new CharAppearance("ROAD", '+', ConsoleSystemInterface.BROWN),

			//Units
			new CharAppearance("Axemen", 'a', ConsoleSystemInterface.BROWN),
			new CharAppearance("Riders", 'r', ConsoleSystemInterface.BLUE),
			new CharAppearance("Spearmen", 's', ConsoleSystemInterface.LEMON),
			new CharAppearance("Sledgemen", 'S', ConsoleSystemInterface.BROWN),
			new CharAppearance("Boat", 'b', ConsoleSystemInterface.RED),
			new CharAppearance("Legion", 'l', ConsoleSystemInterface.RED),
			new CharAppearance("Charriots", 'c', ConsoleSystemInterface.YELLOW),
			new CharAppearance("Phalanx", 'p', ConsoleSystemInterface.GRAY),
			new CharAppearance("Catapult", 'C', ConsoleSystemInterface.BROWN),
			new CharAppearance("Trireme", 't', ConsoleSystemInterface.RED),
			new CharAppearance("Swordsmen", 's', ConsoleSystemInterface.GREEN),
			new CharAppearance("Knights", 'k', ConsoleSystemInterface.BLUE),
			new CharAppearance("Lancers", 'l', ConsoleSystemInterface.RED),
			new CharAppearance("Onager", 'o', ConsoleSystemInterface.RED),
			new CharAppearance("Frigate", 'f', ConsoleSystemInterface.RED),
			new CharAppearance("Musketeer", 'm', ConsoleSystemInterface.PURPLE),
			new CharAppearance("Dragoon", 'd', ConsoleSystemInterface.MAGENTA),
			new CharAppearance("Rifleman", 'r', ConsoleSystemInterface.GREEN),
			new CharAppearance("Artillery", 'A', ConsoleSystemInterface.GRAY),
			new CharAppearance("Steamboat", 's', ConsoleSystemInterface.GRAY),
			new CharAppearance("SMG Commando", 'c', ConsoleSystemInterface.GREEN),
			new CharAppearance("Boogie Commando", 'b', ConsoleSystemInterface.GREEN),
			new CharAppearance("Urban Forces", 'u', ConsoleSystemInterface.CYAN),
			new CharAppearance("Armored Tanks", 'T', ConsoleSystemInterface.GREEN),
			new CharAppearance("Destroyer", 'D', ConsoleSystemInterface.GRAY),
			new CharAppearance("Warrior Troop", 'w', ConsoleSystemInterface.GRAY),
			new CharAppearance("Phantom Mecha", 'p', ConsoleSystemInterface.GRAY),
			new CharAppearance("ADS Unit", 'A', ConsoleSystemInterface.GRAY),
			new CharAppearance("Laser Cannon", 'C', ConsoleSystemInterface.CYAN),
			new CharAppearance("Typhoon USV", 'T', ConsoleSystemInterface.CYAN),
			new CharAppearance("Caravan", 'c', ConsoleSystemInterface.BROWN),
			new CharAppearance("Truck Caravan", 'C', ConsoleSystemInterface.GRAY),
			new CharAppearance("SETTLER", 'S', ConsoleSystemInterface.CYAN),
			new CharAppearance("Emmigrants", 'E', ConsoleSystemInterface.CYAN),
			new CharAppearance("WORKERS", 'w', ConsoleSystemInterface.RED),

			
			
		};
	};
	
	/*
	public static Unit[] getItemDefinitsions(AppearanceFactory appFactory){
		return new Unit[]{
				
			new Unit("SETTLER", "Pioneers", 1, new Roll("1D1"), new Roll("1D1"), 1, 16, false, true, Age.STONE, getSingleResourceRequirement(Resource.FOOD, 2), new ArrayList<Pair<Specialist,Integer>>()),
			new Unit("WORKERS", "Workers", 1, new Roll("1D1"), new Roll("1D1"), 1, 8, false, false, Age.STONE, getSingleResourceRequirement(Resource.FOOD, 2), new ArrayList<Pair<Specialist,Integer>>()),
			new Unit("NOMADS", "Nomads", 2, new Roll("1D2"), new Roll("1D2"), 2, 4, false, false, Age.IRON, getSingleResourceRequirement(Resource.FOOD, 1), new ArrayList<Pair<Specialist,Integer>>()),
			new Unit("MILITIA", "Militia", 1, new Roll("1D3"), new Roll("1D2"), 1, 3, false, false, Age.STONE, getSingleResourceRequirement(Resource.FOOD, 1), new ArrayList<Pair<Specialist,Integer>>()),
			new Unit("DEFENDER", "Defender", 1, new Roll("1D2"), new Roll("1D3"), 1, 3, false, false, Age.STONE, getSingleResourceRequirement(Resource.FOOD, 1), new ArrayList<Pair<Specialist,Integer>>()),
			new Unit("STONE_BARBARIANS", "Cavemen", 1, new Roll("1D2"), new Roll("1D1"), 1, 3, false, false, Age.STONE, getSingleResourceRequirement(Resource.WOOD, 1), new ArrayList<Pair<Specialist,Integer>>()),
			new Unit("TRIREME", "Trireme", 1, new Roll("1D1"), new Roll("1D1"), 1, 3, true, false, Age.STONE, getSingleResourceRequirement(Resource.FOOD, 1), new ArrayList<Pair<Specialist,Integer>>()),
			new ResourceCarrier("TRADE_CARAVAN", "Trade Caravan", 1, new Roll("1D1"), new Roll("1D1"), 1, 8, false, Age.STONE, getSingleResourceRequirement(Resource.FOOD, 1), getSingleSpecialistRequirement(Specialist.TRADER, 1)),
		};
		
	}*/

	private static List<Pair<Specialist, Integer>> getSingleSpecialistRequirement(
			Specialist specialist, int i) {
		List<Pair<Specialist, Integer>> ret = new ArrayList<Pair<Specialist,Integer>>();
		ret.add(new Pair<Specialist, Integer>(specialist, i));
		return ret;
	}

	private static List<Pair<Resource, Integer>> getSingleResourceRequirement(
			Resource resource, int i) {
		List<Pair<Resource, Integer>> ret = new ArrayList<Pair<Resource,Integer>>();
		ret.add(new Pair<Resource, Integer>(resource, i));
		return ret;
	}
	
}
