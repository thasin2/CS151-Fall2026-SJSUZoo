package zoo.animal;

public class TerrestrialAnimals extends Animal {
	private String footPrintShape; // is this a good attribute?
	private String skinType;
	
	
	public TerrestrialAnimals() {
		super();
		this.setFootPrintShape("None");
		this.setSkinType("None");
		
	}
	
	public TerrestrialAnimals(boolean dangerous, String name, String scientificName) {
		// TEMP fix so the project compiles: Animal has no scientificName yet, so it is not stored, and isEndangered defaults to false
		super(dangerous, false, name);
	}

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
