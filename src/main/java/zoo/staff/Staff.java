package zoo.staff;
import java.lang.IllegalArgumentException;
import zoo.exception.ShiftLimitException;
import zoo.exception.StaffSuspendedException;

/* Abstract class representing staff members in the zoo
    full time staff can work up to 6 shifts a week (the 6th shift is overtime), part time staff can work up to 4 shifts a week
    shifts are 8 hours each
    raises can be given to staff, but not volunteers (who have a are staff with pay of 0)
    pay is calculated based on the number of shifts worked and the pay rate
    overrides equals and hashCode based on staff ID, which is unique for each staff member
 */
public abstract class Staff {
    public static final int HOURS_PER_SHIFT = 8; // constant for hours in a shift
    public static final int FULL_TIME_MAX_SHIFTS = 6; // constant for maximum shifts in a week
    public static final int PART_TIME_MAX_SHIFTS = 4; // constant for maximum shifts in a week
    private static final int REGULAR_HOURS_PER_WEEK = 40; // hours after this are overtime
    private static final double OVERTIME_MULTIPLIER = 1.5;

    private final String id;
    private String name;
    private double pay; // pay rate per hour, can be 0 for volunteers
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

    // for hourly staff: adds the overtime premium for hours past REGULAR_HOURS_PER_WEEK
    // straightTimePay is every hour at the base rate plus any bonuses
    // the overtime rate is based on the average hourly pay, so bonuses raise it
    protected double addOvertimePay(double straightTimePay) {
        int hoursWorked = shiftsThisWeek * HOURS_PER_SHIFT;
        int overtimeHours = Math.max(0, hoursWorked - REGULAR_HOURS_PER_WEEK);
        if (overtimeHours == 0) {
            return straightTimePay;
        }

        double regularRate = straightTimePay / hoursWorked;
        double overtimePremium = (OVERTIME_MULTIPLIER - 1) * regularRate * overtimeHours;
        return straightTimePay + overtimePremium;
    }

    public void workShift() {
        if (suspended) {
            throw new StaffSuspendedException("Cannot work a shift while suspended.");
        }
        if (fullTime && shiftsThisWeek >= FULL_TIME_MAX_SHIFTS) {
            throw new ShiftLimitException("Full-time staff cannot work more than " + FULL_TIME_MAX_SHIFTS + " shifts in a week.");
        }
        if (!fullTime && shiftsThisWeek >= PART_TIME_MAX_SHIFTS) {
            throw new ShiftLimitException("Part-time staff cannot work more than " + PART_TIME_MAX_SHIFTS + " shifts in a week.");
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
            throw new ShiftLimitException("Cannot change to part-time while exceeding part-time shift limit.");
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
