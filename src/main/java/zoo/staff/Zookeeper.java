package zoo.staff;
import zoo.animal.Animal;

// For a zookeeper, pay is the hourly rate.
public class Zookeeper extends Staff {
    private static final double HAZARD_PAY_PER_SHIFT = 16.0; // $2 extra per hour of an 8-hour shift with a dangerous animal
    private int hazardShiftsThisWeek = 0; // shifts worked with a dangerous animal this week

    public Zookeeper(String id, String name, double pay, boolean fullTime) {
        super(id, name, pay, fullTime);
    }

    // calculate weekly pay for a zookeeper, including overtime and hazard pay
    // does not mean pay is actually given, that is done in closeOutWeek
    @Override
    protected double calculateWeeklyPay() {
        // straight-time pay: every hour at the base rate, plus hazard pay
        double hazardPay = HAZARD_PAY_PER_SHIFT * hazardShiftsThisWeek;
        double straightTimePay = getPay() * getHoursWorked() + hazardPay;
        return addOvertimePay(straightTimePay);
    }

    @Override
    protected void resetWeeklyCounts() {
        hazardShiftsThisWeek = 0;
    }

    public void workShiftWithAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null.");
        }
        workShift(); // throws if at the shift limit, so a failed shift is never counted
        if (animal.isDangerous()) {
            hazardShiftsThisWeek++;
        }
    }

    @Override
    public StaffRole getRole() {
        return StaffRole.ZOOKEEPER;
    }

}
