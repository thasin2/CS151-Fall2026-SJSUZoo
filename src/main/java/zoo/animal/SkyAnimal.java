package zoo.animal;

public class SkyAnimal extends Animal {
	private int maxHeight; // how high does the animal fly


	// constructors
	public SkyAnimal(String name) {
		super(name);
		this.setMaxHeight(100);
	}

	public SkyAnimal(String name, int maxHeight) {
		super(name);
		this.setMaxHeight(maxHeight);
	}

	//methods

	@Override
	public boolean doATrick() {
		System.out.println("The cool trick was flying all the way to " + getMaxHeight() + " ft and all the way back down. Super awesome!");
		return super.doATrick(); // super already checks if the animal is tired
	}

	@Override
	public boolean existInZoo(){

		System.out.println(getName() + " decided to fly close to the ground, visitors are able to observe the feathers it has!");
		boolean x = super.existInZoo();
		return x;
	}

	// getters and setters
	public int getMaxHeight() {
		return maxHeight;
	}

	public final void setMaxHeight(int maxHeight) {
		if (maxHeight < 0) {
			throw new IllegalArgumentException("Max height cannot be negative.");
		}
		this.maxHeight = maxHeight;
	}

	@Override
	public String toString() {
		return super.toString() + " maxHeight=" + maxHeight + " ft";
	}

}
