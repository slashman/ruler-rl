package net.slashie.ruler.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import net.slashie.ruler.action.player.Walk;
import net.slashie.ruler.ai.unitGroup.BarbarianAI;
import net.slashie.ruler.ai.unitGroup.CivilizationAI;
import net.slashie.ruler.controller.Game;
import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.ruler.domain.entities.CivilizationDefinition;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.Age;
import net.slashie.util.Pair;
import net.slashie.utils.Util;

public class UnitGroupFactory {
	private static HashMap<String,UnitGroupDef> groupDefinitions;
	private static Hashtable<Age, List<String>> groupsByAge = new Hashtable<Age, List<String>>();
	
	public static void init(Properties barbariansFile){
		groupDefinitions = new HashMap<String, UnitGroupDef>();
		String[] groupKeys = barbariansFile.getProperty("groupKeys").split(",");
		for (String groupKey: groupKeys){
			groupKey = groupKey.trim();
			UnitGroupDef definition = new UnitGroupDef();
			definition.setName(barbariansFile.getProperty(groupKey+"_name"));
			definition.setApperanceId(barbariansFile.getProperty(groupKey+"_icon"));
			String[] unitsString = barbariansFile.getProperty(groupKey+"_units").split(",");
			for (String unitString: unitsString){
				//1 to 2 STONE_BARBARIANS
				String[] unitPair = unitString.trim().split(" ");
				definition.addUnit(unitPair[3], Integer.parseInt(unitPair[0]), Integer.parseInt(unitPair[2]));
			}
			Age age = Age.valueOf(barbariansFile.getProperty(groupKey+"_age"));
			List<String> groupsForAge = groupsByAge.get(age);
			if (groupsForAge == null){
				groupsForAge = new ArrayList<String>();
				groupsByAge.put(age, groupsForAge);
			}
			groupsForAge.add(groupKey);
			groupDefinitions.put(groupKey, definition);
		}
	}
	
	public static UnitGroup getBarbarianUnitGroup (Civilization civ, Age age){
		UnitGroupDef def = groupDefinitions.get(pickRandomGroupIdFromAge(age));
		UnitGroup ret = new UnitGroup(civ);
		ret.setAppearanceId("GROUP_"+civ.getCivDefinition().getColor().toString());
		ret.setName(def.getName());
		List<Pair<String, Pair<Integer, Integer>>> units = def.getUnits();
		for (Pair<String, Pair<Integer, Integer>> unit: units){
			int quantity = Util.rand(unit.getB().getA(), unit.getB().getB());
			String unitId = unit.getA();
			for (int i = 0; i < quantity; i++){
				ret.addUnit(ItemFactory.createItem(unitId));
			}
		}
		ret.setSelector(new BarbarianAI(Game.getCurrentGame().getPlayer(), new Walk()));
		ret.setBarbarian(true);
		return ret;
	}
	

	
	

	private static String pickRandomGroupIdFromAge(Age age) {
		List<String> groupIds = groupsByAge.get(age);
		return (String)Util.randomElementOf(groupIds);
	}
}
