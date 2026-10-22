package test;
import org.junit.*;
import zoo.inventory.*;

public class inventoryTest {
    @Test 
    public void testInventory() {
        // Tests budget
        Inventory inventory = new Inventory();
        inventory.addBudget(150);
        inventory.addBudget(-1); // Shouldn't add
        int budget = (int) inventory.getBudget();
        Assert.assertEquals(budget, 150);

        System.out.println(inventory); // show empty inventory
        inventory.stock("food", 10);
        inventory.stock("medicine", 3);
        inventory.stock("supplies", 2); // shouldn't add, not enough money
        inventory.stock("supplies", 1);
        System.out.println(inventory); // show inventory with stuff

        for (int i = 0; i < 10; ++i) {
            Assert.assertEquals(inventory.use("food"), true);
        }

        Assert.assertEquals(inventory.use("food"), false); // should run out of food
        inventory.stock("money", 100); // should print invalid item
        inventory.use("medicine");
        System.out.println(inventory);

    }
}
