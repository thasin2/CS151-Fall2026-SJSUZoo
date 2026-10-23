package zoo.animal;

public class TerrestrialAnimals extends Animal {
	private String skinType;  // furry, waterproof, etc.
	
	// Constructors - can add more (ask during meeting)

	public TerrestrialAnimals(String name, boolean dangerous, String skinType) {
		super(name, dangerous);
		this.skinType = skinType;
	}

	public TerrestrialAnimals(String name, String skinType) {
		super(name);
		this.skinType = skinType;
	}
	
	@Override
	public boolean doATrick() {
		System.out.print("The cool trick was a twirl!");
		super.doATrick();
		return tired(getTrickCount());
	}
	@Override
	public boolean existInZoo(){
		
		System.out.println(getName() + " decided to run across the habitat. We can see the skin type of the animal - " + skinType + ".");
		boolean x = super.existInZoo();
		return x;
	}
	
	// Getters and Setters

	public String getSkinType() {
		return skinType;
	}

	public void setSkinType(String skinType) {
		this.skinType = skinType;
	}

}
