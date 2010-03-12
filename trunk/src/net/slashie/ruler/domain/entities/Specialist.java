package net.slashie.ruler.domain.entities;

public enum Specialist {
	SOLDIER,
	TRADER,
	TINKER,
	MECHANIC,
	ENGINEER,
	SCIENTIFIC;

	public String getDescription() {
		switch (this){
		case SOLDIER:
			return "Soldier";
		case ENGINEER:
			return "Engineer";
		case MECHANIC:
			return "Mechanic";
		case SCIENTIFIC:
			return "Scientific";
		case TINKER:
			return "Tinker";
		case TRADER:
			return "Trader";
		}
		return null;
	}
}
