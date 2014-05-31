package com.schiappa.grillonavigante.KML;

public class Placemark {

	String Name;
	String Description;
	String Coordinates;
	
	public Placemark(){
		
	}
	
	public Placemark(String name, String description, String coordinates) {
		Name = name;
		Description = description;
		Coordinates = coordinates;
	}

	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getCoordinates() {
		return Coordinates;
	}
	public void setCoordinates(String coordinates) {
		Coordinates = coordinates;
	}
	
}
