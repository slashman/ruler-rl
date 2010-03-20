package net.slashie.ruler.domain.entities;

import java.util.ArrayList;
import java.util.List;

import net.slashie.ruler.ai.CivSelector;
import net.slashie.ruler.controller.Game;
import net.slashie.ruler.domain.world.AnchoredShip;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.domain.world.WorldMessage;
import net.slashie.serf.baseDomain.AbstractItem;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.game.Player;
import net.slashie.serf.level.AbstractFeature;
import net.slashie.serf.ui.UserInterface;
import net.slashie.utils.Position;
import net.slashie.utils.Util;

public class UnitGroup extends Player{
	public static final int MAX_SIZE = 5;
	private static final int MONTHS_AUTONOMY_PARAM = 250;
	private CivilizationDefinition civilizationDef;
	private Civilization civilization;
	private boolean isBarbarian;
	private int supplies;
	private int maxUnitMovementCost = -1;
	private MovementType movementType;
	
	public UnitGroup(Civilization civilization) {
		civilizationDef = civilization.getCivDefinition();

		setAppearanceId("LEADER_"+civilizationDef.getColor().toString());
		this.civilization = civilization;
	}
	
	@Override
	public boolean canCarry(AbstractItem item, int quantity) {
		return true;
	}
	
	@Override
	public String getClassifierID() {
		return "UNIT_GROUP_"+hashCode();
	}
	
	@Override
	public int getDarkSightRange() {
		return getSightRange();
	}
	
	/**
	 * Movement cost in quarters of day
	 * @return
	 */
	public int getSpeed() {
		//Get the max cost of the units
		return maxUnitMovementCost ;
	}
	
	@Override
	public int getSightRange() {
		//TODO: Calculate using the units
		return 8;
	}
	
	@Override
	public List<? extends AbstractItem> getEquippedItems() {
		return null;
	}
	
	@Override
	public String getSaveFilename() {
		return civilizationDef.getCivilizationName();
	}

	public Civilization getCivilization() {
		return civilization;
	}

	private boolean canSettleCapability = false;
	private boolean canLayRoadsCapability = false;

	
	public boolean canSettle() {
		return canSettleCapability;
	}
	
	@Override
	public void addItem(AbstractItem toAdd, int quantity) {
		throw new UnsupportedOperationException();
	}
	
	public void addUnit(Unit toAdd) {
		super.addItem(toAdd, 1);
		onUnitsChanged();
	}
	
	
	
	@Override
	public void reduceQuantityOf(AbstractItem what) {
		reduceQuantityOf(what.getFullID(), 1);
	}
	
	@Override
	public void reduceQuantityOf(AbstractItem what, int quantity) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void reduceQuantityOf(String itemId, int quantity) {
		if (quantity != 1)
			throw new UnsupportedOperationException();
		super.reduceQuantityOf(itemId, quantity);
		onUnitsChanged();
	}
	
	private void onUnitsChanged(){
		recalcCapabilities();
		if (getAttackingUnit() != null && !hasItem(getAttackingUnit()))
			setAttackingUnit(null);
		if (getInventory().size() == 0){
			if (this == ((UnitGroup)getLevel().getPlayer()).getEnemyGroup())
				((UnitGroup)getLevel().getPlayer()).setEnemyGroup(null);
			die();
		}
	}
	
	
	
	private void recalcCapabilities() {
		// Check if there's at least a settler in the group
		canSettleCapability = false;
		maxUnitMovementCost = -1;
		movementType = MovementType.LAND;
		List<Equipment> inventory = getInventory();
		for (Equipment e: inventory) {
			Unit u = (Unit)e.getItem();
			if (u.isSettler()){
				canSettleCapability = true;
			}
			if (u.getClassifierId().equals("WORKERS")){
				canLayRoadsCapability = true;
			}
			
			if (u.getMovementCost() > maxUnitMovementCost){
				maxUnitMovementCost = u.getMovementCost(); 
			}
			if (u.isWaterUnit()){
				movementType = MovementType.WATER;
				seaUnit = u;
			}
		}
		;
		
	}

	List<String> availableActionsRedux = new ArrayList<String>();
	private Unit attackingUnit;
	private UnitGroup enemyGroup;
	
