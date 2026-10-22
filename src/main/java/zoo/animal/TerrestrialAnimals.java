package zoo.animal;

public class TerrestrialAnimals extends Animal {
	private String skinType;  // furry, waterproof, etc.
	
	// Constructors - can add more (ask during meeting)
	public TerrestrialAnimals() {
		super();
		this.setSkinType("Thick fur");	
	}
	public TerrestrialAnimals(String name, boolean dangerous, String skinType) {
		super(name, dangerous);
		this.skinType = skinType;
	}
	public TerrestrialAnimals(String name){
		super(name);
		this.skinType = "Thick fur";
	}
	
	@Override
	public void doATrick() {
		super.doATrick();
		System.out.print("The cool trick was a twirl!");
		
	}
	@Override
	public void existInZoo(){
		
		System.out.println(getName() + "decided to run across the habitat. We can see the skin type of the animal - " + skinType + ".");
		super.existInZoo();
	}
	
	// Getters and Setters

	public String getSkinType() {
		return skinType;
	}

	public void setSkinType(String skinType) {
		this.skinType = skinType;
	}

}
