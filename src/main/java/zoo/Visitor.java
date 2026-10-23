package zoo;
 
import java.util.Objects;
import zoo.attractions.Attraction;
import zoo.exception.InsufficientFundsException;
 
/* A person visiting the zoo
    a visitor adds money to their wallet, buys a ticket, enters the zoo,
    goes on attractions, and leaves at the end of the visit
    each time an attraction turns them away they lose patience; at 0 patience they are upset
    two visitors are the same if they have the same name (ignoring case), since the zoo looks visitors up by name
 */
public class Visitor {
    public static final int MAX_PATIENCE = 3;
    private static final int MAX_AGE = 120;
    private static final double MAX_SINGLE_DEPOSIT = 1000.00;
 
    private final String name;
    private final int age;
    private final TicketType ticketType;
    private double wallet;
    private boolean hasTicket;
    private boolean insideZoo;
    private Attraction currentAttraction; // null when the visitor is not on an attraction
    private int patience = MAX_PATIENCE;
 
    // a visitor who has not brought any money yet
    public Visitor(String name, int age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Visitor name cannot be blank.");
        }
        if (age < 0 || age > MAX_AGE) {
            throw new IllegalArgumentException("Age must be from 0 to " + MAX_AGE + ".");
        }
        this.name = name.trim();
        this.age = age;
        this.ticketType = TicketType.fromAge(age);
    }
 
    // a visitor who arrives with some money
    public Visitor(String name, int age, double startingMoney) {
        this(name, age);
        if (startingMoney > 0) {
            addFunds(startingMoney);
        }
    }
 
    // adds money to the visitor's wallet
    public final void addFunds(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be more than $0.");
        }
        if (amount > MAX_SINGLE_DEPOSIT) {
            throw new IllegalArgumentException("You can add at most $" + MAX_SINGLE_DEPOSIT + " at a time.");
        }
        wallet += amount;
    }
 
    public boolean canAffordTicket() {
        return wallet >= ticketType.getPrice();
    }
 
    // pays for a ticket at the visitor's age price, and the zoo records the sale
    public void buyTicket(Zoo zoo) {
        if (hasTicket) {
            throw new IllegalStateException(name + " already has a ticket.");
        }
        if (!canAffordTicket()) {
            throw new InsufficientFundsException(String.format(
                    "%s needs $%.2f for a %s ticket but only has $%.2f.",
                    name, ticketType.getPrice(), ticketType, wallet));
        }
        zoo.recordTicketSale(this); // checked first, so a closed zoo does not take the money
        wallet -= ticketType.getPrice();
        hasTicket = true;
    }
 
    // walks through the gate; the zoo checks that it is open and has room
    public void enterZoo(Zoo zoo) {
        if (insideZoo) {
            throw new IllegalStateException(name + " is already inside the zoo.");
        }
        if (!hasTicket) {
            throw new IllegalStateException(name + " needs a ticket before entering.");
        }
        zoo.admitVisitor(this);
        insideZoo = true;
    }
 
    // leaves any attraction first, then leaves the zoo; the ticket is used up and patience resets
    public void leaveZoo(Zoo zoo) {
        if (!insideZoo) {
            throw new IllegalStateException(name + " is not inside the zoo.");
        }
        if (currentAttraction != null) {
            leaveAttraction();
        }
        zoo.removeVisitor(this);
        insideZoo = false;
        hasTicket = false;
        patience = MAX_PATIENCE;
    }
 
    /* tries to get on an attraction; returns true if the attraction let the visitor in
        if the attraction is closed, full, or worn out, the visitor loses 1 patience
     */
    public boolean visitAttraction(Attraction attraction) {
        if (attraction == null) {
            throw new IllegalArgumentException("Attraction cannot be empty.");
        }
        if (!insideZoo) {
            throw new IllegalStateException(name + " must be inside the zoo to visit an attraction.");
        }
        if (currentAttraction != null) {
            throw new IllegalStateException(name + " is already at " + currentAttraction.getName()
                    + ". Leave it first.");
        }
        // Attraction.admit() prints its own message, so compare the visitor count before and after
        // to know for sure whether the visitor got in
        int countBefore = attraction.getCurrentVisitorCount();
        attraction.admit(this);
        if (attraction.getCurrentVisitorCount() > countBefore) {
            currentAttraction = attraction;
            return true;
        }
        if (patience > 0) {
            patience--;
        }
        if (isUpset()) {
            System.out.println(name + " is upset after being turned away " + MAX_PATIENCE + " times!");
        }
        return false;
    }
 
    public void leaveAttraction() {
        if (currentAttraction == null) {
            throw new IllegalStateException(name + " is not at an attraction.");
        }
        currentAttraction.remove(this);
        System.out.println(name + " left " + currentAttraction.getName() + ".");
        currentAttraction = null;
    }
 
    public boolean isUpset() {
        return patience == 0;
    }
 
    public String getName() {
        return name;
    }
 
    public TicketType getTicketType() {
        return ticketType;
    }
 
    public double getWallet() {
        return wallet;
    }
 
    public boolean hasTicket() {
        return hasTicket;
    }
 
    public boolean isInsideZoo() {
        return insideZoo;
    }
 
    public boolean isAtAttraction() {
        return currentAttraction != null;
    }
 
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Visitor)) {
            return false;
        }
        return name.equalsIgnoreCase(((Visitor) other).name);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
 
    @Override
    public String toString() {
        String where = !insideZoo ? "outside the zoo"
                : currentAttraction != null ? "at " + currentAttraction.getName()
                : "inside the zoo";
        return String.format("%s (age %d, %s ticket) | wallet $%.2f | %s | %s%s",
                name, age, ticketType, wallet, hasTicket ? "has ticket" : "no ticket", where,
                isUpset() ? " | UPSET" : "");
    }
}
