package net.slashie.ruler.data.dao;

import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.serf.level.AbstractCell;
import net.slashie.serf.ui.AppearanceFactory;
import net.slashie.serf.ui.consoleUI.CharAppearance;
import net.slashie.utils.roll.Roll;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.world.WorldTile;

public class EntitiesDAO {
	
	public static AbstractCell[] getCellDefinitions (AppearanceFactory appFactory){
		return new AbstractCell[]{
			//Overworld cells
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
			new CharAppearance("LEADER_LEMON", '@', ConsoleSystemInterface.LEMON),
			new CharAppearance("LEADER_BLUE", '@', ConsoleSystemInterface.BLUE),
			new CharAppearance("LEADER_RED", '@', ConsoleSystemInterface.RED),
			
			//Groups
			new CharAppearance("GROUP_LEMON", '@', ConsoleSystemInterface.GREEN),
			new CharAppearance("GROUP_BLUE", '@', ConsoleSystemInterface.DARK_BLUE),
			new CharAppearance("GROUP_RED", '@', ConsoleSystemInterface.DARK_RED),
			
			// Settlements
			new CharAppearance("SETTLEMENT_1_LEMON", '1', ConsoleSystemInterface.GREEN),
			new CharAppearance("SETTLEMENT_2_LEMON", '2', ConsoleSystemInterface.GREEN),
			new CharAppearance("SETTLEMENT_3_LEMON", '3', ConsoleSystemInterface.GREEN),
			
			new CharAppearance("SETTLEMENT_1_BLUE", '1', ConsoleSystemInterface.BLUE),
			new CharAppearance("SETTLEMENT_2_BLUE", '2', ConsoleSystemInterface.BLUE),
			new CharAppearance("SETTLEMENT_3_BLUE", '3', ConsoleSystemInterface.BLUE),
			
			new CharAppearance("SETTLEMENT_1_RED", '1', ConsoleSystemInterface.RED),
			new CharAppearance("SETTLEMENT_2_RED", '2', ConsoleSystemInterface.RED),
			new CharAppearance("SETTLEMENT_3_RED", '3', ConsoleSystemInterface.RED),
			
			//Cells
			new CharAppearance("WOODS", '&', ConsoleSystemInterface.GREEN),
			new CharAppearance("GRASSLANDS", '.', ConsoleSystemInterface.GREEN),
			new CharAppearance("GRASSLANDS_HORSES", 'h', ConsoleSystemInterface.BROWN),
			new CharAppearance("MOUNTAINS", '^', ConsoleSystemInterface.GRAY),
			new CharAppearance("MOUNTAINS_GOLD", '^', ConsoleSystemInterface.YELLOW),
			new CharAppearance("MOUNTAINS_IRON", '^', ConsoleSystemInterface.WHITE),
			new CharAppearance("MOUNTAINS_PLUTONIUM", '^', ConsoleSystemInterface.LEMON),
			new CharAppearance("DESERT", '~', ConsoleSystemInterface.YELLOW),
			new CharAppearance("DESERT_OIL", 'o', ConsoleSystemInterface.GRAY),
			new CharAppearance("OCEAN", '~', ConsoleSystemInterface.BLUE),
			
			//Units
			new CharAppearance("STONE_BARBARIANS", 'B', ConsoleSystemInterface.BROWN),
			new CharAppearance("NOMADS", 'N', ConsoleSystemInterface.BROWN),
			new CharAppearance("SETTLER", 'S', ConsoleSystemInterface.CYAN),
			
			
		};
	};
	
	public static Unit[] getItemDefinitions(AppearanceFactory appFactory){
		return new Unit[]{
			new Unit("SETTLER", "Pioneers", 1, new Roll("1D1"), new Roll("1D1"), 1),
			new Unit("NOMADS", "Nomads", 2, new Roll("1D2"), new Roll("1D2"), 2),
			new Unit("STONE_BARBARIANS", "Cavemen", 1, new Roll("1D2"), new Roll("1D1"), 1),
		};
		
	}
	
}
