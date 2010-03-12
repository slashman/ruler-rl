package net.slashie.ruler.action.player;

import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.serf.action.Action;
import net.slashie.utils.Position;
import net.slashie.utils.Util;

public class AttackUnitGroup extends Action{
	@Override
	public String getID() {
		return "ATTACK_UNIT_GROUP";
	}
	
	/**
	 *    The front unit attacks the best defense unit of the other army
		   The fight has a number of rounds, based on the higher attack speed of both units
		   If the defending unit has higher attack speed than the attacking unit, it counter attacks for the remaining turns
		   In each round, the attack and defense rolls are done. The loser unit has an 80% chance of losing 1 HP
		   In each round, the winner unit has an 10% chance of becoming veteran
		   If HP is zero, the unit is destroyed
		   If ruler unit is destroyed, a new ruler is selected from its family
    	   If there are no possible rulers, game over
	 */
	@Override
	public void execute() {
		UnitGroup attackingGroup = (UnitGroup) performer;
		//Get the target Unit Group
		Position var = directionToVariation(targetDirection);
        Position destinationPoint = Position.add(performer.getPosition(), var);
        setPosition(destinationPoint);
        
        UnitGroup defendingGroup = (UnitGroup) performer.getLevel().getActorAt(destinationPoint);
        if (defendingGroup == null){
        	youMessage("There's no unit there!");
        	return;
        }
		attackingGroup.setEnemyGroup(defendingGroup);
		// Select the attacking unit
        Unit attackingUnit = attackingGroup.getAttackingUnit();
		
		// Select the defending unit
        Unit defendingUnit = defendingGroup.getDefendingUnit();
        
        // Calculate the rounds
        int attackRounds = attackingUnit.getAttackTurns();
        int counterRounds = defendingUnit.getAttackTurns() - attackingUnit.getAttackTurns();
        if (counterRounds < 0)
        	counterRounds = 0;
        for (int i = 0; i < attackRounds; i++){
        	if (attackingUnit.getHP() == 0 || defendingUnit.getHP() == 0)
        		continue;
        	boolean win = attackingGroup.attackUnit(attackingUnit, defendingGroup, defendingUnit);
        	if (win){
        		youMessage("Your "+attackingUnit.getDescription()+" attack the "+defendingGroup.getDescriptionWithCiv(defendingUnit)+" and win!");
        		theyMessage("The "+attackingGroup.getDescriptionWithCiv(attackingUnit)+" attack your "+defendingUnit.getDescription()+" and win the battle...");
        	} else {
        		youMessage("Your "+attackingUnit.getDescription()+" attack the "+defendingGroup.getDescriptionWithCiv(defendingUnit)+" and lose...");
        		theyMessage("The "+attackingGroup.getDescriptionWithCiv(attackingUnit)+" attack your "+defendingUnit.getDescription()+" and lose!");
        	}
        }
        for (int i = 0; i < counterRounds; i++){
        	if (attackingUnit.getHP() == 0 || defendingUnit.getHP() == 0)
        		continue;
        	boolean win = defendingGroup.attackUnit(defendingUnit, attackingGroup, attackingUnit);
        	if (win) {
        		youMessage("The "+defendingGroup.getDescriptionWithCiv(defendingUnit)+" counterattack sucessfully...");
        		theyMessage("Your "+defendingUnit.getDescription()+" counterattack sucessfully!");
        	} else {
        		youMessage("The "+defendingGroup.getDescriptionWithCiv(defendingUnit)+" counterattack, but lose!");
        		theyMessage("Your "+defendingUnit.getDescription()+" counterattack, but lose...");
        	}
        }
        
        
        
	}
	
	

}
