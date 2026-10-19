package zoo.game.manager;

// How many shifts one role needs this week.
// The base demand grows on a fixed schedule: it goes up by 1 every risePeriod weeks and never goes down.
// Events add extra demand that lasts for one week only.
public class ManagerRoleDemand {
    private final int start;
    private final int risePeriod;
    private int base;
    private int extra; // added by events, cleared at the start of each week

    public ManagerRoleDemand(int start, int risePeriod) {
        if (start < 0 || risePeriod < 1) {
            throw new IllegalArgumentException("Demand cannot be negative and must rise at least every so many weeks.");
        }
        this.start = start;
        this.risePeriod = risePeriod;
        this.base = start;
        this.extra = 0;
    }

    // starts the given week: clears event demand and sets the base demand for that week
    public void startWeek(int week) {
        if (week < 1) {
            throw new IllegalArgumentException("Week must be 1 or more.");
        }
        extra = 0;
        base = start + (week - 1) / risePeriod;
    }

    public void addExtra(int shifts) {
        if (shifts <= 0) {
            throw new IllegalArgumentException("Extra demand must be positive.");
        }
        extra += shifts;
    }

    // getters
    public int getTotal() {
        return base + extra;
    }

    @Override
    public String toString() {
        return extra == 0 ? String.valueOf(base) : getTotal() + " (" + base + " + " + extra + " from events)";
    }
}
