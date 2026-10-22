package zoo.animal;

public class AquaticAnimals extends Animal {
	private final int depthResided; // how many feet deep does the animal live

	// constructors
	public AquaticAnimals(String name, int depthResided){
		super(name);
		if (depthResided < 0) {
			throw new IllegalArgumentException("Depth cannot be negative.");
		}
		this.depthResided = depthResided;
	}

	// methods
	@Override
	public boolean doATrick() {
		System.out.println("The cool trick was jumping out of the water! It swam from the depth of " + depthResided + " feet all the way to the top!");
		return super.doATrick(); // super already checks if the animal is tired
	}

	@Override
	public boolean existInZoo(){
		System.out.println(getName() + " decided to swim on top of the water, and a bunch of visitors took pictures with it.");
		boolean x = super.existInZoo();
		return x;
	}

	@Override
	public String toString() {
		return super.toString() + " depthResided=" + depthResided + " ft";
	}

}
