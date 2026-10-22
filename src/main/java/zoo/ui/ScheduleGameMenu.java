package zoo.ui;

import zoo.game.schedule.*;

public class ScheduleGameMenu {
    private boolean win = false;

    public void run() {
        System.out.println("Welcome to the Schedule Game Menu!");
        // Introduction
        printIntroduction();
        readyCheck();

        // Simulation information & instruction
        mainInterface();
        readyCheck();

        // Inventory Game (Sets up stock + Uses money)
        ScheduleGame sgame = new ScheduleGame();
        inventoryPlay(sgame);

        // Attraction Game (Sets up attraction)
        attractionsPlay(sgame);


        // Runs the three day simulation, stopping after any failed day
        if (runDayOne(sgame)) {
            System.out.println("------------------------");
            System.out.println("Congrats on finishing the first day!");
            if (runDayTwo(sgame)) {
                System.out.println("------------------------");
                System.out.println("Congrats on finishing the second day!");
                win = runDayThree(sgame);
            }
        }

        if (win) {
            System.out.println("------------------------");
            System.out.println("Congrats on winning the game!");
        }
        System.out.println("------------------------");
    }

    // Introduces user to the start of the simulation
    private void printIntroduction() {
        System.out.println("------------------------");
        System.out.println("Instructions for the Schedule Game Menu:");
        System.out.println("\nYou are in charge of planning a zoo schedule for three days (Monday to Wednesday).");
        System.out.println("Given information, you must decide on the best approach for each day.");
        System.out.println("For this simulation, you are responsible for managing the Zoo's inventory and attraction");
        System.out.println("First, you must make decisions on the zoo's inventory.");
    }


    // Gives user information about the Simulation to plan ahead
    // TBD: Can add more information or change it as we go
    private void mainInterface() {
        System.out.println("------------------------");
        System.out.println("Here is your information:");
        System.out.println("1. There are currently 20 animals.");
        System.out.println("2. 10 are fed daily, 5 are fed bi-daily, 5 are fed weekly (starting Monday).");
        System.out.println("3. Each day, an animal has a 5% chance to get sick.");
        System.out.println("4. Each day will have 30 visitors. About half go to attractions.");
        System.out.println("5. A fresh attraction can handle at most 20 visitors before needing maintenance supplies.");
        System.out.println("6. There's only a 50% chance a visitor will end up entering a full attraction.");
        System.out.println("7. Your starting budget is $300.");
        System.out.println("\nRule #1: If you do not reach the total quota of 30 attraction visitors, you fail.");
        System.out.println("Rule #2: If an animal is sick without medicine, you fail.");
        System.out.println("Rule #3: If too many animals go hungry, you fail.");
    }

    // Main choices for Inventory Interface
    private void inventoryChoices() {
        System.out.println("------------------------");
        System.out.println("Here are your options:");
        System.out.println("#1: Buy an item");
        System.out.println("#2: View current inventory & budget");
        System.out.println("#3: Finish inventory work (You cannot alter the inventory after this)");
    }

    // Main gameplay loop for Inventory Set Up
    private void inventoryPlay(ScheduleGame sgame) {
        int ichoice = 0;

        while (ichoice != 3) {
            inventoryChoices();
            ichoice = ConsoleInput.readInt("\nPick a number from 1-3: ", 1, 3);
            if (ichoice == 1) {
                inventoryBuy(sgame);
            }
            else  if (ichoice == 2) {
                System.out.println("------------------------");
                System.out.println(sgame.getInventory());
            }
        }
        System.out.println("------------------------");
        System.out.println("Congrats on finishing the inventory portion!");
    }

    // Main choices for Attraction interface
    private void attractionChoices() {
        System.out.println("\nHere are your choices:");
        System.out.println("#1: Create your attraction");
        System.out.println("#2: View attraction");
        System.out.println("#3: Finish attraction work (You cannot alter the attraction after this)");
    }

    // Main gameplay loop for Attraction set-Up
    private void attractionsPlay(ScheduleGame sgame) {
        int achoice = 0;
        System.out.println("------------------------");
        System.out.println("Welcome to the attraction portion of the game! Set up the zoo's attraction");

        while (achoice != 3) {
            attractionChoices();
            achoice = ConsoleInput.readInt("\nPick a number from 1-3 ", 1, 3);
            System.out.println("------------------------");
            if (achoice == 1) {
                createAttraction(sgame);
            }
            else if (achoice == 2) {
                sgame.displayAttraction();
            }
            else if (achoice == 3) {
                if (sgame.getAttraction() == null) {
                    System.out.println("Cannot leave without creating an attraction. Try again");
                    achoice = 1;
                }
            }
        }
        System.out.println("Congrats on finishing the attraction portion!");
    }

