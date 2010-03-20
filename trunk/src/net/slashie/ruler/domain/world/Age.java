package net.slashie.ruler.domain.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.slashie.ruler.domain.entities.Specialist;

public enum Age {
	STONE,
	IRON,
	MEDIEVAL,
	RENAISSANCE,
	INDUSTRIAL,
	MODERN,
	FUTURE;

	public Age getPreviousAge() {
		switch (this){
		case FUTURE:
			return MODERN;
		case MODERN:
			return INDUSTRIAL;
		case INDUSTRIAL:
			return RENAISSANCE;
		case RENAISSANCE:
			return MEDIEVAL;
		case MEDIEVAL:
			return IRON;
		case IRON:
			return STONE;
		}
		return null;
	}

	public List<Age> getAllAges() {
		List<Age> ret = new ArrayList<Age>();
		switch (this){
		case FUTURE:
			ret.add(FUTURE);
		case MODERN:
			ret.add(MODERN);
		case INDUSTRIAL:
			ret.add(INDUSTRIAL);
		case RENAISSANCE:
			ret.add(RENAISSANCE);
		case MEDIEVAL:		
			ret.add(MEDIEVAL);
		case IRON:
			ret.add(IRON);
		case STONE:
			ret.add(STONE);
		} 
		return ret;
	}

	public List<Specialist> getSpecialists() {
		List<Specialist> ret = new ArrayList<Specialist>();

		switch (this){
		case FUTURE:
			
		case MODERN:
			ret.add(Specialist.ENGINEER);
			ret.add(Specialist.SCIENTIFIC);
		case INDUSTRIAL:
			ret.add(Specialist.MECHANIC);
		case RENAISSANCE:
			
		case MEDIEVAL:
			
		case IRON:
			
		case STONE:
			ret.add(Specialist.TRADER);
			ret.add(Specialist.SOLDIER);
			ret.add(Specialist.TINKER);
			
		}
		return ret;
	}

	public String getDescription() {
		switch (this){
		case FUTURE:
			return "Future";
		case MODERN:
			return "Oil Age";
		case INDUSTRIAL:
			return "Industrial Age";
		case RENAISSANCE:
			return "Renaissance";
		case MEDIEVAL:
			return "Middle Age";
		case IRON:
			return "Iron Age";
		case STONE:
			return "Stone Age";
			
		}
		return "None";
	}

	public Age getNextAge() {
		switch (this){
		case FUTURE:
			return null;
		case MODERN:
			return FUTURE;
		case INDUSTRIAL:
			return MODERN;
		case RENAISSANCE:
			return INDUSTRIAL;
		case MEDIEVAL:
			return RENAISSANCE;
		case IRON:
			return MEDIEVAL;
		case STONE:
			return IRON;
		}
		return null;
	}
}
