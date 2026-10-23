package zoo.ui;
 
import java.util.List;
import zoo.Visitor;
import zoo.Zoo;
import zoo.animal.Animal;
import zoo.attractions.Attraction;
import zoo.game.zoobuilder.ZooBuilderGame;
import zoo.habitat.Habitat;
 
// Text menus for the Zoo Builder game; all the rules are in ZooBuilderGame
public class ZooBuilderMenu {
    private ZooBuilderGame game;
 
    public void run() {
        System.out.println("=== Zoo Builder ===");
        System.out.println("Build your own zoo, then open it to visitors!");
        String zooName = ConsoleInput.readNonEmptyLine("What do you want to name your zoo? ");
        int capacity = ConsoleInput.readInt("How many visitors can be inside at once (1-50)? ", 1, 50);
        game = new ZooBuilderGame(zooName, capacity);
 
        while (true) {
            Zoo zoo = game.getZoo();
            System.out.println("------------------------");
            System.out.println(zoo.getName() + ": " + game.getHabitats().size() + " habitat(s), "
                    + game.countAnimals() + " animal(s), " + zoo.getAttractions().size() + " attraction(s), "
                    + zoo.getRegisteredVisitors().size() + " visitor(s)");
            System.out.println("1. Add a habitat");
            System.out.println("2. Add an animal");
            System.out.println("3. Add an attraction");
            System.out.println("4. Register a visitor");
            System.out.println("5. Remove a habitat");
            System.out.println("6. Remove a visitor");
            System.out.println("7. Look at my zoo");
            System.out.println("8. Grand opening!");
            int choice = ConsoleInput.readInt("Choose (1-8): ", 1, 8);
            try {
                if (choice == 1) {
                    addHabitat();
                } else if (choice == 2) {
                    addAnimal();
                } else if (choice == 3) {
                    addAttraction();
                } else if (choice == 4) {
                    registerVisitor();
                } else if (choice == 5) {
                    removeHabitat();
                } else if (choice == 6) {
                    removeVisitor();
                } else if (choice == 7) {
                    showZoo();
                } else {
                    System.out.println("=== Grand opening of " + zoo.getName() + "! ===");
                    game.grandOpening();
                    System.out.printf("Your zoo's first day is done. You made $%.2f in ticket sales. Great job building it!%n",
                            zoo.getTicketRevenueToday());
                    return;
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                // duplicates, the 100 limit, and other mistakes show a message instead of crashing
                System.out.println("Could not do that: " + e.getMessage());
            }
        }
    }
 
    private void addHabitat() {
        String name = ConsoleInput.readNonEmptyLine("Habitat name (for example Savannah or Penguin Cove): ");
        if (game.findHabitat(name) != null) {
            throw new IllegalArgumentException("There is already a habitat called " + name + ".");
        }
        int capacity = ConsoleInput.readInt("How many animals can it hold (1-10)? ", 1, 10);
        game.addHabitat(name, capacity);
        System.out.println("Built the " + name + " habitat!");
    }
 
    private void addAnimal() {
        Habitat habitat = pickHabitat();
        String name = ConsoleInput.readNonEmptyLine("Animal name: ");
        game.checkCanAddAnimal(habitat, name); // stops here if the habitat is full or the name is taken
        System.out.println("1. Land animal  2. Water animal  3. Flying animal");
        int type = ConsoleInput.readInt("What kind (1-3)? ", 1, 3);
        if (type == 1) {
            game.addLandAnimal(habitat, name, ConsoleInput.readNonEmptyLine("Skin type (for example fur or scales): "));
        } else if (type == 2) {
            game.addWaterAnimal(habitat, name, ConsoleInput.readInt("How deep does it swim, in feet (0-5000)? ", 0, 5000));
        } else {
            game.addFlyingAnimal(habitat, name, ConsoleInput.readInt("How high does it fly, in feet (0-5000)? ", 0, 5000));
        }
    }
 
    private void addAttraction() {
        String name = ConsoleInput.readNonEmptyLine("Attraction name (for example Safari Train): ");
        int capacity = ConsoleInput.readInt("How many riders at once (1-10)? ", 1, 10);
        game.addAttraction(name, capacity);
        System.out.println("Built the " + name + "!");
    }
 
    private void registerVisitor() {
        String name = ConsoleInput.readNonEmptyLine("Visitor name: ");
        int age = ConsoleInput.readInt("Age (0-120): ", 0, 120);
        int money = ConsoleInput.readInt("How much money do they bring ($0-$100)? ", 0, 100);
        Visitor visitor = game.registerVisitor(name, age, money);
        System.out.printf("Registered %s. Their %s ticket will cost $%.2f, and they have $%.2f.%n",
                visitor.getName(), visitor.getTicketType(), visitor.getTicketType().getPrice(), visitor.getWallet());
    }
 
    private void removeHabitat() {
        Habitat habitat = pickHabitat();
        game.removeHabitat(habitat);
        System.out.println("Removed " + habitat.getName() + " and its " + habitat.getAnimals().size() + " animal(s).");
    }
 
    private void removeVisitor() {
        String name = ConsoleInput.readNonEmptyLine("Which visitor? ");
        Visitor visitor = game.removeVisitor(name);
        System.out.println("Removed " + visitor.getName() + ".");
    }
 
    private void showZoo() {
        System.out.println(game.getZoo().getName() + " is " + (game.getZoo().isOpen() ? "open" : "not open yet") + ".");
        System.out.println("Habitats:");
        for (Habitat habitat : game.getHabitats()) {
            System.out.println("  " + habitat.getName() + " (" + habitat.getAnimals().size() + "/" + habitat.getCapacity() + ")");
            for (Animal animal : habitat.getAnimals()) {
                System.out.println("     - " + animal.getName() + " (" + animal.getClass().getSimpleName() + ")");
            }
        }
        System.out.println("Attractions:");
        for (Attraction attraction : game.getZoo().getAttractions()) {
            System.out.println("  " + attraction.getName());
        }
        System.out.println("Visitors:");
        for (Visitor visitor : game.getZoo().getRegisteredVisitors()) {
            System.out.println("  " + visitor);
        }
    }
 
    // lists the habitats and asks the player to pick one
    private Habitat pickHabitat() {
        List<Habitat> habitats = game.getHabitats();
        if (habitats.isEmpty()) {
            throw new IllegalStateException("Add a habitat first.");
        }
        for (int i = 0; i < habitats.size(); i++) {
            System.out.println((i + 1) + ". " + habitats.get(i).getName());
        }
        return habitats.get(ConsoleInput.readInt("Which habitat (1-" + habitats.size() + ")? ", 1, habitats.size()) - 1);
    }
}