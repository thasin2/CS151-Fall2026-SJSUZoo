package zoo.ui;

import java.util.ArrayList;
import java.util.Random;
import zoo.game.manager.ManagerLevel;
import zoo.game.manager.ManagerGame;
import zoo.game.manager.ManagerShiftPlan;
import zoo.game.manager.ManagerWeekReport;
import zoo.staff.PayStub;
import zoo.staff.Staff;
import zoo.staff.StaffRole;

// Text menus for playing the manager game
public class ManagerGameMenu {
    private static final String LINE = "----------------------------------------------------------------------------------------------------";

    public void run() {
        printRules();
        for (ManagerLevel level : ManagerLevel.values()) {
            System.out.printf("  Level %d: %d weeks, start $%,.0f, income $%,.0f a week (+$%,.0f each week), bonus $%,.0f,%n"
                    + "           events %s, score multiplier x%d%n",
                    level.getNumber(), level.getWeeksToWin(), level.getStartingBudget(), level.getStartingIncome(),
                    level.getIncomeGrowth(), level.getBonusAmount(), describeEvents(level), level.getScoreMultiplier());
        }
        int levelNumber = ConsoleInput.readInt("Pick a level (1-" + ManagerLevel.values().length + "): ", 1, ManagerLevel.values().length);
        String name = ConsoleInput.readNonEmptyLine("Your name: ");
        play(new ManagerGame(ManagerLevel.fromNumber(levelNumber), name, new Random()));
    }

    // shown once, when the manager game starts
    private void printRules() {
        System.out.println();
        System.out.println("=== Manager ===");
        System.out.println("Run the zoo staff until the last week of the level without the budget going below $0.");
        System.out.println("Each week, every role needs a number of shifts. You hire or fire staff, then run the week.");
        System.out.println("  - The zoo gets busier: zookeepers need 1 more shift every week, maintenance every 2 weeks, vets every 4.");
        System.out.println("  - Income also grows every week, but more slowly than costs.");
        System.out.println("  - Odd weeks (1, 3, 5, ...) are always bonus weeks, and even weeks can be too.");
        System.out.println("    To earn the bonus, every role must be exactly covered: shifts worked = shifts needed,");
        System.out.println("    with no gaps, no overtime, and no extra paid shifts.");
        System.out.println("  - Full-time staff always work 5 shifts. When needed, they work a 6th (overtime) shift at 1.5x pay.");
        System.out.println("  - Part-time staff work 3 to 4 shifts.");
        System.out.println("  - Everyone works their minimum shifts and gets paid for them, even if they are not needed.");
        System.out.println("  - Every shift that no one can cover is fined.");
        System.out.println("  - Score: every week you survive is worth 1,000,000 points times the level's multiplier.");
        System.out.println("    Your final budget is added too, win or lose, so one more week always beats saving money.");
        System.out.println();
        System.out.println("How to read the weekly table:");
        System.out.println("  Needed:   shifts this role must cover this week, including events.");
        System.out.println("  Paid min: shifts your team works and gets paid for, even when they are not needed.");
        System.out.println("  Max:      most shifts your team can cover without overtime.");
        System.out.println("  Max+OT:   most shifts your team can cover in total, with overtime.");
        System.out.println("  Forecast: what happens if you run the week now with this team.");
        System.out.println();
    }

