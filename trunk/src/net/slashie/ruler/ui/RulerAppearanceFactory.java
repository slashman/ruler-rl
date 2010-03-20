package net.slashie.ruler.ui;

import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.serf.ui.Appearance;

public abstract class RulerAppearanceFactory {
	public static RulerAppearanceFactory thus;
	
	public abstract Appearance getSettlementAppearance(int size, Civilization civ);
}
