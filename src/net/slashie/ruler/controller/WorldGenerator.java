package net.slashie.ruler.controller;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.ruler.action.player.Walk;
import net.slashie.ruler.action.world.NPWalk;
import net.slashie.ruler.ai.world.DayTicker;
import net.slashie.ruler.ai.world.MonthTicker;
import net.slashie.ruler.ai.world.TimeTickerAgent;
import net.slashie.ruler.ai.world.YearTicker;
import net.slashie.ruler.domain.entities.CivilizationDefinition;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.Age;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.factory.CivilizationGenerator;
import net.slashie.ruler.factory.ItemFactory;
import net.slashie.ruler.factory.UnitGroupFactory;
import net.slashie.ruler.procedural.level.CaveLevelGenerator;
import net.slashie.serf.ai.SimpleAI;
import net.slashie.serf.level.Dispatcher;
import net.slashie.serf.level.MapCellFactory;
import net.slashie.serf.ui.consoleUI.CharAppearance;
import net.slashie.utils.Position;
import net.slashie.utils.Util;

public class WorldGenerator {
	private static Map<String, Integer> proportions = new Hashtable<String, Integer>();
	static {
		proportions.put("GRASSLANDS,GRASSLANDS_HORSES", 3);
		proportions.put("MOUNTAINS,MOUNTAINS_GOLD", 0);
		proportions.put("MOUNTAINS,MOUNTAINS_IRON", 0);
		proportions.put("MOUNTAINS,MOUNTAINS_PLUTONIUM",0);
		proportions.put("DESERT,DESERT_OIL",0);
	}
	public static WorldLevel generateWorld(String levelID, int width, int height){
		WorldLevel ret = new WorldLevel();
		//Generate the physical level
		CaveLevelGenerator clg = new CaveLevelGenerator();
		clg.init("OCEAN", "GRASSLANDS");
		ret = clg.generateLevel(width, height);
		ret.setID(levelID);
		ret.setMusicKey("WORLD");
		ret.setDescription("World");
		
		// Sprinkle resources
		Set<Entry<String,Integer>> set = proportions.entrySet();
		Position random = new Position(0,0);
		for (Entry<String, Integer> entry : set){
			int quantity = (int)Math.round((double)width * (double)height * (double)entry.getValue() / 100.0d);
			String required = entry.getKey().split(",")[0];
			String target = entry.getKey().split(",")[1];
			for (int i = 0, safe = 0; i < quantity && safe < 10000; i++, safe++){
				random = new Position(Util.rand(0, width-1), Util.rand(0, height-1));
				if (ret.getMapCell(random).getID().equals(required))
					ret.getCells()[0][random.x][random.y] = MapCellFactory.getMapCellFactory().getMapCell(target);
				else
					i--;
			}
		}
		
		ret.setDispatcher(new Dispatcher());
		
		
		// Add abstract actors
		ret.addActor(new TimeTickerAgent(new DayTicker()));
		ret.addActor(new TimeTickerAgent(new YearTicker()));
		ret.addActor(new TimeTickerAgent(new MonthTicker()));
		
		
		
		// Add barbarian groups
		
		List<CivilizationDefinition> civs = CivilizationGenerator.getCivilizationDefs();
		Position ran = new Position(0,0);
		for (CivilizationDefinition civ: civs){
			int barbarianGroups = Util.rand(5, 10);
			barbarianGroups = 1;
			for (int i = 0; i < barbarianGroups; i++){
				ran.x = Util.rand(1, width - 1);
				ran.y = Util.rand(1, height - 1);
				if (ret.isWater(ran)){
					i--;
					continue;
				}
				UnitGroup barbarians = UnitGroupFactory.getUnitGroup(civ, Age.STONE);
				barbarians.setSelector(new SimpleAI(Game.getCurrentGame().getPlayer(), new Walk()));
				barbarians.setBarbarian(true);
				ret.addActor(barbarians, new Position(ran));
				
			}
		}
		return ret;
	}

	
}
