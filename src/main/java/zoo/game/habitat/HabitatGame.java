package zoo.game.habitat;

import java.util.ArrayList;

import zoo.animal.Animal;
import zoo.game.animal.AnimalGame;
import zoo.habitat.Habitat;
import zoo.inventory.*;
import zoo.staff.MaintenanceStaff;
import zoo.staff.Zookeeper;
import zoo.ui.ConsoleInput;


public class HabitatGame {
    private Inventory habGameInv = new Inventory();
    private Zookeeper zooHabGame = new Zookeeper("1245", "Greta", 10000, true);
    private MaintenanceStaff maintHabGame = new MaintenanceStaff("1246", "Max", 25, true);
    private AnimalGame temp = new AnimalGame();
    private boolean opened = false;

    public Habitat makeHabitat(){
        System.out.println("Let's get some basic info to create your habitat!");
        String habitatName = ConsoleInput.readNonEmptyLine("What do you want to name your Habitat? ");
        int capacity = ConsoleInput.readInt("How many animals can your habitat hold? ", 1, 10);
        System.out.println("You need to create a beginner Animal object to add to your habitat. A note - whatever type of Animal you choose, you have to ensure that all other animals added are of the same type.");
        String animalName = ConsoleInput.readNonEmptyLine("What do you want to name your Animal? ");
        Animal firstAnimal = temp.createAnimalObj(animalName);
        Habitat habitat = new Habitat(habitatName, capacity, firstAnimal);
        System.out.println("Cool, your habitat is made!");
        return habitat; // we want to let this object get modified, so we return the object itself (not a copy)
    }

    public void firstPart(Habitat hab){
        System.out.println("Okay, let's get started. You can either 1 - Add animals to your habitat, or 2 - Open your habitat. Note if you open your habitat, it is prone to random events that could damage it, especially if the capacity of the habitat is high.");
        int choice = ConsoleInput.readInt("1 or 2 ", 1, 2);
        if (choice == 1){
            add(hab);
        }
        else {
            setOpened(true);
            open(hab);
        }
    }

    public void secondPart(Habitat hab){
        System.out.println("You can either 1 - Add animals to your habitat, or 2 - Open your habitat. 3 - Close your habitat. Note - you can only close a habitat after it has been open. You can continue 'opening' a habitat, as this acts like opening shifts to continue the simulation.");
        int choice = ConsoleInput.readInt("1, 2, or 3 ", 1, 3);
        if (choice == 1){
            add(hab);
            secondPart(hab);
        }
        else if (choice == 2){
            setOpened(true);
            open(hab);
            option2(hab);
        }
        else if ((choice == 3) && (isOpened())){
            setOpened(false);
            close(hab);
            option3(hab);
        }
        else {
            System.out.println("The habitat is not open yet, so it cannot be closed. Open it first.");
        }
    }

  
    public void option2(Habitat hab){
            System.out.println("Now, you can 1 - Add animals. 2 - Close your habitat");
        int choice = ConsoleInput.readInt("1 or 2 ", 1, 2);
        if (choice == 1){
            add(hab);
            secondPart(hab);
        }
        else { // the habitat is always open here, since option2 runs right after opening it
            setOpened(false);
            close(hab);
            option3(hab);
        }
    }

    public void option3(Habitat hab){
            System.out.println("Now, you can 1 - Open your habitat. 2 - Add animals");
        int choice = ConsoleInput.readInt("1 or 2 ", 1, 2);
        if (choice == 1){
            setOpened(true);
            open(hab);
            option2(hab);
        }
        else {
            add(hab);
            secondPart(hab);
        }
    }


