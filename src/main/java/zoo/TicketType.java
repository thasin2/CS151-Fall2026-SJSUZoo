package zoo;
 
/* The kinds of tickets the zoo sells
    the ticket type is picked automatically from the visitor's age
 */
public enum TicketType {
    CHILD(15.00),
    ADULT(25.00),
    SENIOR(18.00);
 
    private static final int CHILD_MAX_AGE = 12;
    private static final int SENIOR_MIN_AGE = 65;
 
    private final double price;
 
    TicketType(double price) {
        this.price = price;
    }
 
    public double getPrice() {
        return price;
    }
 
    // children are 12 and under, seniors are 65 and over, everyone else is an adult
    public static TicketType fromAge(int age) {
        if (age <= CHILD_MAX_AGE) {
            return CHILD;
        }
        if (age >= SENIOR_MIN_AGE) {
            return SENIOR;
        }
        return ADULT;
    }
}
 