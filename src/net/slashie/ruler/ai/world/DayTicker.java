package net.slashie.ruler.ai.world;

import net.slashie.ruler.controller.Game;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.ActionSelector;
import net.slashie.serf.action.Actor;

public class DayTicker implements ActionSelector{
	public ActionSelector derive() {
		return new DayTicker();
	}
	
	public String getID() {
		return "DAY_TICKER";
	}
	
	public Action selectAction(Actor who) {
		return DayTickAction.thus;
	}
	
	static class DayTickAction extends Action {
		static DayTickAction thus = new DayTickAction();
		
		@Override
		public void execute() {
			Game.getCurrentGame().advanceDays(10);
		}
		
		@Override
		public String getID() {
			return "DAY_TICK";
		}
		
		/**
		 * Cost in quarters of day
		 */
		@Override
		public int getCost() {
			return 4;
		}
	}
}
