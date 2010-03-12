package net.slashie.ruler.factory;

import java.util.Hashtable;

import net.slashie.ruler.domain.entities.Unit;

public class ItemFactory {
	private static Hashtable<String, Unit> definitions = new Hashtable<String, Unit>();
	public static void init(Unit[] definitions_){
		for (int i = 0; i < definitions_.length; i++)
			definitions.put(definitions_[i].getClassifierId(), definitions_[i]);
	}
	
	public static Unit createItem(String itemId){
		Unit ret = definitions.get(itemId);
		if (ret == null){

		}
		return (Unit) ret.clone();
	}


}
