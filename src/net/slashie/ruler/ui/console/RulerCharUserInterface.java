package net.slashie.ruler.ui.console;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import net.slashie.libjcsi.CSIColor;
import net.slashie.libjcsi.CharKey;
import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.libjcsi.textcomponents.BasicListItem;
import net.slashie.libjcsi.textcomponents.ListBox;
import net.slashie.libjcsi.textcomponents.ListItem;
import net.slashie.libjcsi.textcomponents.MenuBox;
import net.slashie.libjcsi.textcomponents.MenuItem;
import net.slashie.libjcsi.textcomponents.TextBox;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.sound.STMusicManagerNew;
import net.slashie.serf.ui.UserCommand;
import net.slashie.serf.ui.consoleUI.CharAppearance;
import net.slashie.serf.ui.consoleUI.ConsoleUserInterface;
import net.slashie.serf.ui.consoleUI.EquipmentMenuItem;
import net.slashie.ruler.controller.Game;
import net.slashie.ruler.domain.entities.Specialist;
import net.slashie.ruler.domain.entities.Unit;
import net.slashie.ruler.domain.entities.UnitGroup;
import net.slashie.ruler.domain.world.City;
import net.slashie.ruler.domain.world.Resource;
import net.slashie.ruler.domain.world.WorldLevel;
import net.slashie.ruler.ui.RulerUserInterface;

public class RulerCharUserInterface extends ConsoleUserInterface implements RulerUserInterface {
	private ConsoleSystemInterface csi;
	
	private ListBox playerGroupList;
	private ListBox enemyGroupList;

	
	public RulerCharUserInterface(ConsoleSystemInterface csi) {
		this.csi = csi;
	}
	
	@Override
	public void onMusicOn() {
		WorldLevel level = (WorldLevel) getPlayer().getLevel();
		if (level.getMusicKey() != null)
			STMusicManagerNew.thus.playKey(level.getMusicKey());
	}
	
	@Override
	public String getQuitPrompt() {
		return "Do you want to quit?";
	}
	
	@Override
	public void init(ConsoleSystemInterface psi, UserCommand[] gameCommands,
			Action target) {
		super.init(psi, gameCommands, target);
		VP_START.x = 1;
		VP_START.y = 3;
		VP_END.x = 17;
		VP_END.y = 20;
		PC_POS.x = 9;
		PC_POS.y = 11;
		playerGroupList = new ListBox(psi);
		playerGroupList.setBounds(20, 5, 28, 5);
		enemyGroupList = new ListBox(psi);
		enemyGroupList.setBounds(49, 5, 28, 5);
		idList.setBounds(20, 12, 28, 5);
	}
	private StringBuffer cat = new StringBuffer();
	//private DateFormat simpleDateFormat = DateFormat.getDateInstance(DateFormat.LONG, new Locale("en", "us"));
	@Override
	public void drawStatus() {
		// Print status
		UnitGroup a = (UnitGroup) getPlayer();
		csi.print(1, 1, a.getCivilization().getLeaderName() + " of the "+a.getCivilization().getCivDefinition().getCivilizationName());
		csi.print(1, 2, "                        ");
		csi.print(1, 2, formatDate(Game.getCurrentGame().getCurrentTime()));
		
		// Print available actions
		List<String> availableActions = a.getAvailableActions();
		cat.setLength(0);
		for (String availableAction: availableActions){
			cat.append(availableAction);
			cat.append(" ");
		}
		//csi.print(22, 1, "Available Actions", CSIColor.GRAY);
		csi.print(22, 2, cat.toString());
		
		playerGroupList.setElements(getInventoryListItems(getPlayer().getInventory()));
		if ( ((UnitGroup)player).getEnemyGroup() != null){
			UnitGroup enemyGroup = ((UnitGroup)player).getEnemyGroup();
			enemyGroupList.setElements(getInventoryListItems(enemyGroup.getInventory()));
			CharAppearance charApp = (CharAppearance) enemyGroup.getAppearance();
			csi.print(48, 4, charApp.getChar(), charApp.getColor());
			csi.print(50, 4, enemyGroup.getDescription(), CSIColor.WHITE);
		} else {
			csi.print(48, 4, "                    ");
			enemyGroupList.clear();
		}
		CharAppearance charApp = (CharAppearance) a.getAppearance();
		csi.print(19, 4, charApp.getChar(), charApp.getColor());
		csi.print(21, 4, a.getDescription(), CSIColor.WHITE);
		csi.print(19, 3, ">Your Group", CSIColor.GRAY);
		csi.print(48, 3, ">Enemy", CSIColor.GRAY);
		csi.print(19, 11, ">Nearby Units", CSIColor.GRAY);

		playerGroupList.draw();
		enemyGroupList.draw();
		
		// If player is on a city, show city info
		City city = ((WorldLevel)level).getCityAt(player.getPosition());
		if (city != null){
			//Clear
			for (int i = 0; i < 10; i++){
				csi.print(49, 11+i, "                     ");
			}
			
			csi.print(49, 11, ">"+city.getDescription(), CSIColor.GRAY);
			csi.print(50, 12, "Population: "+city.getSize(), CSIColor.GRAY);
			csi.print(50, 13, "Size: "+city.getResourceRange(), CSIColor.WHITE);
			csi.print(50, 14, "Food Consumption: "+city.getFoodConsumption(), CSIColor.WHITE);
			int start = 15;
			int i = 0;
			for (Resource res: Resource.values()){
				if (city.getResources(res) > 0){
					csi.print(50, start+i, res.getDescription()+": "+city.getResources(res), CSIColor.WHITE);
					i++;
				}
			}
			for (Specialist sp: Specialist.values()){
				if (city.getSpecialists(sp) > 0){
					csi.print(50, start+i, sp.getDescription()+": "+city.getSpecialists(sp), CSIColor.YELLOW);
					i++;
				}
			}
		} else {
			//Clear
			for (int i = 0; i < 10; i++){
				csi.print(49, 11+i, "                     ");
			}
		}
	}

