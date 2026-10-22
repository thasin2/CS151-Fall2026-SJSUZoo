package test;
import java.util.List;
import java.util.Random;
import org.junit.*;
import zoo.Maintainable;
import zoo.Visitor;
import zoo.Zoo;
import zoo.attractions.Attraction;
import zoo.exception.DuplicateEntryException;
import zoo.exception.ZooFullException;
import zoo.inventory.Inventory;
 
public class zooTest {
    private Zoo zoo;
    private Attraction ride;
 
    @Before
    public void setUp() {
        zoo = new Zoo("Test Zoo", 2);
        ride = new Attraction();
        ride.setName("Safari Train");
        zoo.addAttraction(ride);
    }
 
    private Visitor letIn(String name) {
        Visitor visitor = new Visitor(name, 20, 30);
        visitor.buyTicket(zoo);
        visitor.enterZoo(zoo);
        return visitor;
    }
 
    @Test
    public void maximumInstancesIs100() {
        for (int i = 0; i < Zoo.MAXIMUM_INSTANCES; i++) {
            zoo.registerVisitor(new Visitor("Guest " + i, 20));
        }
        Assert.assertEquals(100, zoo.getRegisteredVisitors().size());
        try {
            zoo.registerVisitor(new Visitor("One Too Many", 20));
            Assert.fail("The 101st visitor should be rejected");
        } catch (ZooFullException e) {
            Assert.assertEquals(100, zoo.getRegisteredVisitors().size());
        }
    }
 
    @Test(expected = DuplicateEntryException.class)
    public void duplicateVisitorsAreRejected() {
        zoo.registerVisitor(new Visitor("Sam", 20));
        zoo.registerVisitor(new Visitor("SAM", 20));
    }
 
    @Test(expected = DuplicateEntryException.class)
    public void duplicateAttractionsAreRejected() {
        Attraction copy = new Attraction();
        copy.setName("safari train");
        zoo.addAttraction(copy);
    }
 
    @Test(expected = IllegalStateException.class)
    public void cannotSellTicketsWhileClosed() {
        new Visitor("Sam", 20, 30).buyTicket(zoo);
    }
 
    @Test
    public void zooTurnsVisitorsAwayWhenFull() {
        zoo.openForDay();
        letIn("A");
        letIn("B");
        Assert.assertTrue(zoo.isFull());
        Visitor c = new Visitor("C", 20, 30);
        c.buyTicket(zoo);
        try {
            c.enterZoo(zoo);
            Assert.fail("The zoo is full, so C should be turned away");
        } catch (ZooFullException e) {
            Assert.assertFalse(c.isInsideZoo());
        }
    }
 
    @Test
    public void closingSendsEveryoneHomeAndClosesAttractions() {
        zoo.openForDay();
        Visitor a = letIn("A");
        letIn("B");
        a.visitAttraction(ride);
        zoo.closeForTheDay();
        Assert.assertEquals(0, zoo.getVisitorCount());
        Assert.assertEquals(0, ride.getCurrentVisitorCount());
        Assert.assertFalse(ride.isOpen());
        Assert.assertFalse(zoo.isOpen());
    }
 
    @Test
    public void attractionVisitsAreCountedPerDayAndInTotal() {
        zoo.openForDay();
        Visitor a = letIn("A");
        a.visitAttraction(ride);
        zoo.closeForTheDay();
        zoo.openForDay();
        Assert.assertEquals(0, zoo.getAttractionVisitsToday());
        Visitor b = letIn("B");
        b.visitAttraction(ride);
        Assert.assertEquals(1, zoo.getAttractionVisitsToday());
        Assert.assertEquals(2, zoo.getTotalAttractionVisits());
    }
 
    @Test
    public void refundGivesMoneyBackAndSendsVisitorHome() {
        zoo.openForDay();
        Visitor a = letIn("A");
        zoo.refundVisitor(a);
        Assert.assertEquals(30.00, a.getWallet(), 0.001);
        Assert.assertEquals(0.00, zoo.getTicketRevenueToday(), 0.001);
        Assert.assertFalse(a.isInsideZoo());
    }
 
    @Test
    public void generatedVisitorsAreUniqueAndRegistered() {
        Zoo big = new Zoo("Big Zoo", 100);
        List<Visitor> group = big.generateVisitors(30, new Random(1));
        Assert.assertEquals(30, group.size());
        Assert.assertEquals(30, big.getRegisteredVisitors().size());
        // asking again reuses the same 30 people instead of making new ones
        big.generateVisitors(30, new Random(2));
        Assert.assertEquals(30, big.getRegisteredVisitors().size());
    }
 
    @Test
    public void maintenanceUsesSuppliesAndFixesWornAttractions() {
        zoo.openForDay();
        for (int i = 0; i < 8; i++) { // each visit wears the ride down by 10
            Visitor v = new Visitor("Rider " + i, 20, 30);
            v.buyTicket(zoo);
            v.enterZoo(zoo);
            v.visitAttraction(ride);
            v.leaveZoo(zoo);
        }
        List<Maintainable> needsWork = zoo.findThingsNeedingMaintenance();
        Assert.assertEquals(1, needsWork.size());
 
        Inventory inventory = new Inventory();
        inventory.addBudget(100);
        inventory.stock("supplies", 2);
        Assert.assertEquals(1, zoo.performMaintenance(inventory, 2));
        Assert.assertEquals(100, ride.getCondition());
        Assert.assertTrue(zoo.findThingsNeedingMaintenance().isEmpty());
    }
}