    // Runs the first day
    private boolean runDayOne(ScheduleGame sgame) {
        System.out.println("------------------------");
        System.out.println("Welcome to the first day!");
        System.out.println("The day will run its duration.");
        readyCheck();
        sgame.dayCycle(1);
        endDayAction(sgame);

        return sgame.failCheck();
    }

    // Runs the second day
    private boolean runDayTwo(ScheduleGame sgame) {
        System.out.println("------------------------");
        System.out.println("Welcome to the second day!");
        readyCheck();
        sgame.dayCycle(2);
        endDayAction(sgame);

        return sgame.failCheck();
    }

    // Runs the third day
    private boolean runDayThree(ScheduleGame sgame) {
        System.out.println("------------------------");
        System.out.println("Welcome to the final day!");
        readyCheck();
        sgame.dayCycle(3);
        endDayAction(sgame);

        return sgame.finalFailCheck();
    }

    // Choices given at the end of each day cycle
    private void endDayChoices() {
        System.out.println("------------------------");
        System.out.println("The day has finished. Here are your options.");
        System.out.println("#1: View inventory");
        System.out.println("#2: View attraction");
        System.out.println("#3: View condition and perform maintenance if needed");
        System.out.println("#4: Finish the day");
    }

    // What each end of day choice will do
    private void endDayAction(ScheduleGame sgame) {
        int endAction = 0;
        while (endAction != 4) {
            endDayChoices();
            endAction = ConsoleInput.readInt("\nWhat would you like to do? ", 1, 4);
            if (endAction == 1) {
                System.out.println("------------------------");
                System.out.println(sgame.getInventory());
            }
            else if (endAction ==2 ) {
                System.out.println("------------------------");
                sgame.displayAttraction();
            }
            else if (endAction == 3) {
                checkMaintenance(sgame);
            }
        }
    }

    // Allows the user to view condition and perform maintenance if needed
    private void checkMaintenance(ScheduleGame sgame) {
        System.out.println("------------------------");
        System.out.println("The condition of your attraction is: " + sgame.getAttraction().getCondition());
        if (sgame.getAttraction().needsMaintenance()) {
            System.out.println("The attraction is in poor condition and needs maintenance soon");
        }
        boolean answer = ConsoleInput.readYesNo("Would you like to perform maintenance? enter y or n ");
        if (answer) {
            sgame.maintenanceWork();
        }
    }

    // Allows the user to buy items into the inventory
    private void inventoryBuy(ScheduleGame sgame) {
        System.out.println("------------------------");
        System.out.println("What item would you like to buy?");
        System.out.println("#1: Food (3.49 per unit)");
        System.out.println("#2: Medicine (20.00 per unit)");
        System.out.println("#3: Supplies (45.99 per unit)");
        int buyChoice = ConsoleInput.readInt("Pick a number from 1-3: ", 1, 3);
        int amount = ConsoleInput.readInt("How much? (1 to 15 units per purchase) ", 1, 15);
        switch (buyChoice) {
            case 1:
                sgame.getInventory().stock("food", amount);
                break;
            case 2:
                sgame.getInventory().stock("medicine", amount);
                break;
            case 3:
                sgame.getInventory().stock("supplies", amount);
                break;
        }
    }
    
    // Method that allows the user to create an Attraction
    private void createAttraction(ScheduleGame sgame) {
        String name = ConsoleInput.readNonEmptyLine("What would you like to name your attraction? ");
        System.out.println("\nTip: Balance allowing enough visitors per day without over-working the attraction");
        int capacity = ConsoleInput.readInt("Would you like default attraction capacity (Enter 0) or custom (Pick 1-10): ", 0, 10);
        sgame.setUpAttraction(name, capacity);
    }

    // Asks the user if they're ready (A Pause to get user input)
    private void readyCheck() {
        boolean ready = false;
        while (!ready) {
            ready = ConsoleInput.readYesNo("\nAre you ready? (enter y or n) ");
        }
    }

}
