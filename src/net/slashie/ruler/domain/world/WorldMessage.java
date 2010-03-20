package net.slashie.ruler.domain.world;

import net.slashie.serf.action.Actor;
import net.slashie.utils.Position;

public class WorldMessage {
	private String youMessage;
	private String theMessage;
	private Position position;
	private Actor performer;
	public final static int MESSAGE_RANGE = 8;
	public WorldMessage(String youMessage, String theMessage, Position position, Actor performer) {
		this.youMessage = youMessage;
		this.theMessage = theMessage;
		this.position = position;
		this.performer = performer;
	}
	public String getYouMessage() {
		return youMessage;
	}
	public String getTheMessage() {
		return theMessage;
	}
	public Position getPosition() {
		return position;
	}
	public Actor getPerformer() {
		return performer;
	}
}
