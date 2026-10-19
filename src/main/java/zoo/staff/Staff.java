package zoo.staff;
import zoo.exception.InvalidPayException;
import zoo.exception.ShiftLimitException;

/* Abstract class representing staff members in the zoo
    full time staff can work up to 6 shifts a week (the 6th shift is overtime), part time staff can work up to 4 shifts a week
    full time staff always work at least 5 shifts a week, part time staff at least 3
    shifts are 8 hours each, and pay must always be positive
    pay is calculated based on the number of shifts worked and the pay rate
    overrides equals and hashCode based on staff ID, which is unique for each staff member
 */
public abstract class Staff {
    private static final int HOURS_PER_SHIFT = 8;
    private static final int FULL_TIME_MAX_SHIFTS = 6; // the 6th shift is overtime
    private static final int FULL_TIME_REGULAR_SHIFTS = 5; // full-time minimum, and the most shifts before overtime
    private static final int PART_TIME_MAX_SHIFTS = 4;
    private static final int PART_TIME_MIN_SHIFTS = 3;
    private static final int REGULAR_HOURS_PER_WEEK = 40; // hours after this are overtime
    private static final double OVERTIME_MULTIPLIER = 1.5;

    private final String id;
    private final String name;
    private final double pay; // hourly rate or yearly salary, depending on the subclass
    private final boolean fullTime;
    private int shiftsThisWeek; // each shift is 8 hours


    protected Staff(String id, String name, double pay, boolean fullTime) {
        this.id = requireText(id, "Staff ID");
        this.name = requireText(name, "Staff name");
        if (pay <= 0) {
            throw new InvalidPayException("Pay must be positive.");
        }
        this.pay = pay;
        this.fullTime = fullTime;
        this.shiftsThisWeek = 0;
    }

    protected abstract double calculateWeeklyPay(); // subclasses can calculate weekly pay based on their own rules

    public abstract StaffRole getRole(); // each subclass reports which job it does

    // subclasses that count things during the week (such as hazard shifts) reset them here
    // called by closeOutWeek after the week's pay is worked out
    protected void resetWeeklyCounts() {
    }

    protected int getHoursWorked() {
        return shiftsThisWeek * HOURS_PER_SHIFT;
    }

    // for hourly staff: adds the overtime premium for hours past REGULAR_HOURS_PER_WEEK
    // straightTimePay is every hour at the base rate plus any bonuses
    // the overtime rate is based on the average hourly pay, so bonuses raise it
    protected double addOvertimePay(double straightTimePay) {
        int hoursWorked = getHoursWorked();
        int overtimeHours = Math.max(0, hoursWorked - REGULAR_HOURS_PER_WEEK);
        if (overtimeHours == 0) {
            return straightTimePay;
        }

        double regularRate = straightTimePay / hoursWorked;
        double overtimePremium = (OVERTIME_MULTIPLIER - 1) * regularRate * overtimeHours;
        return straightTimePay + overtimePremium;
    }

    public void workShift() {
        if (shiftsThisWeek >= getMaximumShifts()) {
            throw new ShiftLimitException((fullTime ? "Full-time" : "Part-time") + " staff cannot work more than "
                    + getMaximumShifts() + " shifts in a week.");
        }

        shiftsThisWeek++;
    }

    // works out the week's pay, then resets the shifts and any weekly counts for the next week
    public final double closeOutWeek() {
        if (shiftsThisWeek == 0) {
            throw new IllegalStateException("Cannot close out week with zero shifts worked.");
        }

        double weeklyPay = calculateWeeklyPay();
        shiftsThisWeek = 0;
        resetWeeklyCounts();
        return weeklyPay;
    }

    // fewest shifts this staff member must work in a week, even if they are not needed
    public int getMinimumShifts() {
        return fullTime ? FULL_TIME_REGULAR_SHIFTS : PART_TIME_MIN_SHIFTS;
    }

    // most shifts this staff member can work in a week without overtime
    public int getRegularShiftLimit() {
        return fullTime ? FULL_TIME_REGULAR_SHIFTS : PART_TIME_MAX_SHIFTS;
    }

    // most shifts this staff member can work in a week, including overtime
    public int getMaximumShifts() {
        return fullTime ? FULL_TIME_MAX_SHIFTS : PART_TIME_MAX_SHIFTS;
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

    // returns the text trimmed, or throws if it is null or blank
    private static String requireText(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " cannot be null or empty.");
        }
        return value.trim();
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
                '}';
    }

}
