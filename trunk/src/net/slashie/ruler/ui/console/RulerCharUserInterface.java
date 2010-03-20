package net.slashie.ruler.ui.console;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import net.slashie.serf.SworeException;
import net.slashie.serf.action.Action;
import net.slashie.serf.action.Actor;
import net.slashie.serf.game.Equipment;
import net.slashie.serf.sound.STMusicManagerNew;
import net.slashie.serf.ui.UserCommand;
import net.slashie.serf.ui.consoleUI.CharAppearance;
import net.slashie.serf.ui.consoleUI.ConsoleUserInterface;
import net.slashie.serf.ui.consoleUI.EquipmentMenuItem;
import net.slashie.util.Pair;
import net.slashie.ruler.action.ActionException;
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
	private MenuBox selectUnitList;
	private TextBox availableCommandsTextBox;
	
	public RulerCharUserInterface(ConsoleSystemInterface csi) {
		this.csi = csi;
	}
	
	@Override
	public boolean drawIdList() {
		return false;
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
		enemyGroupList.setBounds(20, 13, 28, 5);
		selectUnitList = new MenuBox(psi);
		selectUnitList.setBounds(48, 3, 29, 16);
		selectUnitList.setBorder(true);
		selectUnitList.setPromptSize(1);
		availableCommandsTextBox = new TextBox(psi);
		availableCommandsTextBox.setBounds(29, 0, 50, 3);
	}
	private StringBuffer cat = new StringBuffer();
	//private DateFormat simpleDateFormat = DateFormat.getDateInstance(DateFormat.LONG, new Locale("en", "us"));
	@Override
	public void drawStatus() {
		// Print status
		UnitGroup a = (UnitGroup) getPlayer();
		csi.print(1, 0, a.getCivilization().getLeaderName() + " of the "+a.getCivilization().getCivDefinition().getCivilizationName());
		csi.print(1, 1, "                        ");
		csi.print(1, 1, a.getCivilization().getCurrentAge().getDescription() + " "+a.getCivilization().getAccumulatedScienceOutput()+"/"+a.getCivilization().getNextScienceCost(), CSIColor.YELLOW);
		csi.print(1, 20, "                        ");
		csi.print(1, 20, "Supplies: "+a.getSupplies());
		csi.print(1, 21, "                        ");
		csi.print(1, 21, formatDate(Game.getCurrentGame().getCurrentTime()));
		
		// Print available actions
		List<String> availableActions = a.getAvailableActions();
		cat.setLength(0);
		for (String availableAction: availableActions){
			cat.append(availableAction);
			cat.append(" ");
		}
		//csi.print(22, 1, "Available Actions", CSIColor.GRAY);
		availableCommandsTextBox.setText(cat.toString());
		availableCommandsTextBox.draw();
		playerGroupList.setElements(getInventoryListItems(getPlayer().getInventory()));
		if ( ((UnitGroup)player).getEnemyGroup() != null){
			csi.print(18, 12, "                           ");
			UnitGroup enemyGroup = ((UnitGroup)player).getEnemyGroup();
			enemyGroupList.setElements(getInventoryListItems(enemyGroup.getInventory()));
			CharAppearance charApp = (CharAppearance) enemyGroup.getAppearance();
			csi.print(19, 12, charApp.getChar(), charApp.getColor());
			csi.print(21, 12, enemyGroup.getDescription(), CSIColor.WHITE);
		} else {
			csi.print(18, 12, "                           ");
			enemyGroupList.clear();
		}
		CharAppearance charApp = (CharAppearance) a.getAppearance();
		csi.print(19, 4, charApp.getChar(), charApp.getColor());
		csi.print(21, 4, a.getDescription(), CSIColor.WHITE);
		csi.print(19, 3, ">Your Group", CSIColor.GRAY);
		csi.print(19, 11, ">Enemy", CSIColor.GRAY);

		playerGroupList.draw();
		enemyGroupList.draw();
		
		// If player is on a city, show city info
		City city = ((WorldLevel)level).getCityAt(player.getPosition());
		//Clear
		for (int i = 0; i < 20; i++){
			csi.print(49, 3+i, "                             ");
		}
		if (city != null){
			int i = 3;
			csi.print(49, i++, ">"+city.getDescription(), CSIColor.GRAY);
			csi.print(50, i++, "Population: "+(city.getSize()*10000), CSIColor.GRAY);
			csi.print(50, i++, "City Size: "+city.getResourceRange(), CSIColor.WHITE);
			csi.print(50, i++, "Food Consumption: "+city.getFoodConsumption(), CSIColor.WHITE);
			
			Unit currentAssembly = city.getCurrentAssemblyUnit();
			if (currentAssembly != null){
				csi.print(50, i++, "Assembly: "+currentAssembly.getDescription(), CSIColor.WHITE);
			}
			csi.print(49, i++, ">Resources", CSIColor.GRAY);
			for (Resource res: Resource.values()){
				if (city.getAvailableResources(res) != 0){
					String resourceDesc = res.getDescription()+": "+city.getFullResources(res);
					if (city.getCommitResources(res)> 0){
						resourceDesc += "(-"+city.getCommitResources(res)+")";
					}
					if (city.getTradedResources(res)> 0){
						resourceDesc += "(+"+city.getTradedResources(res)+")";
					}
					csi.print(50, i++, resourceDesc, CSIColor.WHITE);
				}
			}
			if (city.getCommitmentsMessages().size()>0)
				csi.print(49, i++, ">Commits", CSIColor.GRAY);
			for (String commit: city.getCommitmentsMessages()){
				csi.print(50, i++, commit, CSIColor.WHITE);
			}
			
			csi.print(49, i++, ">Specialists", CSIColor.GRAY);
			for (Specialist sp: Specialist.values()){
				if (city.getSpecialists(sp) > 0){
					csi.print(50, i++, sp.getDescription()+": "+city.getSpecialists(sp), CSIColor.YELLOW);
				}
			}
			if (city.getUnits().size()>0)
				csi.print(49, i++, ">Units", CSIColor.GRAY);
			for (Unit u: city.getUnits()){
				csi.print(50, i++, u.getDescription()+" (1 Food)", CSIColor.WHITE);
			}

		}
	}
	
	
	@Override
	public MenuItem getMenuItemForPicking(Equipment e) {
		return new UnitEquipmentMenuItem(e);
	}
	
	private Comparator<MenuItem> menuItemComparator = new Comparator<MenuItem>(){
		public int compare(MenuItem o1, MenuItem o2) {
			int at1 = ((Unit)((UnitEquipmentMenuItem)o1).getEquipment().getItem()).getAttackRoll().getMax();
			int at2 = ((Unit)((UnitEquipmentMenuItem)o2).getEquipment().getItem()).getAttackRoll().getMax();
			return at2 - at1;
		}
	};
	
	@Override
	public Comparator<MenuItem> getMenuItemComparator() {
		return menuItemComparator;
	}

	public Resource selectResourceFrom(String prompt,
			List<Pair<Resource, Integer>> availableResources)
			throws ActionException {
		csi.saveBuffer();
		Vector menuItems = new Vector();
		for (Pair<Resource, Integer> resourcePair : availableResources){
			menuItems.add(new SimpleMenuItem(resourcePair.getB()+" "+resourcePair.getA().getDescription(), resourcePair.getA()));
		}
		selectUnitList.setPrompt(prompt);
		selectUnitList.setMenuItems(menuItems);
		selectUnitList.draw();
		SimpleMenuItem selectedMenuItem = (SimpleMenuItem) selectUnitList.getSelection();
		csi.restore();
		csi.refresh();
		if (selectedMenuItem == null)
			return null;
		else 
			return (Resource) selectedMenuItem.getValue();
	}
	
	public Specialist selectSpecialistFrom(String prompt,
			List<Specialist> availableSpecialists) throws ActionException {
		csi.saveBuffer();
		Vector menuItems = new Vector();
		for (Specialist u : availableSpecialists){
			menuItems.add(new SimpleMenuItem(u.getDescription(), u));
		}
		selectUnitList.setPrompt(prompt);
		selectUnitList.setMenuItems(menuItems);
		selectUnitList.draw();
		SimpleMenuItem selectedMenuItem = (SimpleMenuItem) selectUnitList.getSelection();
		csi.restore();
		csi.refresh();
		if (selectedMenuItem == null)
			return null;
		else 
			return (Specialist) selectedMenuItem.getValue();
	}

	public Unit selectUnitFrom(String prompt, List<Unit> list) throws ActionException {
		csi.saveBuffer();
		Vector unitMenuItems = getUnitMenuItems(list);
		if (unitMenuItems == null){
			throw new ActionException();
		}
		selectUnitList.setPrompt(prompt);
		selectUnitList.setMenuItems(unitMenuItems);
		selectUnitList.draw();
		UnitMenuItem selectedMenuItem = (UnitMenuItem) selectUnitList.getSelection();
		csi.restore();
		csi.refresh();
		if (selectedMenuItem == null)
			return null;
		else 
			return selectedMenuItem.getUnit();
	}

	private Vector getUnitMenuItems(List<Unit> list) {
		Vector menuItems = new Vector();
		for (Unit u : list){
			menuItems.add(new UnitMenuItem(u));
		}
		return menuItems;
	}
	List<ListItem> reusable = new ArrayList<ListItem>();
	private List<ListItem> getInventoryListItems(List<Equipment> ntorinventoryy) {
		reusable.clear();
		for (Equipment e: ntorinventoryy){
			if (e.getItem() == ((UnitGroup)getPlayer()).getAttackingUnit())
				reusable.add(new UnitListItem((Unit)e.getItem(), ">"+((Unit)e.getItem()).getMenuDescription()));
			else
				reusable.add(new UnitListItem((Unit)e.getItem(), ((Unit)e.getItem()).getMenuDescription()));
		}
		Collections.sort(reusable, new Comparator<ListItem>(){
			public int compare(ListItem o1, ListItem o2) {
				int at1 = ((UnitListItem)o1).getUnit().getAttackRoll().getMax();
				int at2 = ((UnitListItem)o2).getUnit().getAttackRoll().getMax();
				return at2 - at1;
			}
		});
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