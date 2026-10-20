package zoo.animal;

public class SkyAnimal extends Animal {
	private int maxHeight; // how high does the animal fly

	
	// constructors
	public SkyAnimal() {
		super();
		this.setMaxHeight(0);
	}
	
	public SkyAnimal(boolean dangerous, boolean isEndangered, String scientificName, String diet, int maxHeight) {
		super(dangerous, isEndangered, scientificName, diet);
		this.setMaxHeight(maxHeight);
	}

	public SkyAnimal(boolean dangerous, boolean isEndangered, String scientificName, String name, String diet, int maxHeight) {
		super(dangerous, isEndangered, scientificName, name, diet);
		this.setMaxHeight(maxHeight);
	}
	
	//methods
	
	@Override
	public void doATrick() {
		super.doATrick();
		System.out.println("The cool trick was flying upside down!");
	}
	
	// getters and setters
	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

}
