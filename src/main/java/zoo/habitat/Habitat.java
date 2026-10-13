package zoo.habitat;
import java.util.ArrayList;

import zoo.habitat.*;

public class Habitat {
	private String name;
	private int capacity;
	private ArrayList<String> Animals; // figure out getter and setter for this
	// we want to add animals to this list, and when we return dont
	// want to return the object itself
	
	public Habitat() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}
