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
        // should only return here once option 3 (finish) is selected, inventory portion is done & finalized

        attractionsPlay(sgame);


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


        // Continue here, implementing each day cycle
    }

    // Introduces user to the start of the simulation
    private void printIntroduction() {
        System.out.println("------------------------");
        System.out.println("Instructions for the Schedule Game Menu:");
        System.out.println("You are in charge of planning a zoo schedule for three days (Monday to Wednesday).");
        System.out.println("Given information, you must decide on the best approach for each day.");
        System.out.println("First, you must make decisions on the zoo's inventory.");
    }


    // Gives user information about the Simulation to plan ahead
    // TBD: Can add more information or change it as we go
    private void mainInterface() {
        System.out.println("------------------------");
        System.out.println("Here is your information:");
        System.out.println("There are currently 20 animals.");
        System.out.println("10 are fed daily, 5 are fed bi-daily, 5 are fed weekly (starting Monday).");
        System.out.println("Each day, an animal has a 5% chance to get sick.");
        System.out.println("Each day will have 30 visitors. Around half look at animals, and half go to attractions.");
        System.out.println("A fresh attraction can handle at most 20 visitors before needing maintenance supplies.");
        System.out.println("Your budget is $300.");
        System.out.println("Rule #1: If you do not reach the total quota of 35 attraction visitors, you fail.");
        System.out.println("Rule #2: If an animal is sick without medicine, you fail.");
        System.out.println("Rule #3: If too many animals go hungry, you fail.");

    }

    // Main choices for Inventory Interface
    private void inventoryChoices() {
        System.out.println("------------------------");
        System.out.println("Here are your options:");
        System.out.println("#1: Buy an item");
        System.out.println("#2: View current inventory & budget");
        System.out.println("#3: Finish inventory work");
    }

    // Main gameplay loop for Inventory Set Up
    private void inventoryPlay(ScheduleGame sgame) {
        int ichoice = 0;

        while (ichoice != 3) {
            inventoryChoices();
            ichoice = ConsoleInput.readInt("Pick a number from 1-3: ", 1, 3);
            if (ichoice == 1) {
                inventoryBuy(sgame);
            }
            else  if (ichoice == 2) {
                System.out.println("------------------------");
                sgame.inventory.displayInventory();
            }
        }
        System.out.println("------------------------");
        System.out.println("Congrats on finishing the inventory portion!");
        sgame.inventory.displayInventory();
    }

    // Main choices for Attraction interface
    private void attractionChoices() {
        System.out.println("------------------------");
        System.out.println("Here are your choices:");
        System.out.println("#1: Create your attraction");
        System.out.println("#2: View attraction");
        System.out.println("#3: Finish attraction work");
    }

    // Main gameplay loop for Attraction set-Up
    private void attractionsPlay(ScheduleGame sgame) {
        int achoice = 0;
        System.out.println("------------------------");
        System.out.println("Welcome to the attraction portion of the game! Set up the zoo's attraction");

        while (achoice != 3) {
            attractionChoices();
            achoice = ConsoleInput.readInt("Pick a number from 1-3 ", 1, 3);
            System.out.println("------------------------");
            if (achoice == 1) {
                createAttraction(sgame);
            }
            else if (achoice == 2) {
                sgame.displayAttraction();
            }
            else if (achoice == 3) {
                if (sgame.attraction == null) {
                    System.out.println("Cannot leave without creating an attraction. Try again");
                    achoice = 1;
                }
            }
        }
        System.out.println("------------------------");
        System.out.println("Congrats on finishing the attraction portion!");
        sgame.displayAttraction();
    }

    // Runs the first day
    private boolean runDayOne(ScheduleGame sgame) {
        System.out.println("------------------------");
        System.out.println("Welcome to the first day!");
        System.out.println("The day will run it's duration.");
        readyCheck();
        sgame.Day1();
        endDayAction(sgame);

        return sgame.failCheck();
    }

    // Runs the second day
    private boolean runDayTwo(ScheduleGame sgame) {
        System.out.println("------------------------");
        System.out.println("Welcome to the second day!");
        readyCheck();
        sgame.Day2();
        endDayAction(sgame);

        return sgame.failCheck();
    }

    // Runs the third day
    private boolean runDayThree(ScheduleGame sgame) {
        System.out.println("------------------------");
        System.out.println("Welcome to the final day!");
        readyCheck();
        sgame.Day3();
        endDayAction(sgame);

        return sgame.finalFailCheck();
    }

    /* Ideas for the end of each Day Cycle:
        Able to check inventory & attraction status.
        Able to check maintenance status for attraction.
        Able to perform maintenance
        Optional: Able to feed animals and treat animals (Maybe order staff to do these things)
        If doesn't repair any broken attractions by end of Day, game ends.
        */
    private void endDayChoices() {
        System.out.println("------------------------");
        System.out.println("The day has finished. Here are your options.");
        System.out.println("#1: View inventory");
        System.out.println("#2: View attraction");
        System.out.println("#3: View condition and perform maintenance if needed");
        // Need to add a 4th option for viewing animals and deciding to feed hungry / treat sick animals
        System.out.println("#4: Finish the day");
    }

    private void endDayAction(ScheduleGame sgame) {
        int endAction = 0;
        while (endAction != 4) {
            endDayChoices();
            endAction = ConsoleInput.readInt("What would you like to do? ", 1, 4);
            if (endAction == 1) {
                sgame.inventory.displayInventory();
            }
            else if (endAction ==2 ) {
                sgame.displayAttraction();
            }
            else if (endAction == 3) {
                checkMaintenance(sgame);
            }
        }
    }

    private void checkMaintenance(ScheduleGame sgame) {
        System.out.println("------------------------");
        System.out.println("The condition of your attraction is: " + sgame.attraction.getCondition());
        if (sgame.attraction.needsMaintenance()) {
            System.out.println("The attraction is in poor condition and needs maintenance soon");
        }
        boolean answer = ConsoleInput.readYesNo("Would you like to perform maintenance? enter y or n ");
        if (answer) {
            sgame.maintenanceWork();
        }
    }

    // Method for buying things in Inventory portion
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
                sgame.inventory.stock("food", amount);
                break;
            case 2:
                sgame.inventory.stock("medicine", amount);
                break;
            case 3:
                sgame.inventory.stock("supplies", amount);
                break;
        }
    }
    
    // Method for creating an Attraction in Attraction set-Up portion
    private void createAttraction(ScheduleGame sgame) {
        String name = ConsoleInput.readLine("What would you like to name your attraction? ");
        int capacity = ConsoleInput.readInt("Would you like default attraction capacity (Enter 0) or custom (Pick 1-10): ", 0, 10);
        sgame.setUpAttraction(name, capacity);
    }

    // Asks the user if they're ready (Pauses to get user input)
    private void readyCheck() {
        boolean ready = false;
        while (!ready) {
            ready = ConsoleInput.readYesNo("Are you ready? (enter y or n) ");
        }
    }

}
