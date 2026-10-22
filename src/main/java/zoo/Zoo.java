package zoo;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import zoo.attractions.Attraction;
import zoo.exception.DuplicateEntryException;
import zoo.exception.InsufficientFundsException;
import zoo.exception.ZooFullException;
import zoo.inventory.Inventory;
 
/* The zoo itself: its attractions, its visitors, and ticket sales
    a day goes: openForDay() -> visitors arrive, go on attractions, and leave -> closeForTheDay()
    the zoo tracks how many attraction visits happen each day and in total, so games can check a quota
    MAXIMUM_INSTANCES is defined only here, and every class that needs the limit uses Zoo.MAXIMUM_INSTANCES
 */
public class Zoo {
    public static final int MAXIMUM_INSTANCES = 100;
 
    private static final String[] GUEST_FIRST_NAMES = {
        "Alex", "Bella", "Carlos", "Dana", "Eli", "Fatima", "Gabe", "Hana", "Ivan", "Jade",
        "Kai", "Luna", "Mateo", "Nia", "Omar", "Priya", "Quinn", "Rosa", "Sam", "Tara"
    };
    private static final int GUEST_MIN_AGE = 3;
    private static final int GUEST_MAX_AGE = 80;
    private static final double GUEST_MIN_MONEY = 10.00;
    private static final double GUEST_MAX_MONEY = 60.00;
 
    private final String name;
    private final int capacity; // most visitors allowed inside at the same time
    private final List<Attraction> attractions = new ArrayList<>();
    private final List<Visitor> registeredVisitors = new ArrayList<>();
    private final List<Visitor> visitorsInside = new ArrayList<>();
 
    private boolean open;
    private int dayNumber;
    private int visitorsToday;
    private int ticketsSoldToday;
    private int refundsToday;
    private double ticketRevenueToday;
    private double totalTicketRevenue;
    private int attractionVisitsAtStartOfDay; // used to work out how many attraction visits happened today
 
