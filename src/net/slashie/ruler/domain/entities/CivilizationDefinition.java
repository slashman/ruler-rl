package net.slashie.ruler.domain.entities;

import java.io.Serializable;

import net.slashie.utils.Util;

public class CivilizationDefinition implements Serializable {
	private String civilizationName;
	private String defaultLeaderName;
	private CivilizationColors color;
	private String civilizationId;
	private String[] cityNames;
	
	public String getCivilizationName() {
		return civilizationName;
	}
	public void setCivilizationName(String civilizationName) {
		this.civilizationName = civilizationName;
	}
	public CivilizationColors getColor() {
		return color;
	}
	public void setColor(CivilizationColors color) {
		this.color = color;
	}
	public String getCivilizationId() {
		return civilizationId;
	}
	public void setCivilizationId(String civilizationId) {
		this.civilizationId = civilizationId;
	}
	public String getDefaultLeaderName() {
		return defaultLeaderName;
	}
	public void setDefaultLeaderName(String defaultLeaderName) {
		this.defaultLeaderName = defaultLeaderName;
	}
	public String getACityName() {
		return Util.randomElementOf(cityNames);
	}
	
	public void setCityNames(String[] cityNames) {
		this.cityNames = cityNames;
	}
	
}
