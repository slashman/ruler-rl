package net.slashie.ruler.factory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.world.Age;

public class ItemFactory {
	private static Hashtable<String, Unit> definitions = new Hashtable<String, Unit>();
	private static Map<Age, List<Unit>> hashUnitsByAge = new Hashtable<Age, List<Unit>>(); 
	public static void init(Unit[] definitions_){
		for (int i = 0; i < definitions_.length; i++){
			definitions.put(definitions_[i].getClassifierId(), definitions_[i]);
			List<Unit> unitsByAge = hashUnitsByAge.get(definitions_[i].getAvailableAge());
			if (unitsByAge == null){
				unitsByAge = new ArrayList<Unit>();
				hashUnitsByAge.put(definitions_[i].getAvailableAge(), unitsByAge);
			}
			unitsByAge.add(definitions_[i]);
		}
	}
	
	public static Unit createItem(String itemId){
		Unit ret = definitions.get(itemId);
		if (ret == null){

		}
		return (Unit) ret.clone();
	}

	public static List<Unit> getUnitsByAge(Age age) {
		return hashUnitsByAge.get(age);
	}


}
