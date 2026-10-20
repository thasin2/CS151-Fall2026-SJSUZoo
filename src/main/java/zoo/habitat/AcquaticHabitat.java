package zoo.habitat;

import java.util.ArrayList;

import zoo.animal.Animal;

public class AcquaticHabitat extends Habitat {
	private String typeOfWater; //fresh, salty
	private String waterHabitatType; //pond, water tank aquarium, etc. I can make subclasses here, but not sure what methods would be implemented
	private int maxDepth;

	
	// constructors
	public AcquaticHabitat() {
		super();
		this.setMaxDepth(0);
	}

	public AcquaticHabitat(String name, int capacity, ArrayList<Animal> Animals, int size, int maxDepth) {
		super(name, capacity, Animals, size);
		this.setMaxDepth(maxDepth);
	}

	public AcquaticHabitat(String name, int capacity, Animal animal, int size, int maxDepth) {
		super(name, capacity, animal, size);
		this.setMaxDepth(maxDepth);
	}

	
	
	// getters and setters
	public String getWaterHabitatType() {
		return waterHabitatType;
	}

	public void setWaterHabitatType(String waterHabitatType) {
		this.waterHabitatType = waterHabitatType;
	}

	public String getTypeOfWater() {
		return typeOfWater;
	}

	public void setTypeOfWater(String typeOfWater) {
		this.typeOfWater = typeOfWater;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

}
