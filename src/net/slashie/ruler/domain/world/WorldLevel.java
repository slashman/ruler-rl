package net.slashie.ruler.domain.world;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.slashie.ruler.domain.entities.Civilization;
import net.slashie.serf.level.BufferedLevel;
import net.slashie.utils.Position;

public class WorldLevel extends BufferedLevel {
	private String musicKey;
	private Map<Position, City> cities = new Hashtable<Position, City>();
	private List<City> citiesL = new ArrayList<City>();

	public String getMusicKey() {
		return musicKey;
	}

	public void setMusicKey(String musicKey) {
		this.musicKey = musicKey;
	}

	public void addSettlement(String name, Position position, Civilization civ) {
		//getCells()[position.z][position.x][position.y] = MapCellFactory.getMapCellFactory().getMapCell("SETTLEMENT_1_"+civ.getCivDefinition().getColor().toString());
		City city = new City(name, 1, civ);
		city.setPosition(new Position(position));
		addSettlement(city);
	}
	
	public boolean isWater(Position p){
		return getMapCell(p).isWater();
	}
	
	@Override
	public boolean isWalkable(Position where) {
		return super.isWalkable(where) && !isWater(where);
	}

	
	public Integer getResourcesAt(int x, int y, Resource r) {
		return ((WorldTile)getMapCell(x,y,0)).getResource(r);
	}

	public City getCityAt(Position position) {
		return cities.get(position);
	}

	public List<City> getCities() {
		return citiesL;
	}

	public void addSettlement(City city) {
		addFeature(city);
		city.calculateResources();
		cities.put(city.getPosition(), city);
		citiesL.add(city);
	}
	
	public void addWorldMessage(WorldMessage message){
		if (message.getPerformer() == getPlayer()){
			addMessage(message.getYouMessage());
		} else {
			if (Position.distance(message.getPosition(), getPlayer().getPosition()) <= WorldMessage.MESSAGE_RANGE){
				addMessage(message.getTheMessage());
			}
		}
		
	}

	
	public int getResourcesAround(Position position, Resource resource, int range) {
		int count = 0;
		for (int x = position.x - range; x <= position.x + range; x++){
			for (int y = position.y - range; y <= position.y + range; y++){
				if (!isValidCoordinate(x, y))
					continue;
				count += getResourcesAt(x,y,resource);
			}
		}
		return count;
	}
}