	List<ListItem> reusable = new ArrayList<ListItem>();
	private List<ListItem> getInventoryListItems(List<Equipment> inventory) {
		reusable.clear();
		for (Equipment e: inventory){
			if (e.getItem() == ((UnitGroup)getPlayer()).getAttackingUnit())
				reusable.add(new UnitListItem((Unit)e.getItem(), ">"+((Unit)e.getItem()).getMenuDescription()));
			else
				reusable.add(new UnitListItem((Unit)e.getItem(), ((Unit)e.getItem()).getMenuDescription()));
		}
		return reusable;
	}

	private final static String [] MONTHS = new String [] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	private final static String [] ERAS = new String [] {"A.C.", "D.C"};
	private String formatDate(Calendar currentTime) {
		return MONTHS[currentTime.get(Calendar.MONTH)]+" "+currentTime.get(Calendar.DAY_OF_MONTH)+", "+currentTime.get(Calendar.YEAR)+" "+ERAS[currentTime.get(Calendar.ERA)];
	}

	@Override
	public void showDetailedInfo(Actor a) {
		csi.saveBuffer();
		csi.cls();
		
		csi.print(5, 4, "Press Space");
		csi.waitKey(CharKey.SPACE);
		csi.restore();
	}
	
	@Override
	public void showInventory() {
		Equipment.eqMode = true;
		int xpos = 1, ypos = 0;
  		MenuBox menuBox = new MenuBox(csi);
  		menuBox.setHeight(17);
  		menuBox.setWidth(50);
  		menuBox.setPosition(1,5);
  		menuBox.setBorder(false);
  		TextBox itemDescription = new TextBox(csi);
  		itemDescription.setBounds(52,9,25,5);
  		csi.saveBuffer();
  		csi.cls();
  		csi.print(xpos,ypos,    "------------------------------------------------------------------------", ConsoleSystemInterface.WHITE);
  		csi.print(xpos,ypos+1,  "Units", ConsoleSystemInterface.WHITE);
  		csi.print(xpos,ypos+2,    "------------------------------------------------------------------------", ConsoleSystemInterface.WHITE);
  		csi.print(xpos,24,  "[Space] to continue, Up and Down to browse");
  		int choice = 0;
  		while (true){
  	  		List<Equipment> inventory = getPlayer().getInventory();
  	  		Vector menuItems = new Vector();
  	  		for (Equipment item: inventory){
  	  			menuItems.add(new EquipmentMenuItem(item));
  	  		}
  	  		menuBox.setMenuItems(menuItems);
  	  		menuBox.draw();
  	  		csi.refresh();
  	  		
	  		CharKey x = new CharKey(CharKey.NONE);
			while (x.code != CharKey.SPACE && !x.isArrow())
				x = csi.inkey();
			if (x.code == CharKey.SPACE || x.code == CharKey.ESC){
				break;
			}
  		}
 		
		csi.restore();
		csi.refresh();
		Equipment.eqMode = false;	
	}
	
	@Override
	public int switchChat(String prompt, String... options) {
		MenuBox selectionBox = new MenuBox(csi);
		selectionBox.setPosition(20,2);
		selectionBox.setWidth(31);
		selectionBox.setHeight(8);
  		Vector<MenuItem> menuItems = new Vector<MenuItem>();
  		int i = 0;
  		for (String option: options){
  			menuItems.add(new SimpleItem(i,option));
  			i++;
  		}
  		selectionBox.setMenuItems(menuItems);
  		selectionBox.setPromptSize(2);
  		selectionBox.setBorder(true);
  		selectionBox.setPrompt(prompt);
  		selectionBox.draw();
  		
		while (true) {
			csi.refresh();
			SimpleItem itemChoice = ((SimpleItem)selectionBox.getSelection());
			if (itemChoice == null)
				break;
			return itemChoice.getValue();
		}
		return -1;	
	}
}

class SimpleItem implements MenuItem{
	private String text;
	private int value;

	SimpleItem (int value, String text){
		this.text = text;
		this.value = value;
	}
	
	public char getMenuChar() {
		return '*';
	}
	
	public int getMenuColor() {
		return ConsoleSystemInterface.WHITE;
	}
	
	public String getMenuDescription() {
		return text;
	}
	
	public int getValue(){
		return value;
	}
}