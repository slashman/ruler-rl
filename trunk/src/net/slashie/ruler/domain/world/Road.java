package net.slashie.ruler.domain.world;

import net.slashie.serf.level.AbstractFeature;

public class Road extends AbstractFeature{
	@Override
	public String getClassifierID() {
		return "ROAD"+super.toString();
	}
	
	@Override
	public String getDescription() {
		return "A road";
	}
	
	public Road() {
		setAppearanceId("ROAD");
	}
}
