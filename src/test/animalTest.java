package test;

import org.junit.*;

import zoo.Zoo;
import zoo.animal.*;
import zoo.exception.ZooFullException;

public class animalTest {
    @Test
    public void testAnimal() {
        // Test code
        Zoo zoo = new Zoo("Thaneesha's Zoo", 2);


        //Creating animals for testing
        Animal snake = new TerrestrialAnimals("Snake", "Scales");
        //Animal bison = new TerrestrialAnimals("Bison");
        Animal giraffe = new TerrestrialAnimals("Giraffe", "Pattern fur");

        Animal seaLion = new AquaticAnimals("Sea Lion", 100);
        //Animal dolphin = new AquaticAnimals("Dolphin", 200);
        //Animal shark = new AquaticAnimals("Shark", 600);

        Animal blueJay = new SkyAnimal("Blue Jay", 400);
        Animal eagle = new SkyAnimal("Eagle", 600);
        Animal heron = new SkyAnimal("Heron", 300);

        // After doing a trick for each of these animals, it should set their trickCount to 1, isHungry value to true

        snake.doATrick();
        eagle.doATrick();
        seaLion.doATrick();
        seaLion.doATrick();

        
        Assert.assertEquals(snake.getTrickCount(), 1);
        Assert.assertEquals(eagle.getTrickCount(), 1);
        Assert.assertEquals(seaLion.getTrickCount(), 2);

        Assert.assertEquals(snake.isHungry(), true);
        Assert.assertEquals(eagle.isHungry(), true);
        Assert.assertEquals(seaLion.isHungry(), true);


        // Let's see if the tired method works as expected (2 or more tricks AND a hungry animal = tired, this will show with isActive being set to false, and tired() returning true)

        blueJay.doATrick(); // 3 tricks
        blueJay.doATrick();
        blueJay.doATrick();

        giraffe.doATrick(); // 1 trick
        
        //seaLion already did 2 tricks, so we can use that here as well

        Assert.assertEquals(blueJay.isActive(), false);
        Assert.assertEquals(giraffe.isActive(), true);
        Assert.assertEquals(heron.isActive(), true); // no tricks, should be active
        Assert.assertEquals(seaLion.isActive(), false);


        // Since existInZoo() will return different results each time (has random events), we can not reliably have a unit test for this, as it will not always return the same boolean value
        // We will test the 100 instances, will be similar to how Angelica implemented + tested it in the zooTest class

  
        for (int i = 0; i < Zoo.MAXIMUM_INSTANCES; i++) {
            String fillName = "Animal" + i;
            zoo.registerAnimal(new Animal(fillName));
        }
        Assert.assertEquals(100, zoo.getRegisteredAnimals().size());
        try {
            zoo.registerAnimal(new Animal("Animal101"));
            Assert.fail("The 101st animal should be rejected");
        } catch (ZooFullException e) {
            Assert.assertEquals(100, zoo.getRegisteredAnimals().size());
        }
    }
}
