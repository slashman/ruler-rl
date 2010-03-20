package net.slashie.ruler.ai.world;

import java.util.List;

import net.slashie.ruler.controller.Game;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.ActionSelector;
import net.slashie.serf.action.Actor;
import net.slashie.util.Pair;
import net.slashie.utils.Util;

public class MonthProductionTicker implements ActionSelector{
	public ActionSelector derive() {
		return new MonthProductionTicker();
	}
	
	public String getID() {
		return "MONTH_TICKER";
	}
	
	public Action selectAction(Actor who) {
		return MonthTickAction.thus;
	}
	
	static class MonthTickAction extends Action {
		static MonthTickAction thus = new MonthTickAction();
		
		@Override
		public void execute(){
			//Advance in the production of units all around the globe
			List<City> cities = ((WorldLevel)performer.getLevel()).getCities();
			for (City city: cities){
				city.advanceProduction();
				city.advanceScience();
			}
			
		}
		
		@Override
		public String getID() {
			return "MONTH_TICK";
		}
		
		/**
		 * Cost in quarters of day
		 */
		@Override
		public int getCost() {
			return 4 * 30;
		}
	}
}
