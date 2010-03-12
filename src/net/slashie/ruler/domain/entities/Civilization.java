package net.slashie.ruler.domain.entities;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class Civilization implements Serializable{
	private String leaderName;
	private CivilizationDefinition civDefinition;
	private Map<String, String> enemies = new Hashtable<String, String>(); 
	
	public String getLeaderName() {
		return leaderName;
	}
	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	public CivilizationDefinition getCivDefinition() {
		return civDefinition;
	}
	public void setCivDefinition(CivilizationDefinition civDefinition) {
		this.civDefinition = civDefinition;
	}

	public boolean hasEnemy(Civilization civilization) {
		String val = enemies.get(civilization.getCivDefinition().getCivilizationId()); 
		return val != null && val.equals("true");
	}
	
	public void setEnemy(Civilization civilization){
		enemies.put(civilization.getCivDefinition().getCivilizationId(), "true");
	}
	
	public void setPeace(Civilization civilization){
		enemies.put(civilization.getCivDefinition().getCivilizationId(), "false");
	}
	
	
}
