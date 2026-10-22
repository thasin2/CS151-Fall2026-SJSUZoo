package zoo.attractions;

import zoo.Visitor;
import java.util.ArrayList; 

public class Attraction implements zoo.Maintainable {
    private boolean isOpen;
    private int totalVisited = 0;
    private ArrayList<Visitor> currentVisitors = new ArrayList<>();
    private int capacity;
    private int condition;
    private String name = "Unknown";

    // Create an attraction and open it
    public Attraction() {
        this.isOpen = true;
        this.capacity = 5; // default capacity
        this.condition = 100; // default condition
    }

    // Creates an attraction with a custom capacity
    public Attraction(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Attraction capacity must be at least 1.");
        }
        this.isOpen = true;
        this.capacity = capacity;
        this.condition = 100;
    }

    @Override
    public String toString() {
        return "Attraction name: " + name + "\nAttraction is " + (isOpen ? "open" : "closed") + " and is " +
        (isFull() ? "full" : "not full") + "\nCurrent visitors: " + getCurrentVisitors() + "\nTotal visited: " +
        totalVisited + "\nCapacity: " + capacity;
    }

    // Adds visitor to the attraction if possible
    public boolean admit(Visitor visitor) {
        if (!isOpen || condition <= 0 || currentVisitors.size() >= capacity) {
            return false;
        }
        else {
            totalVisited++;
            condition -= 5; // each visitor reduces condition by 5
            currentVisitors.add(visitor);
            return true;
        }
    }

    // Opens attraction if in good enough condition
    public void open() {
        if (condition > 5) {
            isOpen = true;
            System.out.println("The attraction is now open.");
        }
        else {
            System.out.println("Attraction is worn out and cannot be opened.");
        }
    }

    // Removes all visitors and closes attraction
    public void close() {
        currentVisitors.clear();
        isOpen = false;
        System.out.println("The attraction is now closed.");
    }

    // Returns all visitor names
    public String getCurrentVisitors() {
        if (currentVisitors.size() == 0) {
            return "None";
        }
        return String.join(", ", currentVisitors.stream().map(Visitor::getName).collect(java.util.stream.Collectors.toList()));
    }

    public int getCurrentVisitorCount() {
        return currentVisitors.size();
    }

    // Return the first visitor in an attraction
    public Visitor getCurrentVisitor() {
        return currentVisitors.get(0);
    }

    public int getTotalVisited() {
        return totalVisited;
    }

    // Returns a specific visitor from the attraction
    public boolean remove(Visitor visitor) {
        return currentVisitors.remove(visitor);
    }

    public boolean isFull() {
        return currentVisitors.size() >= capacity;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Attraction name cannot be blank.");
        }
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    // Maintainable interface methods
    @Override
    public int getCondition() {
        return condition;
    }

    @Override
    public boolean needsMaintenance() {
        return condition < 25; // needs maintenance if condition is below 25
    }

    @Override
    public void performMaintenance() {
        condition = 100; // restore condition to 100
        System.out.println("Attraction " + name + " successfully repaired");
    }

}
