package zoo.habitat;

import java.util.ArrayList;

import zoo.animal.Animal;

public class TerrestrialHabitat extends Habitat {
	private int numOfTrees;
	private int numOfShrubs;
	private int openSpaceSize; //how much "open space" is there, for animals like bison that need "grazing" area
	
	// constructors
	public TerrestrialHabitat() {
		super();
		this.setNumOfTrees(0);
		this.setNumOfShrubs(0);
		this.setOpenSpaceSize(0);
		
	}
	public TerrestrialHabitat(String name, int capacity, ArrayList<Animal> Animals, int size, int numOfTrees, int numOfShrubs, int openSpaceSize) {
		super(name, capacity, Animals, size);
		this.setNumOfTrees(numOfTrees);
		this.setNumOfShrubs(numOfShrubs);
		this.setOpenSpaceSize(openSpaceSize);
	}

	public TerrestrialHabitat(String name, int capacity, Animal animal, int size,  int numOfTrees, int numOfShrubs, int openSpaceSize) {
		super(name, capacity, animal, size);
		this.setNumOfTrees(numOfTrees);
		this.setNumOfShrubs(numOfShrubs);
		this.setOpenSpaceSize(openSpaceSize);
		
	}
	
	// methods?
	
	
	
	// getters and setters

	public int getNumOfTrees() {
		return numOfTrees;
	}

	public void setNumOfTrees(int numOfTrees) {
		this.numOfTrees = numOfTrees;
	}

	public int getNumOfShrubs() {
		return numOfShrubs;
	}

	public void setNumOfShrubs(int numOfShrubs) {
		this.numOfShrubs = numOfShrubs;
	}

	public int getOpenSpaceSize() {
		return openSpaceSize;
	}

	public void setOpenSpaceSize(int openSpaceSize) {
		this.openSpaceSize = openSpaceSize;
	}

}
