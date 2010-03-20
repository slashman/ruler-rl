package net.slashie.ruler.domain.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.slashie.ruler.controller.Game;
import net.slashie.ruler.domain.world.Age;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.ui.Display;
import net.slashie.ruler.ui.RulerUserInterface;
import net.slashie.serf.action.Actor;
import net.slashie.serf.ui.UserInterface;

public class Civilization implements Serializable{
	private String leaderName;
	private CivilizationDefinition civDefinition;
	private Map<String, String> enemies = new Hashtable<String, String>();
	private Age currentAge;
	private int currentTechAdvancement;
	private int nextAgeAdvancement = 500;
	private List<City> cities = new ArrayList<City>();
	private Actor leaderGroup;
	
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

	public Age getCurrentAge() {
		return currentAge;
	}
	public void setCurrentAge(Age currentAge) {
		this.currentAge = currentAge;
	}
	public List<Specialist> getAvailableSpecialists() {
		return currentAge.getSpecialists();
	}

	public void addScienceProduction(int science){
		currentTechAdvancement += science;
		if (currentTechAdvancement > nextAgeAdvancement ){
			if (currentAge.getNextAge() != null){
				currentAge = currentAge.getNextAge();
				if (this == ((UnitGroup)Game.getCurrentGame().getPlayer()).getCivilization())
					Display.thus.showAgeUp(this, currentAge);
				Game.getCurrentGame().getPlayer().getLevel().addMessage(leaderName+" takes "+civDefinition.getCivilizationName()+" to the "+currentAge.getDescription());
				nextAgeAdvancement *= 2;
				currentTechAdvancement = 0;
			}
		}
	}
	
	public void addCity(City c){
		cities.add (c);
	}
	
	public void removeCity(City c){
		cities.remove(c);
	}
	
	public int getTotalScienceOutput(){
		int acum = 0;
		for (City city: cities){
			acum += city.getScienceOutput();
		}
		return acum;
	}
	public int getNextScienceCost() {
		return nextAgeAdvancement;
	}

	public Actor getLeaderGroup() {
		return leaderGroup;
	}
	public void setLeaderGroup(Actor leaderGroup) {
		this.leaderGroup = leaderGroup;
	}
	
	public Civilization(Age currentAge) {
		setCurrentAge(currentAge);
	}
	public int  getAccumulatedScienceOutput() {
		return currentTechAdvancement;
	}
}
