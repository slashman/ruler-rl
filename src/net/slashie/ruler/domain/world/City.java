package net.slashie.ruler.domain.world;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.ruler.domain.entities.Specialist;
import net.slashie.serf.action.Action;
import net.slashie.serf.level.AbstractFeature;
import net.slashie.serf.ui.Appearance;
import net.slashie.serf.ui.AppearanceFactory;
import net.slashie.util.Pair;
import net.slashie.utils.Position;
import net.slashie.utils.Util;

public class City extends AbstractFeature{
	private String name;
	private Map<Resource, Integer> resources = new Hashtable<Resource, Integer>();
	private Map<Specialist, Integer> specialists = new Hashtable<Specialist, Integer>();
	private int size = 1;
	private Civilization civ;
	private Appearance appearance;
	private Specialist influencedSpecialist;

	public City(String name, int size, Civilization civ) {
		this.civ = civ;
		this.name = name;
		updateAppearance();
	}
	
	public void updateAppearance(){
		setAppearance(AppearanceFactory.getAppearanceFactory().getAppearance("SETTLEMENT_"+size+"_"+civ.getCivDefinition().getColor()));
	}
	
	
	@Override
	public Appearance getAppearance() {
		return appearance;
	}
	
	@Override
	public String getClassifierID() {
		return "City";
	}
	
	@Override
	public String getDescription() {
		return name;
	}
	
	public void calculateResources(){
		WorldLevel cityLevel = (WorldLevel) getLevel();
		for (Resource resource: Resource.values()){
			resources.remove(resource);
		}
		int range = getResourceRange();
		for (int x = getPosition().x - range; x <= getPosition().x + range; x++){
			for (int y = getPosition().y - range; y <= getPosition().y + range; y++){
				for (Resource resource: Resource.values()){
					addResource(resource, cityLevel.getResourcesAt(x,y,resource));
				}
			}
		}
	}
	
	public int getResourceRange(){
		return (int)Math.floor(getSize() / 3.0d) +1;
	}
	
	public int getSize() {
		return size;
	}

	public void addResource(Resource r, int quantity){
		Integer q = resources.get(r);
		if (q == null)
			resources.put(r, quantity);
		else
			resources.put(r, q+quantity);
	}

	public int getResources(Resource r) {
		Integer q = resources.get(r);
		if (q == null)
			return 0;
		else
			return q;
	}
	
	public int getFoodConsumption(){
		//return (int)(Math.pow(getSize()+1, 3)); 
		return getSize() * 4;
	}

	public void tryGrow() {
		int food = getResources(Resource.FOOD);
		//TODO: Enhance calculations
		int remaining = food - getFoodConsumption();
		if (remaining < 0){
			if (Position.distance(getLevel().getPlayer().getPosition(), getPosition()) < 10){
				getLevel().addMessage("The people of "+getDescription()+" starve!");
			}
			if (size > 0){
				size--;
				sizeChanged();
			}
			
		}
		if (remaining > 0){
			if (Position.distance(getLevel().getPlayer().getPosition(), getPosition()) < 10){
				getLevel().addMessage("The population of "+getDescription()+" increases!");
			}
			if (size < 15){
				size++;
				sizeChanged();
			}
		}
	}
	
	private void sizeChanged(){
		updateAppearance();
		calculateResources();
	}

	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
	}

	public void trySpecialists() {
		if (influencedSpecialist == null)
			influencedSpecialist = Specialist.SOLDIER;
		// get the specialists
		int numberOfSpecialists = getSize();
		for (int i = 0; i < numberOfSpecialists; i++){
			if (Util.chance(10))
				continue;
			addSpecialist(influencedSpecialist);
		}
	}

	private void addSpecialist(Specialist s) {
		Integer q = specialists.get(s);
		if (q == null)
			specialists.put(s, 1);
		else
			specialists.put(s, q+1);
	}

	public int getSpecialists(Specialist sp) {
		Integer q = specialists.get(sp);
		if (q == null)
			return 0;
		else
			return q;
	}
}
