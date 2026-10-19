package zoo.staff;

// A record of one staff member's pay for one week, made when the manager runs payroll.
public class PayStub {
    private final String staffId;
    private final String staffName;
    private final StaffRole role;
    private final int shiftsWorked;
    private final double amount;

    public PayStub(String staffId, String staffName, StaffRole role, int shiftsWorked, double amount) {
        if (staffId == null || staffName == null || role == null) {
            throw new IllegalArgumentException("Pay stub details cannot be null.");
        }
        if (shiftsWorked < 0 || amount < 0) {
            throw new IllegalArgumentException("Shifts and amount cannot be negative.");
        }
        this.staffId = staffId;
        this.staffName = staffName;
        this.role = role;
        this.shiftsWorked = shiftsWorked;
        this.amount = amount;
    }

    // getters
    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%-4s %-16s %-13s %d shifts  $%,.2f", staffId, staffName, role.getDisplayName(), shiftsWorked, amount);
    }
}
