package zoo.animal;

public class AquaticAnimals extends Animal {
	private int depthResided; // how many feet deep does the animal live
	
	// constructors
	// Note - default constructor not used by any methods/classes in program - so it was removed (dead-code requirement)
		
	public AquaticAnimals(String name, boolean dangerous, int depthResided) {
		super(name, dangerous);
		this.depthResided = depthResided;
	}
	public AquaticAnimals(String name, int depthResided){
		super(name);
		this.depthResided = depthResided;
	}

	
	// methods
	@Override
	public boolean doATrick() {
		System.out.println("The cool trick was jumping out of the water! It swam from the depth of " + depthResided + " feet all the way to the top!");
		super.doATrick();
		return tired(getTrickCount());
	}

	@Override
	public boolean existInZoo(){
		System.out.println(getName() + " decided to swim on top of the water, and a bunch of visitors took pictures with it.");
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
