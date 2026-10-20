package zoo.animal;

public class Animal {
	
	
	// no attributes made final, as we may want to enter or change values later based on constructor used
    private boolean dangerous;
    private boolean isHungry; // can implement this as a int level
    private boolean isActive;
    private boolean isEndangered; 
    private String name;
    private String scientificName;
    private String diet; // can be used by Staff/Inventory to check what food to be given
    private boolean injured; // can add a health level as well, will talk with team
    // CONSTRUCTORS
    private int trickCount;
    
    //Default
    public Animal() {
    	this.dangerous = false;
    	this.isHungry = false;
    	this.isActive = false;
    	this.isEndangered = false;
    	this.name = "Unnamed";
    	this.scientificName = "Unknown";
    	this.setDiet("Unknown");
    	this.trickCount = 0;
    }
    
    //most "required" attributes constructor
    public Animal(boolean dangerous, boolean isEndangered, String scientificName, String diet) {
        this.dangerous = dangerous;
        this.isEndangered = isEndangered;
        this.scientificName = scientificName;
        this.diet = diet;
        
    }
    
    //another constructor with personal name for animal
    public Animal(boolean dangerous, boolean isEndangered, String scientificName, String name, String diet) {
        this.dangerous = dangerous;
        this.isEndangered = isEndangered;
        this.scientificName = scientificName;
        this.name = name;
        this.diet = diet;
    } 
    
    // methods
    
    public void doATrick() {
    	System.out.println(getName() + "did a cool trick!");
    	setHungry(true);
    	trickCount++;
    }
    
    public void tired(int trickCount) {
    	if ((trickCount>3) && (isHungry)) {
    		setActive(false);
    		System.out.println(getName() + "is tired.");
    	}
    	
    }
    
    
    // GETTERS and SETTERS for all private attributes
    public boolean isDangerous() {
        return dangerous;
    }
    public void setDangerous(boolean dangerous) {
    	this.dangerous = dangerous;
    }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isHungry() {
		return isHungry;
	}

	public void setHungry(boolean isHungry) {
		this.isHungry = isHungry;
		
		if (!isHungry) {
			setActive(true);
			setTrickCount(0);
		}
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isEndangered() {
		return isEndangered;
	}
	public void setEndangered(boolean isEndangered) {
		this.isEndangered = isEndangered;
	}

	public String getScientificName() {
		return scientificName;
	}
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getDiet() {
		return diet;
	}

	public void setDiet(String diet) {
		this.diet = diet;
	}

	public boolean isInjured() {
		return injured;
	}

	public void setInjured(boolean injured) {
		this.injured = injured;
	}

	public int getTrickCount() {
		return trickCount;
	}

	public void setTrickCount(int trickCount) {
		this.trickCount = trickCount;
	}

}

