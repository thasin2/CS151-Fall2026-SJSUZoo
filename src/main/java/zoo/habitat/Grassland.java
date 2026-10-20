package zoo.habitat;

import java.util.ArrayList;

import zoo.animal.Animal;

public class Grassland extends TerrestrialHabitat {
	private String type; // savannah, praire, meadow, etc.
	private int lengthOfGrass;
	private int numOfFlowers;
	
	// constructors
	public Grassland() {
		super();
		this.setType(null);
	}

	public Grassland(String name, int capacity, ArrayList<Animal> Animals, int size, int numOfTrees, int numOfShrubs,
			int openSpaceSize, String type) {
		super(name, capacity, Animals, size, numOfTrees, numOfShrubs, openSpaceSize);
		this.setType(type);
	}

	public Grassland(String name, int capacity, Animal animal, int size, int numOfTrees, int numOfShrubs,
			int openSpaceSize, String type) {
		super(name, capacity, animal, size, numOfTrees, numOfShrubs, openSpaceSize);
		this.setType(type);
	}
	
	//methods?
	
	
	
	// getters and setters

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLengthOfGrass() {
		return lengthOfGrass;
	}

	public void setLengthOfGrass(int lengthOfGrass) {
		this.lengthOfGrass = lengthOfGrass;
	}

	public int getNumOfFlowers() {
		return numOfFlowers;
	}

	public void setNumOfFlowers(int numOfFlowers) {
		this.numOfFlowers = numOfFlowers;
	}
	
	
	

}
