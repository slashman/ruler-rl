package net.slashie.ruler.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.slashie.ruler.action.player.Walk;
import net.slashie.ruler.ai.unitGroup.CivilizationAI;
import net.slashie.ruler.controller.Game;
import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.ruler.domain.entities.CivilizationColors;
import net.slashie.ruler.domain.entities.CivilizationDefinition;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.Age;

public class CivilizationGenerator {
	private static Map<String, CivilizationDefinition> civDefinitions;
	private static List<CivilizationDefinition> civList;
	public static void init(Properties civFile){
		civDefinitions = new HashMap<String, CivilizationDefinition>();
		civList = new ArrayList<CivilizationDefinition>();
		String[] civKeys = civFile.getProperty("civKeys").split(",");
		for (String civKey: civKeys){
			CivilizationDefinition definition = new CivilizationDefinition();
			definition.setCivilizationName(civFile.getProperty(civKey+"_name"));
			definition.setDefaultLeaderName(civFile.getProperty(civKey+"_leaderName"));
			definition.setColor(CivilizationColors.valueOf(civFile.getProperty(civKey+"_color")));
			definition.setCityNames(civFile.getProperty(civKey+"_cities").split(","));
			definition.setCivilizationId(civKey);
			civDefinitions.put(civKey, definition);
			civList.add(definition);
		}
		
	}
	
	public static UnitGroup getCivilization(String civId, String leaderName){
		Civilization civ = new Civilization(Age.STONE);
		civ.setCivDefinition(civDefinitions.get(civId));
		if (leaderName.trim().equals("")){
			leaderName = civ.getCivDefinition().getDefaultLeaderName();
		}
		civ.setLeaderName(leaderName);
		
		UnitGroup ret = new UnitGroup(civ);
		ret.addUnit(ItemFactory.createItem(Unit.SETTLER_ID));
		ret.addUnit(ItemFactory.createItem(Unit.SETTLER_ID));
		for (int i = 0; i < 2; i++){
			ret.addUnit(ItemFactory.createItem("Axemen"));
		}
		ret.setStrongestUnitAsAttacker();
		ret.setName("Caravan");
		ret.restock();
		return ret;
	}

	public static List<CivilizationDefinition> getCivilizationDefs() {
		return civList;
	}


	public static Civilization getBarbarianCiv(CivilizationDefinition civilizationDef) {
		Civilization ret = new Civilization(Age.STONE);
		ret.setCivDefinition(civilizationDef);
		ret.setLeaderName("No one");
		return ret;
	}

	public static Civilization getCivilization(CivilizationDefinition civilizationDef) {
		Civilization ret = new Civilization(Age.STONE);
		ret.setCivDefinition(civilizationDef);
		ret.setLeaderName(civilizationDef.getDefaultLeaderName());
		ret.setCurrentAge(Age.STONE);
		UnitGroup caravan = new UnitGroup(ret);
		//ret.addItem(ItemFactory.createItem("SETTLER"), 2);
		caravan.addUnit(ItemFactory.createItem("SETTLER"));
		caravan.addUnit(ItemFactory.createItem("SETTLER"));
		for (int i = 0; i < 2; i++){
			caravan.addUnit(ItemFactory.createItem("Axemen"));
		}
		caravan.setStrongestUnitAsAttacker();
		caravan.setName("Caravan");
		caravan.restock();
		caravan.setAppearanceId("LEADER_"+civilizationDef.getColor().toString());
		caravan.setSelector(new CivilizationAI(caravan, Game.getCurrentGame().getPlayer(), new Walk()));
		caravan.setBarbarian(false);
		caravan.setInfiniteSupplies(true);
		ret.setLeaderGroup(caravan);
		return ret;
	}
}
