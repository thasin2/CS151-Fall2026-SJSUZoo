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

    public Attraction(int capacity) {
        this.isOpen = true;
        this.capacity = capacity;
        this.condition = 100;
    }

    public void status() {
        System.out.println("Attraction name: " + name);
        System.out.println("Attraction is " + (isOpen ? "open" : "closed") + " and is " + (isFull() ? "full" : "not full"));
        System.out.println("Current visitors: " + getCurrentVisitors());
        System.out.println("Total visited: " + totalVisited);
        System.out.println("Capacity: " + capacity);
    }

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

    public boolean remove(Visitor visitor) {
        return currentVisitors.remove(visitor);
    }

    public void open() {
        if (condition > 5) {
            isOpen = true;
        }
        else {
            System.out.println("Attraction is worn out and cannot be opened");
        }
    }

    public void close() {
        // removes all visitors
        currentVisitors.clear();
        isOpen = false;
    }

    public int getCurrentVisitorCount() {
        return currentVisitors.size();
    }

    public String getCurrentVisitors() {
        if (currentVisitors.size() == 0) {
            return "None";
        }
        return String.join(", ", currentVisitors.stream().map(Visitor::getName).collect(java.util.stream.Collectors.toList()));
    }

    public Visitor getCurrentVisitor() {
        return currentVisitors.get(0);
    }

    public int getTotalVisited() {
        return totalVisited;
    }

    public boolean isFull() {
        return currentVisitors.size() >= capacity;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Maintainable interface methods
    public int getCondition() {
        return condition;
    }

    public boolean needsMaintenance() {
        return condition < 25; // needs maintenance if condition is below 25
    }

    public void performMaintenance() {
        condition = 100; // restore condition to 100
    }

}
