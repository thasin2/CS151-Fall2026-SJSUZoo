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
        this.food = buyFood(food);
        this.medicine = buyMedicine(medicine);
        this.supplies = buySupplies(supplies);
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
    // assuming budget is stored in Inventory as of now
    public void setBudget(double amount) {
        budget = amount;
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
        double purchaseCost = amount * price;
        if (purchaseCost > budget) {
            System.out.println("Not enough budget to buy item.");
            return 0;
        }
        else {
            budget -= purchaseCost;
            System.out.println("Bought and stocked item for $" + purchaseCost);
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

    // May change use to take no int parameter if only one unit is used each time
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
        System.out.println("Current Inventory:");
        System.out.println("Food: " + food);
        System.out.println("Medicine: " + medicine);
        System.out.println("Supplies: " + supplies);
        System.out.println("Budget: $" + budget);
    }

}
