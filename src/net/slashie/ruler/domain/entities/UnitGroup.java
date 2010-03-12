package net.slashie.ruler.domain.entities;

import java.util.ArrayList;
import java.util.List;

import net.slashie.ruler.controller.Game;
import net.slashie.ruler.factory.CivilizationGenerator;
import net.slashie.serf.baseDomain.AbstractItem;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.game.Player;
import net.slashie.serf.ui.UserInterface;
import net.slashie.utils.Util;

public class UnitGroup extends Player{
	private CivilizationDefinition civilizationDef;
	private Civilization civilization;
	private boolean isBarbarian;
	
	
	public UnitGroup(Civilization civilization) {
		civilizationDef = civilization.getCivDefinition();

		setAppearanceId("LEADER_"+civilizationDef.getColor().toString());
		this.civilization = civilization;
	}
	
	public UnitGroup(CivilizationDefinition civilizationDef) {
		this.civilizationDef = civilizationDef;
		setAppearanceId("GROUP_"+civilizationDef.getColor().toString());
		this.civilization = CivilizationGenerator.getBarbarianCiv(civilizationDef);
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
		//TODO: Calculate using the units
		return 16;
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
		List<Equipment> inventory = getInventory();
		for (Equipment e: inventory) {
			if (((Unit)e.getItem()).getClassifierId().equals("SETTLER")){
				canSettleCapability = true;
			}
		}
		;
	}

	List<String> availableActionsRedux = new ArrayList<String>();
	private Unit attackingUnit;
	private UnitGroup enemyGroup;
	
	public List<String> getAvailableActions(){
		availableActionsRedux.clear();
		availableActionsRedux.add("[a]ttack with...");
		availableActionsRedux.add("[e]xamine");
		if (canSettle()){
			availableActionsRedux.add("[s]ettle");
		}
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
			reduceQuantityOf(removeUnit);
	}

	public Unit getAttackingUnit() {
		if (attackingUnit == null){
			if (getInventory().size() > 0)
				attackingUnit = (Unit)getInventory().get(0).getItem();
		}
		return attackingUnit;
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
	
}
