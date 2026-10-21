package zoo.ui;

// Starts the program: the user picks a role to play. Typing EXIT at any prompt ends the program.
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the SJSU Zoo! Type EXIT at any prompt to quit.");
        try {
            while (true) {
                System.out.println();
                System.out.println("Pick a role to play:");
                System.out.println("1. Manager");
                // Add 2nd option for a Zookeeper (uses visitors, attractions, zoo, inventory, animal/habitat?)
                // Add 3rd option for a Veterinarian (focuses on animal, inventory, habitat?)
                // Or have a 2nd option for a Zoo simulation
                ConsoleInput.readInt("Choose a role: ", 1, 1);
                new ManagerGameMenu().run();
            }
        } catch (ExitRequestedException e) {
            System.out.println("Goodbye!");
        }
    }
}
