package net.slashie.ruler.controller;

import net.slashie.serf.game.Player;
import net.slashie.serf.level.AbstractLevel;
import net.slashie.serf.level.LevelMetaData;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.utils.Position;

public class LevelMaster {

	public static AbstractLevel createLevel(LevelMetaData levelMetaData, Player p) {
		WorldLevel ret = null;
		String levelID = levelMetaData.getLevelID();
		if (levelID.startsWith("WORLD")){
			ret = WorldGenerator.generateWorld(levelID, 40, 40);

		} 
		
		if (ret.getExitFor("_BACK") != null){
			Position pos = ret.getExitFor("_BACK");
			ret.removeExit("_BACK");
			ret.addExit(pos, levelMetaData.getExit("_BACK"));
			
		}
		
		if (ret.getExitFor("_NEXT") != null){
			Position pos = ret.getExitFor("_NEXT");
			ret.removeExit("_NEXT");
			ret.addExit(pos, levelMetaData.getExit("_NEXT"));
		}
		
		return ret;
	}

}
