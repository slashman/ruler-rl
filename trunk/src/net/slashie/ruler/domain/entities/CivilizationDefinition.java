package net.slashie.ruler.domain.entities;

import java.io.Serializable;

public class CivilizationDefinition implements Serializable {
	private String civilizationName;
	private CivilizationColors color;
	private String civilizationId;
	
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
	
}