	public List<String> getAvailableActions(){
		availableActionsRedux.clear();
		availableActionsRedux.add("[f]ormUnits");
		availableActionsRedux.add("[d]isband");
		if (canSettle()){
			availableActionsRedux.add("[s]ettle");
		}
		if (canBuildRoads()){
			availableActionsRedux.add("[b]uildRoad");
		}
		City c = ((WorldLevel)getLevel()).getCityAt(getPosition()); 
		if (c != null){
			availableActionsRedux.add("[c]reateUnit");
			availableActionsRedux.add("[g]arrison");
			
			if (c.getUnits().size() > 0)
				availableActionsRedux.add("[e]nlistUnit");
			availableActionsRedux.add("[i]nfluence");
			availableActionsRedux.add("[l]oadCaravan");
			availableActionsRedux.add("[t]rade");
			
		}
		availableActionsRedux.add("[E]xamine");
		availableActionsRedux.add("[S]ave");
		availableActionsRedux.add("[Q]uit");
		return availableActionsRedux;
	}

	public boolean isBarbarian() {
		return isBarbarian;
	}

	public void setBarbarian(boolean isBarbarian) {
		this.isBarbarian = isBarbarian;
	}
	
	/**
	 * In each round, the attack and defense rolls are done. The loser unit has an 80% chance of losing 1 HP
	 * In each round, the winner unit has an 10% chance of becoming veteran
	 * If HP is zero, the unit is destroyed
		   
	 * @param attackingUnit
	 */
	public boolean attackUnit(Unit attackingUnit, UnitGroup defendingGroup, Unit defendingUnit) {
		int attackRoll = attackingUnit.getAttackRoll().roll();
		int defenseRoll = defendingUnit.getDefenseRoll().roll();
		boolean wins = attackRoll >= defenseRoll;
		if (wins){
			if (Util.chance(95)){
				defendingUnit.damage(1);
				defendingGroup.checkUnitDeath();
			}
			if (Util.chance(10)){
				attackingUnit.setVeteran(true);
				attackingUnit.levelUp();
			}
		} else {
			if (Util.chance(95)){
				attackingUnit.damage(1);
				checkUnitDeath();
			}
			if (Util.chance(5)){
				defendingUnit.setVeteran(true);
				defendingUnit.levelUp();
			}
		}
		return wins;
	}

	private void checkUnitDeath() {
		Unit removeUnit = null;
		for (Equipment unitE: getInventory()){
			Unit unit = (Unit) unitE.getItem();
			if (unit.getHP() < 1){
				removeUnit = unit;
				break;
			}
		} 
		if (removeUnit != null)
			removeUnit(removeUnit);
	}

	public Unit getAttackingUnit() {
		if (attackingUnit == null){
			if (getInventory().size() > 0)
				attackingUnit = getStrongestAttackUnit();
		}
		return attackingUnit;
	}
	
	public void setStrongestUnitAsAttacker(){
		attackingUnit = getStrongestAttackUnit();
	}

	public Unit getStrongestAttackUnit() {
		int strongestAttack = -1;
		Unit strongest = null;
		List<Unit> units = compileUnitList();
		for (Unit unit: units){
			if (unit.getAttackRoll().getMax() > strongestAttack){
				strongestAttack = unit.getAttackRoll().getMax();
				strongest = unit;
			}
		}
		return strongest;
	}

	public void setAttackingUnit(Unit attackingUnit) {
		this.attackingUnit = attackingUnit;
	}

	public Unit getDefendingUnit() {
		int higherDef = -1;
		Unit higherUnit = null;
		for (Equipment unitE: getInventory()){
			Unit u = (Unit) unitE.getItem();
			if (u.getDefenseRoll().getMax() > higherDef){
				higherDef = u.getDefenseRoll().getMax();
				higherUnit = u;
			}
		}
		return higherUnit;
	}

	public String getDescriptionWithCiv(Unit defendingUnit) {
		return getCivilization().getCivDefinition().getCivilizationName()+"' "+defendingUnit.getDescription();
	}

	public void checkDeath() {
		if (getInventory().size() == 0){
			UserInterface.getUI().refresh();
			informPlayerEvent (DEATH);
			
		}
	}

	public UnitGroup getEnemyGroup() {
		return enemyGroup;
	}

	public void setEnemyGroup(UnitGroup enemyGroup) {
		this.enemyGroup = enemyGroup;
	}

	public void reduceUnitOfClassifier(String classifierId) {
		List<Equipment> inventory = getInventory();
		for (Equipment e: inventory){
			Unit u = (Unit) e.getItem();
			if (u.getClassifierId().equals(classifierId)){
				reduceQuantityOf(u.getFullID(), 1);
				return;
			}
		}
	}


	public int getSize() {
		return getInventory().size();
	}

