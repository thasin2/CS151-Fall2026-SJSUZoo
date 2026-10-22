package zoo.game.schedule;
import zoo.inventory.*;
import zoo.attractions.*;
import zoo.Visitor;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;


public class ScheduleGame {
    public Inventory inventory;
    public Attraction attraction;
    Random random = new Random();
    private ArrayList<Visitor> visitors = new ArrayList<>();
    private int left_hungry;
    private boolean health;

    private static final ArrayList<String> names = new ArrayList<>(List.of("Angie", "Ang", "Aaron", "Ashley", "Amy", 
    "Andrew", "Arthur", "Ant", "Bart", "Beatrice", "Beth", "Bethany", "Cat", "Ced", "Dart", "Darren", "Dez", "Enda",
    "Farrah", "Fez", "Gia", "Geo", "Gan", "Guppo", "Hannah", "Han", "Hart", "Heather", "Ingrid", "Jack", "Joe", 
    "Jack", "Justin", "Justine", "Jana", "Jan", "Karen", "Kat", "Kam", "Kon", "Lia", "Leo", "Lan", "Laya", "Matt",
    "Mat", "Maddy", "Mark", "Mandy", "Matthew", "Neal", "Nick", "Nic", "Noel", "Noelle", "Nia", "Niya", "Nico",
    "Nicky", "Oat", "Oak", "Orta", "Oakley", "Owna", "Patt", "Pat", "Picka", "Polly", "Pam", "Pit", "Qan", "Rhett",
    "Rick", "Ron", "Resa", "Riza", "Rock", "Sam", "San", "Soul", "Sammie", "Sona", "Sont", "Sean", "Shet", "Tan",
    "Tony", "Tunt", "Tara", "Tia", "Tierra", "Tiana", "Tez", "Tom", "Tommy", "Umma", "Van", "Vecna", "Wyatt", "Yara"));

    public ScheduleGame() {
        inventory = new Inventory();
        inventory.addBudget(300);
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

    public void Day1() {
       setUpVisitors();

       // Simulates visitors to attractions through one day
       attractionCycle();

       attraction.close();

       // Tweakable later to have the user control this
       feedAnimals(20);
       health = inventory.use("medicine");

       // Removes all visitors from the zoo
       visitors.clear();
    }

    public void Day2() {
        attraction.open();
        setUpVisitors();
        attractionCycle();
        attraction.close();

        feedAnimals(10);
        health = inventory.use("medicine");
        visitors.clear();
    }

    public void Day3() {
        attraction.open();
        setUpVisitors();
        attractionCycle();
        attraction.close();

        feedAnimals(15);
        health = inventory.use("medicine");
        visitors.clear();
    }

    public void attractionCycle() {
        // 13 to 17 visitors will want to attend a attraction
       for (int j = random.nextInt(5) + 14; j > 0; --j) {
        Visitor attVisitor = visitors.remove(0);
        checkIfFull();
        attraction.admit(attVisitor);
       }
    }
    // feeds animal and tallies if animal is not fed
    public void feedAnimals(int amount) {
       for (int i = 0; i < amount; ++i) {
        if (!inventory.use("food")) {
            left_hungry++;
        }
       }
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

    
    // adds 30 visitors to the simulation (stored in visitors arrayList)
    public void setUpVisitors() {

        for (int i = 0; i < 30; ++i) {
            // generates a random age from 8 to 80
            int age = random.nextInt(72) + 8;
            // picks a random name from the list
            String name = names.get(random.nextInt(names.size()));
            // creates a visitor and adds it to a list
            visitors.add(new Visitor(name, age));
        }
    }

    // Checks at the end of day 1 and 2 if user failed simulation (left more than 5 animals hungry or left a sick animal)
    public boolean failCheck() {
        boolean check = true;
        if (left_hungry >= 5) {
            failMessage(0);
            check = false;
        }
        else if (!health) {
            failMessage(1);
            check = false;
        }
        return check;

    }

    // Checks if user succeeded after final day (Same as failCheck + if Quota of 35 attraction visitors not met)
    public boolean finalFailCheck() {
        boolean check = failCheck();
        if (attraction.getTotalVisited() < 35) {
            failMessage(2);
            check = false;
        }
        return check;
    }

    // 50% chance for a current visitor to leave a full attraction when another visitor wants to enter
    public void checkIfFull() {
        if (attraction.isFull()) {
            int leave = random.nextInt(2);
            if (leave == 0) {
                Visitor remVisitor = (attraction.getCurrentVisitor());
                attraction.remove(remVisitor);
            }
        }
    }

    public void failMessage(int x) {
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
            "total visitors, not meeting the quota of 35");
        }
    }


}
