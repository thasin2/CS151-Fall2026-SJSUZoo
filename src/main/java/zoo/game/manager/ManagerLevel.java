package zoo.game.manager;

// The three levels of the manager game.
// Demand grows every week on every level. Income also grows, but more slowly, and the gap is wider on higher levels.
// Odd weeks are always bonus weeks; even weeks can also be bonus weeks by chance.
// Higher levels last longer, start with less money, have more random events, and depend more on bonuses.
public enum ManagerLevel {
    LEVEL_1(1, 8, 4000.0, 8500.0, 300.0, 2000.0, 0.30, 1, 1),
    LEVEL_2(2, 12, 3500.0, 8400.0, 200.0, 5000.0, 0.50, 2, 3),
    LEVEL_3(3, 20, 3000.0, 6300.0, 150.0, 10000.0, 0.85, 2, 25);

    private final int number;
    private final int weeksToWin;
    private final double startingBudget;
    private final double startingIncome; // income in week 1
    private final double incomeGrowth; // added to the weekly income every week
    private final double bonusAmount; // paid in a bonus week if every shift is covered
    private final double eventChance; // chance of each possible event happening in a week, from 0 to 1
    private final int maxEventsPerWeek;
    private final int scoreMultiplier; // each week survived is worth this many times more on harder levels

    ManagerLevel(int number, int weeksToWin, double startingBudget, double startingIncome, double incomeGrowth,
            double bonusAmount, double eventChance, int maxEventsPerWeek, int scoreMultiplier) {
        this.number = number;
        this.weeksToWin = weeksToWin;
        this.startingBudget = startingBudget;
        this.startingIncome = startingIncome;
        this.incomeGrowth = incomeGrowth;
        this.bonusAmount = bonusAmount;
        this.eventChance = eventChance;
        this.maxEventsPerWeek = maxEventsPerWeek;
        this.scoreMultiplier = scoreMultiplier;
    }

    // finds the level with the given number, from 1 to 3
    public static ManagerLevel fromNumber(int number) {
        for (ManagerLevel level : values()) {
            if (level.number == number) {
                return level;
            }
        }
        throw new IllegalArgumentException("Level must be from 1 to " + values().length + ".");
    }

    // the income for a given week, which grows by incomeGrowth every week
    public double getIncomeForWeek(int week) {
        if (week < 1 || week > weeksToWin) {
            throw new IllegalArgumentException("Week must be from 1 to " + weeksToWin + ".");
        }
        return startingIncome + incomeGrowth * (week - 1);
    }

    // getters
    public int getNumber() {
        return number;
    }

    public int getWeeksToWin() {
        return weeksToWin;
    }

    public double getStartingBudget() {
        return startingBudget;
    }

    public double getStartingIncome() {
        return startingIncome;
    }

    public double getIncomeGrowth() {
        return incomeGrowth;
    }

    public double getBonusAmount() {
        return bonusAmount;
    }

    public double getEventChance() {
        return eventChance;
    }

    public int getMaxEventsPerWeek() {
        return maxEventsPerWeek;
    }

    public int getScoreMultiplier() {
        return scoreMultiplier;
    }
}
