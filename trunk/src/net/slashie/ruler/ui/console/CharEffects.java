package net.slashie.ruler.ui.console;

import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.serf.ui.consoleUI.effects.CharDirectionalMissileEffect;
import net.slashie.serf.ui.consoleUI.effects.CharEffect;

public class CharEffects {
	private CharEffect [] effects = new CharEffect[]{
		new CharDirectionalMissileEffect("BOLT_EFFECT", "\\|/--/|\\", ConsoleSystemInterface.BLUE, 50),

	};

	public CharEffect[] getEffects() {
		return effects;
	}

}