	@Override
	public void landOn(Position destinationPoint) {
		super.landOn(destinationPoint);
		WorldLevel l = (WorldLevel) getLevel();
		// Reembark
		AbstractFeature feature = getLevel().getFeatureAt(destinationPoint);
		if (feature instanceof AnchoredShip){
			if (getSize() > MAX_SIZE - 1){
				addWorldMessage("You can't reembark all your units", "");
			} else {
				level.destroyFeature(feature);
				addUnit(((AnchoredShip)feature).getSeaUnit());
				addWorldMessage("You board your "+((AnchoredShip)feature).getSeaUnit().getDescription(),"");
			}
		}
		
		// Siege
		City c = l.getCityAt(destinationPoint);
		if (c != null && c.getCivilization() != getCivilization()){
			boolean recapture = c.getOriginalCivilization() == getCivilization();
			if (recapture){
				addWorldMessage(
						"You recover "+c.getOriginalName()+"!",
						c.getDescription()+" has been recaptured by "+getCivilization().getCivDefinition().getCivilizationName()
						);
				c.setName(c.getOriginalName());
				c.setFlag("ALREADY_INVADED", false);
				c.releaseResource(Resource.FOOD, 2, "INVASION");
			} else {
				String newName = ((CivSelector)getSelector()).chooseNewNameForSettlement(c);
				if (c.isBarbarian() && !isBarbarian()){
					addWorldMessage(
							"You have brought civilization to the people of this tribe",
							getCivilization().getCivDefinition().getCivilizationName()+" has brought civilization to the people of this tribe"
							);
					c.civilizeBy(getCivilization(), newName);
					if (c.getFlag("ALREADY_INVADED"))
						c.releaseResource(Resource.FOOD, 2, "INVASION");
				} else {
					addWorldMessage(
						"You invade "+c.getDescription()+"!",
						c.getDescription()+" is invaded by "+getCivilization().getCivDefinition().getCivilizationName()
						);
					if (!c.getFlag("ALREADY_INVADED")){
						c.commitResource(Resource.FOOD, 2, "Invasion", "INVASION");
						c.setFlag("ALREADY_INVADED", true);
					}
				}
				if (newName != null)
					c.setName(newName);
				
				
			}
			//Claim the city
			c.changeCivilization(getCivilization());
			
		}
	}
	
	public void addWorldMessage(String youMessage, String theMessage){
		((WorldLevel)getLevel()).addWorldMessage(new WorldMessage(youMessage, theMessage, getPosition(), this));
	}

	public void removeUnit(Unit u){
		reduceQuantityOf(u);
		if (u == seaUnit)
			seaUnit = null;
	}
	
	List<Unit> reusableUnitList = new ArrayList<Unit>();
	private Unit seaUnit;
	private boolean isInfiniteSupplies; 
	public List<Unit> compileUnitList(){
		List<Equipment> eq = getInventory();
		reusableUnitList.clear();
		for (Equipment e: eq){
			reusableUnitList.add((Unit)e.getItem());
		}
		return reusableUnitList;
	}

	public int getSupplies() {
		return supplies;
	}

	public void setSupplies(int supplyDays) {
		this.supplies = supplyDays;
	}

	public void restock() {
		int requiredSupplies = MONTHS_AUTONOMY_PARAM * 3;
		supplies = requiredSupplies;
	}

	public void useSupplies(int supplyCost) {
		if (isInfiniteSupplies)
			return;
		supplies -= supplyCost;
		if (supplies < 0)
			supplies = 0;
		
	}
	
	@Override
	public void updateStatus() {
		super.updateStatus();
		if (needsSupplies() && supplies == 0){
			if (this == Game.getCurrentGame().getPlayer())
				addWorldMessage("You are starving!","");
			damageRandomUnit();
		}
	}

	private boolean needsSupplies() {
		return ((WorldLevel)getLevel()).getCityAt(getPosition()) == null;
	}

	private void damageRandomUnit() {
		if (getInventory().size() == 0){
			return;
		}
		if (Util.chance(20)){
			Unit randomUnit = (Unit)Util.randomElementOf(compileUnitList());
			if (randomUnit.isWaterUnit())
				return;
			randomUnit.damage(1);
			checkUnitDeath();
		}
	}

	public MovementType getMovementType() {
		return movementType;
	}

	public void setMovementType(MovementType movementType) {
		this.movementType = movementType;
	}

	public Unit getSeaUnit() {
		return seaUnit;
	}

	public boolean canBuildRoads() {
		return canLayRoadsCapability;
	}

	public boolean isInfiniteSupplies() {
		return isInfiniteSupplies;
	}

	public void setInfiniteSupplies(boolean isInfiniteSupplies) {
		this.isInfiniteSupplies = isInfiniteSupplies;
	}



	
}
