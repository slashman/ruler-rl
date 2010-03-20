package net.slashie.ruler.ui.console;

import net.slashie.libjcsi.ConsoleSystemInterface;
import net.slashie.libjcsi.textcomponents.MenuItem;

public class SimpleMenuItem implements MenuItem{
	private String description;
	private Object value;
	public SimpleMenuItem(String description, Object value) {
		this.description = description;
		this.value = value; 
	}
	public char getMenuChar() {
		return '*';
	}
	
	public int getMenuColor() {
		return ConsoleSystemInterface.WHITE;
	}
	
	public String getMenuDescription() {
		return description;
	}
	public Object getValue() {
		return value;
	}
	
	

}
