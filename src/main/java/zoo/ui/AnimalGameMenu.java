package zoo.ui;

import zoo.animal.*;
import zoo.game.animal.AnimalGame;

public class AnimalGameMenu { // This mainly just has the front end elements, all the real logic work is done in the AnimalGame class

        public void run () {

        System.out.println("Welcome! You chose to play the Animal game");
        System.out.println("----------------------------------------------------");
       
        

        System.out.println("In this game, you get to create an animal, then you can make it do tricks and observe it exist in its habitat. The animal will get tired, hungry, and maybe get injured. It is your job to take care of the animal and make it as ready as possible to be a perfect zoo animal!");

        System.out.println(".....................................................");

        AnimalGame game = new AnimalGame();
       
        String userAnimalName = game.nameAnimal();

        Animal userAnimal = game.createAnimalObj(userAnimalName);

        System.out.println("Your animal has been created! We just placed it in a habitat, let's see what it does to start off, and we can jump in if the animal suddenly needs our help.");

        System.out.println("=====================================================");

        game.firstPart(userAnimal);
        System.out.println("----------------------------------------------------");
        System.out.println(".....................................................");
        System.out.println("Cool! Now you know what it is like to observe an animal it it's habitat. You can make it do tricks now, but just a note - the animal is more likely to get injured once it does a trick. It can also get tired and become hungry.");

        System.out.println(".....................................................");
        game.secondPart(userAnimal);

        System.out.println("=====================================================");
        System.out.println("When your animal is tired, you might get multiple notifications! The main way to respond to this is by feeding the animal.");
        System.out.println("----------------------------------------------------");


        System.out.println("Now that you get the jist of the game, you can keep playing until you want to stop. To stop, just type EXIT.");
        System.out.println("----------------------------------------------------");

    

        while(game.secondPart(userAnimal)){
            System.out.println("----------------------------------------------------");
        }

        return;


    }
}
