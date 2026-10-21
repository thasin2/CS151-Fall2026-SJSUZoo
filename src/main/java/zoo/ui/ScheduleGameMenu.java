package zoo.ui;

public class ScheduleGameMenu {

    public void run() {
        System.out.println("Welcome to the Schedule Game Menu!");
        printInstructions();
        readyCheck();

        inventoryInterface();
        readyCheck();

        inventoryChoices();
        inventoryPlay();

        // Implement schedule game menu logic here
    }

    // Gives user instructions at the start
    private void printInstructions() {
        System.out.println("Instructions for the Schedule Game Menu:");
        System.out.println("You are in charge of planning a zoo schedule for three days (Monday to Wednesday).");
        System.out.println("Given information, you must decide on the best approach for each day.");
        System.out.println("First, you must make decisions on the zoo's inventory.");
        System.out.println("Are you ready?");
    }


    // Gives user information about Inventory to plan ahead
    private void inventoryInterface() {
        System.out.println("------------------------");
        System.out.println("Here is your information:");
        System.out.println("There are currently 20 animals.");
        System.out.println("10 are fed daily, 5 are fed bi-daily, 5 are fed weekly (starting Monday).");
        System.out.println("Each day, there's a 5% chance an animal gets sick. If an animal is sick without medicine, you fail.");
        System.out.println("Each day will have around 30 visitors. Around half look at animals, and half go to attractions.");
        System.out.println("A fresh attraction can handle at most 10 visitors before needing maintenance supplies.");
        System.out.println("If you do not reach the total quota of 35 visitors, you fail.");
        System.out.println("Your budget is $300.");

    }

    private void inventoryChoices() {
        System.out.println("Here are your options:");
        System.out.println("#1: Buy an item");
        System.out.println("#2: View current inventory & budget");
        System.out.println("#3: Finish inventory work");
    }

    private void inventoryPlay() {
        int choice1 = ConsoleInput.readInt("What would you like to do first?", 1, 3);

        switch (choice1) {
            case 1:
                System.out.println("What item would you like to buy?");
                // Continue here
                break;
            case 2:
                break;
            case 3:
                break;
        }

        int foodAmount = ConsoleInput.readInt("How much food would you like to buy (1-60):", 1, 60);
        int medicineAmount = ConsoleInput.readInt("How much medicine would you like to buy (1-10):", 1, 10);
    }

    private void readyCheck() {
        boolean ready = false;
        while (!ready) {
            ready = ConsoleInput.readYesNo("Are you ready?");
        }
    }

}
