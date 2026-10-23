package zoo.animal;


public class Animal {
	
	// name and dangerous never change after the animal is made; the rest change as the animal plays
    private final boolean dangerous;
    private boolean isHungry;
    private boolean isActive;
    private final String name;
    private boolean injured;
    private int trickCount;
    // CONSTRUCTORS
    //most "required" attributes constructor
    public Animal(String name, boolean dangerous) {
		this.name = requireName(name);
        this.dangerous = dangerous;
		this.isActive = true;
    }

    //another constructor with name for only
    public Animal(String name) {
        this(name, false);
    }

	// returns the name trimmed, or throws if it is null or blank
	private static String requireName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Animal name cannot be blank.");
		}
		return name.trim();
	}
    
    // METHODS ------------------------------------------
    
	// method 1
    public boolean doATrick() {
		if ((getTrickCount() < 3)) {
    	System.out.println(" " + getName() + ", good job!");
		setTrickCount(getTrickCount() + 1);
    	boolean g = setHungry(true); // every time a zoo animal does a trick, they will get hungry, they get hungry after 3, but the user can keep calling until 5 tricks are done
		return g;
		}
		else {
		System.out.println(getName() + " can not do anymore tricks right now.");
		}
		return tired(getTrickCount()); // double checking here, incase it was not caught earlier
    }
    
	// method 2
    public boolean tired(int trickCount) {
    	if ((trickCount>=2) && (isHungry())) {
    		setActive(false);
    		System.out.println(getName() + " is tired. Please feed the animal");
			return true;
    	}
		return false;
    }
	
	// method 3
	public boolean existInZoo(){ // just a regular method that talks through the Animal walking, prancing, running in the Zoo - can get randomly injured
		System.out.println(getName() + " is in front of the visitors right now. So cool!");

		int x = (int)(Math.random() * 10); // will generate random numbers from 0 to 9
		if ((x<7) && (getTrickCount()>=1)){
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

	public String getName() {
		return name;
	}

	public boolean isHungry() {
		return isHungry;
	}

	public boolean setHungry(boolean isHungry) {
		this.isHungry = isHungry;

		

		if (!isHungry) {
			setActive(true);
			setTrickCount(0);
		}
		boolean y = tired(trickCount); // checking if the animal had 2 tricks and are hungry, animal will print out am tired
		return y;
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
		if (trickCount < 0) {
			throw new IllegalArgumentException("Trick count cannot be negative.");
		}
		this.trickCount = trickCount;
	}

	// two animals are the same if they have the same name (ignoring case), like visitors
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Animal)) {
			return false;
		}
		return name.equalsIgnoreCase(((Animal) other).name);
	}

	@Override
	public int hashCode() {
		return name.toLowerCase().hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{name='" + name + "', dangerous=" + dangerous + ", hungry=" + isHungry
				+ ", active=" + isActive + ", injured=" + injured + ", trickCount=" + trickCount + "}";
	}

}

