package zoo.game.manager;

import java.util.ArrayList;
import java.util.Random;
import zoo.animal.Animal;
import zoo.exception.BudgetExceededException;
import zoo.staff.MaintenanceStaff;
import zoo.staff.Manager;
import zoo.staff.PayStub;
import zoo.staff.Staff;
import zoo.staff.StaffRole;
import zoo.staff.Veterinarian;
import zoo.staff.Zookeeper;

/* The manager game: run the zoo staff for the level's number of weeks without the budget going below $0
    each week, demand grows on a fixed schedule, random events may add more, and income grows a little
    odd weeks are bonus weeks (even weeks can be by chance): covering every role exactly earns the level's bonus
    (exactly means shifts worked = shifts needed: no gaps, no overtime, and no extra paid shifts)
    the player hires or fires staff, then runs the week:
    shifts are assigned by a ManagerShiftPlan for each role, payroll is paid,
    each unfilled shift is fined, the weekly income comes in, and the bonus is paid if it was earned
 */
public final class ManagerGame {
    private static final StaffRole[] ROLES_WITH_DEMAND = {StaffRole.ZOOKEEPER, StaffRole.VETERINARIAN, StaffRole.MAINTENANCE};
    private static final double EXTRA_BONUS_WEEK_CHANCE = 0.25; // chance that an even week is also a bonus week
    private static final double HIRING_COST = 500.0;
    private static final double UNFILLED_SHIFT_PENALTY = 2000.0;
    private static final int POINTS_PER_WEEK = 1000000; // times the level's score multiplier
    private static final int MAX_MONEY_POINTS = 250000; // money counts up to this much either way

    // market pay rates, set by the game so the player cannot hire staff for almost nothing
    private static final double ZOOKEEPER_HOURLY_RATE = 20.0;
    private static final double MAINTENANCE_HOURLY_RATE = 18.0;
    private static final double VET_FULL_TIME_SALARY = 100000.0;
    private static final double VET_PART_TIME_SALARY = 80000.0; // same rate per shift as full-time: 4 shifts for 4/5 of the salary
    private static final double MANAGER_SALARY = 78000.0;

    // event sizes
    private static final int DANGEROUS_WORK_SHIFTS = 3;
    private static final int MAX_EMERGENCIES = 2;

    private final ManagerLevel level;
    private final Random random;
    private final Manager manager;
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final ManagerRoleDemand zookeeperDemand = new ManagerRoleDemand(15, 1); // +1 every week
    private final ManagerRoleDemand vetDemand = new ManagerRoleDemand(5, 4); // +1 every 4 weeks
    private final ManagerRoleDemand maintenanceDemand = new ManagerRoleDemand(10, 2); // +1 every 2 weeks
    private final ArrayList<String> eventMessages = new ArrayList<>();
    private double budget;
    private int week = 1;
    private int emergencies; // sick animals this week, each needs a vet
    private Animal emergencyAnimal;
    private int hazardShifts; // shifts with a dangerous animal this week, only zookeepers can do them
    private Animal dangerousAnimal;
    private boolean bonusWeek; // true if covering every shift this week earns the bonus
    private int bonusesEarned = 0;
    private int weeksSurvived = 0;
    private boolean over = false;
    private boolean won = false;
    private int nextZookeeperNumber = 1;
    private int nextVetNumber = 1;
    private int nextMaintenanceNumber = 1;

    public ManagerGame(ManagerLevel level, String managerName, Random random) {
        if (level == null || random == null) {
            throw new IllegalArgumentException("Level and random cannot be null.");
        }
        this.level = level;
        this.random = random;
        this.budget = level.getStartingBudget();
        this.manager = new Manager("MGR", managerName, MANAGER_SALARY, true); // checks the name

        animals.add(new Animal("Lion", false));
        animals.add(new Animal("Elephant", true));
        animals.add(new Animal("Red Panda", false));
        animals.add(new Animal("Penguin", true));
        animals.add(new Animal("Giraffe", true));

        // starting team: covers the starting demand exactly, with no overtime
        for (int i = 0; i < 3; i++) {
            manager.addTeamMember(createStaff(StaffRole.ZOOKEEPER, true));
        }
        manager.addTeamMember(createStaff(StaffRole.VETERINARIAN, true));
        for (int i = 0; i < 2; i++) {
            manager.addTeamMember(createStaff(StaffRole.MAINTENANCE, true));
        }

        startWeek();
    }

    // hires paid staff for the given role, which costs HIRING_COST right away
    public Staff hire(StaffRole role, boolean fullTime) {
        checkNotOver();
        if (role == null || role == StaffRole.MANAGER) {
            throw new IllegalArgumentException("You can only hire zookeepers, veterinarians, or maintenance staff.");
        }
        checkCanAffordHire();
        Staff newStaff = createStaff(role, fullTime);
        manager.addTeamMember(newStaff);
        budget -= HIRING_COST;
        return newStaff;
    }

    // removes a staff member from the team right away, at no cost
    public Staff fire(String staffId) {
        checkNotOver();
        Staff member = manager.findTeamMember(staffId); // throws if the ID is not on the team
        manager.removeTeamMember(member);
        return member;
    }

