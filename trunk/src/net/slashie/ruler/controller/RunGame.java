package net.slashie.ruler.controller;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import net.slashie.libjcsi.CharKey;
import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.libjcsi.jcurses.JCursesConsoleInterface;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;
import net.slashie.serf.SworeException;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.ActionFactory;
import net.slashie.serf.game.SworeGame;
import net.slashie.serf.level.MapCellFactory;
import net.slashie.serf.sound.SFXManager;
import net.slashie.serf.sound.STMusicManagerNew;
import net.slashie.serf.ui.Appearance;
import net.slashie.serf.ui.AppearanceFactory;
import net.slashie.serf.ui.CommandListener;
import net.slashie.serf.ui.EffectFactory;
import net.slashie.serf.ui.UISelector;
import net.slashie.serf.ui.UserAction;
import net.slashie.serf.ui.UserCommand;
import net.slashie.serf.ui.UserInterface;
import net.slashie.serf.ui.consoleUI.ConsoleUISelector;
import net.slashie.serf.ui.consoleUI.ConsoleUserInterface;
import net.slashie.serf.ui.consoleUI.effects.CharEffectFactory;
import net.slashie.ruler.action.player.AddUnitToGroup;
import net.slashie.ruler.action.player.AssembleUnit;
import net.slashie.ruler.action.player.AttackUnitGroup;
import net.slashie.ruler.action.player.Buildroad;
import net.slashie.ruler.action.player.DisbandUnit;
import net.slashie.ruler.action.player.EstablishTradeRoute;
import net.slashie.ruler.action.player.GarrisonUnit;
import net.slashie.ruler.action.player.InfluenceCity;
import net.slashie.ruler.action.player.LoadResource;
import net.slashie.ruler.action.player.SetAttackingUnit;
import net.slashie.ruler.action.player.Settle;
import net.slashie.ruler.action.player.Walk;
import net.slashie.ruler.data.dao.EntitiesDAO;
import net.slashie.ruler.domain.entities.ResourceCarrier;
import net.slashie.ruler.domain.entities.Specialist;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.world.Age;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.ruler.factory.CivilizationGenerator;
import net.slashie.ruler.factory.ItemFactory;
import net.slashie.ruler.factory.UnitGroupFactory;
import net.slashie.ruler.ui.Display;
import net.slashie.ruler.ui.RulerAppearanceFactory;
import net.slashie.ruler.ui.console.CharDisplay;
import net.slashie.ruler.ui.console.CharEffects;
import net.slashie.ruler.ui.console.CharRulerAppearanceFactory;
import net.slashie.ruler.ui.console.RulerCharUserInterface;
import net.slashie.ruler.ui.console.RulerConsoleUISelector;
import net.slashie.util.FileUtil;
import net.slashie.util.Pair;
import net.slashie.utils.roll.Roll;
import net.slashie.utils.sound.midi.STMidiPlayer;

public class RunGame {
	private final static int JCURSES_CONSOLE = 0, SWING_GFX = 1, SWING_CONSOLE = 2;
	//private static SystemInterface si;
	private static UserInterface ui;
	private static UISelector uiSelector;
	
	private static Game currentGame;
	private static boolean createNew = true;
	private static int mode;
	
	public static String getConfigurationVal(String key){
		return configuration.getProperty(key);
	}

