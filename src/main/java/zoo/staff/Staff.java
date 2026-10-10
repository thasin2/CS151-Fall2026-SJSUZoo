package zoo.staff;
import java.lang.IllegalArgumentException;

public abstract class Staff {
    private final String id;
    private String name;
    private double pay;
    private boolean fullTime;
    private int shiftsThisWeek; // each shift is 8 hours
    private boolean suspended; // true means they're suspended, false means they are not

    protected Staff(String id, String name, double pay, boolean fullTime) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff name cannot be null or empty.");
        }
        if (pay < 0) {
            throw new IllegalArgumentException("Pay cannot be negative or zero.");
        }

        this.id = id.trim();
        this.name = name.trim();
        this.pay = pay;
        this.fullTime = fullTime; 
        this.shiftsThisWeek = 0; // default to 0 shifts
        this.suspended = false; // default to not suspended
    }

    // getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPay() {
        return pay;
    }

    public boolean isFullTime() {
        return fullTime;
    }

    public int getShiftsThisWeek() {
        return shiftsThisWeek;
    }

    public boolean isSuspended() {
        return suspended;
    }

    // setters
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff name cannot be null or empty.");
        }
        this.name = name;
    }

    public void setPay(double pay) {
        if (pay < 0) {
            throw new IllegalArgumentException("Pay cannot be negative.");
        }
        this.pay = pay;
    }

}