    // runs the current week, then starts the next one unless the game is over
    public ManagerWeekReport runWeek() {
        checkNotOver();

        while (manager.getShiftsThisWeek() < manager.getMinimumShifts()) {
            manager.workShift();
        }

        ManagerShiftPlan zookeeperPlan = planShifts(StaffRole.ZOOKEEPER);
        ManagerShiftPlan vetPlan = planShifts(StaffRole.VETERINARIAN);
        ManagerShiftPlan maintenancePlan = planShifts(StaffRole.MAINTENANCE);
        zookeeperPlan.carryOut(dangerousAnimal, emergencyAnimal);
        vetPlan.carryOut(dangerousAnimal, emergencyAnimal);
        maintenancePlan.carryOut(dangerousAnimal, emergencyAnimal);
        boolean exactlyCovered = zookeeperPlan.isExactlyCovered() && vetPlan.isExactlyCovered()
                && maintenancePlan.isExactlyCovered();
        int unfilledZookeeper = zookeeperPlan.getUnfilledShifts();
        int unfilledVet = vetPlan.getUnfilledShifts();
        int unfilledMaintenance = maintenancePlan.getUnfilledShifts();

        ArrayList<PayStub> payStubs = manager.runPayroll();
        double payroll = 0;
        for (PayStub stub : payStubs) {
            payroll += stub.getAmount();
        }
        int unfilled = unfilledZookeeper + unfilledVet + unfilledMaintenance;
        double penalty = UNFILLED_SHIFT_PENALTY * unfilled;
        double income = level.getIncomeForWeek(week);
        double bonus = 0;
        if (bonusWeek && exactlyCovered) {
            bonus = level.getBonusAmount();
            bonusesEarned++;
        }
        budget = budget - payroll - penalty + income + bonus;

        ManagerWeekReport report = new ManagerWeekReport(week, payStubs, unfilledZookeeper, unfilledVet, unfilledMaintenance,
                penalty, income, bonusWeek, bonus, budget);
        endWeek();
        return report;
    }

    // the game ends if the budget went below $0 or the last week is done, otherwise the next week starts
    private void endWeek() {
        if (budget < 0) {
            over = true;
            return;
        }
        weeksSurvived = week;
        if (week == level.getWeeksToWin()) {
            over = true;
            won = true;
            return;
        }
        week++;
        startWeek();
    }

    // works out the shifts for one role with the current team, demand, and events
    // the menu uses it as a forecast, and runWeek uses it to run the week, so both always agree
    public ManagerShiftPlan planShifts(StaffRole role) {
        int roleHazardShifts = role == StaffRole.ZOOKEEPER ? hazardShifts : 0;
        int roleEmergencies = role == StaffRole.VETERINARIAN ? emergencies : 0;
        return new ManagerShiftPlan(manager.getTeamByRole(role), getDemand(role).getTotal(), roleHazardShifts,
                roleEmergencies);
    }

    // true if every role would be exactly covered if the week ran now, which earns the bonus in a bonus week
    public boolean isForecastExactlyCovered() {
        for (StaffRole role : ROLES_WITH_DEMAND) {
            if (!planShifts(role).isExactlyCovered()) {
                return false;
            }
        }
        return true;
    }

    // how many shifts the current team can cover for a role
    // withOvertime counts the 6th shift of full-time staff, otherwise only regular shifts count
    public int getShiftCapacity(StaffRole role, boolean withOvertime) {
        int capacity = 0;
        for (Staff member : manager.getTeamByRole(role)) {
            capacity += withOvertime ? member.getMaximumShifts() : member.getRegularShiftLimit();
        }
        return capacity;
    }

    // how many shifts of a role the team will work no matter what, because of shift minimums
    public int getMinimumShifts(StaffRole role) {
        int shifts = 0;
        for (Staff member : manager.getTeamByRole(role)) {
            shifts += member.getMinimumShifts();
        }
        return shifts;
    }

    public ManagerRoleDemand getDemand(StaffRole role) {
        switch (role) {
            case ZOOKEEPER:
                return zookeeperDemand;
            case VETERINARIAN:
                return vetDemand;
            case MAINTENANCE:
                return maintenanceDemand;
            default:
                throw new IllegalArgumentException("Only zookeepers, veterinarians, and maintenance staff have demand.");
        }
    }

    // each week survived is worth POINTS_PER_WEEK times the level's multiplier, so harder levels score more
    // the final budget is added too, win or lose, but capped at MAX_MONEY_POINTS either way
    // the money part can differ by at most 2 * MAX_MONEY_POINTS, which is less than one week,
    // so surviving one more week always beats any amount of money
    public int getScore() {
        return getWeekPoints() + getMoneyPoints();
    }

    public int getWeekPoints() {
        return weeksSurvived * POINTS_PER_WEEK * level.getScoreMultiplier();
    }

    public int getMoneyPoints() {
        return (int) Math.max(-MAX_MONEY_POINTS, Math.min(MAX_MONEY_POINTS, budget));
    }

    public int getPointsPerWeek() {
        return POINTS_PER_WEEK * level.getScoreMultiplier();
    }

