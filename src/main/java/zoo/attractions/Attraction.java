package zoo.attractions;

import java.util.ArrayList; 

public class Attraction {
    private boolean isOpen;

    private int totalVisited = 0;
    private ArrayList<String> currentVisitors = new ArrayList<>();
    private int capacity;

    // Create an attraction and open it
    public Attraction() {
        this.isOpen = true;
        this.capacity = 5; // default capacity
    }

    public Attraction(int capacity) {
        this.isOpen = true;
        this.capacity = capacity;
    }

    // note to self: change parameters to a visitor object after visitor class is implemented
    public void admit(String visitor) {
        if (!isOpen) {
            System.out.println("Store is closed. Try again later");
        }
        else if (currentVisitors.size() >= capacity) {
            System.out.println("Attraction is full. Please wait for a visitor to leave.");
        }
        else {
            System.out.println("Visitor is now attending the attraction");
            totalVisited++;
            currentVisitors.add(visitor);
        }
    }
    public void leaving(String visitor) {
        currentVisitors.remove(visitor);
        System.out.println(visitor + " has left the attraction.");
    }

    public void close() {
        isOpen = false;
    }

    public void open() {
        isOpen = true;
    }

    public int getCurrentVisitorCount() {
        return currentVisitors.size();
    }

    public String getCurrentVisitors() {
        return String.join(", ", currentVisitors);
    }

    public int getTotalVisited() {
        return totalVisited;
    }

    public boolean isFull() {
        return currentVisitors.size() >= capacity;
    }

    public void status() {
        System.out.println("Attraction is " + (isOpen ? "open" : "closed") + " and is " + (isFull() ? "full" : "not full"));
        System.out.println("Current visitors: " + getCurrentVisitors());
        System.out.println("Total visited: " + totalVisited);
        System.out.println("Capacity: " + capacity);
    }

}
