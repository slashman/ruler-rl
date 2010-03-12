package net.slashie.ruler.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import net.slashie.serf.action.Actor;
import net.slashie.serf.game.Player;
import net.slashie.serf.game.SworeGame;
import net.slashie.serf.level.AbstractLevel;
import net.slashie.serf.level.LevelMetaData;
import net.slashie.serf.sound.STMusicManagerNew;
import net.slashie.serf.ui.CommandListener;
import net.slashie.serf.ui.UserInterface;
import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.factory.CivilizationGenerator;
import net.slashie.ruler.ui.Display;
import net.slashie.utils.OutParameter;

public class Game extends SworeGame{
	private static Game currentGame;
	
	@Override
	public AbstractLevel createLevel(LevelMetaData metadata) {
		return LevelMaster.createLevel(metadata, getPlayer());
	}

	@Override
	public Player generatePlayer(int gameType, SworeGame game) {
		OutParameter name = new OutParameter();
		OutParameter civilization = new OutParameter();
		Display.thus.createPlayer(name, civilization);
		return CivilizationGenerator.getCivilization((String)civilization.getObject(), (String) name.getObject());
	}

	@Override
	public String getDeathMessage() {
		return "Your Die..";
	}

	@Override
	public String getFirstMessage(Actor player) {
		return "Go now, Champion!";
	}

	@Override
	public void onGameResume() {
		currentGame = this;
		WorldLevel expeditionLevel = (WorldLevel)getPlayer().getLevel();
		if (expeditionLevel.getMusicKey() != null)
			STMusicManagerNew.thus.playKey(expeditionLevel.getMusicKey());
	}

	@Override
	public void onGameStart(int gameType) {
		currentGame = this;
		Display.thus.showIntro(((UnitGroup)getPlayer()).getCivilization());
		loadMetadata();
		currentTime = GregorianCalendar.getInstance(new Locale("en", "us"));
		currentTime.set(Calendar.ERA, GregorianCalendar.BC);
		currentTime.set(Calendar.YEAR, 4001);
		currentTime.set(Calendar.MONTH, Calendar.DECEMBER);
		currentTime.set(Calendar.DAY_OF_MONTH, 28);
		loadLevel("WORLD");
		
		currentGame.setPlayer(getPlayer());
	}

	private void loadMetadata() {
		LevelMetaData md = null;
		
		md = new LevelMetaData("WORLD");
		md.addExits("_START", "_BACK");
		md.addExits("_START", "_NEXT");
		addMetaData("WORLD", md);
	}

	@Override
	public void onGameWon() {
		Display.thus.showWin(((UnitGroup)getPlayer()).getCivilization());
		System.exit(0);
	}

	@Override
	public void onLevelLoad(AbstractLevel aLevel) {
		WorldLevel level = (WorldLevel)aLevel;
		if (level.getMusicKey() != null)
			STMusicManagerNew.thus.playKey(level.getMusicKey());
	}
	
	public static Game getCurrentGame() {
		return currentGame;
	}

	public void commandSelected (int commandCode){
		super.commandSelected(commandCode);
		switch (commandCode){
		case CommandListener.HELP:
			Display.thus.showHelp();
			break;
		}
	}
	
	private Calendar currentTime;

	public Calendar getCurrentTime() {
		return currentTime;
	}

	public void advanceDays(int i) {
		currentTime.add(Calendar.DAY_OF_MONTH, i);
	}
	
	/**
	 * A UnitGroup is enemy of the player if:
	 *  - It's Barbarian
	 *  - The civilization is enemy of the players' civilization
	 *   
	 * @param group
	 * @return
	 */

	public boolean isEnemy(UnitGroup group) {
		if (group.isBarbarian())
			return true;
		if (((UnitGroup)getPlayer()).getCivilization().hasEnemy(group.getCivilization())){
			return true;
		}
		return false;
	}
	
	@Override
	public void afterPlayerAction() {
		((UnitGroup)getPlayer()).checkDeath();
		
	}
}
