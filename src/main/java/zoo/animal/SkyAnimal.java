package zoo.animal;

public class SkyAnimal extends Animal {
	private int maxHeight; // how high does the animal fly

	
	// constructors
	public SkyAnimal() {
		super();
		this.setMaxHeight(0);
	}
	
	public SkyAnimal(String name, boolean dangerous, int maxHeight) {
		super(name, dangerous);
		this.setMaxHeight(maxHeight);
	}

	public SkyAnimal(String name) {
		super(name);
		this.setMaxHeight(100);
	}
	//methods
	
	@Override
	public void doATrick() {
		super.doATrick();
		System.out.println("The cool trick was flying all the way to " + getMaxHeight() + " ft and all the way back down. Super awesome!");
	}

	@Override
	public boolean existInZoo(){
		
		System.out.println(getName() + "decided to fly close to the ground, visitors are able to observe the feathers it has!");
		boolean x = super.existInZoo();
		return x;
	}
	
	// getters and setters
	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

}
