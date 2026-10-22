package zoo.inventory;

public class Inventory {
    private int food;
    private int medicine;
    private int supplies;
    private double foodPrice = 3.49;
    private double medicinePrice = 20.00;
    private double suppliesPrice = 45.99;
    private double budget = 0;

    // Creates an empty inventory
    public Inventory() {
        this.food = 0;
        this.medicine = 0;
        this.supplies = 0;
    }

    // displays the inventory stats directly to user
    @Override
    public String toString() {
        return "Current inventory:\n" + "Food: " + food + "\nMedicine: " + medicine +
        "\nSupplies: " + supplies + "\nRemaining budget: $" + budget;
    }

    // stocks the inventory with X amount of an item
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

    // adds money to the Inventory's budget
    public void addBudget(double amount) {
        if (amount > 0) {
            budget += amount;
        }
        else {
            System.out.println("Must add a positive value to budget.");
        }
    }

    // One unit at a time, "uses" an item, returns false if cannot retrieve
    public boolean use(String item) {
        if (item.equalsIgnoreCase("food")) {
            if (food < 1) {
                System.out.println("Not enough food in inventory.");
                return false;
            }
            food--;
            return true;
        }
        else if (item.equalsIgnoreCase("medicine")) {
            if (medicine < 1) {
                System.out.println("Not enough medicine in inventory.");
                return false;
            }
            medicine--;
            return true;
        }
        else if (item.equalsIgnoreCase("supplies")) {
            if (supplies < 1) {
                System.out.println("Not enough supplies in inventory.");
                return false;
            }
            supplies--;
            return true;
        }
        else {
            System.out.println("Invalid item.");
            return false;
        }
    }
    
    // buys X amount of an item by taking inventory's budget
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

}
