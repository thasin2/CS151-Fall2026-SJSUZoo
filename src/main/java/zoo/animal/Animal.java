package zoo.animal;

public class Animal {
	// need to make at least 3 subclasses
	// specific animals (ie - Elephants, Crows, etc.), or broader - Canidae, Felidae
	
    private final boolean dangerous;
    private boolean isHungry;
    private boolean isActive;
    private final boolean isEndangered;
    private String name;
    
    
    public Animal() {
    	this.dangerous = false;
    	this.isHungry = false;
    	this.isActive = false;
    	this.isEndangered = false;
    	this.name = "Unnamed";
    }
    
    public Animal(boolean dangerous, boolean isEndangered, String name) {
        this.dangerous = dangerous;
        this.isEndangered = isEndangered;
        this.name = name;
        
    }
    
    

    public boolean isDangerous() {
        return dangerous;
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
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}

