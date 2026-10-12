package zoo.attractions;

public class Attraction {
    private boolean isOpen;

    private static int totalVisitors = 0;
    private static int currentVisitors = 0;

    public Attraction() {
        this.isOpen = true;
    }

    // note to self: add parameter for a visitor to attending & leaving
    public void attending() {
        if (!isOpen) {
            System.out.println("Store is closed. Try again later");
        }
        else {
            System.out.println("Visitor is now attending the attraction");
            totalVisitors++;
            currentVisitors++;
        }
    }
    public void leaving() {
        currentVisitors--;
    }

    public void close() {
        isOpen = false;
    }

    public int getCurrentVisitors() {
        return currentVisitors;
    }

    public int getTotalVisitors() {
        return totalVisitors;
    }

}
