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
    holds the zoo and its habitats, and checks every rule (duplicates, full habitats, the 100 limit)
    it never reads input; ZooBuilderMenu in the ui folder asks the player and calls these methods
    every rule that is broken throws an exception with a message, which the menu prints
 */
public class ZooBuilderGame {
    private final Random random = new Random();
    private final ArrayList<Habitat> habitats = new ArrayList<>();
    private final Zoo zoo;

    public ZooBuilderGame(String zooName, int visitorCapacity) {
        this.zoo = new Zoo(zooName, visitorCapacity); // the zoo checks the name and capacity
    }

    // ===== Adding things =====

    public void addHabitat(String name, int capacity) {
        if (habitats.size() >= Zoo.MAXIMUM_INSTANCES) {
            throw new IllegalStateException("The zoo already has the maximum of " + Zoo.MAXIMUM_INSTANCES + " habitats.");
        }
        if (findHabitat(name) != null) {
            throw new IllegalArgumentException("There is already a habitat called " + name + ".");
        }
        habitats.add(new Habitat(name, capacity, new ArrayList<Animal>()));
    }

    // checks that the habitat has room and the name is new, before the player types in the rest
    public void checkCanAddAnimal(Habitat habitat, String name) {
        if (habitat.getAnimals().size() >= habitat.getCapacity()) {
            throw new IllegalStateException(habitat.getName() + " is full (" + habitat.getCapacity() + " animals).");
        }
        if (countAnimals() >= Zoo.MAXIMUM_INSTANCES) {
            throw new IllegalStateException("The zoo already has the maximum of " + Zoo.MAXIMUM_INSTANCES + " animals.");
        }
        if (findAnimal(name) != null) {
            throw new IllegalArgumentException("There is already an animal called " + name + ".");
        }
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

    private void addAnimal(Habitat habitat, String name, Animal animal) {
        checkCanAddAnimal(habitat, name);
        habitat.addToAnimals(animal); // the habitat only accepts animals of the same kind
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

    public Habitat removeHabitat(int index) {
        return habitats.remove(index);
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
        for (Habitat habitat : habitats) {
            if (!habitat.getAnimals().isEmpty() && !habitat.habitatOpen()) {
                System.out.println("The staff quickly repairs " + habitat.getName() + ".");
                habitat.performMaintenance();
            }
        }
        zoo.admitGroup(new ArrayList<>(zoo.getRegisteredVisitors())); // everyone buys a ticket if they can afford it
        zoo.sendVisitorsToAttractions(1.0, random); // everyone inside tries an attraction
        zoo.closeForTheDay(); // everyone goes home and the day's report prints
    }

    // ===== Looking things up =====

    public Habitat findHabitat(String name) {
        for (Habitat habitat : habitats) {
            if (habitat.getName().equalsIgnoreCase(name)) {
                return habitat;
            }
        }
        return null;
    }

    public Animal findAnimal(String name) {
        for (Habitat habitat : habitats) {
            for (Animal animal : habitat.getAnimals()) {
                if (animal.getName().equalsIgnoreCase(name)) {
                    return animal;
                }
            }
        }
        return null;
    }

    public int countAnimals() {
        int count = 0;
        for (Habitat habitat : habitats) {
            count += habitat.getAnimals().size();
        }
        return count;
    }

    public List<Habitat> getHabitats() {
        return new ArrayList<>(habitats); // a copy, so the menu cannot change the list directly
    }

    public Zoo getZoo() {
        return zoo;
    }
}