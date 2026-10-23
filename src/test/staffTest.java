package test;
import java.util.ArrayList;
import org.junit.*;
import zoo.Maintainable;
import zoo.Zoo;
import zoo.animal.Animal;
import zoo.exception.InvalidPayException;
import zoo.exception.JobLimitException;
import zoo.exception.ShiftLimitException;
import zoo.exception.ZooFullException;
import zoo.staff.MaintenanceStaff;
import zoo.staff.Manager;
import zoo.staff.PayStub;
import zoo.staff.Staff;
import zoo.staff.Veterinarian;
import zoo.staff.Zookeeper;

public class staffTest {
    private static final double DELTA = 0.001;

    // a simple Maintainable that always needs maintenance, so jobs can be counted
    private static class BrokenFence implements Maintainable {
        private int condition = 10;

        public int getCondition() {
            return condition;
        }

        public boolean needsMaintenance() {
            return condition < 50;
        }

        public void performMaintenance() {
            condition = 10; // breaks again right away, so the next job also counts
        }
    }

    private static void workShifts(Staff staff, int shifts) {
        for (int i = 0; i < shifts; i++) {
            staff.workShift();
        }
    }

    // MAXIMUM_INSTANCES

    @Test
    public void maximumInstancesIs100IncludingTheManager() {
        Manager manager = new Manager("MGR", "Morgan", 52000, true);
        for (int i = 1; i < Zoo.MAXIMUM_INSTANCES; i++) {
            manager.addTeamMember(new Zookeeper("Z" + i, "Keeper " + i, 20, true));
        }
        Assert.assertEquals(99, manager.getTeamSize()); // 99 on the team + the manager = 100 staff
        try {
            manager.addTeamMember(new Zookeeper("Z100", "One Too Many", 20, true));
            Assert.fail("The 101st staff member should be rejected");
        } catch (ZooFullException e) {
            Assert.assertEquals(99, manager.getTeamSize());
        }
    }

    // duplicates and team rules

    @Test(expected = IllegalArgumentException.class)
    public void duplicateStaffIdsAreRejected() {
        Manager manager = new Manager("MGR", "Morgan", 52000, true);
        manager.addTeamMember(new Zookeeper("Z1", "Pat", 20, true));
        manager.addTeamMember(new Veterinarian("Z1", "Someone Else", 104000, true));
    }