	private static void init(){
		if (createNew){
			System.out.println(GameInfo.getName()+" "+GameInfo.getVersion());
			System.out.println("Powered By Serf "+SworeGame.getVersion());
			System.out.println("Reading configuration");
	    	readConfiguration();
            try {
    			
    			switch (mode){
				case SWING_GFX:
					/*System.out.println("Initializing Graphics Appearances");
					initializeGAppearances();
					break;*/
				case JCURSES_CONSOLE:
				case SWING_CONSOLE:
					System.out.println("Initializing Char Appearances");
					initializeCAppearances();
					break;
    			}
				System.out.println("Initializing Action Objects");
				initializeActions();
				
				System.out.println("Loading Data");
				initializeCells();
				System.out.println("Initializing Scenario");
				initializeScenario(configuration);
				
				boolean isConsole = false;
				ConsoleSystemInterface csi = null;
				switch (mode){
				case SWING_GFX:
					/*System.out.println("Initializing Swing GFX System Interface");
					SwingSystemInterface si = new SwingSystemInterface();
					System.out.println("Initializing Swing GFX User Interface");
					UserInterface.setSingleton(new GFXUserInterface());
					GFXCuts.initializeSingleton();
					Display.thus = new GFXDisplay(si, UIconfiguration);
					PlayerGenerator.thus = new GFXPlayerGenerator(si);
					//PlayerGenerator.thus.initSpecialPlayers();
					EffectFactory.setSingleton(new GFXEffectFactory());
					((GFXEffectFactory)EffectFactory.getSingleton()).setEffects(new GFXEffects().getEffects());
					ui = UserInterface.getUI();
					initializeUI(si);*/
					break;
				case JCURSES_CONSOLE:
					System.out.println("Initializing JCurses System Interface");
					try{
						csi = new JCursesConsoleInterface();
					}
		            catch (ExceptionInInitializerError eiie){
		            	crash("Fatal Error Initializing JCurses", eiie);
		            	eiie.printStackTrace();
		                System.exit(-1);
		            }
		            isConsole = true;
					break;
				case SWING_CONSOLE:
					isConsole = true;
					System.out.println("Initializing Swing Console System Interface");
					csi = null;
					csi = new WSwingConsoleInterface("RULER - Santiago Zapata, 2010", configuration);
				}
				
				if (isConsole){
					System.out.println("Initializing Console User Interface");
					UserInterface.setSingleton(new RulerCharUserInterface(csi));
					Display.thus = new CharDisplay(csi);
					RulerAppearanceFactory.thus = new CharRulerAppearanceFactory();
					EffectFactory.setSingleton(new CharEffectFactory());
					((CharEffectFactory)EffectFactory.getSingleton()).setEffects(new CharEffects().getEffects());
					ui = UserInterface.getUI();
					initializeUI(csi);
				}
				
            } catch (SworeException crle){
            	crash("Error initializing", crle);
            } catch (IOException e) {
            	crash("Error initializing", e);
			}
            STMusicManagerNew.initManager();
        	if (configuration.getProperty("enableSound") != null && configuration.getProperty("enableSound").equals("true")){ // Sound
        		if (configuration.getProperty("enableMusic") == null || !configuration.getProperty("enableMusic").equals("true")){ // Music
    	    		STMusicManagerNew.thus.setEnabled(false);
    		    } else {
    		    	System.out.println("Initializing Midi Sequencer");
    	    		try {
    	    			STMidiPlayer.sequencer = MidiSystem.getSequencer ();
    	    			//STMidiPlayer.setVolume(0.1d);
    	    			STMidiPlayer.sequencer.open();
    	    			
    	    		} catch(MidiUnavailableException mue) {
    	            	SworeGame.addReport("Midi device unavailable");
    	            	System.out.println("Midi Device Unavailable");
    	            	STMusicManagerNew.thus.setEnabled(false);
    	            	return;
    	            }
    	    		System.out.println("Initializing Music Manager");
    				
    		    	
    	    		Enumeration keys = configuration.keys();
    	    	    while (keys.hasMoreElements()){
    	    	    	String key = (String) keys.nextElement();
    	    	    	if (key.startsWith("mus_")){
    	    	    		String music = key.substring(4);
    	    	    		STMusicManagerNew.thus.addMusic(music, configuration.getProperty(key));
    	    	    	}
    	    	    }
    	    	    STMusicManagerNew.thus.setEnabled(true);
    		    }
    	    	if (configuration.getProperty("enableSFX") == null || !configuration.getProperty("enableSFX").equals("true")){
    		    	SFXManager.setEnabled(false);
    		    } else {
    		    	SFXManager.setEnabled(true);
    		    }
        	}
			createNew = false;
    	}
	}
	
