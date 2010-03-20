package net.slashie.ruler.domain.entities;

import java.util.List;

import net.slashie.ruler.domain.world.Age;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.serf.baseDomain.AbstractItem;
import net.slashie.util.Pair;
import net.slashie.utils.Util;
import net.slashie.utils.roll.Roll;

public class Unit extends AbstractItem implements Cloneable{
	private static final String[] HP_STRINGS = new String[]{
		"",
		"*",
		"**",
		"***",
		"****",
		"*****"};

	public final static String SETTLER_ID = "SETTLER";
	private String classifierID;
	private String description;
	private int hp;
	private boolean isVeteran;
	private Roll attackRoll;
	private Roll defenseRoll;
	private int attackTurns;
	private int level;
	private Age availableAge;
	private boolean isWaterUnit;
	private boolean isSettler;
	private List<Pair<Resource, Integer>> resourceRequeriments;
	private List<Pair<Specialist, Integer>> specialistRequirements;
	private int movementCost;
	
	public Unit(
			String classifierID, 
			String description, 
			int baseHP, 
			Roll attackRoll, 
			Roll defenseRoll, 
			int attackTurns,
			int movementCost,
			boolean isWaterUnit,
			boolean isSettler,
			Age availableAge,
			List<Pair<Resource, Integer>> resourceRequeriments,
			List<Pair<Specialist, Integer>> specialistRequirements
			) {
		super(classifierID);
		this.classifierID = classifierID;
		this.description = description;
		this.hp = baseHP;
		this.attackRoll = new Roll(attackRoll);
		this.defenseRoll =  new Roll(defenseRoll);
		this.attackTurns = attackTurns;
		this.availableAge = availableAge;
		this.isWaterUnit = isWaterUnit;
		this.resourceRequeriments = resourceRequeriments;
		this.specialistRequirements = specialistRequirements;
		this.movementCost = movementCost;
		this.isSettler = isSettler;
	}
	
	@Override
	public String getFullID() {
		// Item classifier Id. Used to stack things
		return toString().hashCode()+"";
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public Object clone() {
		try {
			Unit ret = (Unit)super.clone();
			ret.attackRoll = new Roll(attackRoll);
			ret.defenseRoll = new Roll(defenseRoll);
			return ret;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}


	public void damage(int i) {
		hp -= i;
	}

	public int getHP() {
		return hp;
	}


	public boolean isVeteran() {
		return isVeteran;
	}

	public void setVeteran(boolean isVeteran) {
		this.isVeteran = isVeteran;
	}
	
	public void levelUp(){
		level++;
		if (Util.chance(50)){
			attackRoll.addModifier(1);
		} else {
			defenseRoll.addModifier(1);
		}
	}

	public Roll getAttackRoll() {
		return attackRoll;
	}

	public Roll getDefenseRoll() {
		return defenseRoll;
	}

	public int getAttackTurns() {
		return attackTurns;
	}

	public String getClassifierId() {
		return classifierID;
	}

	public String getMenuDescription() {
		return 
			(getLevel()>0?"Lv"+getLevel()+" ":"")+
			Util.limit(getDescription(),8)+" "+
			getHPString()+" "+
			getAttackRoll().getString()+" "+
			getDefenseRoll().getString();
	}

	private String getHPString() {
		return HP_STRINGS[getHP()];
	}

	public int getLevel() {
		return level;
	}

	public Age getAvailableAge() {
		return availableAge;
	}

	public boolean requiresCoast() {
		return isWaterUnit;
	}

	public List<Pair<Resource, Integer>> getResourceRequirements() {
		return resourceRequeriments;
	}

	public List<Pair<Specialist, Integer>> getSpecialistRequirements() {
		return specialistRequirements;
	}

	public Integer getRequiredResource(Resource a) {
		for (Pair<Resource, Integer> requirement: resourceRequeriments){
			if (requirement.getA() == a){
				return requirement.getB();
			}
		}
		return -1;
	}

	
	public int getMovementCost() {
		return movementCost;
	}

	public boolean isWaterUnit() {
		return isWaterUnit;
	}

	
	public boolean isSettler() {
		return isSettler;
	}
	
	
}


