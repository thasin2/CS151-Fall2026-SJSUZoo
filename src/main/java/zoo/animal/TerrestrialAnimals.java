package zoo.animal;

public class TerrestrialAnimals extends Animal {
	
	private final String skinType;  // furry, waterproof, etc.

	// Constructors - can add more (ask during meeting)

	public TerrestrialAnimals(String name, boolean dangerous, String skinType) {
		super(name, dangerous);
		this.skinType = skinType;
	}

	public TerrestrialAnimals(String name, String skinType) {
		super(name);
		if (skinType == null || skinType.trim().isEmpty()) {
			throw new IllegalArgumentException("Skin type cannot be blank.");
		}
		this.skinType = skinType.trim();
	}

	@Override
	public boolean doATrick() {
		System.out.print("The cool trick was a twirl!");
		return super.doATrick(); // super already checks if the animal is tired
	}
	@Override
	public boolean existInZoo(){

		System.out.println(getName() + " decided to run across the habitat. We can see the skin type of the animal - " + skinType + ".");
		boolean x = super.existInZoo();
		return x;
	}

	@Override
	public String toString() {
		return super.toString() + " skinType='" + skinType + "'";
	}

}
