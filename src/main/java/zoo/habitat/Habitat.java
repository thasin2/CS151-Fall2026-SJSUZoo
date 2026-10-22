package zoo.habitat;

import java.util.ArrayList;
import zoo.animal.*;

public class Habitat implements zoo.Maintainable { // Extends Maintainable interface

	// PRIVATE INSTANCE VARIABLES ------------------------------------------------------------------------------------------
	
	private String name; // Name of Habitat; Example: Savannah, Rainforest, Meadow, etc.
	private int capacity; // Maximum number of animals habitat can hold
	private ArrayList<Animal> animals; // An ArrayList of all the animals that are currently in the habitat
	private int condition; // The condition of the habitat (0-100)
	
	// CONSTRUCTORS ----------------------------------------------------------------------------------------------------------

	// Default: No args, but set with some basic values
	public Habitat() {
		this.name = "Meadow"; // Assuming this as a baseline habitat
		this.capacity = 10;
		this.animals = new ArrayList<Animal>();
		this.condition = 100;
	}

	// Constructor, in the case that the user has a prespecified animal list
	public Habitat(String name, int capacity, ArrayList<Animal> animals) {
		this.name = name;
		this.capacity = capacity;
		this.animals = new ArrayList<Animal>(animals);
		this.condition = 100;
	}

	//Constructor, adding one animal to the habitat during creation
	public Habitat(String name, int capacity, Animal animal) {
		this.name = name;
		this.capacity = capacity;
		this.animals = new ArrayList<Animal>();
		addToAnimals(animal);
		this.condition = 100;
	
	}
	
	// METHODS ---------------------------------------------------------------------------------------------------------------

	// method 1 - has some random events that cause condition to worsen
	public boolean habitatOpen(){
		System.out.println("The habitat is open! Visitors can come and see all the animals!");
		
		for (Animal x: animals){

			System.out.println(x.getName() + " is now awake and active.");
			x.setActive(true);
			x.setHungry(true);
		}
		int x = getAnimals().size();
		int y = (int)(Math.random() * 10); // will generate random numbers from 0 to 9
		if (x>capacity/2) { // Capacity is nearing a high level, damage is more likely to be incurred.
			if (y<7){ // 70% chance of damage with a near capacity habitat
				setCondition(25);
				System.out.println("It looks like your habitat got damaged");
				if (needsMaintenance()){
					return false;
				}
			}
		}
		if (y < 4){
			System.out.println("It looks like a terrible thunderstorm occured which tore down some of the infrastructure in the habitat.");
			setCondition(20);
			if (needsMaintenance()){
				return false;
			}
		}
		System.out.println("The habitat is thriving and all the animals are coexisting amazingly!");
		return true;
	}
	
	// method 2 - closes the habitat
	public void habitatClose(){
		System.out.println("Putting the animals to sleep, going through the steps to close the habitat!");
		for (Animal x: animals){
			//System.out.println(x.getName() + " has been fed.");
			//x.setHungry(false);
			System.out.println(x.getName() + " is now sleeping and inactive.");
			x.setActive(false);
		}
		System.out.println("Since no animals are visible, all the visitors have left. We have put up a sign that the habitat is closed.");
	}

	// method 3 - makes sure the animals added make sense with the other habitat
	public boolean ensureSafeAdd(Animal animal){
		System.out.println("You wanted to add an animal to your habitat. The Zoo system has to check if this animal is compatible.");
		// All habitats can only have one type of animal (Terrestrial, SkyAnimal, AquaticAnimal, etc.)
		// Hence we just check one of the elements, and ensure that the new Animal to-be added is of that type as well
		if (animals.isEmpty()){
			System.out.println("Animal added!");
			return true;
		}
		if (animals.size() <= capacity) {
		Animal current = animals.get(0);
		if ((current instanceof AquaticAnimals) && (animal instanceof AquaticAnimals)){
			System.out.println("The animal you want to add is an aquatic animal, and is thus, compatible.");
			return true;
		}
		else if ((current instanceof TerrestrialAnimals) && (animal instanceof TerrestrialAnimals)){
			System.out.println("The animal you want to add is a terrestrial animal, and is thus, compatible.");
			return true;
		}
		else if ((current instanceof SkyAnimal) && (animal instanceof SkyAnimal)){
			System.out.println("The animal is compatible with the habitat. It has been added.");
			return true;
		}
		else {
			System.out.println("The animal you want to add is not compatible with the habitat.");
			System.out.println("Try again with another animal! The following type will be compatible: " + current.getClass().getSimpleName());
			return false;

		}
	}
	else {
		System.out.println("You reached capacity, sorry.");
		return false;
	}
	}

	// GETTERS and SETTERS, and Interface methods -------------------------------------------------------------------------------------------
	
	@Override
	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition){
		this.condition = condition;
	}

	@Override
	public boolean needsMaintenance() {
		if (condition<30){
			System.out.println("Oh no! Your habitat needs some maintenance! Let's call in the Zookeeper.");
			return true;
		}
		return false;
	}

	@Override
	public void performMaintenance() {
		System.out.println("Maintenance performed!");
		this.condition = 100;
	}
	
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
		return new ArrayList<Animal>(animals); // safe copy return
	}

	// customized, includes ensureSafeAdd method
	public boolean addToAnimals(Animal animal) {
		if (ensureSafeAdd(animal)) {
			animals.add(animal);
			return true;
		}
		else {
			System.out.println("Result: Animal not added.");
			return false;
		}
	}

}
