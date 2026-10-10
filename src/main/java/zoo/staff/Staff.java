package zoo.staff;
import java.lang.IllegalArgumentException;

public abstract class Staff {
    public static final int HOURS_PER_SHIFT = 8; // constant for hours in a shift
    public static final int FULL_TIME_MAX_SHIFTS = 5; // constant for maximum shifts in a week
    public static final int PART_TIME_MAX_SHIFTS = 4; // constant for maximum shifts in a week

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
            throw new IllegalArgumentException("Pay cannot be negative.");
        }

        this.id = id.trim();
        this.name = name.trim();
        this.pay = pay;
        this.fullTime = fullTime; 
        this.shiftsThisWeek = 0; // default to 0 shifts
        this.suspended = false; // default to not suspended
    }

    protected abstract double calculateWeeklyPay(); // subclasses can calculate weekly pay based on their own rules

    public void workShift() {
        if (suspended) {
            throw new IllegalStateException("Cannot work a shift while suspended.");
        }
        if (fullTime && shiftsThisWeek >= FULL_TIME_MAX_SHIFTS) {
            throw new IllegalStateException("Full-time staff cannot work more than " + FULL_TIME_MAX_SHIFTS + " shifts in a week.");
        }
        if (!fullTime && shiftsThisWeek >= PART_TIME_MAX_SHIFTS) {
            throw new IllegalStateException("Part-time staff cannot work more than " + PART_TIME_MAX_SHIFTS + " shifts in a week.");
        }

        shiftsThisWeek++;
    }

    public void giveRaise(double percent) {
        if (percent <= 0) {
            throw new IllegalArgumentException("Raise percentage cannot be zero or negative.");
        } else if (percent > 30) {
            throw new IllegalArgumentException("Raise percentage cannot exceed 30%.");
        } else if (this.pay == 0) {
            throw new IllegalStateException("Cannot give a raise to volunteer with zero pay.");
        }

        pay  = pay + pay * percent / 100;
    }

    public double closeOutWeek() {
        if (shiftsThisWeek == 0) {
            throw new IllegalStateException("Cannot close out week with zero shifts worked.");
        }

        double weeklyPay = calculateWeeklyPay(); // calculate the weekly pay before resetting shifts
        shiftsThisWeek = 0; // reset shifts for the new week
        return weeklyPay; // return the weekly pay for the week that just ended
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
        this.name = name.trim();
    }

    public void setPay(double pay) {
        if (pay < 0) {
            throw new IllegalArgumentException("Pay cannot be negative.");
        }
        this.pay = pay;
    }

    public void suspend() {
        this.suspended = true;
    }

    public void reinstate() {
        this.suspended = false;
    }

    public void changeToFullTime() {
        this.fullTime = true;
    }

    public void changeToPartTime() {
        if (shiftsThisWeek > PART_TIME_MAX_SHIFTS) {
            throw new IllegalStateException("Cannot change to part-time while exceeding part-time shift limit.");
        }

        this.fullTime = false;
    }

    // overrides
    @Override 
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Staff)) return false;
        Staff other = (Staff) obj;
        return id.equals(other.id);
    }

    @Override 
    public int hashCode() {
        return id.hashCode();
    }

    @Override 
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pay=" + pay +
                ", fullTime=" + fullTime +
                ", shiftsThisWeek=" + shiftsThisWeek +
                ", suspended=" + suspended +
                '}';
    }

}
