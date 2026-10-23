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
        inventory.stock(Item.FOOD, 10);
        inventory.stock(Item.MEDICINE, 3);
        inventory.stock(Item.SUPPLIES, 2); // shouldn't add, not enough money
        inventory.stock(Item.SUPPLIES, 1);
        System.out.println(inventory); // show inventory with stuff

        for (int i = 0; i < 10; ++i) {
            Assert.assertEquals(inventory.use(Item.FOOD), true);
        }

        Assert.assertEquals(inventory.use(Item.FOOD), false); // should run out of food
        inventory.use(Item.MEDICINE);
        System.out.println(inventory);

    }
}