	private static Font loadFont() {
		try {
			Font ret = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("res/MONOFONT.TTF"))).deriveFont(Font.PLAIN, 10);
			return ret;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Font("Monospaced", Font.PLAIN, 10);

	}

	private static void initializeScenario(Properties configuration) throws IOException{
		Properties civs = new Properties();
		civs.load(new FileInputStream("scenarios/"+configuration.getProperty("useScenario")+"/civilizations.dat"));
		CivilizationGenerator.init(civs);
		civs = new Properties();
		civs.load(new FileInputStream("scenarios/"+configuration.getProperty("useScenario")+"/barbarians.dat"));
		UnitGroupFactory.init(civs);
		
		BufferedReader itemsReader = FileUtil.getReader("scenarios/"+configuration.getProperty("useScenario")+"/units.csv");
		String rowS = itemsReader.readLine();
		rowS = itemsReader.readLine();
		rowS = itemsReader.readLine();

		List<Unit> unitDefinitions = new ArrayList<Unit>();
		while (rowS != null){
			//System.out.println(rowS);
			String[] row = rowS.split(",");
			if (row[3].equals("Commerce")){
				unitDefinitions.add(new ResourceCarrier(row[0], row[4], i(row[7]), new Roll(row[5]), new Roll(row[6]) , i(row[8]), i(row[9]), row [10].equals("true") , 
						getAge(row[1]), getResources(row), getSpecialists(row)));
			} else {
				unitDefinitions.add(new Unit(row[0], row[4], i(row[7]), new Roll(row[5]), new Roll(row[6]) , i(row[8]), i(row[9]), row [10].equals("true") , 
							row [11].equals("true"), getAge(row[1]), getResources(row), getSpecialists(row)));
			}
			rowS = itemsReader.readLine();			
		}
		
		ItemFactory.init(unitDefinitions.toArray(new Unit[unitDefinitions.size()]));
	}
	
	
	private static Age getAge(String string) {
		return Age.valueOf(string);
	}

	private static List<Pair<Resource, Integer>> getResources(String[] row) {
		List<Pair<Resource, Integer>> ret = new ArrayList<Pair<Resource,Integer>>();
		ret.add(new Pair<Resource, Integer> (Resource.FOOD, i(row[12])));
		ret.add(new Pair<Resource, Integer> (Resource.HORSES, i(row[13])));
		ret.add(new Pair<Resource, Integer> (Resource.WOOD, i(row[14])));
		ret.add(new Pair<Resource, Integer> (Resource.IRON_ORE, i(row[15])));
		ret.add(new Pair<Resource, Integer> (Resource.OIL, i(row[16])));
		return ret;
	}

	private static List<Pair<Specialist, Integer>> getSpecialists(String[] row) {
		List<Pair<Specialist, Integer>> ret = new ArrayList<Pair<Specialist,Integer>>();
		ret.add(new Pair<Specialist, Integer> (Specialist.SOLDIER, i(row[17])));
		ret.add(new Pair<Specialist, Integer> (Specialist.TINKER, i(row[18])));
		ret.add(new Pair<Specialist, Integer> (Specialist.MECHANIC, i(row[19])));
		ret.add(new Pair<Specialist, Integer> (Specialist.ENGINEER, i(row[20])));
		ret.add(new Pair<Specialist, Integer> (Specialist.TRADER, i(row[21])));
		return ret;
	}

	private static int i(String string) {
		if (string.equals(""))
			return 0;
		return Integer.parseInt(string);
	}


	private static Properties configuration;
	private static Properties UIconfiguration;
	private static String uiFile;
	
	private static void readConfiguration(){
		configuration = new Properties();
	    try {
	    	configuration.load(new FileInputStream("configuration.properties"));
	    } catch (IOException e) {
	    	System.out.println("Error loading configuration file, please confirm existence of expedition.properties");
	    	System.exit(-1);
	    }
	    
	    if (mode == SWING_GFX){
		    UIconfiguration = new Properties();
		    try {
		    	UIconfiguration.load(new FileInputStream(uiFile));
		    } catch (IOException e) {
		    	System.out.println("Error loading configuration file, please confirm existence of "+uiFile);
		    	System.exit(-1);
		    }
	    }

	}
	
	private static void	title() {
		
		int choice = Display.thus.showTitleScreen();
		switch (choice){
		case 0:
			newGame();
			break;
		case 1:
			loadGame();
			break;
		case 2:
			System.out.println("Thank you for playing!");
			System.exit(0);
			break;
		}
		
	}
	
	
	private static void loadGame(){
		File saveDirectory = new File("savegame");
		File[] saves = saveDirectory.listFiles(new SaveGameFilenameFilter() );
		
		int index = Display.thus.showSavedGames(saves);
		if (index == -1)
			title();
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saves[index]));
			currentGame = (Game) ois.readObject();
			ois.close();
		} catch (IOException ioe){
 
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe){
			crash("Invalid savefile or wrong version", new SworeException("Invalid savefile or wrong version"));
		}
		currentGame.setInterfaces(ui, uiSelector);
		if (currentGame.getPlayer().getLevel() == null){
			crash("Player wasnt loaded", new Exception("Player wasnt loaded"));
		}
		currentGame.setPlayer(currentGame.getPlayer());
		ui.setPlayer(currentGame.getPlayer());
		uiSelector.setPlayer(currentGame.getPlayer());
		currentGame.resume();
	}
	
	private static void newGame(){
		if (currentGame != null){
			ui.removeCommandListener(currentGame);
		}
		currentGame = new Game();
		currentGame.setCanSave(true);
		currentGame.setInterfaces(ui, uiSelector);
		
		currentGame.newGame(1);
	}

	private static void initializeUI(Object si){
		Action walkAction = new Walk();
		Action attackAction = new AttackUnitGroup();
		Action settle = new Settle();
		Action setAttackingUnit = new SetAttackingUnit();
		Action assembleUnit = new AssembleUnit();
		Action influenceCity = new InfluenceCity();
		Action addToGroup = new AddUnitToGroup();
		Action loadResource = new LoadResource();
		Action trade = new EstablishTradeRoute();
		Action garrison = new GarrisonUnit();
		Action buildRoad = new Buildroad();
		Action disband = new DisbandUnit();


		UserAction[] userActions = new UserAction[] {
		    new UserAction(settle, CharKey.s),
		    new UserAction(setAttackingUnit, CharKey.f),
		    new UserAction(influenceCity, CharKey.i),
		    new UserAction(assembleUnit, CharKey.c),
		    new UserAction(loadResource, CharKey.l),
		    new UserAction(trade, CharKey.t),
		    new UserAction(addToGroup, CharKey.e),
		    new UserAction(buildRoad, CharKey.b),
		    new UserAction(garrison, CharKey.g),
		    new UserAction(disband, CharKey.d)

		};

		UserCommand[] userCommands = new UserCommand[]{
			new UserCommand(CommandListener.PROMPTQUIT, CharKey.Q),
			//new UserCommand(CommandListener.HELP, CharKey.F1),
			new UserCommand(CommandListener.LOOK, CharKey.E),
			new UserCommand(CommandListener.PROMPTSAVE, CharKey.S),
			//new UserCommand(CommandListener.HELP, CharKey.h),
			//new UserCommand(CommandListener.SHOWINVEN, CharKey.i),
			new UserCommand(CommandListener.SWITCHMUSIC, CharKey.T),
		};
		switch (mode){
		case SWING_GFX:
			/*((GFXUserInterface)ui).init((SwingSystemInterface)si, userCommands, UIconfiguration, target);
			uiSelector = new GFXUISelector();
			((GFXUISelector)uiSelector).init((SwingSystemInterface)si, userActions, UIconfiguration, walkAction, target, attack, (GFXUserInterface)ui);*/
			break;
		case JCURSES_CONSOLE: case SWING_CONSOLE:
			((RulerCharUserInterface)ui).init((ConsoleSystemInterface)si, userCommands, null);
			uiSelector = new RulerConsoleUISelector();
			((ConsoleUISelector)uiSelector).init((ConsoleSystemInterface)si, userActions, walkAction, null, attackAction, (ConsoleUserInterface)ui);
			break;
		}
	}
	
	public static void main(String args[]){
		//mode = SWING_GFX;
		mode = SWING_CONSOLE;
		uiFile = "slash-barrett.ui";
		if (args!= null && args.length > 0){
			if (args[0].equalsIgnoreCase("sgfx")){
				mode = SWING_GFX;
				if (args.length > 1)
					uiFile = args[1];
				else
					uiFile = "slash-barrett.ui";
			}
			else if (args[0].equalsIgnoreCase("jc"))
				mode = JCURSES_CONSOLE;
			else if (args[0].equalsIgnoreCase("sc"))
				mode = SWING_CONSOLE;
		}
		
		init();
		System.out.println("Launching game");
		try {
			while (true){
				title();
			}
		} catch (Exception e){
			Game.crash("Unrecoverable Exception [Press Space]",e);
			//si.waitKey(CharKey.SPACE);
		}
	}

	/*private static void initializeGAppearances(){
		Appearance[] definitions = new GFXAppearances().getAppearances();
		for (int i=0; i<definitions.length; i++){
			AppearanceFactory.getAppearanceFactory().addDefinition(definitions[i]);
		}
	}*/
	
	private static void initializeCAppearances(){
		Appearance[] definitions = EntitiesDAO.getCharAppearances();
		for (int i=0; i<definitions.length; i++){
			AppearanceFactory.getAppearanceFactory().addDefinition(definitions[i]);
		}
	}
	
	private static void initializeActions(){
		ActionFactory af = ActionFactory.getActionFactory();
		Action[] definitions = new Action[]{
				new AttackUnitGroup()
		};
		for (int i = 0; i < definitions.length; i++)
			af.addDefinition(definitions[i]);
	}
	
	private static void initializeCells(){
		MapCellFactory.getMapCellFactory().init(EntitiesDAO.getCellDefinitions(AppearanceFactory.getAppearanceFactory()));
	}

	

    public static void crash(String message, Throwable exception){
    	System.out.println(GameInfo.getName() + GameInfo.getVersion()+": Error");
        System.out.println("");
        System.out.println("Unrecoverable error: "+message);
        exception.printStackTrace();
        if (currentGame != null){
        	System.out.println("Trying to save game");
        	GameFiles.saveGame(currentGame, currentGame.getPlayer());
        }
        System.exit(-1);
    }
    
}

class SaveGameFilenameFilter implements FilenameFilter {

	public boolean accept(File arg0, String arg1) {
		//if (arg0.getName().endsWith(".sav"))
		if (arg1.endsWith(".sav"))
			return true;
		else
			return false;
	}
	
}