    @Test
    public void staffWithTheSameIdAreEqual() {
        Assert.assertEquals(new Zookeeper("Z1", "Pat", 20, true), new Zookeeper("Z1", "Sam", 25, false));
        Assert.assertNotEquals(new Zookeeper("Z1", "Pat", 20, true), new Zookeeper("Z2", "Pat", 20, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void aManagerCannotBeOnATeam() {
        Manager manager = new Manager("MGR", "Morgan", 52000, true);
        manager.addTeamMember(new Manager("MGR2", "Riley", 52000, true));
    }

    // input checks

    @Test(expected = InvalidPayException.class)
    public void payMustBePositive() {
        new Zookeeper("Z1", "Pat", 0, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nameCannotBeBlank() {
        new Zookeeper("Z1", "   ", 20, true);
    }

    // shifts

    @Test(expected = ShiftLimitException.class)
    public void fullTimeStaffCannotWorkASeventhShift() {
        workShifts(new Zookeeper("Z1", "Pat", 20, true), 7);
    }

    @Test(expected = ShiftLimitException.class)
    public void partTimeStaffCannotWorkAFifthShift() {
        workShifts(new Zookeeper("Z1", "Pat", 20, false), 5);
    }

    @Test(expected = IllegalStateException.class)
    public void cannotCloseOutAWeekWithNoShifts() {
        new Zookeeper("Z1", "Pat", 20, true).closeOutWeek();
    }

    @Test
    public void closingOutTheWeekResetsShifts() {
        Zookeeper keeper = new Zookeeper("Z1", "Pat", 20, true);
        workShifts(keeper, 5);
        keeper.closeOutWeek();
        Assert.assertEquals(0, keeper.getShiftsThisWeek());
    }

    // zookeeper pay: hourly, with overtime and hazard pay

    @Test
    public void zookeeperRegularWeek() {
        Zookeeper keeper = new Zookeeper("Z1", "Pat", 20, true);
        workShifts(keeper, 5);
        Assert.assertEquals(800.0, keeper.closeOutWeek(), DELTA); // 40 hours * $20
    }

    @Test
    public void zookeeperOvertimeIsTimeAndAHalf() {
        Zookeeper keeper = new Zookeeper("Z1", "Pat", 20, true);
        workShifts(keeper, 6);
        Assert.assertEquals(1040.0, keeper.closeOutWeek(), DELTA); // 48 * $20 + 8 overtime hours * $10
    }

    @Test
    public void zookeeperGetsHazardPayOnlyForDangerousAnimals() {
        Zookeeper keeper = new Zookeeper("Z1", "Pat", 20, true);
        Animal lion = new Animal("Lion", true);
        Animal penguin = new Animal("Penguin", false);
        for (int i = 0; i < 3; i++) {
            keeper.workShiftWithAnimal(lion);
        }
        keeper.workShiftWithAnimal(penguin);
        keeper.workShiftWithAnimal(penguin);
        Assert.assertEquals(848.0, keeper.closeOutWeek(), DELTA); // $800 + 3 hazard shifts * $16
    }

    @Test
    public void hazardPayRaisesTheOvertimeRate() {
        Zookeeper keeper = new Zookeeper("Z1", "Pat", 20, true);
        Animal lion = new Animal("Lion", true);
        for (int i = 0; i < 6; i++) {
            keeper.workShiftWithAnimal(lion);
        }
        // straight time $960 + $96 hazard = $1056, so the average rate is $22 and overtime adds 8 * $11
        Assert.assertEquals(1144.0, keeper.closeOutWeek(), DELTA);
    }

    @Test
    public void hazardShiftsResetEachWeek() {
        Zookeeper keeper = new Zookeeper("Z1", "Pat", 20, true);
        Animal lion = new Animal("Lion", true);
        for (int i = 0; i < 5; i++) {
            keeper.workShiftWithAnimal(lion);
        }
        keeper.closeOutWeek();
        workShifts(keeper, 5);
        Assert.assertEquals(800.0, keeper.closeOutWeek(), DELTA);
    }

    // veterinarian pay: yearly salary, with an emergency bonus

    @Test
    public void vetGetsSalaryPlusEmergencyBonus() {
        Veterinarian vet = new Veterinarian("V1", "Dr. Kim", 104000, true);
        workShifts(vet, 5);
        vet.handleEmergency(new Animal("Elephant", true));
        vet.handleEmergency(new Animal("Red Panda", false));
        Assert.assertEquals(2400.0, vet.closeOutWeek(), DELTA); // $104,000 / 52 + 2 * $200
    }

    // manager pay: yearly salary, with a team bonus

    @Test
    public void managerGetsSalaryPlusTeamBonus() {
        Manager manager = new Manager("MGR", "Morgan", 52000, true);
        manager.addTeamMember(new Zookeeper("Z1", "Pat", 20, true));
        manager.addTeamMember(new Veterinarian("V1", "Dr. Kim", 104000, true));
        workShifts(manager, 5);
        Assert.assertEquals(1050.0, manager.closeOutWeek(), DELTA); // $52,000 / 52 + 2 * $25
    }

    @Test
    public void payrollPaysOnlyStaffWhoWorked() {
        Manager manager = new Manager("MGR", "Morgan", 52000, true);
        Zookeeper worker = new Zookeeper("Z1", "Pat", 20, true);
        manager.addTeamMember(worker);
        manager.addTeamMember(new Zookeeper("Z2", "Sam", 20, true)); // works no shifts
        workShifts(worker, 5);
        workShifts(manager, 5);
        ArrayList<PayStub> stubs = manager.runPayroll();
        Assert.assertEquals(2, stubs.size()); // the worker and the manager
        Assert.assertEquals(800.0, stubs.get(0).getAmount(), DELTA);
        Assert.assertEquals(1050.0, stubs.get(1).getAmount(), DELTA);
    }

    // maintenance staff: job limit and job bonus

    @Test
    public void maintenanceStaffGetJobBonus() {
        MaintenanceStaff fixer = new MaintenanceStaff("M1", "Max", 20, false);
        fixer.workShift();
        BrokenFence fence = new BrokenFence();
        for (int i = 0; i < 4; i++) {
            fixer.service(fence);
        }
        Assert.assertFalse(fixer.hasJobsLeft());
        Assert.assertEquals(220.0, fixer.closeOutWeek(), DELTA); // 8 hours * $20 + 4 jobs * $15
    }

    @Test(expected = JobLimitException.class)
    public void eachShiftAllowsOnlyFourJobs() {
        MaintenanceStaff fixer = new MaintenanceStaff("M1", "Max", 20, false);
        fixer.workShift();
        BrokenFence fence = new BrokenFence();
        for (int i = 0; i < 5; i++) {
            fixer.service(fence);
        }
    }

    @Test(expected = JobLimitException.class)
    public void noJobsWithoutAShift() {
        new MaintenanceStaff("M1", "Max", 20, false).service(new BrokenFence());
    }
}
