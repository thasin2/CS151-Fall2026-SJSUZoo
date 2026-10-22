package zoo.game.schedule;
import zoo.inventory.*;
import zoo.attractions.*;
import zoo.Visitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;


public class ScheduleGame {
    private static final int VISITORS_PER_DAY = 30;
    private Inventory inventory;
    private Attraction attraction;
    private final Random random = new Random();
    private ArrayList<Visitor> visitors = new ArrayList<>();
    private int leftHungry;
    private boolean health;

    // List of made-up names for Visitors, each name only once, so no two visitors in a day are the same
    private static final List<String> NAMES = new ArrayList<>(List.of("Angie", "Ang", "Aaron", "Ashley", "Amy", 
    "Andrew", "Arthur", "Ant", "Bart", "Beatrice", "Beth", "Bethany", "Cat", "Ced", "Dart", "Darren", "Dez", "Enda",
    "Farrah", "Fez", "Gia", "Geo", "Gan", "Guppo", "Hannah", "Han", "Hart", "Heather", "Ingrid", "Jack", "Joe", 
    "Jade", "Justin", "Justine", "Jana", "Jan", "Karen", "Kat", "Kam", "Kon", "Lia", "Leo", "Lan", "Laya", "Matt",
    "Mat", "Maddy", "Mark", "Mandy", "Matthew", "Neal", "Nick", "Nic", "Noel", "Noelle", "Nia", "Niya", "Nico",
    "Nicky", "Oat", "Oak", "Orta", "Oakley", "Owna", "Patt", "Pat", "Picka", "Polly", "Pam", "Pit", "Qan", "Rhett",
    "Rick", "Ron", "Resa", "Riza", "Rock", "Sam", "San", "Soul", "Sammie", "Sona", "Sont", "Sean", "Shet", "Tan",
    "Tony", "Tunt", "Tara", "Tia", "Tierra", "Tiana", "Tez", "Tom", "Tommy", "Umma", "Van", "Vecna", "Wyatt", "Yara"));

    // Creates a object for the zoo simulation
    public ScheduleGame() {
        inventory = new Inventory();
        inventory.addBudget(300);
    }

    // Sets up the attraction in the simulation
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

    public Inventory getInventory() {
        return inventory;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    // Shows the attraction to user
    public void displayAttraction() {
        if (attraction == null) {
            System.out.println("Attraction has not been created yet. Try again");
        }
        else {
            System.out.println(attraction);
        }
    }

    // Simulates one day in the zoo simulation (Makes visitors, runs the attraction, takes inventory to feed & treat animals)
    public void dayCycle(int day) {
        attraction.open();
       setUpVisitors();
       attractionCycle();
       attraction.close();

       switch (day) {
        case 1:
            feedAnimals(20);
            break;
        case 2:
            feedAnimals(10);
            break;
        case 3:
            feedAnimals(15);
            break;
       }
       health = inventory.use("medicine");
       visitors.clear();
    }

    // performs maintenance work if possible
    public void maintenanceWork() {
        if (inventory.use("supplies")) {
            attraction.performMaintenance();
            System.out.println("------------------------");
            System.out.println("Successfully performed maintenance.");
        }
        else {
            System.out.println("------------------------");
            System.out.println("Not enough supplies. Maintenance could not be performed");
        }
    }

    // Checks at the end of day 1 and 2 if user failed simulation (more than 5 animals hungry or a sick animal)
    public boolean failCheck() {
        boolean check = true;
        if (leftHungry >= 5) {
            failMessage(0);
            check = false;
        }
        else if (!health) {
            failMessage(1);
            check = false;
        }
        return check;

    }

    // Checks if user succeeded after final day (Same as failCheck + if Quota of 30 attraction visitors not met)
    public boolean finalFailCheck() {
        boolean check = failCheck();
        if (attraction.getTotalVisited() < 30) {
            failMessage(2);
            check = false;
        }
        return check;
    }

    // Around 13 to 17 visitors will attend an attraction per day.
    private void attractionCycle() {
       for (int j = random.nextInt(5) + 13; j > 0; --j) {
        Visitor attVisitor = visitors.remove(0);
        checkIfFull();
        attraction.admit(attVisitor);
       }
    }

    // feeds animal and tallies how many animals are not fed.
    private void feedAnimals(int amount) {
       for (int i = 0; i < amount; ++i) {
        if (!inventory.use("food")) {
            leftHungry++;
        }
       }
    }

    // adds 30 visitors to the simulation (stored in visitors arrayList)
    private void setUpVisitors() {
        // shuffles the names, so every visitor today gets a different name
        ArrayList<String> shuffledNames = new ArrayList<>(NAMES);
        Collections.shuffle(shuffledNames, random);
        for (int i = 0; i < VISITORS_PER_DAY; ++i) {
            // generates a random age from 8 to 80
            int age = random.nextInt(72) + 8;
            // creates a visitor and adds it to a list
            visitors.add(new Visitor(shuffledNames.get(i), age));
        }
    }

    // 50% chance for a current visitor to leave a full attraction when another visitor wants to enter
    private void checkIfFull() {
        if (attraction.isFull()) {
            int leave = random.nextInt(2);
            if (leave == 0) {
                Visitor remVisitor = (attraction.getCurrentVisitor());
                attraction.remove(remVisitor);
            }
        }
    }

    // messages given when failing rules 1, 2, or 3.
    private void failMessage(int x) {
        System.out.println("------------------------");
        System.out.println("Sadly, you have failed the zoo simulation");
        if (x == 0) {
            System.out.println("Unfortunately, you did not feed enough animals");
        }
        else if (x == 1) {
            System.out.println("Unfortunately, you left an animal sick at the end of the day");
        }
        else if (x == 2) {
            System.out.println("Unfortunately, you only had " + attraction.getTotalVisited() +
            " total visitors, not meeting the quota of 30");
        }
    }

    @Override
    public String toString() {
        return "ScheduleGame{attraction=" + (attraction == null ? "none" : attraction.getName())
                + ", animalsLeftHungry=" + leftHungry + ", medicineUsedToday=" + health + "}";
    }


}
