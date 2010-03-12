package net.slashie.ruler.factory;

import java.util.ArrayList;
import java.util.List;

import net.slashie.util.Pair;

public class UnitGroupDef {
	private String name;
	private String apperanceId;
	private List<Pair<String, Pair<Integer, Integer>>> units = new ArrayList<Pair<String,Pair<Integer,Integer>>>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApperanceId() {
		return apperanceId;
	}
	public void setApperanceId(String apperanceId) {
		this.apperanceId = apperanceId;
	}
	
	
	public void addUnit(String unitId, int min, int max) {
		units.add(new Pair<String, Pair<Integer, Integer>> (unitId, new Pair<Integer, Integer>(min, max)));
	}
	public List<Pair<String, Pair<Integer, Integer>>> getUnits() {
		return units;
	}
}