    // the yearly or hourly pay a new hire would get for a role
    public double getMarketRate(StaffRole role, boolean fullTime) {
        switch (role) {
            case ZOOKEEPER:
                return ZOOKEEPER_HOURLY_RATE;
            case VETERINARIAN:
                return fullTime ? VET_FULL_TIME_SALARY : VET_PART_TIME_SALARY;
            case MAINTENANCE:
                return MAINTENANCE_HOURLY_RATE;
            default:
                throw new IllegalArgumentException("You can only hire zookeepers, veterinarians, or maintenance staff.");
        }
    }

    // sets this week's demand, events, and bonus week
    private void startWeek() {
        zookeeperDemand.startWeek(week);
        vetDemand.startWeek(week);
        maintenanceDemand.startWeek(week);
        rollEvents();
        bonusWeek = week % 2 == 1 || random.nextDouble() < EXTRA_BONUS_WEEK_CHANCE; // odd weeks are guaranteed
    }

    // each try has the level's event chance, and the same event cannot happen twice in one week
    private void rollEvents() {
        eventMessages.clear();
        emergencies = 0;
        hazardShifts = 0;

        ArrayList<ManagerEvent> possible = new ArrayList<>();
        for (ManagerEvent event : ManagerEvent.values()) {
            possible.add(event);
        }
        for (int i = 0; i < level.getMaxEventsPerWeek() && !possible.isEmpty(); i++) {
            if (random.nextDouble() < level.getEventChance()) {
                applyEvent(possible.remove(random.nextInt(possible.size())));
            }
        }
    }

    private void applyEvent(ManagerEvent event) {
        switch (event) {
            case ANIMAL_EMERGENCY:
                emergencies = 1 + random.nextInt(MAX_EMERGENCIES);
                emergencyAnimal = animals.get(random.nextInt(animals.size()));
                vetDemand.addExtra(emergencies);
                if (emergencies == 1) {
                    eventMessages.add("The " + emergencyAnimal.getName()
                            + " is sick: +1 vet shift. Treating it earns a vet the emergency bonus.");
                } else {
                    eventMessages.add(emergencies + " animals are sick, including the " + emergencyAnimal.getName()
                            + ": +" + emergencies + " vet shifts. Each one treated earns a vet the emergency bonus.");
                }
                break;
            case DANGEROUS_ANIMAL_WORK:
                dangerousAnimal = pickDangerousAnimal();
                hazardShifts = DANGEROUS_WORK_SHIFTS;
                zookeeperDemand.addExtra(DANGEROUS_WORK_SHIFTS);
                eventMessages.add("The " + dangerousAnimal.getName() + " enclosure needs close care: +"
                        + DANGEROUS_WORK_SHIFTS + " zookeeper shifts, with hazard pay.");
                break;
        }
    }

    private Animal pickDangerousAnimal() {
        ArrayList<Animal> dangerous = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.isDangerous()) {
                dangerous.add(animal);
            }
        }
        return dangerous.get(random.nextInt(dangerous.size()));
    }

    private Staff createStaff(StaffRole role, boolean fullTime) {
        double pay = getMarketRate(role, fullTime);
        switch (role) {
            case ZOOKEEPER:
                int z = nextZookeeperNumber++;
                return new Zookeeper("Z" + z, "Zookeeper " + z, pay, fullTime);
            case VETERINARIAN:
                int v = nextVetNumber++;
                return new Veterinarian("V" + v, "Vet " + v, pay, fullTime);
            default:
                int m = nextMaintenanceNumber++;
                return new MaintenanceStaff("M" + m, "Maintenance " + m, pay, fullTime);
        }
    }

    private void checkCanAffordHire() {
        if (budget < HIRING_COST) {
            throw new BudgetExceededException(String.format("Hiring costs $%,.2f, but the budget has only $%,.2f.", HIRING_COST, budget));
        }
    }

    private void checkNotOver() {
        if (over) {
            throw new IllegalStateException("The game is over.");
        }
    }

    // getters
    public StaffRole[] getRolesWithDemand() {
        return ROLES_WITH_DEMAND.clone(); // a copy, so callers cannot change the game's list
    }

    public ManagerLevel getLevel() {
        return level;
    }

    public Manager getManager() {
        return manager;
    }

    public double getBudget() {
        return budget;
    }

    public int getWeek() {
        return week;
    }

    public int getWeeksToWin() {
        return level.getWeeksToWin();
    }

    public double getIncomeThisWeek() {
        return level.getIncomeForWeek(week);
    }

    public boolean isBonusWeek() {
        return bonusWeek;
    }

    public int getBonusesEarned() {
        return bonusesEarned;
    }

    public double getHiringCost() {
        return HIRING_COST;
    }

    public double getUnfilledShiftPenalty() {
        return UNFILLED_SHIFT_PENALTY;
    }

    public ArrayList<String> getEventMessages() {
        return new ArrayList<>(eventMessages);
    }

    public int getWeeksSurvived() {
        return weeksSurvived;
    }

    public boolean isOver() {
        return over;
    }

    public boolean hasWon() {
        return won;
    }
}
