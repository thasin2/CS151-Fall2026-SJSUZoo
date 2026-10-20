package zoo.habitat;

import java.util.ArrayList;

import zoo.animal.Animal;

public class Rainforest extends TerrestrialHabitat {
	private int densityLev; // how dense is the habitat (out of 100) from trees, plants, etc.
	private int humidityLev;
	

	public Rainforest() {
		super();
		this.setDensityLev(0);
		this.setHumidityLev(0);
	}

	public Rainforest(String name, int capacity, ArrayList<Animal> Animals, int size, int numOfTrees, int numOfShrubs,
			int openSpaceSize, int densityLev) {
		super(name, capacity, Animals, size, numOfTrees, numOfShrubs, openSpaceSize);
		this.setDensityLev(densityLev);
	}

	public Rainforest(String name, int capacity, Animal animal, int size, int numOfTrees, int numOfShrubs,
			int openSpaceSize, int densityLev) {
		super(name, capacity, animal, size, numOfTrees, numOfShrubs, openSpaceSize);
		this.setDensityLev(densityLev);
	}
	
	// methods
	public void experienceRain() { // can make a zookeeper cause rain in this habitat, check in meeting
		setHumidityLev(100);
	}
	
	
	// getters and setters
	public int getDensityLev() {
		return densityLev;
	}

	public void setDensityLev(int densityLev) {
		this.densityLev = densityLev;
	}

	public int getHumidityLev() {
		return humidityLev;
	}

	public void setHumidityLev(int humidityLev) {
		this.humidityLev = humidityLev;
		
		if (humidityLev < 20) {
			for (Animal x: getAnimals()) {
				x.setActive(false);
			}
				
		}
	}

}
