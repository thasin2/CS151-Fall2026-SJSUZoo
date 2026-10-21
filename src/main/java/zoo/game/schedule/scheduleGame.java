package zoo.game.schedule;
import zoo.inventory.*;
import zoo.attractions.*;



public class ScheduleGame {
    public Inventory inventory;
    public Attraction attraction;

    public ScheduleGame() {
        inventory = new Inventory();
        inventory.setBudget(300);
    }

    public void setUpAttraction(String name, int capacity) {
        if (attraction != null) {
            System.out.println("------------------------");
            System.out.println("Attraction is already created. Failed to create");
        }
        else if (capacity == 0) {
            attraction = new Attraction();
            attraction.setName(name);
        }
        else {
            attraction = new Attraction(capacity);
            attraction.setName(name);
        }
    }

    public void displayAttraction() {
        if (attraction == null) {
            System.out.println("Attraction has not been created yet. Try again");
        }
        else {
            attraction.status();
        }
    }

    public void endDay() {
        /* Ideas for the end of each Day Cycle:
        Able to check inventory & attraction status.
        Able to check maintenance status for attraction.
        Able to perform maintenance
        Optional: Able to feed animals and treat animals (Maybe order staff to do these things)
        If doesn't repair any broken attractions by end of Day, game ends.
        */
    }

    /* For days 2 or 3, add spontaneous events for users to do stuff hands-on in zoo
    Ideas:
    Feed or not feed angry animal
    Handle an angry visitor that wants to enter a full attraction
    */
    
    public void Day1() {
        /* Cycle:
        Each day, create visitors (can re-use for future days maybe)
        Admit visitors into attractions based on rng (Or just fix it as half the visitors go)
        For every visitor that enters the zoo (is created), chance for a visitor to leave the attraction (can RNG or fix)
        ^ All these things in the back-end will control how much attractions are visited each day (Maintenance & Quota)
        */
    }

    public void Day2() {

    }

    public void Day3() {

    }
}
