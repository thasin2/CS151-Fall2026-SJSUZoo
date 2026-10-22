package zoo.ui;

// Starts the program: the user picks a role to play. Typing EXIT at any prompt ends the program.
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the SJSU Zoo! Type EXIT at any prompt to quit.");
        try {
            while (true) {
                System.out.println();
                System.out.println("Pick what you want to do:");
                System.out.println("1. Play as a Manager");
                System.out.println("2. Schedule the zoo for 3 days");
                System.out.println("3. Observe an Animal!");
                int choice = ConsoleInput.readInt("Choose a role: ", 1, 3); // will change this when my game is ready
                if (choice == 1) {
                    new ManagerGameMenu().run();
                } else if (choice == 2) {
                    new ScheduleGameMenu().run();
                } else if (choice == 3) {
                    new AnimalGameMenu().run();
                }
                System.out.println();
            }
        } catch (ExitRequestedException e) {
            System.out.println("Goodbye!");
        }
    }
}
