package net.slashie.ruler.domain.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.ruler.domain.entities.Garrison;
import net.slashie.ruler.domain.entities.Specialist;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.factory.ItemFactory;
import net.slashie.ruler.ui.RulerAppearanceFactory;
import net.slashie.serf.action.Action;
import net.slashie.serf.level.AbstractFeature;
import net.slashie.serf.ui.Appearance;
import net.slashie.serf.ui.AppearanceFactory;
import net.slashie.util.Pair;
import net.slashie.utils.Position;
import net.slashie.utils.Util;

public class City extends AbstractFeature{
	// Number of units that can defend a city per each city size
	public static final int GARRISON_SIZE = 5;
	private String name;
	private String originalName;
	private Map<Resource, Integer> resources = new Hashtable<Resource, Integer>();
	private Map<Resource, Integer> commitResources = new Hashtable<Resource, Integer>();
	private Map<Resource, Integer> tradedResources = new Hashtable<Resource, Integer>();
	private Map<Specialist, Integer> specialists = new Hashtable<Specialist, Integer>();
	private Map<String, String> commits = new Hashtable<String, String>();
	
	private int size = 1;
	private Civilization civ;
	private Civilization originalCivilization;
	private transient Appearance appearance;
	
	private Garrison garrison;
	
   	  // Specialist production
	private Specialist influencedSpecialist;
	
	  // Unit Assembly
	private Unit currentAssemblyUnit;
	List<Pair<Resource, Integer>> pendingResources;
	private boolean isBarbarian;

	public City(String name, int size, Civilization civ) {
		this.civ = civ;
		this.name = name;
		this.originalCivilization = civ;
		this.originalName = name;
		civ.addCity(this);
		updateAppearance();
	}
	
	public void updateAppearance(){
		setAppearance(RulerAppearanceFactory.thus.getSettlementAppearance(size, civ));
	}
	
	
	@Override
	public Appearance getAppearance() {
		if (appearance == null)
			updateAppearance();
		return appearance;
	}
	
