package net.slashie.ruler.ai.world;

import net.slashie.serf.action.ActionSelector;
import net.slashie.serf.action.Actor;

public class TimeTickerAgent extends Actor{
	@Override
	public String getClassifierID() {
		return "TIME_TICKER_AGENT";
	}
	
	public TimeTickerAgent(ActionSelector ticker){
		setSelector(ticker);
	}
	
	public boolean isInvisible (){
		return true;
	}
	
	@Override
	public String getDescription() {
		return "";
	}
	
}
