package zoo.staff;


import zoo.animal.Animal;
import zoo.habitat.Habitat;
import zoo.inventory.Inventory;

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

    public boolean feedAnimal(Animal anim, Inventory storage){
        if (anim.isHungry()){
            if (storage.use("food")) {
            anim.setHungry(false);
            System.out.println(anim.getName() + " has been fed. It is not hungry anymore! Also the animal is active!");
            anim.setActive(true);
            return true;
            }
            System.out.println("Stock the inventory!");
            return false;
            }
        System.out.println("The animal is not hungry!");
        return true;
        }

    public boolean maintainHabitat(Habitat environ, Inventory storage){
        if (environ.needsMaintenance()){
            System.out.println("Oh no! Your habitat needs some maintenance! Let's call in the Zookeeper.");
            if (storage.use("supplies")){
                environ.setCondition(100);
                System.out.println(environ.getName() + " has been maintained! The zookeeper did a great job!");
                return true;
            }
            System.out.println("Stock the inventory!");
            return false;
        }
        System.out.println("The environment does not need maintenance.");
        return true;
    }

    @Override
    public StaffRole getRole() {
        return StaffRole.ZOOKEEPER;
    }

}