    public void add(Habitat x){
        System.out.println("You chose to add an animal. But there are safety checks with this process, so please bear with the system.");
        String animalName = ConsoleInput.readNonEmptyLine("What do you want to name your Animal? ");
        Animal animalAddition = temp.createAnimalObj(animalName);
        boolean y = x.addToAnimals(animalAddition);
        while(!y){
            System.out.println("Do you want to try adding the animal again?");
            String choose = ConsoleInput.readLine("yes/no ");
            if (choose.equalsIgnoreCase("yes")){
                add(x);
                y = true;
            }
            else if ((choose.equalsIgnoreCase("no"))){
                y = true;
            }
            else {
                System.out.println("Please enter yes or no. ");
                y = false;
            }
        }
    }
    public void open(Habitat x){
        boolean y = x.habitatOpen();
        if (!y){
            System.out.println("We have to maintain the habitat. Let's call the maintenance staff.");
            maintenance(x, habGameInv);
        }
        else {
            System.out.println("Yay! A successful opening shift without any damages to the habitat.");
        }
    }
    public void close(Habitat hab){
        System.out.println("Since you want to close the habitat, we have to feed and make the animals sleep.");
        ArrayList<Animal> listOfAnimals = hab.getAnimals();
        for (Animal x: listOfAnimals){
            feed(x);
            System.out.println("Fed " + x.getName() + ".");
        }
        hab.habitatClose();

        
    }
    // maintenance staff repair the habitat, which uses one supply piece
    public void maintenance(Habitat hab, Inventory stor){
        if (!hab.needsMaintenance()){
            System.out.println("The habitat does not need maintenance.");
            return;
        }
        System.out.println("Oh no! Your habitat needs some maintenance! Let's call in the maintenance staff.");
        if (!stor.use(Item.SUPPLIES)){
            System.out.println("Let's go to the stocking area!");
            stockingProcesses(Item.SUPPLIES);
            maintenance(hab, stor);
            return;
        }
        startShiftIfNeeded();
        maintHabGame.service(hab);
        System.out.println(hab.getName() + " has been maintained! " + maintHabGame.getName() + " did a great job!");
    }

    // the maintenance worker starts a new shift when this week's shifts allow no more jobs
    // after the most shifts in a week, the week is paid out and a new week starts
    private void startShiftIfNeeded(){
        if (maintHabGame.hasJobsLeft()){
            return;
        }
        if (maintHabGame.getShiftsThisWeek() >= maintHabGame.getMaximumShifts()){
            double weeklyPay = maintHabGame.closeOutWeek();
            System.out.printf("A new week starts. %s was paid $%,.2f for last week.%n", maintHabGame.getName(), weeklyPay);
        }
        maintHabGame.workShift();
    }

    public void stockingProcesses(Item item){
    System.out.println("Welcome to the stocking area! Here is everything you have:");
    System.out.println(habGameInv.toString());
    System.out.println("---------------------------------------------------");
    System.out.println("Each supply piece costs $45.99.");
    System.out.println("Each food piece costs $3.49.");
    String value = ConsoleInput.readLine("Do we have enough money to stock our inventory with what is needed? Say yes/no ");
    if (value.equalsIgnoreCase("no")){
            int amount = ConsoleInput.readInt("What amount should we add to our budget to purchase more inventory stock? Enter between 1 and 1000. ", 1, 1000);
            habGameInv.addBudget(amount);
    }
    else if (value.equalsIgnoreCase("yes")){
        System.out.println("Good! Let's move on then.");
    }
    else {
        System.out.println("Enter a correct response");
        stockingProcesses(item);
        return;
    }
    System.out.println(habGameInv.toString());
    int numberAvail = 1;
    if (item == Item.FOOD){
        numberAvail = ((int)(habGameInv.getBudget() / 3.49));
    }
    if (item == Item.MEDICINE){
        numberAvail = ((int)(habGameInv.getBudget() / 20));
    }
    if (item == Item.SUPPLIES){
        numberAvail = ((int)(habGameInv.getBudget()/45.99));
    }
    if (numberAvail < 1){
        System.out.println("There is not enough money to buy even one. Let's add more to the budget.");
        stockingProcesses(item);
        return;
    }
    int numberToStock = ConsoleInput.readInt("How much do you want to stock? Enter a reasonable number in our budget. " , 1, numberAvail);
    habGameInv.stock(item, numberToStock);
    System.out.println(habGameInv.toString());

}
public void feed(Animal a){
    boolean hungerNotSolved = zooHabGame.feedAnimal(a, habGameInv);
    if (!hungerNotSolved){
        System.out.println("Let's go to the stocking area!");
        stockingProcesses(Item.FOOD);
        feed(a);
    }
}


public boolean isOpened() {
    return opened;
    }

public void setOpened(boolean opened) {
    this.opened = opened;
    }

    
}
