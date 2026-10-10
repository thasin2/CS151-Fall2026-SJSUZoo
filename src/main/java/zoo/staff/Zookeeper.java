package zoo.staff;
import zoo.animal.Animal;

// For a zookeeper, pay is the hourly rate.
public class Zookeeper extends Staff {
    private static final int REGULAR_HOURS_PER_WEEK = 40; // hours after this are overtime
    private static final double OVERTIME_MULTIPLIER = 1.5;
    private static final double HAZARD_PAY_PER_HOUR = 2.0; // extra pay per hour on a shift with a dangerous animal
    private int hazardShiftsThisWeek = 0; // shifts worked with a dangerous animal this week

    public Zookeeper(String id, String name, double pay, boolean fullTime) {
        super(id, name, pay, fullTime);
    }

    // calculate weekly pay for a zookeeper, including overtime and hazard pay
    // does not mean pay is actually given, that is done in closeOutWeek
    @Override
    protected double calculateWeeklyPay() {
        int hoursWorked = getShiftsThisWeek() * HOURS_PER_SHIFT;
        int overtimeHours = Math.max(0, hoursWorked - REGULAR_HOURS_PER_WEEK);

        // straight-time pay: every hour at the base rate, plus hazard pay
        double hazardPay = HAZARD_PAY_PER_HOUR * hazardShiftsThisWeek * HOURS_PER_SHIFT;
        double straightTimePay = getPay() * hoursWorked + hazardPay;
        if (overtimeHours == 0) {
            return straightTimePay;
        }

        // the overtime rate is based on the average hourly pay, which includes hazard pay
        double regularRate = straightTimePay / hoursWorked;
        double overtimePremium = (OVERTIME_MULTIPLIER - 1) * regularRate * overtimeHours;
        return straightTimePay + overtimePremium;
    }

    @Override
    public double closeOutWeek() {
        double weeklyPay = super.closeOutWeek(); // pays out using this week's hazard shifts
        hazardShiftsThisWeek = 0; // reset for next week
        return weeklyPay;
    }

    public void workShiftWithAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null.");
        }
        workShift(); // throws if suspended or at the shift limit, so a failed shift is never counted
        if (animal.isDangerous()) {
            hazardShiftsThisWeek++;
        }
    }

    // getters
    public int getHazardShiftsThisWeek() {
        return hazardShiftsThisWeek;
    }

}
