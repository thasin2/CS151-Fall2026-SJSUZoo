package zoo.attractions;

import zoo.Visitor;
import java.util.ArrayList; 

public class Attraction implements zoo.Maintainable {
    private boolean isOpen;

    private int totalVisited = 0;
    private ArrayList<Visitor> currentVisitors = new ArrayList<>();
    private int capacity;
    private int condition;

    // Create an attraction and open it
    public Attraction() {
        this.isOpen = true;
        this.capacity = 5; // default capacity
        this.condition = 100; // default condition
    }

    public Attraction(int capacity) {
        this.isOpen = true;
        this.capacity = capacity;
    }

    public void status() {
        System.out.println("Attraction is " + (isOpen ? "open" : "closed") + " and is " + (isFull() ? "full" : "not full"));
        System.out.println("Current visitors: " + getCurrentVisitors());
        System.out.println("Total visited: " + totalVisited);
        System.out.println("Capacity: " + capacity);
    }

    public void admit(Visitor visitor) {
        if (!isOpen) {
            System.out.println("Store is closed. Try again later");
        }
        else if (currentVisitors.size() >= capacity) {
            System.out.println("Attraction is full. Please wait for a visitor to leave.");
        }
        else {
            System.out.println(visitor.getName() + " is now attending the attraction");
            totalVisited++;
            condition -= 1; // each visitor reduces condition by 1
            currentVisitors.add(visitor);
        }  
    }

    public void leaving(Visitor visitor) {
        currentVisitors.remove(visitor);
        condition -= 1; // each visitor reduces condition by 1
        System.out.println(visitor.getName() + " has left the attraction.");
    }

    public void open() {
        if (condition > 25) {
            isOpen = true;
        }
        else if (10 < condition && condition < 25) {
            System.out.println("Attraction is open but in need of maintenance soon");
            isOpen = true;
        }
        else {
            System.out.println("Attraction is worn out and cannot be opened safely");
        }
    }

    public void close() {
        // removes all visitors
        for (Visitor visitor : currentVisitors) {
            leaving(visitor);
        }
        System.out.println("All visitors have left and the attraction is now closed.");
        isOpen = false;

    }

    public int getCurrentVisitorCount() {
        return currentVisitors.size();
    }

    public String getCurrentVisitors() {
        return String.join(", ", currentVisitors.stream().map(Visitor::getName).collect(java.util.stream.Collectors.toList()));
    }

    public int getTotalVisited() {
        return totalVisited;
    }

    public boolean isFull() {
        return currentVisitors.size() >= capacity;
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
        System.out.println("Attraction has been restored to full condition.");
    }

}
