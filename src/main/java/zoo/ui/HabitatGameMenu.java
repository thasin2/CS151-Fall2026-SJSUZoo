package zoo.ui;

import zoo.game.habitat.HabitatGame;
import zoo.habitat.Habitat;

public class HabitatGameMenu {

    public void run () {
        

        System.out.println("Welcome! You chose to play the Habitat game");
        System.out.println("----------------------------------------------------");

        System.out.println("In this game, you can create and observe a habitat, then you can add animals to it and maintain the habitat when it gets damaged.");
        System.out.println(".....................................................");

        HabitatGame game = new HabitatGame();

        Habitat userHabitat = game.makeHabitat();
        System.out.println("----------------------------------------------------");

        System.out.println("You can maintain your habitat after opening it, close your habitat (which will also feed your animals), and you can add extra animals to your habitat (they have to be of the same type)!");

        game.firstPart(userHabitat);
        System.out.println("----------------------------------------------------");
        System.out.println(".....................................................");

        System.out.println("Now that you get the gist of the simulation, play on until you do not want to, by typing EXIT");

        while(true){
            System.out.println("----------------------------------------------------");
            game.secondPart(userHabitat);
        }



        



    }
    
}
