package test;
import org.junit.*;
import zoo.attractions.*;
import zoo.Visitor;

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

        // Testing closed & full properties
        Attraction joyRide = new Attraction();
        Assert.assertEquals(joyRide.isOpen(), true);
        joyRide.close();
        Assert.assertEquals(joyRide.isOpen(), false);
        joyRide.admit(visitor1); // Should not admit John
        Assert.assertEquals(joyRide.getCurrentVisitorCount(), 0);
        joyRide.status();
        joyRide.open();
        joyRide.admit(visitor1);
        joyRide.admit(visitor2);
        joyRide.admit(visitor3);
        joyRide.admit(visitor4);
        joyRide.admit(visitor5);
        Assert.assertEquals(joyRide.isFull(), true); // Should print "Attraction is full. Please wait for a visitor to leave."
        joyRide.admit(visitor6); // Should print "Attraction is full. Please wait for a visitor to leave."
        joyRide.leaving(visitor3);
        joyRide.admit(visitor7);
        joyRide.status();
        Assert.assertEquals(joyRide.getCurrentVisitorCount(), 5);
        joyRide.close(); // Should remove all visitors and close attraction

    }
}
