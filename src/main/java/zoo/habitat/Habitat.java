package zoo.habitat;
import java.util.ArrayList;

import zoo.animal.Animal;
import zoo.habitat.*;

public class Habitat {
	private String name;
	private int capacity;
	private ArrayList<Animal> Animals; // figure out getter and setter for this
	// we want to add animals to this list, and when we return dont
	// want to return the object itself
	
	private int condition;
	private boolean isDamaged;
	private int size;
	
	//constructors

	//default
	public Habitat() {
		this.name = "Unknown";
		this.capacity = 0;
		this.Animals = new ArrayList<Animal>();
		this.size = 0;
	}
	
	//if you have the Animal arraylist already
	public Habitat(String name, int capacity, ArrayList<Animal> Animals, int size) {
		this.name = name;
		this.capacity = capacity;
		this.Animals = new ArrayList<Animal>(Animals);
		this.size = size;
	}
	
	
	//only have one animal to add to a habitat at creation
	public Habitat(String name, int capacity, Animal animal, int size) {
		this.name = name;
		this.capacity = capacity;
		addToAnimals(animal);
		this.size = size;
	}
	
	//methods
	
	

	
	
	//getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public ArrayList<Animal> getAnimals() {
		return Animals;
	}

	public void addToAnimals(Animal animal) {
		Animals.add(animal);
	}


	public boolean isDamaged() {
		return isDamaged;
	}

	public void setDamaged(boolean isDamaged) {
		this.isDamaged = isDamaged;
		
		if (isDamaged) {
			//can have other ways to setCondition to worse numbers -- will 
			// discuss with team
			setCondition(30);
		}
	}

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}

}
