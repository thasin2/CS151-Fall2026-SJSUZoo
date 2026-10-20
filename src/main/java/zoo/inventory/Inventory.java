package zoo.inventory;

public class Inventory {
    private int food;
    private int medicine;
    private int supplies;
    private double foodPrice;
    private double medicinePrice;
    private double suppliesPrice;

    // Creates an empty inventory
    public Inventory() {
        this.food = 0;
        this.medicine = 0;
        this.supplies = 0;
    }

    // Creates an inventory with given amounts of food, medicine, and supplies
    public Inventory(int food, int medicine, int supplies) {
        this.food = food;
        this.medicine = medicine;
        this.supplies = supplies;
    }

    // TBD: if stock and get methods should be combined or individual for each item
    public void stock (String item, int amount) {
        if (item.equalsIgnoreCase("food")) {
            buyFood(amount);
        }
        else if (item.equalsIgnoreCase("medicine")) {
            buyMedicine(amount);
        }
        else if (item.equalsIgnoreCase("supplies")) {
            buySupplies(amount);
        }
        // Could add something here that interacts with Zoo's total budget?
        else {
            System.out.println("Invalid item. Could not stock.");
        }
    }

    // TBD: Budget concept for inventory items
    private int budget = 0;
    public int addToBudget(int amount) {
        budget += amount;
        return budget;
    }
    private double totalCost = 0;
    public void buyFood(int amount) {
        totalCost = amount * foodPrice;
        if (totalCost > budget) {
            System.out.println("Not enough budget to buy food.");
        }
        else {
            budget -= totalCost;
            this.food += amount;
            System.out.println("Bought " + amount + " units of food for $" + totalCost);
        }
    }
    public void buyMedicine(int amount) {
        totalCost = amount * medicinePrice;
        if (totalCost > budget) {
            System.out.println("Not enough budget to buy medicine.");
        }
        else {
            budget -= totalCost;
            this.medicine += amount;
            System.out.println("Bought " + amount + " units of medicine for $" + totalCost);
        }
    }
    public void buySupplies(int amount) {
        totalCost = amount * suppliesPrice;
        if (totalCost > budget) {
            System.out.println("Not enough budget to buy supplies.");
        }
        else {
            budget -= totalCost;
            this.supplies += amount;
            System.out.println("Bought " + amount + " units of supplies for $" + totalCost);
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
            System.out.println("Invalid item. Could not set price.");
        }
    }

    // May change getFood, getMedicine, useSupplies to take no parameter if only one is used each time
    public int getFood(int amount) {
        if (amount > food) {
            System.out.println("Not enough food in inventory.");
            return 0;
        }
        food -= amount;
        return amount;
    }
    public int getMedicine(int amount) {
        if (amount > medicine) {
            System.out.println("Not enough medicine in inventory.");
            return 0;
        }
        medicine -= amount;
        return amount;
    }
    public int useSupplies(int amount) {
        if (amount > supplies) {
            System.out.println("Not enough supplies in inventory.");
            return 0;
        }
        supplies -= amount;
        return amount;
    }

    public void displayInventory() {
        System.out.println("Current Inventory:" + "\nFood: " + food + "\nMedicine: " + medicine + "\nSupplies: " + supplies);
    }

}
