package zoo.animal;

public class TerrestrialAnimals extends Animal {
	private String footPrintShape; // is this a good attribute?
	private String skinType;
	
	
	
	// Constructors - can add more (ask during meeting)
	public TerrestrialAnimals() {
		super();
		this.setFootPrintShape("None");
		this.setSkinType("None");	
	}
	public TerrestrialAnimals(boolean dangerous, boolean isEndangered, String scientificName, String diet, String footPrintShape, String skinType) {
		super(dangerous, isEndangered, scientificName, diet);
		this.footPrintShape = footPrintShape;
		this.skinType = skinType;	
	}
	
	@Override
	public void doATrick() {
		super.doATrick();
		System.out.print("The cool trick was a twirl!");
		
	}
	
	// Getters and Setters
	public String getFootPrintShape() {
		return footPrintShape;
	}

	public void setFootPrintShape(String footPrintShape) {
		this.footPrintShape = footPrintShape;
	}

	public String getSkinType() {
		return skinType;
	}

	public void setSkinType(String skinType) {
		this.skinType = skinType;
	}

}
