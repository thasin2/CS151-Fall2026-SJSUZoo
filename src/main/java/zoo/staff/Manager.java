package zoo.staff;
import java.util.ArrayList;
import zoo.Zoo;
import zoo.exception.ZooFullException;

// For a manager, pay is the yearly salary.
// They also earn a flat weekly bonus for each staff member on their team.
// A team holds only non-manager staff, so a manager can never manage themselves or another manager.
public class Manager extends Staff {
    private static final int WEEKS_PER_YEAR = 52;
    private static final double TEAM_BONUS = 25.0; // flat extra pay per team member each week
    private ArrayList<Staff> team = new ArrayList<>(); // staff this manager is responsible for

    public Manager(String id, String name, double pay, boolean fullTime) {
        super(id, name, pay, fullTime);
    }

    // calculate weekly pay for a manager, including the team bonus
    // does not mean pay is actually given, that is done in closeOutWeek
    @Override
    protected double calculateWeeklyPay() {
        double salaryPay = getPay() / WEEKS_PER_YEAR;
        double teamPay = TEAM_BONUS * team.size();
        return salaryPay + teamPay;
    }

    public void addTeamMember(Staff member) {
        if (member == null) {
            throw new IllegalArgumentException("Team member cannot be null.");
        }
        if (member instanceof Manager) {
            throw new IllegalArgumentException("A manager cannot be on a team, including their own.");
        }
        if (team.contains(member)) {
            throw new IllegalArgumentException(member.getName() + " is already on this team.");
        }
        // the manager counts too, so there are never more than MAXIMUM_INSTANCES staff
        if (team.size() + 1 >= Zoo.MAXIMUM_INSTANCES) {
            throw new ZooFullException("The team is full. The zoo can have at most " + Zoo.MAXIMUM_INSTANCES
                    + " staff, including the manager.");
        }
        team.add(member);
    }

    public void removeTeamMember(Staff member) {
        if (member == null) {
            throw new IllegalArgumentException("Team member cannot be null.");
        }
        if (!team.remove(member)) {
            throw new IllegalArgumentException(member.getName() + " is not on this team.");
        }
    }

    // finds a team member by staff ID, ignoring upper or lower case
    public Staff findTeamMember(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Staff ID cannot be null or empty.");
        }
        for (Staff member : team) {
            if (member.getId().equalsIgnoreCase(id.trim())) {
                return member;
            }
        }
        throw new IllegalArgumentException("No one with ID " + id.trim() + " is on this team.");
    }

    // returns a new list of the team members who do the given job
    public ArrayList<Staff> getTeamByRole(StaffRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        ArrayList<Staff> members = new ArrayList<>();
        for (Staff member : team) {
            if (member.getRole() == role) {
                members.add(member);
            }
        }
        return members;
    }

    // closes out the week for every team member who worked, then for the manager
    // each closeOutWeek call uses that staff member's own pay rule
    // staff with no shifts this week are not paid
    public ArrayList<PayStub> runPayroll() {
        ArrayList<PayStub> stubs = new ArrayList<>();
        for (Staff member : team) {
            if (member.getShiftsThisWeek() > 0) {
                stubs.add(payOut(member));
            }
        }
        if (getShiftsThisWeek() > 0) {
            stubs.add(payOut(this));
        }
        return stubs;
    }

    private PayStub payOut(Staff staff) {
        int shifts = staff.getShiftsThisWeek(); // read before closeOutWeek resets it
        double amount = staff.closeOutWeek();
        return new PayStub(staff.getId(), staff.getName(), staff.getRole(), shifts, amount);
    }

    @Override
    public String toString() {
        return super.toString() + " teamSize=" + team.size();
    }

    @Override
    public StaffRole getRole() {
        return StaffRole.MANAGER;
    }

    // getters
    public ArrayList<Staff> getTeam() {
        return new ArrayList<>(team); // copy, so callers cannot change the team without the checks above
    }

    public int getTeamSize() {
        return team.size();
    }

}
