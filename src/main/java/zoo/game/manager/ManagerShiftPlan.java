package zoo.game.manager;

import java.util.ArrayList;
import zoo.animal.Animal;
import zoo.staff.Staff;
import zoo.staff.Veterinarian;
import zoo.staff.Zookeeper;

/* The shifts each staff member of one role will work this week, worked out before anyone works
    the menu forecast and the real week both use this plan, so they always agree
    1. every staff member works their minimum shifts, even if demand does not need them
    2. more regular shifts are added until demand is met or no one has regular shifts left
    3. full-time staff work a 6th (overtime) shift to fill what is left
    dangerous-animal shifts go to the first zookeeper shifts in the plan
 */
public class ManagerShiftPlan {
    private final ArrayList<Staff> workers = new ArrayList<>();
    private final int[] plannedShifts;
    private final int[] plannedHazardShifts;
    private final int demand;
    private final int emergencies;
    private int regularCapacity = 0;
    private int filled = 0;
    private int hazardShiftsLeft;
    private boolean carriedOut = false;

    // team: everyone on the team with this role
    // hazardShifts: dangerous-animal shifts, which only zookeepers can do
    // emergencies: sick animals that each need a vet who works this week
    public ManagerShiftPlan(ArrayList<Staff> team, int demand, int hazardShifts, int emergencies) {
        if (team == null || demand < 0 || hazardShifts < 0 || emergencies < 0) {
            throw new IllegalArgumentException("Team cannot be null and counts cannot be negative.");
        }
        this.demand = demand;
        this.emergencies = emergencies;
        this.hazardShiftsLeft = hazardShifts;
        for (Staff member : team) {
            workers.add(member);
            regularCapacity += member.getRegularShiftLimit();
        }
        plannedShifts = new int[workers.size()];
        plannedHazardShifts = new int[workers.size()];

        for (int i = 0; i < workers.size(); i++) {
            while (plannedShifts[i] < workers.get(i).getMinimumShifts()) {
                addShift(i);
            }
        }
        for (int i = 0; i < workers.size(); i++) {
            while (filled < demand && plannedShifts[i] < workers.get(i).getRegularShiftLimit()) {
                addShift(i);
            }
        }
        for (int i = 0; i < workers.size(); i++) {
            while (filled < demand && plannedShifts[i] < workers.get(i).getMaximumShifts()) {
                addShift(i);
            }
        }
    }

    // shifts of demand no one will cover, including dangerous work when no zookeeper works
    // and emergencies when no vet works this week
    public int getUnfilledShifts() {
        int unfilled = Math.max(Math.max(0, demand - filled), hazardShiftsLeft);
        if (filled == 0) {
            unfilled += emergencies;
        }
        return unfilled;
    }

    // shifts past what the team can cover without overtime
    public int getOvertimeShifts() {
        return Math.max(0, filled - regularCapacity);
    }

    // minimum shifts that are paid for even though demand does not need them
    public int getUnneededShifts() {
        return Math.max(0, filled - demand);
    }

    // true if the team works exactly the shifts needed: no gaps, no overtime, and no extra paid shifts
    public boolean isExactlyCovered() {
        return getUnfilledShifts() == 0 && getOvertimeShifts() == 0 && getUnneededShifts() == 0;
    }

    // makes every worker work their planned shifts, then spreads the emergencies across the vets who worked
    // only the game calls this, and only once per plan
    void carryOut(Animal dangerousAnimal, Animal emergencyAnimal) {
        if (carriedOut) {
            throw new IllegalStateException("This plan was already carried out.");
        }
        carriedOut = true;
        ArrayList<Staff> workedThisWeek = new ArrayList<>();
        for (int i = 0; i < workers.size(); i++) {
            Staff worker = workers.get(i);
            for (int shift = 0; shift < plannedShifts[i]; shift++) {
                if (shift < plannedHazardShifts[i]) {
                    ((Zookeeper) worker).workShiftWithAnimal(dangerousAnimal);
                } else {
                    worker.workShift();
                }
            }
            if (plannedShifts[i] > 0) {
                workedThisWeek.add(worker);
            }
        }
        if (workedThisWeek.isEmpty()) {
            return; // unhandled emergencies are already counted in getUnfilledShifts
        }
        for (int i = 0; i < emergencies; i++) {
            Veterinarian vet = (Veterinarian) workedThisWeek.get(i % workedThisWeek.size());
            vet.handleEmergency(emergencyAnimal);
        }
    }

    private void addShift(int index) {
        Staff worker = workers.get(index);
        plannedShifts[index]++;
        filled++;
        if (hazardShiftsLeft > 0 && worker instanceof Zookeeper) {
            plannedHazardShifts[index]++;
            hazardShiftsLeft--;
        }
    }
}
