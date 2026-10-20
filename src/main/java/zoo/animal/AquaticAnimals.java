package zoo.animal;

public class AquaticAnimals extends Animal {
	private String waterEnvironment; // freshwater, sea water, mix, etc.
	private int depthResided; // how many feet deep does the animal live
	private int speed; // with what speed does the animal swim
	

	
	// constructors
	
	//default
	public AquaticAnimals() {
		super();
		this.waterEnvironment = null;
		this.depthResided = 0;
		this.speed = 0;
			
	}
	
	public AquaticAnimals(boolean dangerous, boolean isEndangered, String scientificName, String diet, String waterEnvironment) {
		super(dangerous, isEndangered, scientificName, diet);
		this.waterEnvironment = waterEnvironment;
	}
	public AquaticAnimals(boolean dangerous, boolean isEndangered, String scientificName, String name, String diet, String waterEnvironment) {
		super(dangerous, isEndangered, scientificName, name, diet);
		this.waterEnvironment = waterEnvironment;
	}
	
	// methods ? 
	@Override
	public void doATrick() {
		super.doATrick();
		System.out.println("The cool trick was jumping out of the water!");
	}
	
	
	// getters and setters
	public String getWaterEnvironment() {
		return waterEnvironment;
	}

	public void setWaterEnvironment(String waterEnvironment) {
		this.waterEnvironment = waterEnvironment;
	}

	public int getDepthResided() {
		return depthResided;
	}

	public void setDepthResided(int depthResided) {
		this.depthResided = depthResided;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
