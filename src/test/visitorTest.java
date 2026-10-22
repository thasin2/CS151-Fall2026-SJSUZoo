package test;
import org.junit.*;
import zoo.TicketType;
import zoo.Visitor;
import zoo.Zoo;
import zoo.attractions.Attraction;
import zoo.exception.InsufficientFundsException;
 
public class visitorTest {
    private Zoo zoo;
 
    @Before
    public void setUp() {
        zoo = new Zoo("Test Zoo", 10);
        zoo.openForDay();
    }
 
    @Test
    public void ticketTypeDependsOnAge() {
        Assert.assertEquals(TicketType.CHILD, new Visitor("Kid", 8).getTicketType());
        Assert.assertEquals(TicketType.ADULT, new Visitor("Adult", 30).getTicketType());
        Assert.assertEquals(TicketType.SENIOR, new Visitor("Grandpa", 70).getTicketType());
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void blankNameIsRejected() {
        new Visitor("   ", 20);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void negativeAgeIsRejected() {
        new Visitor("Sam", -1);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void cannotAddNegativeMoney() {
        new Visitor("Sam", 20).addFunds(-5);
    }
 
    @Test
    public void buyingATicketTakesMoneyAndRecordsTheSale() {
        Visitor sam = new Visitor("Sam", 20, 30);
        sam.buyTicket(zoo);
        Assert.assertTrue(sam.hasTicket());
        Assert.assertEquals(5.00, sam.getWallet(), 0.001);
        Assert.assertEquals(25.00, zoo.getTicketRevenueToday(), 0.001);
    }
 
    @Test(expected = InsufficientFundsException.class)
    public void cannotBuyATicketWithoutEnoughMoney() {
        new Visitor("Sam", 20, 10).buyTicket(zoo);
    }
 
    @Test(expected = IllegalStateException.class)
    public void cannotEnterWithoutATicket() {
        new Visitor("Sam", 20, 30).enterZoo(zoo);
    }
 
    @Test
    public void enteringAndLeavingTheZoo() {
        Visitor sam = new Visitor("Sam", 20, 30);
        sam.buyTicket(zoo);
        sam.enterZoo(zoo);
        Assert.assertTrue(sam.isInsideZoo());
        Assert.assertEquals(1, zoo.getVisitorCount());
        sam.leaveZoo(zoo);
        Assert.assertFalse(sam.isInsideZoo());
        Assert.assertFalse(sam.hasTicket()); // the ticket is used up
        Assert.assertEquals(0, zoo.getVisitorCount());
    }
 
    @Test
    public void visitingAndLeavingAnAttraction() {
        Attraction ride = new Attraction();
        ride.setName("Safari Train");
        zoo.addAttraction(ride);
        Visitor sam = new Visitor("Sam", 20, 30);
        sam.buyTicket(zoo);
        sam.enterZoo(zoo);
        Assert.assertTrue(sam.visitAttraction(ride));
        Assert.assertTrue(sam.isAtAttraction());
        Assert.assertEquals(1, ride.getCurrentVisitorCount());
        sam.leaveAttraction();
        Assert.assertFalse(sam.isAtAttraction());
        Assert.assertEquals(0, ride.getCurrentVisitorCount());
    }
 
    @Test
    public void leavingTheZooAlsoLeavesTheAttraction() {
        Attraction ride = new Attraction();
        ride.setName("Safari Train");
        zoo.addAttraction(ride);
        Visitor sam = new Visitor("Sam", 20, 30);
        sam.buyTicket(zoo);
        sam.enterZoo(zoo);
        sam.visitAttraction(ride);
        sam.leaveZoo(zoo);
        Assert.assertEquals(0, ride.getCurrentVisitorCount());
    }
 
    @Test
    public void visitorGetsUpsetAfterBeingTurnedAwayThreeTimes() {
        Attraction tiny = new Attraction(1);
        tiny.setName("Tiny Ride");
        zoo.addAttraction(tiny);
        Visitor first = new Visitor("First", 20, 30);
        Visitor sam = new Visitor("Sam", 20, 30);
        for (Visitor v : new Visitor[] {first, sam}) {
            v.buyTicket(zoo);
            v.enterZoo(zoo);
        }
        first.visitAttraction(tiny); // the ride is now full
        for (int i = 0; i < Visitor.MAX_PATIENCE; i++) {
            Assert.assertFalse(sam.isUpset());
            Assert.assertFalse(sam.visitAttraction(tiny));
        }
        Assert.assertTrue(sam.isUpset());
    }
 
    @Test
    public void visitorsWithTheSameNameAreEqual() {
        Assert.assertEquals(new Visitor("Sam", 20), new Visitor("sam", 45));
        Assert.assertNotEquals(new Visitor("Sam", 20), new Visitor("Sammy", 20));
    }
}
