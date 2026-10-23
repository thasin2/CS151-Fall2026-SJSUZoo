package zoo.game.animal;

import zoo.animal.*;
import zoo.inventory.Inventory;
import zoo.inventory.Item;
import zoo.staff.*;
import zoo.ui.*;

public class AnimalGame {
    private Inventory aniGameInv = new Inventory();
    private Veterinarian vetAniGame = new Veterinarian("1234", "John", 10000, true);
    private Zookeeper zooAniGame = new Zookeeper("1235", "Pam", 10000, true);

    public String nameAnimal() {
    System.out.println("Let's start by naming your Animal.");
    String name = ConsoleInput.readNonEmptyLine("What do you want to name your Animal? ");
    System.out.println("This is what we have: " + name);
    String value = ConsoleInput.readLine("Say yes or no if this is correct. ");
    if (value.equalsIgnoreCase("yes")){
        return name;
    }
    else {
        while (value.equalsIgnoreCase("no")) {
            name = ConsoleInput.readNonEmptyLine("What do you want to name your Animal? ");
            System.out.println("This is what we have: " + name);
            value = ConsoleInput.readLine("Say yes or no if this is correct. ");
        }
    }
    return name;
    }

    public Animal createAnimalObj(String name){
        System.out.println("What type of animal do you want?");
        System.out.println("1 - Aquatic");
        System.out.println("2 - Terrestrial");
        System.out.println("3 - Sky");
        int choice = ConsoleInput.readInt("Enter a number: ", 1, 3);
        if (choice == 1){
            System.out.println("Great! You chose Aquatic.");
            int depth = ConsoleInput.readInt("To what depth should this animal be able to swim? You can choose between 0 and 5000. ", 0, 5000);
            return new AquaticAnimals(name, depth);
        }
        if (choice == 2){
            System.out.println("Great! You chose Terrestrial.");
            String skin = ConsoleInput.readNonEmptyLine("What skin type should this animal have? ");
            return new TerrestrialAnimals(name, skin);
        }
        // choice is 3, since ConsoleInput only accepts 1 to 3
        System.out.println("Great! You chose Sky.");
        int height = ConsoleInput.readInt("What is the max height this animal can fly to? The range is between 0 and 5000. ", 0, 5000);
        return new SkyAnimal(name, height);
    }

    public void firstPart(Animal a){
        if (a.existInZoo()){
            System.out.println(a.getName() + " was just hanging around for a bit.");
        }
        else {
            this.healingAnimalProcess(a);
        }
    }

    public void healingAnimalProcess(Animal a){
        if (vetAniGame.fixAnimal(a, aniGameInv)){
            return;
        }
        else {
            System.out.println("Let's go to the stocking area!");
            stockingProcess(Item.MEDICINE);
            healingAnimalProcess(a);
        }
    }

public void stockingProcess(Item item){
    System.out.println("Welcome to the stocking area! Here is everything you have:");
    System.out.println(aniGameInv.toString());
    System.out.println("---------------------------------------------------");
    System.out.println("Each food piece costs $3.49.");
    System.out.println("Each medicine item costs $20.");
    String value = ConsoleInput.readLine("Do we have enough money to stock our inventory with what is needed? Say yes/no ");
    if (value.equalsIgnoreCase("no")){
            int amount = ConsoleInput.readInt("What amount should we add to our budget to purchase more inventory stock? Enter between 1 and 100. ", 1, 100);
            aniGameInv.addBudget(amount);
    }
    else if (value.equalsIgnoreCase("yes")){
        System.out.println("Good! Let's move on then.");
    }
    else {
        System.out.println("Enter a correct response");
        stockingProcess(item);
        return;
    }
    System.out.println(aniGameInv.toString());
    int numberAvail = 1;
    if (item == Item.FOOD){
        numberAvail = ((int)(aniGameInv.getBudget() / 3.49));
    }
    if (item == Item.MEDICINE){
        numberAvail = ((int)(aniGameInv.getBudget() / 20));
    }
    if (numberAvail < 1){
        System.out.println("There is not enough money to buy even one. Let's add more to the budget.");
        stockingProcess(item);
        return;
    }
    int numberToStock = ConsoleInput.readInt("How much do you want to stock? Enter a reasonable number in our budget. " , 1, numberAvail);
    aniGameInv.stock(item, numberToStock);

    System.out.println(aniGameInv.toString());

}
public void secondPart(Animal a){
    String value = ConsoleInput.readLine("Do you want to make the Animal do a trick? yes/no ");
    if (value.equalsIgnoreCase("yes")){
        if(a.doATrick()) {
            System.out.println("It seems we need to feed the animal.");
            feed(a);
        }
    }
    else {
        System.out.println("Let's let the animal just exist then.");
        firstPart(a);
    }
}

public void feed(Animal a){
    boolean hunger = zooAniGame.feedAnimal(a, aniGameInv);
    if (!hunger){
        System.out.println("Let's go to the stocking area!");
        stockingProcess(Item.FOOD);
        feed(a);
    }
}

}