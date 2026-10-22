package zoo.animal;


public class Animal {
	
	// no attributes made final, as we may want to enter or change values later based on constructor used
    private boolean dangerous;
    private boolean isHungry;
    private boolean isActive;
    private String name;
    private boolean injured;
    // CONSTRUCTORS
    private int trickCount;
    
    //Default
    public Animal() {
    	this.dangerous = false;
    	this.isHungry = false;
    	this.isActive = false;
    	this.name = "Unnamed";
    	this.trickCount = 0;
    }
    
    //most "required" attributes constructor
    public Animal(String name, boolean dangerous) {
		this.name = name;
        this.dangerous = dangerous;
    }
    
    //another constructor with name for only
    public Animal(String name) {
        this.dangerous = false;
        this.name = name;
    }
    
    // METHODS ------------------------------------------
    
	// method 1
    public void doATrick() {
		if ((getTrickCount() < 3)) {
    	System.out.println(getName() + "did a cool trick!");
		setTrickCount(getTrickCount() + 1);
    	setHungry(true); // every time a zoo animal does a trick, they will get hungry, they get hungry after 3, but the user can keep calling until 5 tricks are done
		}
		System.out.println(getName() + "can not do anymore tricks right now.");
    }
    
	// method 2
    public boolean tired(int trickCount) {
    	if ((trickCount>2) && (isHungry())) {
    		setActive(false);
    		System.out.println(getName() + "is tired. Please feed the animal");
			return true;
    	}
		return false;
    }
	
	// method 3
	public boolean existInZoo(){ // just a regular method that talks through the Animal walking, prancing, running in the Zoo - can get randomly injured
		System.out.println(getName() + "is in front of the visitors. So cool!");

		int x = (int)(Math.random() * 10); // will generate random numbers from 0 to 10
		if ((x<5) && (getTrickCount()>=2)){
			setInjured(true);
			System.out.println("Aw man! The animal was just existing, and it got randomly injured. Please get the vet to fix the animal.");
			setActive(false);
			return false;
		}
		return true;
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

		// should have a check here that asks the zookeeper to feed (zoo keeper can say no or yes, can lead to animal becoming hungry)

		if (!isHungry) {
			setActive(true);
			setTrickCount(0);
		}
		tired(trickCount); // checking if the animal had 2 tricks and are hungry, animal will print out am tired
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

