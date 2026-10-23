package test;

import java.util.ArrayList;
import org.junit.*;
import zoo.Zoo;
import zoo.animal.*;
import zoo.exception.ZooFullException;
import zoo.habitat.*;


public class habitatTest {
    @Test
    public void testHabitat() {
    Zoo zoo = new Zoo("Thaneesha's Zoo Part 2", 2);
    

   
    ArrayList<Animal> testAnimalsList = new ArrayList<Animal> ();
    testAnimalsList.add(new Animal("Animal1", false));
    testAnimalsList.add(new Animal("Animal2", false));
    testAnimalsList.add(new Animal("Animal3", false));
    Habitat testHabitat2 = new Habitat("HabitatName", 5, testAnimalsList);

    Habitat testHabitat3 = new Habitat("HabitatName2", 5, new Animal("AnimalTest", false));

    // test habitatOpen(); it sets all Animals to Active and Hungry; we can not check the random event (will not yield same result every time)

    testHabitat2.habitatOpen();
    for (Animal x: testHabitat2.getAnimals()){
        Assert.assertEquals(x.isHungry(), true);
        Assert.assertEquals(x.isActive(), true);
    }
    testHabitat3.habitatOpen();
    for (Animal x: testHabitat3.getAnimals()){
        Assert.assertEquals(x.isHungry(), true);
        Assert.assertEquals(x.isActive(), true);
    }

    // check if habitatClose properly makes all animals inactive

    
    testHabitat2.habitatClose();
    for (Animal x: testHabitat2.getAnimals()){
        Assert.assertEquals(x.isActive(), false);
    }
    testHabitat3.habitatClose();
    for (Animal x: testHabitat3.getAnimals()){
        Assert.assertEquals(x.isActive(), false);
    }

    // check adding to Animal list in habitat (should only be of same type, and not exceed capacity listed)

    boolean x = true;
    int count = 1;

    x = true;
    count = 1;
    while (x){
        String name = "Animal" + count;
        count++;
        x = testHabitat2.addToAnimals(new TerrestrialAnimals(name, "Thick Fur"));
    }
    Assert.assertEquals(testHabitat2.getAnimals().size(), 5);


    x = true;
    count = 1;
    while (x){
        String name = "Animal" + count;
        count++;
        x = testHabitat3.addToAnimals(new SkyAnimal(name, 20));
    }
    Assert.assertEquals(testHabitat3.getAnimals().size(), 5);

    //We will test the 100 instances, will be similar to how Angelica implemented + tested it in the zooTest class

    for (int i = 0; i < Zoo.MAXIMUM_INSTANCES; i++) {
        String fillName = "Habitat" + i;
        zoo.registerHabitat(new Habitat(fillName, 5, new Animal()));
    }
        Assert.assertEquals(100, zoo.getRegisteredHabitats().size());
    try {
        zoo.registerHabitat(new Habitat("Habitat101", 5, new Animal()));
        Assert.fail("The 101st Habitat should be rejected");
    } catch (ZooFullException e) {
        Assert.assertEquals(100, zoo.getRegisteredHabitats().size());
    }
}
}
