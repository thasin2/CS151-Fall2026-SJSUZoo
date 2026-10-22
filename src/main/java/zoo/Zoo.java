package zoo;
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import zoo.attractions.Attraction;
import zoo.exception.DuplicateEntryException;
import zoo.exception.InsufficientFundsException;
import zoo.exception.ZooFullException;
 
/* The zoo itself: its attractions, its visitors, and ticket sales
    a day goes: openForDay() -> visitors arrive, go on attractions, and leave -> closeForTheDay()
    the zoo tracks how many attraction visits happen each day and in total, so games can check a quota
    MAXIMUM_INSTANCES is defined only here, and every class uses Zoo.checkLimit to enforce it
 */
public class Zoo {
    public static final int MAXIMUM_INSTANCES = 100;
 
 
    private final String name;
    private final int capacity; // most visitors allowed inside at the same time
    private final List<Attraction> attractions = new ArrayList<>();
    private final List<Visitor> registeredVisitors = new ArrayList<>();
    private final List<Visitor> visitorsInside = new ArrayList<>();
 
    private boolean open;
    private int dayNumber;
    private int visitorsToday;
    private int ticketsSoldToday;
    private double ticketRevenueToday;
    private double totalTicketRevenue;
    private int attractionVisitsAtStartOfDay; // used to work out how many attraction visits happened today
 
    /* the one shared check for the 100 limit
        any class can call Zoo.checkLimit(list, "thing") right before adding to a list;
        it throws an error with a message instead of letting the list go over MAXIMUM_INSTANCES
     */
    public static void checkLimit(Collection<?> items, String itemType) {
        if (items.size() >= MAXIMUM_INSTANCES) {
            throw new ZooFullException("Cannot add another " + itemType + ". The limit is "
                    + MAXIMUM_INSTANCES + " " + itemType + "s.");
        }
    }
 
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
        checkLimit(attractions, "attraction");
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
        checkLimit(registeredVisitors, "visitor");
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
 
    // REPORTS  =====
 
    public void printDailyReport() {
        System.out.println("===== " + name + " - Day " + dayNumber + " Report =====");
        System.out.println("Status: " + (open ? "OPEN" : "CLOSED"));
        System.out.printf("Visitors inside now: %d / %d%n", visitorsInside.size(), capacity);
        System.out.println("Visitors who came in today: " + visitorsToday);
        System.out.printf("Tickets sold today: %d ($%.2f)%n", ticketsSoldToday, ticketRevenueToday);
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
 
    public boolean isFull() {
        return visitorsInside.size() >= capacity;
    }
 
    public boolean isOpen() {
        return open;
    }
 
    public String getName() {
        return name;
    }
 
    public int getVisitorCount() {
        return visitorsInside.size();
    }
 
    public double getTicketRevenueToday() {
        return ticketRevenueToday;
    }
 
    @Override
    public String toString() {
        return String.format("%s [%s, day %d] %d/%d visitors inside, %d attractions, $%.2f in tickets today",
                name, open ? "open" : "closed", dayNumber, visitorsInside.size(), capacity,
                attractions.size(), ticketRevenueToday);
    }
}