package zoo.inventory;

public class Inventory {
    private int food;
    private int medicine;
    private int supplies;

    // Creates an empty inventory
    public Inventory() {
        this.food = 0;
        this.medicine = 0;
        this.supplies = 0;
    }

    // Creates an inventory with given amounts of food, medicine, and supplies
    // Should tweak if continuing with Inventory budget concept
    public Inventory(int food, int medicine, int supplies) {
        this.food = food;
        this.medicine = medicine;
        this.supplies = supplies;
    }

    // TBD: if stock and get methods should be combined or individual for each item
    public void stock (String item, int amount) {
        if (item.equalsIgnoreCase("food")) {
            this.food += buyFood(amount);
        }
        else if (item.equalsIgnoreCase("medicine")) {
            this.medicine += buyMedicine(amount);
        }
        else if (item.equalsIgnoreCase("supplies")) {
            this.supplies += buySupplies(amount);
        }
        else {
            System.out.println("Invalid item. Could not stock.");
        }
    }

    // TBD: Budget concept for inventory items
    // Budget can either be taken from Zoo class, or managed in Inventory class itself
    private double foodPrice = 3.49;
    private double medicinePrice = 20.00;
    private double suppliesPrice = 9.99;
    private double budget = 0;
    private double totalCost;
    // assuming budget is stored in Inventory as of now
    public double addToBudget(double amount) {
        budget += amount;
        return budget;
    }
    private int buyFood(int amount) {
        return buyItem(amount, foodPrice);
    }
    private int buyMedicine(int amount) {
        return buyItem(amount, medicinePrice);
    }
    private int buySupplies(int amount) {
        return buyItem(amount, suppliesPrice);
    }
    private int buyItem(int amount, double price) {
        totalCost = amount * price;
        if (totalCost > budget) {
            System.out.println("Not enough budget to buy item.");
            return 0;
        }
        else {
            budget -= totalCost;
            System.out.println("Bought and stocked item for $" + totalCost);
            return amount;
        }
    }
    public void setPrice(String item, double price) {
        if (item.equalsIgnoreCase("food")) {
            this.foodPrice = price;
        }
        else if (item.equalsIgnoreCase("medicine")) {
            this.medicinePrice = price;
        }
        else if (item.equalsIgnoreCase("supplies")) {
            this.suppliesPrice = price;
        }
        else {
            System.out.println("Invalid item.");
        }
    }
    // End of budget concept

    // May change useFood, useMedicine, useSupplies to take no parameter if only one is used each time
    public boolean use(String item, int amount) {
        if (amount <= 0) {
            System.out.println("Cannot use negative amount.");
            return false;
        }
        if (item.equalsIgnoreCase("food")) {
            if (amount > food) {
                System.out.println("Not enough food in inventory.");
                return false;
            }
            food -= amount;
            return true;
        }
        else if (item.equalsIgnoreCase("medicine")) {
            if (amount > medicine) {
                System.out.println("Not enough medicine in inventory.");
                return false;
            }
            medicine -= amount;
            return true;
        }
        else if (item.equalsIgnoreCase("supplies")) {
            if (amount > supplies) {
                System.out.println("Not enough supplies in inventory.");
                return false;
            }
            supplies -= amount;
            return true;
        }
        else {
            System.out.println("Invalid item.");
            return false;
        }
    }
    
    public void displayInventory() {
        System.out.println("Current Inventory:" + "\nFood: " + food + "\nMedicine: " + medicine + "\nSupplies: " + supplies);
    }

}
