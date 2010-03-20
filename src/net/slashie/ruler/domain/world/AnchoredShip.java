package net.slashie.ruler.domain.world;

import net.slashie.ruler.domain.entities.Unit;
import net.slashie.serf.level.AbstractFeature;
import net.slashie.serf.ui.Appearance;

public class AnchoredShip extends AbstractFeature{
	private Unit seaUnit;
	public AnchoredShip(Unit seaUnit) {
		this.seaUnit = seaUnit;
		setAppearanceId("ANCHORED_SHIP");
	}
	
	@Override
	public String getDescription() {
		return seaUnit.getDescription();
	}
	
	@Override
	public String getClassifierID() {
		return seaUnit.getClassifierId()+"_ANCHORED";
	}

	public Unit getSeaUnit() {
		return seaUnit;
	}
	
	
	
}
