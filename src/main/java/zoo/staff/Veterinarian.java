package zoo.staff;
import zoo.animal.Animal;
import zoo.inventory.*;

// For a vet, pay is the yearly salary.
public class Veterinarian extends Staff {
    private static final int WEEKS_PER_YEAR = 52;
    private static final double EMERGENCY_BONUS = 200.0; // flat extra pay for each emergency handled
    private int emergenciesThisWeek = 0; // number of emergencies handled this week

    public Veterinarian(String id, String name, double pay, boolean fullTime) {
        super(id, name, pay, fullTime);
    }

    // calculate weekly pay for a veterinarian, including emergency bonuses
    // does not mean pay is actually given, that is done in closeOutWeek
    @Override
    protected double calculateWeeklyPay() {
        double salaryPay = getPay() / WEEKS_PER_YEAR;
        double emergencyPay = EMERGENCY_BONUS * emergenciesThisWeek;
        return salaryPay + emergencyPay;
    }

    @Override
    protected void resetWeeklyCounts() {
        emergenciesThisWeek = 0;
    }

    public void handleEmergency(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null.");
        }
        emergenciesThisWeek++;
    }

    @Override
    public StaffRole getRole() {
        return StaffRole.VETERINARIAN;
    }

    public boolean fixAnimal(Animal x, Inventory y){
        System.out.println("The vet is here!");
        if (x.isInjured()){
            System.out.println("The vet found the injury on " + x.getName() + "! The vet is making a prompt diagnosis and treatment plan.");
            if (y.use(Item.MEDICINE)) {
                x.setInjured(false);
                x.setActive(true);
                System.out.println("The animal is healed! It is back out and active!");
                return true;
            }
            else {
                System.out.println("The vet left, we have to stock the inventory! (Medicine not available)");
                return false;
            }
        }
        System.out.println("The vet could not find a injury.");
        return true;
    }

}
