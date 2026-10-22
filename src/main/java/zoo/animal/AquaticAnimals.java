package zoo.animal;

public class AquaticAnimals extends Animal {
	private int depthResided; // how many feet deep does the animal live
	
	// constructors
	
	//default
	public AquaticAnimals() {
		super();
		this.depthResided = 100;
			
	}
	
	public AquaticAnimals(String name, boolean dangerous, int depthResided) {
		super(name, dangerous);
		this.depthResided = depthResided;
	}
	public AquaticAnimals(String name) {
		super(name);
		this.depthResided = 100;
	}
	
	// methods
	@Override
	public void doATrick() {
		super.doATrick();
		System.out.println("The cool trick was jumping out of the water! It swam from the depth of " + depthResided + " feet all the way to the top!");
	}

	@Override
	public boolean existInZoo(){
		System.out.println(getName() + "decided to swim on top of the water, and a bunch of visitors took pictures with it.");
		boolean x = super.existInZoo();
		return x;
	}

	
	// getters and setters

	public int getDepthResided() {
		return depthResided;
	}

	public void setDepthResided(int depthResided) {
		this.depthResided = depthResided;
	}

}
