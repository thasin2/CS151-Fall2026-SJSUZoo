package zoo.staff;
import zoo.animal.Animal;

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
    public double closeOutWeek() {
        double weeklyPay = super.closeOutWeek(); // pays out using this week's emergency count
        emergenciesThisWeek = 0; // reset for next week
        return weeklyPay;
    }

    public void handleEmergency(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null.");
        }
        if (isSuspended()) {
            throw new IllegalStateException("Cannot handle an emergency while suspended.");
        }
        emergenciesThisWeek++;
    }

    // getters
    public int getEmergenciesThisWeek() {
        return emergenciesThisWeek;
    }

}