	@Override
	public String getClassifierID() {
		return "City"+super.toString();
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
					if (!cityLevel.isValidCoordinate(x, y))
						continue;
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

	public int getAvailableResources(Resource r) {
		return getFullResources(r) - getCommitResources(r) + getTradedResources(r);
	}
	
	public int getCommitResources(Resource r) {
		Integer c = commitResources.get(r);
		if (c == null)
			c = 0;
		return c;
	}
	
	public int getFullResources(Resource r) {
		Integer q = resources.get(r);
		if (q == null)
			q = 0;
		return q;
	}
	
	public int getTradedResources(Resource r) {
		Integer q = tradedResources.get(r);
		if (q == null)
			q = 0;
		return q;
	}
	
	public int getFoodConsumption(){
		//return (int)(Math.pow(getSize()+1, 3)); 
		return getSize() * 2;
	}

	public void tryGrow() {
		int food = getAvailableResources(Resource.FOOD);
		//TODO: Enhance calculations
		int remaining = food - getFoodConsumption();
		if (remaining < 0){
			if (Position.distance(getLevel().getPlayer().getPosition(), getPosition()) < 10){
				youMessage("The people of "+getDescription()+" starve!");
			}
			if (size > 0){
				size--;
				sizeChanged();
			}
			
		}
		if (remaining > 0){
			if (Position.distance(getLevel().getPlayer().getPosition(), getPosition()) < 10){
				youMessage("The population of "+getDescription()+" increases!");
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
	

	public List<Unit> getAvailableUnitsForAssembly() {
		List<Unit> ret = new ArrayList<Unit>();
		Age age = civ.getCurrentAge();
		Age preAge = age.getPreviousAge();
		addUnitsByAge(ret, age);
		if (preAge != null)
			addUnitsByAge(ret, preAge);
		return ret;
	}
	
	private void addUnitsByAge(List<Unit> ret, Age age){
		List<Unit> unitsByAge = ItemFactory.getUnitsByAge(age);
		if (unitsByAge == null)
			return;
		nextUnit: for (Unit unit: unitsByAge){
			if (unit.requiresCoast()){
				if (!isCoast()){
					continue nextUnit;
				}
			}
			List<Pair<Resource, Integer>> requirements = unit.getResourceRequirements();
			for (Pair<Resource, Integer> requirement: requirements){
				if (getAvailableResources(requirement.getA()) < requirement.getB()){
					continue nextUnit;
				}
			}
			
			// Check Specialist Cost
			List<Pair<Specialist, Integer>> specialistRequirements = unit.getSpecialistRequirements();
			for (Pair<Specialist, Integer> specialistRequirement: specialistRequirements){
				if (getSpecialists(specialistRequirement.getA()) < specialistRequirement.getB()){
					continue nextUnit;
				}
			}
			
			ret.add(unit);
		}
	}

	private boolean isCoast() {
		for (int x = getPosition().x-1; x <= getPosition().x+1; x++)
			for (int y = getPosition().y-1; y <= getPosition().y+1; y++){
				if (!getLevel().isValidCoordinate(x,y))
					continue;
				if (((WorldTile)getLevel().getMapCell(x, y, 0)).isWater())
					return true;
			}
		return false;
	}

	public Unit getCurrentAssemblyUnit() {
		return currentAssemblyUnit;
	}

	public void setCurrentAssemblyUnit(Unit currentAssemblyUnit) {
		this.currentAssemblyUnit = currentAssemblyUnit;
	}


	public void commitResource(Resource r, Integer q, String commitId) {
		commitResource(r, q, "", commitId);
	}

	public void commitResource(Resource r, Integer q, String commitTo, String commitId) {
		if (!commitTo.equals(""))
			commits.put(commitId, commitTo+": "+q+" "+r.getDescription());
		Integer cq = commitResources.get(r);
		if (cq == null)
			commitResources.put(r, q);
		else
			commitResources.put(r, cq+q);
	}
	
	public void releaseResource(Resource r, Integer q, String commitId){
		commits.remove(commitId);
		Integer cq = commitResources.get(r);
		commitResources.put(r, cq-q);
	}
	
	public List<String> getCommitmentsMessages(){
		List<String> ret = new ArrayList<String>();
		for (String key: commits.keySet()){
			ret.add(commits.get(key));
		}
		Collections.sort(ret);
		return ret;
	}

	public void spendSpecialistsFor(Unit unit) {
		List<Pair<Specialist, Integer>> specialistRequirements = unit.getSpecialistRequirements();
		for (Pair<Specialist, Integer> specialistRequirement: specialistRequirements){
			reduceSpecialists(specialistRequirement.getA(), specialistRequirement.getB());
		}
	}

	private void reduceSpecialists(Specialist a, Integer b) {
		Integer q = specialists.get(a);
		if (q == null && b == 0){
			return;
		}
		specialists.put(a, q-b);
	}

	public void advanceProduction() {
		if (pendingResources == null || currentAssemblyUnit == null)
			return;
		for (Pair<Resource, Integer> pending: pendingResources){
			pending.setB(pending.getB() - currentAssemblyUnit.getRequiredResource(pending.getA()) );
		}
		
		boolean completed = true;
		//Check if production is completed
		for (Pair<Resource, Integer> pending: pendingResources){
			if (pending.getB() > 0)
				completed = false;
		}
		
		if (completed){
			youMessage(currentAssemblyUnit.getDescription()+" raised at "+getDescription());
			// Release resources to city
			List<Pair<Resource, Integer>> requirements = currentAssemblyUnit.getResourceRequirements();
			for (Pair<Resource, Integer> requirement: requirements){
				releaseResource(requirement.getA(), requirement.getB(), "COM_"+currentAssemblyUnit.getClassifierId());
			}
			addUnit((Unit)currentAssemblyUnit.clone());
			currentAssemblyUnit = null;
		}
		
		
	}
	
	private void youMessage(String string) {
		if (getCivilization() == ((UnitGroup)level.getPlayer()).getCivilization())
			level.addMessage(string);
	}

	public void addUnit(Unit unit) {
		if (garrison == null)
			initGarrison((WorldLevel)getLevel());
		garrison.addUnit(unit);
		commitResource(Resource.FOOD, 1, "GARRISON_UNIT_"+unit.getFullID());
	}

	public void startCreation(Unit selectedUnit) {
		currentAssemblyUnit = selectedUnit;
		// Commit resources to unit
		List<Pair<Resource, Integer>> requirements = selectedUnit.getResourceRequirements();
		pendingResources = new ArrayList<Pair<Resource,Integer>>(); 
		for (Pair<Resource, Integer> requirement: requirements){
			commitResource(requirement.getA(), requirement.getB(), "Raise "+selectedUnit.getDescription(), "COM_"+selectedUnit.getClassifierId());
			pendingResources.add(new Pair<Resource, Integer>(requirement.getA(), requirement.getB()));
		}
	}

	public void setInfluencedSpecialist(Specialist selectedSpecialist) {
		this.influencedSpecialist = selectedSpecialist;
	}

	public List<Unit> getUnits() {
		if (garrison == null)
			return new ArrayList<Unit>();
		else
			return garrison.compileUnitList();
	}

	public void removeUnit(Unit selectedUnit) {
		garrison.removeUnit(selectedUnit);
	}

	public void addTradeAgreement(City from, Resource acquiredResource, int quantity,
			String tradeId) {
		commits.put(tradeId, "Trade from "+from.getDescription()+": "+quantity+" "+acquiredResource.getDescription());
		Integer q = tradedResources.get(acquiredResource);
		if (q == null)
			tradedResources.put(acquiredResource, quantity);
		else
			tradedResources.put(acquiredResource, q+quantity);
	}

	public Civilization getCivilization() {
		return civ;
	}
	
	public void changeCivilization(Civilization civ) {
		this.civ.removeCity(this);
		this.civ = civ;
		this.civ.addCity(this);
		
		updateAppearance();
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public Civilization getOriginalCivilization() {
		return originalCivilization;
	}

	private void initGarrison(WorldLevel worldLevel) {
		this.garrison = new Garrison(this);
		this.garrison.setPosition(getPosition());
		this.garrison.setBarbarian(isBarbarian());
		worldLevel.addActor(this.garrison);
	}

	public boolean isBarbarian() {
		return isBarbarian;
	}
	
	public void setBarbarian(boolean val){
		this.isBarbarian = val;
	}
	
	public void civilizeBy(Civilization civ, String name){
		this.originalCivilization = civ;
		this.originalName = name;
		setBarbarian(false);
	}
	
	public int getScienceOutput(){
		return  
			getSpecialists(Specialist.TINKER)+
			getSpecialists(Specialist.SCIENTIFIC) * 3;
	}

	public void advanceScience() {
		civ.addScienceProduction(getScienceOutput());
	}


	public Unit getStrongestAttackUnit() {
		return garrison.getStrongestAttackUnit();
	}

	public boolean canCreateSettler() {
		List<Unit> units = getAvailableUnitsForAssembly();
		for (Unit u: units){
			if (u.getClassifierId().startsWith(Unit.SETTLER_ID))
				return true;
		}
		return false;
	}

}
