package test;
import org.junit.*;
import zoo.attractions.*;
import zoo.Visitor;
import zoo.inventory.*;

public class attractionsTest {
    @Test 
    public void testAttractions() {
        // Test code

        // Creating fake visitors
        Visitor visitor1 = new Visitor("John", 25);
        Visitor visitor2 = new Visitor("Alice", 30);
        Visitor visitor3 = new Visitor("Bob", 50);
        Visitor visitor4 = new Visitor("Eve", 45);
        Visitor visitor5 = new Visitor("Charlie", 18);
        Visitor visitor6 = new Visitor("David",32);
        Visitor visitor7 = new Visitor("Grace", 16);

        // Testing closed & open properties
        Attraction joyRide = new Attraction();
        Assert.assertEquals(joyRide.isOpen(), true);
        joyRide.close();
        Assert.assertEquals(joyRide.isOpen(), false);
        joyRide.admit(visitor1); // Should not admit John
        Assert.assertEquals(joyRide.getCurrentVisitorCount(), 0);
        System.out.println(joyRide);

        // Testing admits & full capacity
        joyRide.open();
        joyRide.admit(visitor1);
        joyRide.admit(visitor2);
        joyRide.admit(visitor3);
        joyRide.admit(visitor4);
        joyRide.admit(visitor5);
        Assert.assertEquals(joyRide.isFull(), true);
        Assert.assertEquals(joyRide.admit(visitor6), false);
        joyRide.remove(visitor3);
        Assert.assertEquals(joyRide.admit(visitor7), true); // should return true now that a visitor has left
        joyRide.setName("Joyride");
        System.out.println(joyRide);
        Assert.assertEquals(joyRide.getCurrentVisitorCount(), 5);
        joyRide.close(); // Should remove all visitors and close attraction
        Assert.assertEquals(joyRide.getCurrentVisitors(), "None");
        Assert.assertEquals(joyRide.getTotalVisited(), 6);
        Assert.assertEquals(joyRide.getCondition(), 70);

        // Should wear down the attraction
        for (int i = 0; i < 3; i++) {
            joyRide.open();
            joyRide.admit(visitor1);
            joyRide.admit(visitor2);
            joyRide.admit(visitor3);
            joyRide.admit(visitor4);
            joyRide.admit(visitor6);
            joyRide.close();
        }

        Assert.assertEquals(joyRide.getCondition(), 0);
        joyRide.open();
        Assert.assertEquals(joyRide.isOpen(), false); // shouldn't open as below condition

        // Repairs the attraction
        Inventory inventory = new Inventory();
        inventory.addBudget(50);
        inventory.stock("supplies", 1);
        if (inventory.use("supplies")) {
            joyRide.performMaintenance();
        }
        Assert.assertEquals(joyRide.getCondition(), 100);

        // Final attraction tests
        joyRide.open();
        joyRide.admit(visitor1);
        Assert.assertEquals(joyRide.getCurrentVisitor(), visitor1);
        joyRide.remove(visitor1);
        Assert.assertEquals(joyRide.getCurrentVisitors(), "None");
        joyRide.close();
        Assert.assertEquals(joyRide.getTotalVisited(), 21);
    }
}
