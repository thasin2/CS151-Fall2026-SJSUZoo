package zoo.animal;

// dumb Anaimal class with just a flag to more accurately represent zoo keepers' pay
public class Animal {
    private final boolean dangerous;

    public Animal(boolean dangerous) {
        this.dangerous = dangerous;
    }

    public boolean isDangerous() {
        return dangerous;
    }
}