    private void play(ManagerGame game) {
        while (!game.isOver()) {
            showWeek(game);
            System.out.println("1. Hire staff");
            System.out.println("2. Fire staff");
            System.out.println("3. View team");
            System.out.println("4. Run the week");
            int choice = ConsoleInput.readInt("Choose an option: ", 1, 4);
            try {
                switch (choice) {
                    case 1:
                        hireStaff(game);
                        break;
                    case 2:
                        fireStaff(game);
                        break;
                    case 3:
                        showTeam(game);
                        break;
                    default:
                        showReport(game, game.runWeek());
                        break;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        showResult(game);
    }

    private void showWeek(ManagerGame game) {
        System.out.println();
        System.out.println(LINE);
        System.out.printf("Level %d - Week %d of %d - Manager %s%n", game.getLevel().getNumber(), game.getWeek(),
                game.getWeeksToWin(), game.getManager().getName());
        System.out.printf("Budget: %s   Income this week: $%,.2f   Bonuses earned: %d%n", formatMoney(game.getBudget()),
                game.getIncomeThisWeek(), game.getBonusesEarned());
        System.out.printf("Fine per unfilled shift: $%,.2f   Hiring cost: $%,.2f%n", game.getUnfilledShiftPenalty(),
                game.getHiringCost());
        System.out.println();
        System.out.printf("%-13s %7s %10s %5s %7s   %s%n", "Role", "Needed", "Paid min", "Max", "Max+OT", "Forecast");
        for (StaffRole role : game.getRolesWithDemand()) {
            System.out.printf("%-13s %7d %10d %5d %7d   %s%n", role.getDisplayName(), game.getDemand(role).getTotal(),
                    game.getMinimumShifts(role), game.getShiftCapacity(role, false), game.getShiftCapacity(role, true),
                    describeForecast(game.planShifts(role), game.getUnfilledShiftPenalty()));
        }

        System.out.println();
        if (game.isBonusWeek()) {
            System.out.printf("BONUS WEEK: cover every role exactly to earn $%,.0f. %s%n", game.getLevel().getBonusAmount(),
                    game.isForecastExactlyCovered() ? "Your team is on track."
                            : "Not on track: every role must show \"Exactly covered\".");
        } else {
            System.out.println("Not a bonus week.");
        }

        ArrayList<String> events = game.getEventMessages();
        System.out.println(events.isEmpty() ? "No events this week." : "Events this week:");
        for (String event : events) {
            System.out.println("  - " + event);
        }
        System.out.println(LINE);
    }

    private void hireStaff(ManagerGame game) {
        StaffRole[] roles = game.getRolesWithDemand();
        for (int i = 0; i < roles.length; i++) {
            System.out.println((i + 1) + ". " + roles[i].getDisplayName());
        }
        StaffRole role = roles[ConsoleInput.readInt("Role to hire: ", 1, roles.length) - 1];
        boolean fullTime = ConsoleInput.readYesNo("Full-time? (y/n): ");
        System.out.printf("A %s %s earns %s. Hiring costs $%,.2f now.%n", fullTime ? "full-time" : "part-time",
                role.getDisplayName().toLowerCase(), formatPay(role, game.getMarketRate(role, fullTime)), game.getHiringCost());
        if (ConsoleInput.readYesNo("Hire? (y/n): ")) {
            Staff hired = game.hire(role, fullTime);
            System.out.println("Hired " + hired.getName() + " (" + hired.getId() + ").");
        }
    }

    private void fireStaff(ManagerGame game) {
        showTeam(game);
        String id = ConsoleInput.readNonEmptyLine("ID of the staff member to fire: ");
        Staff fired = game.fire(id);
        System.out.println("Fired " + fired.getName() + " (" + fired.getId() + ").");
    }

    private void showTeam(ManagerGame game) {
        System.out.println();
        System.out.printf("%-5s %-16s %-13s %-10s %s%n", "ID", "Name", "Role", "Type", "Pay");
        for (Staff member : game.getManager().getTeam()) {
            String type = member.isFullTime() ? "full-time" : "part-time";
            System.out.printf("%-5s %-16s %-13s %-10s %s%n", member.getId(), member.getName(),
                    member.getRole().getDisplayName(), type, formatPay(member.getRole(), member.getPay()));
        }
        System.out.println("Team size: " + game.getManager().getTeamSize());
    }

    private void showReport(ManagerGame game, ManagerWeekReport report) {
        System.out.println();
        System.out.println("=== Week " + report.getWeek() + " results ===");
        for (PayStub stub : report.getPayStubs()) {
            System.out.println("  " + stub);
        }
        System.out.printf("Payroll: $%,.2f%n", report.getPayroll());
        for (StaffRole role : game.getRolesWithDemand()) {
            int unfilled = report.getUnfilledShifts(role);
            if (unfilled > 0) {
                System.out.println("Unfilled " + role.getDisplayName().toLowerCase() + " shifts: " + unfilled);
            }
        }
        System.out.printf("Fines: $%,.2f%n", report.getPenalty());
        System.out.printf("Income: $%,.2f%n", report.getIncome());
        if (report.getBonus() > 0) {
            System.out.printf("Bonus: $%,.2f for covering every role exactly%n", report.getBonus());
        } else if (report.wasBonusWeek()) {
            System.out.println("Bonus missed: not every role was exactly covered.");
        }
        System.out.println("Budget now: " + formatMoney(report.getBudgetAfter()));
    }

    private void showResult(ManagerGame game) {
        System.out.println();
        System.out.println(LINE);
        if (game.hasWon()) {
            System.out.printf("You won level %d! You kept the zoo running for all %d weeks with %s left.%n",
                    game.getLevel().getNumber(), game.getWeeksToWin(), formatMoney(game.getBudget()));
            System.out.println("Bonuses earned: " + game.getBonusesEarned());
        } else {
            System.out.printf("The budget went below $0 in week %d. Game over.%n", game.getWeek());
        }
        System.out.printf("Weeks survived: %d x %,d points = %,d%n", game.getWeeksSurvived(), game.getPointsPerWeek(),
                game.getWeekPoints());
        System.out.printf("Final budget:   %,d%n", game.getMoneyPoints());
        System.out.printf("Score:          %,d%n", game.getScore());
        System.out.println(LINE);
    }

    // for example "Covered, using 2 overtime shifts" or "3 short (-$3,000 fine)"
    private String describeForecast(ManagerShiftPlan plan, double finePerShift) {
        int unfilled = plan.getUnfilledShifts();
        int overtime = plan.getOvertimeShifts();
        int unneeded = plan.getUnneededShifts();
        String forecast;
        if (unfilled > 0) {
            forecast = String.format("%d short (-$%,.0f fine)", unfilled, unfilled * finePerShift);
            if (overtime > 0) {
                forecast += ", even after " + countShifts(overtime, "overtime shift");
            }
        } else if (plan.isExactlyCovered()) {
            forecast = "Exactly covered";
        } else {
            forecast = "Covered";
            if (overtime > 0) {
                forecast += ", using " + countShifts(overtime, "overtime shift");
            }
        }
        if (unneeded > 0) {
            forecast += ", " + countShifts(unneeded, "paid shift") + " not needed";
        }
        return forecast;
    }

    // for example "1 overtime shift" or "2 overtime shifts"
    private String countShifts(int count, String label) {
        return count + " " + label + (count == 1 ? "" : "s");
    }

    private String describeEvents(ManagerLevel level) {
        return String.format("%.0f%% chance, up to %d a week", level.getEventChance() * 100, level.getMaxEventsPerWeek());
    }

    // puts the minus sign before the dollar sign, for example -$150.00
    private String formatMoney(double amount) {
        String formatted = String.format("$%,.2f", Math.abs(amount));
        return amount < 0 ? "-" + formatted : formatted;
    }

    // vets and managers are paid a yearly salary, everyone else is paid by the hour
    private String formatPay(StaffRole role, double pay) {
        if (role == StaffRole.VETERINARIAN || role == StaffRole.MANAGER) {
            return String.format("$%,.0f/year", pay);
        }
        return String.format("$%,.2f/hour", pay);
    }
}
