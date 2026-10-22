package zoo.game.zoobuilder;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import zoo.Visitor;
import zoo.Zoo;
import zoo.animal.Animal;
import zoo.animal.AquaticAnimals;
import zoo.animal.SkyAnimal;
import zoo.animal.TerrestrialAnimals;
import zoo.attractions.Attraction;
import zoo.habitat.Habitat;
 
/* Zoo Builder game logic: the zoo the player is building
    every habitat, animal, attraction, and visitor is stored in the Zoo, which blocks duplicates
    and uses the shared Zoo.checkLimit for the 100 limit
    it never reads input; ZooBuilderMenu in the ui folder asks the player and calls these methods
    every rule that is broken throws an exception with a message, which the menu prints
 */
public class ZooBuilderGame {
    private final Random random = new Random();
    private final Zoo zoo;
 
    public ZooBuilderGame(String zooName, int visitorCapacity) {
        this.zoo = new Zoo(zooName, visitorCapacity); // the zoo checks the name and capacity
    }
 
    // ===== Adding things =====
 
    public void addHabitat(String name, int capacity) {
        zoo.registerHabitat(new Habitat(name, capacity, new ArrayList<Animal>())); // the zoo blocks duplicates and the 100 limit
    }
 
    // checks that the habitat has room, the name is new, and the zoo is under the limit,
    // before the player types in the rest
    public void checkCanAddAnimal(Habitat habitat, String name) {
        if (habitat.getAnimals().size() >= habitat.getCapacity()) {
            throw new IllegalStateException(habitat.getName() + " is full (" + habitat.getCapacity() + " animals).");
        }
        if (findAnimal(name) != null) {
            throw new IllegalArgumentException("There is already an animal called " + name + ".");
        }
        Zoo.checkLimit(zoo.getRegisteredAnimals(), "animal");
    }
 
    public void addLandAnimal(Habitat habitat, String name, String skinType) {
        addAnimal(habitat, name, new TerrestrialAnimals(name, skinType));
    }
 
    public void addWaterAnimal(Habitat habitat, String name, int depth) {
        addAnimal(habitat, name, new AquaticAnimals(name, depth));
    }
 
    public void addFlyingAnimal(Habitat habitat, String name, int maxHeight) {
        addAnimal(habitat, name, new SkyAnimal(name, maxHeight));
    }
 
    // the habitat checks the animal is the right kind; only then is it added to the zoo's records
    private void addAnimal(Habitat habitat, String name, Animal animal) {
        checkCanAddAnimal(habitat, name);
        if (habitat.addToAnimals(animal)) {
            zoo.registerAnimal(animal);
        }
    }
 
    public void addAttraction(String name, int capacity) {
        Attraction attraction = new Attraction(capacity);
        attraction.setName(name);
        zoo.addAttraction(attraction); // the zoo blocks duplicate names and the 100 limit
    }
 
    public Visitor registerVisitor(String name, int age, int money) {
        Visitor visitor = new Visitor(name, age, money);
        zoo.registerVisitor(visitor); // the zoo blocks duplicate names and the 100 limit
        return visitor;
    }
 
    // ===== Removing things =====
 
    public void removeHabitat(Habitat habitat) {
        zoo.unregisterHabitat(habitat); // its animals are removed from the zoo too
    }
 
    public Visitor removeVisitor(String name) {
        Visitor visitor = zoo.findVisitor(name);
        if (visitor == null) {
            throw new IllegalArgumentException("No visitor named " + name + ".");
        }
        zoo.unregisterVisitor(visitor);
        return visitor;
    }
 
    // ===== Opening =====
 
    // the zoo needs at least 1 animal, 1 attraction, and 1 visitor before it can open
    public boolean isReadyToOpen() {
        return countAnimals() > 0 && !zoo.getAttractions().isEmpty() && !zoo.getRegisteredVisitors().isEmpty();
    }
 
    // runs the zoo's first day: habitats open, visitors buy tickets and ride, then everyone goes home
    public void grandOpening() {
        if (!isReadyToOpen()) {
            throw new IllegalStateException("Not ready yet! You need at least 1 animal, 1 attraction, and 1 visitor.");
        }
        zoo.openForDay();
        for (Habitat habitat : zoo.getRegisteredHabitats()) {
            if (!habitat.getAnimals().isEmpty() && !habitat.habitatOpen()) {
                System.out.println("The staff quickly repairs " + habitat.getName() + ".");
                habitat.performMaintenance();
            }
        }
        zoo.admitGroup(new ArrayList<>(zoo.getRegisteredVisitors())); // everyone buys a ticket if they can afford it
        System.out.println(zoo.getVisitorCount() + " visitor(s) are exploring " + zoo.getName() + "!");
        zoo.sendVisitorsToAttractions(1.0, random); // everyone inside tries an attraction
        zoo.closeForTheDay(); // everyone goes home and the day's report prints
    }
 
    // ===== Looking things up =====
 
    public Habitat findHabitat(String name) {
        for (Habitat habitat : zoo.getRegisteredHabitats()) {
            if (habitat.getName().equalsIgnoreCase(name)) {
                return habitat;
            }
        }
        return null;
    }
 
    public Animal findAnimal(String name) {
        for (Animal animal : zoo.getRegisteredAnimals()) {
            if (animal.getName().equalsIgnoreCase(name)) {
                return animal;
            }
        }
        return null;
    }
 
    public int countAnimals() {
        return zoo.getRegisteredAnimals().size();
    }
 
    public List<Habitat> getHabitats() {
        return zoo.getRegisteredHabitats(); // read-only, so the menu cannot change the list directly
    }
 
    public Zoo getZoo() {
        return zoo;
    }
 
    @Override
    public String toString() {
        return "ZooBuilderGame{zoo='" + zoo.getName() + "', habitats=" + getHabitats().size() + ", animals=" + countAnimals() + "}";
    }
}