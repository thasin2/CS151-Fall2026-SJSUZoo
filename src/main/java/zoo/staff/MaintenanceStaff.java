package zoo.staff;
import java.util.ArrayList;
import zoo.Maintainable;
import zoo.exception.JobLimitException;

// For maintenance staff, pay is the hourly rate.
// They also earn a flat bonus for each job (cleaning or repair) they complete.
public class MaintenanceStaff extends Staff {
    private static final double JOB_BONUS = 15.0; // flat extra pay for each job completed
    private static final int MAX_JOBS_PER_SHIFT = 4; // jobs allowed for each shift worked this week
    private int jobsThisWeek = 0; // number of jobs completed this week

    public MaintenanceStaff(String id, String name, double pay, boolean fullTime) {
        super(id, name, pay, fullTime);
    }

    // calculate weekly pay for maintenance staff, including overtime and job bonuses
    // does not mean pay is actually given, that is done in closeOutWeek
    @Override
    protected double calculateWeeklyPay() {
        // straight-time pay: every hour at the base rate, plus job bonuses
        double jobPay = JOB_BONUS * jobsThisWeek;
        double straightTimePay = getPay() * getHoursWorked() + jobPay;
        return addOvertimePay(straightTimePay);
    }

    @Override
    protected void resetWeeklyCounts() {
        jobsThisWeek = 0;
    }

    // cleans or repairs anything that is Maintainable, such as a habitat or an attraction
    // only counts as a job if the target actually needed maintenance
    // each shift worked this week allows MAX_JOBS_PER_SHIFT jobs
    public void service(Maintainable target) {
        if (target == null) {
            throw new IllegalArgumentException("Maintenance target cannot be null.");
        }
        if (!hasJobsLeft()) {
            throw new JobLimitException("Job limit reached. Each shift worked allows " + MAX_JOBS_PER_SHIFT + " jobs. Work another shift to do more jobs.");
        }
        if (!target.needsMaintenance()) {
            throw new IllegalStateException("Target does not need maintenance yet.");
        }
        target.performMaintenance();
        jobsThisWeek++;
    }

    // true while the shifts worked this week still allow another job
    public boolean hasJobsLeft() {
        return jobsThisWeek < getShiftsThisWeek() * MAX_JOBS_PER_SHIFT;
    }

    // returns a new list of only the targets that need maintenance, worst condition first
    // ties keep the order they had in the original list
    public ArrayList<Maintainable> getUrgentTargets(ArrayList<Maintainable> targets) {
        if (targets == null) {
            throw new IllegalArgumentException("Target list cannot be null.");
        }

        ArrayList<Maintainable> urgent = new ArrayList<>();
        for (Maintainable target : targets) {
            if (target == null) {
                throw new IllegalArgumentException("Target list cannot contain null.");
            }
            if (!target.needsMaintenance()) {
                continue;
            }

            // insert after every target with the same or worse condition, so the list stays sorted
            int index = 0;
            while (index < urgent.size() && urgent.get(index).getCondition() <= target.getCondition()) {
                index++;
            }
            urgent.add(index, target);
        }
        return urgent;
    }

    @Override
    public StaffRole getRole() {
        return StaffRole.MAINTENANCE;
    }

}