    public Zoo(String name, int capacity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Zoo name cannot be blank.");
        }
        if (capacity < 1 || capacity > MAXIMUM_INSTANCES) {
            throw new IllegalArgumentException("Capacity must be from 1 to " + MAXIMUM_INSTANCES + ".");
        }
        this.name = name.trim();
        this.capacity = capacity;
    }
 
    // SETTING UP =====
 
    // adds an attraction; names must be unique (ignoring case), and there can be at most 100
    public void addAttraction(Attraction attraction) {
        if (attraction == null) {
            throw new IllegalArgumentException("Attraction cannot be empty.");
        }
        if (getAttraction(attraction.getName()) != null) {
            throw new DuplicateEntryException("There is already an attraction called " + attraction.getName() + ".");
        }
        if (attractions.size() >= MAXIMUM_INSTANCES) {
            throw new ZooFullException("The zoo already has the maximum of " + MAXIMUM_INSTANCES + " attractions.");
        }
        attractions.add(attraction);
    }
 
    // finds an attraction
    public Attraction getAttraction(String attractionName) {
        for (Attraction attraction : attractions) {
            if (attraction.getName().equalsIgnoreCase(attractionName.trim())) {
                return attraction;
            }
        }
        return null;
    }
 
    // adds a new visitor to the zoo's records; blocks duplicates and enforces the 100 limit
    public void registerVisitor(Visitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("Visitor cannot be empty.");
        }
        if (registeredVisitors.contains(visitor)) {
            throw new DuplicateEntryException("A visitor named " + visitor.getName() + " is already registered.");
        }
        if (registeredVisitors.size() >= MAXIMUM_INSTANCES) {
            throw new ZooFullException("The system already has the maximum of " + MAXIMUM_INSTANCES + " visitors.");
        }
        registeredVisitors.add(visitor);
    }
 
    // removes a visitor 
    public void unregisterVisitor(Visitor visitor) {
        if (visitor.isInsideZoo()) {
            throw new IllegalStateException(visitor.getName() + " must leave the zoo before being removed.");
        }
        registeredVisitors.remove(visitor);
    }
 
    // find registered visitor 
    public Visitor findVisitor(String visitorName) {
        for (Visitor visitor : registeredVisitors) {
            if (visitor.getName().equalsIgnoreCase(visitorName.trim())) {
                return visitor;
            }
        }
        return null;
    }
 
    // OPEN/CLOSING =====
 
    // starts a new day: daily numbers reset and every attraction that is safe to open opens
    public void openForDay() {
        if (open) {
            throw new IllegalStateException(name + " is already open.");
        }
        open = true;
        dayNumber++;
        visitorsToday = 0;
        ticketsSoldToday = 0;
        refundsToday = 0;
        ticketRevenueToday = 0;
        attractionVisitsAtStartOfDay = getTotalAttractionVisits();
        for (Attraction attraction : attractions) {
            attraction.open();
        }
        System.out.println(name + " is open for day " + dayNumber + "!");
    }
 
    // everyone inside is sent home, attractions close, and the day's report is printed
    public void closeForTheDay() {
        if (!open) {
            throw new IllegalStateException(name + " is already closed.");
        }
        // copy the list, because leaveZoo() removes visitors from visitorsInside while we loop;
        // visitors also leave their attractions here, so every attraction is empty before it closes
        for (Visitor visitor : new ArrayList<>(visitorsInside)) {
            visitor.leaveZoo(this);
        }
        for (Attraction attraction : attractions) {
            attraction.close();
        }
        open = false;
        System.out.println(name + " is now closed. Here is today's report:");
        printDailyReport();
    }
 
    // package-private, so only classes in the zoo package can use them
 
    void recordTicketSale(Visitor visitor) {
        if (!open) {
            throw new IllegalStateException(name + " is closed and is not selling tickets.");
        }
        double price = visitor.getTicketType().getPrice();
        ticketRevenueToday += price;
        totalTicketRevenue += price;
        ticketsSoldToday++;
    }
 
    void admitVisitor(Visitor visitor) {
        if (!open) {
            throw new IllegalStateException(name + " is closed. Come back tomorrow!");
        }
        if (!registeredVisitors.contains(visitor)) {
            registerVisitor(visitor); // walk-ins are added to the records automatically
        }
        if (isFull()) {
            throw new ZooFullException(name + " is full (" + capacity + " visitors). Wait for someone to leave.");
        }
        visitorsInside.add(visitor);
        visitorsToday++;
    }
 
    void removeVisitor(Visitor visitor) {
        visitorsInside.remove(visitor);
    }
 
    // DAY =====
 
    /* gets a group of visitors for the day
        visitors already in the records who are not inside are reused first,
        then new guests with a random age and some money are created (up to the 100 limit)
     */
    public List<Visitor> generateVisitors(int count, Random random) {
        if (count < 0) {
            throw new IllegalArgumentException("Number of visitors cannot be negative.");
        }
        List<Visitor> group = new ArrayList<>();
        for (Visitor visitor : registeredVisitors) {
            if (group.size() == count) {
                return group;
            }
            if (!visitor.isInsideZoo()) {
                if (!visitor.canAffordTicket()) {
                    visitor.addFunds(randomMoney(random)); // returning guests bring new money for the day
                }
                group.add(visitor);
            }
        }
        while (group.size() < count && registeredVisitors.size() < MAXIMUM_INSTANCES) {
            Visitor guest = new Visitor(makeUniqueGuestName(random),
                    GUEST_MIN_AGE + random.nextInt(GUEST_MAX_AGE - GUEST_MIN_AGE + 1),
                    randomMoney(random));
            registerVisitor(guest);
            group.add(guest);
        }
        return group;
    }

    /* sells tickets to a group and lets them in
        visitors who cannot afford a ticket are turned away; once the zoo is full, the rest wait outside
        returns how many got in
     */
    public int admitGroup(List<Visitor> group) {
        int admitted = 0;
        int cannotAfford = 0;
        int noRoom = 0;
        for (Visitor visitor : group) {
            if (visitor.isInsideZoo()) {
                continue;
            }
            if (isFull()) {
                noRoom++;
                continue;
            }
            try {
                if (!visitor.hasTicket()) {
                    visitor.buyTicket(this);
                }
                visitor.enterZoo(this);
                admitted++;
            } catch (InsufficientFundsException e) {
                cannotAfford++;
            }
        }
        System.out.printf("%d visitor(s) came in. %d could not afford a ticket. %d were turned away because the zoo was full.%n",
                admitted, cannotAfford, noRoom);
        return admitted;
    }
 
    // sends one visitor to an attraction; returns true if they got in
    public boolean sendToAttraction(Visitor visitor, Attraction attraction) {
        if (!visitorsInside.contains(visitor)) {
            throw new IllegalStateException(visitor.getName() + " is not inside " + name + ".");
        }
        if (!attractions.contains(attraction)) {
            throw new IllegalArgumentException(attraction.getName() + " is not one of this zoo's attractions.");
        }
        return visitor.visitAttraction(attraction);
    }
 
    /* each free visitor inside has the given chance (0.0 to 1.0) of trying a random attraction
        returns how many actually got on one
     */
    public int sendVisitorsToAttractions(double chance, Random random) {
        checkChance(chance);
        if (attractions.isEmpty()) {
            System.out.println(name + " has no attractions yet.");
            return 0;
        }
        int gotOn = 0;
        for (Visitor visitor : new ArrayList<>(visitorsInside)) {
            if (!visitor.isAtAttraction() && random.nextDouble() < chance) {
                Attraction pick = attractions.get(random.nextInt(attractions.size()));
                if (visitor.visitAttraction(pick)) {
                    gotOn++;
                }
            }
        }
        return gotOn;
    }
 
    // each visitor on an attraction has the given chance (0.0 to 1.0) of getting off; returns how many left
    public int releaseVisitorsFromAttractions(double chance, Random random) {
        checkChance(chance);
        int left = 0;
        for (Visitor visitor : new ArrayList<>(visitorsInside)) {
            if (visitor.isAtAttraction() && random.nextDouble() < chance) {
                visitor.leaveAttraction();
                left++;
            }
        }
        return left;
    }
 
    // UPSET VISITOR =====
 
    public List<Visitor> getUpsetVisitors() {
        List<Visitor> upset = new ArrayList<>();
        for (Visitor visitor : visitorsInside) {
            if (visitor.isUpset()) {
                upset.add(visitor);
            }
        }
        return upset;
    }
 
    // gives an upset visitor their ticket money back, and they go home
    public void refundVisitor(Visitor visitor) {
        if (!visitorsInside.contains(visitor)) {
            throw new IllegalStateException(visitor.getName() + " is not inside " + name + ".");
        }
        double price = visitor.getTicketType().getPrice();
        ticketRevenueToday -= price;
        totalTicketRevenue -= price;
        refundsToday++;
        visitor.receiveRefund(price);
        visitor.leaveZoo(this);
        System.out.printf("%s was refunded $%.2f and went home.%n", visitor.getName(), price);
    }
 
    // MAINTENCE =====
 
    // everything that needs upkeep, as Maintainable, so it can be serviced without knowing its exact type
    public List<Maintainable> findThingsNeedingMaintenance() {
        List<Maintainable> needsWork = new ArrayList<>();
        for (Attraction attraction : attractions) {
            if (attraction.needsMaintenance()) {
                needsWork.add(attraction);
            }
        }
        return needsWork;
    }
 
    /* services everything that needs it, using supplies from the inventory
        stops early if the supplies run out; returns how many were serviced
     */
    public int performMaintenance(Inventory inventory, int suppliesEach) {
        if (suppliesEach < 1) {
            throw new IllegalArgumentException("Each repair must use at least 1 supply.");
        }
        List<Maintainable> needsWork = findThingsNeedingMaintenance();
        if (needsWork.isEmpty()) {
            System.out.println("Nothing needs maintenance right now.");
            return 0;
        }
        int serviced = 0;
        for (Maintainable item : needsWork) {
            if (!inventory.use("supplies")) {
                System.out.println("Ran out of supplies. " + (needsWork.size() - serviced) + " item(s) still need work.");
                break;
            }
            item.performMaintenance();
            serviced++;
        }
        return serviced;
    }
 
    // REPORTS  =====
 
    public void printDailyReport() {
        System.out.println("===== " + name + " - Day " + dayNumber + " Report =====");
        System.out.println("Status: " + (open ? "OPEN" : "CLOSED"));
        System.out.printf("Visitors inside now: %d / %d%n", visitorsInside.size(), capacity);
        System.out.println("Visitors who came in today: " + visitorsToday);
        System.out.printf("Tickets sold today: %d ($%.2f), refunds: %d%n", ticketsSoldToday, ticketRevenueToday, refundsToday);
        System.out.printf("All-time ticket revenue: $%.2f%n", totalTicketRevenue);
        System.out.println("Attraction visits today: " + getAttractionVisitsToday()
                + " (all-time: " + getTotalAttractionVisits() + ")");
        for (Attraction attraction : attractions) {
            System.out.printf("  %-18s %d on now, %d total, condition %d%s%n",
                    attraction.getName(), attraction.getCurrentVisitorCount(), attraction.getTotalVisited(),
                    attraction.getCondition(), attraction.needsMaintenance() ? "  ** NEEDS MAINTENANCE **" : "");
        }
    }
 
    private void checkChance(double chance) {
        if (chance < 0 || chance > 1) {
            throw new IllegalArgumentException("Chance must be from 0.0 to 1.0.");
        }
    }
 
    // a whole-dollar amount from GUEST_MIN_MONEY to GUEST_MAX_MONEY
    private double randomMoney(Random random) {
        return GUEST_MIN_MONEY + random.nextInt((int) (GUEST_MAX_MONEY - GUEST_MIN_MONEY) + 1);
    }
 
    private String makeUniqueGuestName(Random random) {
        String first = GUEST_FIRST_NAMES[random.nextInt(GUEST_FIRST_NAMES.length)];
        String candidate = first;
        int number = 2;
        while (findVisitor(candidate) != null) {
            candidate = first + " " + number;
            number++;
        }
        return candidate;
    }
 
    // GETTERS =====
 
    public int getTotalAttractionVisits() {
        int total = 0;
        for (Attraction attraction : attractions) {
            total += attraction.getTotalVisited();
        }
        return total;
    }
 
    public int getAttractionVisitsToday() {
        return getTotalAttractionVisits() - attractionVisitsAtStartOfDay;
    }
 
    public List<Attraction> getAttractions() {
        return Collections.unmodifiableList(attractions);
    }
 
    public List<Visitor> getRegisteredVisitors() {
        return Collections.unmodifiableList(registeredVisitors);
    }
 
    public List<Visitor> getVisitorsInside() {
        return Collections.unmodifiableList(visitorsInside);
    }
 
    public boolean isFull() {
        return visitorsInside.size() >= capacity;
    }
 
    public boolean isOpen() {
        return open;
    }
 
    public String getName() {
        return name;
    }
 
    public int getCapacity() {
        return capacity;
    }
 
    public int getVisitorCount() {
        return visitorsInside.size();
    }
 
    public int getDayNumber() {
        return dayNumber;
    }
 
    public int getVisitorsToday() {
        return visitorsToday;
    }
 
    public double getTicketRevenueToday() {
        return ticketRevenueToday;
    }
 
    public double getTotalTicketRevenue() {
        return totalTicketRevenue;
    }
 
    @Override
    public String toString() {
        return String.format("%s [%s, day %d] %d/%d visitors inside, %d attractions, $%.2f in tickets today",
                name, open ? "open" : "closed", dayNumber, visitorsInside.size(), capacity,
                attractions.size(), ticketRevenueToday);
    }
}
