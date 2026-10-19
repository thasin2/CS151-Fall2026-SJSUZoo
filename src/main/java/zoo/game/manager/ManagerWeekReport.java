package zoo.game.manager;

import java.util.ArrayList;
import zoo.staff.PayStub;
import zoo.staff.StaffRole;

// The results of one week of the manager game.
public class ManagerWeekReport {
    private final int week;
    private final ArrayList<PayStub> payStubs;
    private final int unfilledZookeeperShifts;
    private final int unfilledVetShifts;
    private final int unfilledMaintenanceShifts;
    private final double penalty;
    private final double income;
    private final boolean bonusWeek;
    private final double bonus; // 0 if the bonus was not earned
    private final double budgetAfter;

    public ManagerWeekReport(int week, ArrayList<PayStub> payStubs, int unfilledZookeeperShifts, int unfilledVetShifts,
            int unfilledMaintenanceShifts, double penalty, double income, boolean bonusWeek, double bonus, double budgetAfter) {
        if (payStubs == null) {
            throw new IllegalArgumentException("Pay stubs cannot be null.");
        }
        this.week = week;
        this.payStubs = new ArrayList<>(payStubs);
        this.unfilledZookeeperShifts = unfilledZookeeperShifts;
        this.unfilledVetShifts = unfilledVetShifts;
        this.unfilledMaintenanceShifts = unfilledMaintenanceShifts;
        this.penalty = penalty;
        this.income = income;
        this.bonusWeek = bonusWeek;
        this.bonus = bonus;
        this.budgetAfter = budgetAfter;
    }

    // total of every pay stub this week
    public double getPayroll() {
        double total = 0;
        for (PayStub stub : payStubs) {
            total += stub.getAmount();
        }
        return total;
    }

    public int getUnfilledShifts(StaffRole role) {
        switch (role) {
            case ZOOKEEPER:
                return unfilledZookeeperShifts;
            case VETERINARIAN:
                return unfilledVetShifts;
            case MAINTENANCE:
                return unfilledMaintenanceShifts;
            default:
                return 0;
        }
    }

    // getters
    public int getWeek() {
        return week;
    }

    public ArrayList<PayStub> getPayStubs() {
        return new ArrayList<>(payStubs);
    }

    public double getPenalty() {
        return penalty;
    }

    public double getIncome() {
        return income;
    }

    public boolean wasBonusWeek() {
        return bonusWeek;
    }

    public double getBonus() {
        return bonus;
    }

    public double getBudgetAfter() {
        return budgetAfter;
    }

    @Override
    public String toString() {
        return String.format("Week %d: payroll $%,.2f, penalty $%,.2f, income $%,.2f, bonus $%,.2f, budget $%,.2f",
                week, getPayroll(), penalty, income, bonus, budgetAfter);
    }
}
