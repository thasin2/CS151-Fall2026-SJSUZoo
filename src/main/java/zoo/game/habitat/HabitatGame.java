package zoo.game.habitat;

import java.util.ArrayList;

import zoo.animal.Animal;
import zoo.game.animal.AnimalGame;
import zoo.habitat.Habitat;
import zoo.inventory.Inventory;
import zoo.staff.Zookeeper;
import zoo.ui.ConsoleInput;


public class HabitatGame {
    Inventory habGameInv = new Inventory();
    Zookeeper zooHabGame = new Zookeeper("1245", "Greta", 10000, true);
    AnimalGame temp = new AnimalGame();

    public Habitat makeHabitat(){
        System.out.println("Let's get some basic info to create your habitat!");
        String habitatName = ConsoleInput.readLine("What do you want to name your Habitat? ");
        int capacity = ConsoleInput.readInt("How many animals can your habitat hold?", 1, 10);
        System.out.println("You need to create a beginner Animal object to add to your habitat. A note - whatever type of Animal you choose, you have to ensure that all other animals added are of the same type.");
        String animalName = ConsoleInput.readLine("What do you want to name your Animal? ");
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
        else if (choice == 2){
            open(hab);
        }
        else {
            System.out.println("Please enter a correct number.");
        }
    }

    public void secondPart(Habitat hab){
        System.out.println("You can either 1 - Add animals to your habitat, or 2 - Open your habitat. 3 - Close your habitat");
        int choice = ConsoleInput.readInt("1, 2, or 3 ", 1, 3);
        if (choice == 1){
            add(hab);
            secondPart(hab);
        }
        else if (choice == 2){
            open(hab);
            option2(hab);
        }
        else if (choice == 3){
            close(hab);
            option3(hab);
        }
        else {
            System.out.println("Please enter a correct number.");
        }
    }

  
    public void option2(Habitat hab){
            System.out.println("Now, you can 1 - Add animals. 2 - Close your habitat");
        int choice = ConsoleInput.readInt("1 or 2 ", 1, 2);
        if (choice == 1){
            add(hab);
            secondPart(hab);
        }
        else if (choice == 2){
            close(hab);
            option3(hab);
        }
        else {
            System.out.println("Please enter a correct number.");
        }
    }

    public void option3(Habitat hab){
            System.out.println("Now, you can 1 - Open your habitat. 2 - Add animals");
        int choice = ConsoleInput.readInt("1 or 2 ", 1, 2);
        if (choice == 1){
            open(hab);
            option2(hab);
        }
        else if (choice == 2){
            add(hab);
            secondPart(hab);
        }
        else {
            System.out.println("Please enter a correct number.");
        }
    }


    public void add(Habitat x){
        System.out.println("You chose add an animal. But there are safety checks with this process, so please bear with the system.");
        String animalName = ConsoleInput.readLine("What do you want to name your Animal? ");
        Animal animalAddition = temp.createAnimalObj(animalName);
        boolean y = x.addToAnimals(animalAddition);
        if (!y){
            System.out.println("Do you want to try adding the animal again (with the correct type this time)?");
            String choose = ConsoleInput.readLine("yes/no ");
            if (choose.equalsIgnoreCase("yes")){
                add(x);
            }
            else {
                return;
            }
        }
    }
    public void open(Habitat x){
        boolean y = x.habitatOpen();
        if (!y){
            System.out.println("We have to maintain the habitat. Let's go to the zookeeper.");
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
            temp.feed(x);
            System.out.println("Fed " + x.getName() + ".");
        }
        hab.habitatClose();

        
    }
    public void maintenance(Habitat hab, Inventory stor){
        if (zooHabGame.maintainHabitat(hab, stor)){
            return;
        }
        else {
            System.out.println("Let's go to the stocking area!");
            stockingProcess("supplies");
            maintenance(hab, stor);
        }
    }

    public void stockingProcess(String item){
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
    if (value.equalsIgnoreCase("yes")){
        System.out.println("Good! Let's move on then.");
    }
    else {
        System.out.println("Enter a correct response");
        stockingProcess(item);
        return;
    }
    System.out.println(habGameInv.toString());
    int numberAvail = 1;
    if (item.equals("food")){
        numberAvail = ((int)(habGameInv.getBudget() / 3.49));
    }
    if (item.equals("medicine")){
        numberAvail = ((int)(habGameInv.getBudget() / 20));
    }
    if (item.equals("supplies")){
        numberAvail = ((int)(habGameInv.getBudget()/45.99));
    }
    int numberToStock = ConsoleInput.readInt("How much do you want to stock? Enter a reasonable number in our budget. " , 1, numberAvail);
    habGameInv.stock(item, numberToStock);
    System.out.println(habGameInv.toString());

}
public void feed(Animal a){
    boolean hunger = zooHabGame.feedAnimal(a, habGameInv);
    if (!hunger){
        System.out.println("Let's go to the stocking area!");
        stockingProcess("food");
        feed(a);
    }
}

    
}
