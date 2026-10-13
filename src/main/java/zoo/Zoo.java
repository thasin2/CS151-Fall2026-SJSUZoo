package zoo;

import java.util.ArrayList;

public class Zoo {
    private String name;
    private int capacity;
    private ArrayList<Visitor> visitors;

    public Zoo(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.visitors = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getVisitorCount() {
        return visitors.size();
    }

    public boolean isFull() {
        return visitors.size() >= capacity;
    }

    public boolean admitVisitor(Visitor visitor) {
        if (visitor == null || isFull()) {
            return false;
        }

        visitors.add(visitor);
        return true;
    }

    public boolean removeVisitor(Visitor visitor) {
        return visitors.remove(visitor);
    }
}

