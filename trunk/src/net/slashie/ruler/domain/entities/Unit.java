package net.slashie.ruler.domain.entities;

import net.slashie.serf.baseDomain.AbstractItem;
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
	private String classifierID;
	private String description;
	private int hp;
	private boolean isVeteran;
	private Roll attackRoll;
	private Roll defenseRoll;
	private int attackTurns;
	private int level;
	
	public Unit(String classifierID, String description, int baseHP, Roll attackRoll, Roll defenseRoll, int attackTurns) {
		super(classifierID);
		this.classifierID = classifierID;
		this.description = description;
		this.hp = baseHP;
		this.attackRoll = new Roll(attackRoll);
		this.defenseRoll =  new Roll(defenseRoll);
		this.attackTurns = attackTurns;
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
	
	
}


