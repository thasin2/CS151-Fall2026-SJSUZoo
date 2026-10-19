package zoo;

/* Interface for anything in the zoo that wears down and needs upkeep, such as habitats and attractions
    condition is a value from 0 to 100, where 100 is perfect
    each class decides how its condition drops and when it needs maintenance
    maintenance staff can service any Maintainable without knowing what type it is
 */
public interface Maintainable {
    int getCondition(); // current condition, from 0 to 100

    boolean needsMaintenance(); // true when condition is low enough that it should be serviced

    void performMaintenance(); // restores condition to 100
}
