package net.slashie.ruler.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.ruler.domain.entities.CivilizationColors;
import net.slashie.ruler.domain.entities.CivilizationDefinition;
import net.slashie.ruler.domain.entities.UnitGroup;

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
			definition.setColor(CivilizationColors.valueOf(civFile.getProperty(civKey+"_color")));
			definition.setCivilizationId(civKey);
			civDefinitions.put(civKey, definition);
			civList.add(definition);
		}
		
	}
	
	public static UnitGroup getCivilization(String civId, String leaderName){
		Civilization civ = new Civilization();
		civ.setLeaderName(leaderName);
		civ.setCivDefinition(civDefinitions.get(civId));
		UnitGroup ret = new UnitGroup(civ);
		//ret.addItem(ItemFactory.createItem("SETTLER"), 2);
		ret.addUnit(ItemFactory.createItem("SETTLER"));
		ret.addUnit(ItemFactory.createItem("SETTLER"));
		for (int i = 0; i < 2; i++){
			ret.addUnit(ItemFactory.createItem("NOMADS"));
		}
		ret.setName("Caravan");
		return ret;
	}

	public static List<CivilizationDefinition> getCivilizationDefs() {
		return civList;
	}


	public static Civilization getBarbarianCiv(CivilizationDefinition civilizationDef) {
		Civilization ret = new Civilization();
		ret.setCivDefinition(civilizationDef);
		ret.setLeaderName("No one");
		return ret;
	}
}
