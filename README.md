# CS151-Fall2026-SJSUZoo

## Initial Commits Contributions

Overview:
Our project simulates a working zoo simulation.
In the zoo, there will be visitors who can look at animals or visit attractions.
Additionally, there will be a variety of staff members and an Inventory.

Developers:
Thaneesha Singh: Animal, Habitat, Animal & Habitat game, and related specifics

Nam Vo: Inventory, Attraction, Schedule Game, and related specifics

Bao Tran: Staff, Manager game, and related specifics

Angelica Perez: Zoo, Visitor, Zoobuilder Game and related specifics

Design:

The Animal class is a superclass with subclasses that allow the formation of any Sky Animals, Acquatic Animals, and Terrestrial Animals. The subclasses have one additional attribute that helps to differentiate them (as well as altering method implementations). All the classes in this hierarchy share methods that allow the animals to do a trick, to become tired, and to simply exist in the zoo. There are specific implementations in the subclasses, that are particular to those animal "types". These methods can "at-chance" make the Animals inactive or hungry, which is used in the UI for the simulation of the Animal class. 

The attraction class is designed as a variable that Visitors can interact with through the zoo. Each attraction has a max capacity, can be opened and closed, and is able to keep track of both current visitors and total visitors for each day.

The Habitat class is a single class that allows the formation of a Habitat, that Animals can belong to (in the form of a ArrayList). In the class, you have functions to open and close the habitat, which can have at-random events of damage to the habitat's condition. There is also a method to add to the habitat, which has particular functionality of ensuring that only the same type of Animal (or the general Animal class) can be added to the habitat. Meaning, only Terrestrial Animals and Animals can coexist in a habitat, you can not have a terrestrial and aquatic animal together. The random damage to the habitat allows further usage cases for the UI simulation of the habitat in the game.

The inventory class manages the zoo's resources. In order for certain actions to be performed such as feeding animals, treating sick animals, and performing maintenance, the inventory must be stocked with a sufficient budget and item units.

The staff class is an abstract class extended by numerous Staff role subclasses, including Manager. Manager manages the team that will oversee the zoo, including add team member, remove team member, as well as handling their pay stub. Other staff roles include the Zookeeper, Veterinarian, and Maintenance staff. Each will have their own wage rules, as well the ability to work full time or part time. Zookeepers can feed animals, Vets can fix animal, and Maintenance staff can service any objects that implements Maintainable.

The Visitor class represents a person at the zoo, with their own wallet, ticket, and patience. Each visitor gets a ticket type from the TicketType enum based on their age, with separate prices for children, adults, and seniors. Visitors can buy tickets, enter and leave the zoo, and ride attractions, but they cannot enter without a ticket and lose patience when a full ride turns them away. 

The Zoo class holds every visitor, attraction, habitat, and animal in the zoo. It handles registering and removing these objects, blocking duplicates, and enforcing the 100-object limit through a shared checkLimit method that any class can use. The Zoo also runs the day, including opening, admitting groups of visitors, sending them to attractions, and closing with a daily report.

Errors such as a full zoo, a duplicate entry, or not enough money are handled by three custom exceptions: ZooFullException, DuplicateEntryException, and InsufficientFundsException. The Zoo Builder game ties these classes together by letting the user create their own zoo and hold a grand opening.

Both the attraction & habitat class implement the Maintainable interface, meaning they must be actively repaired after a certain amount of usage. Otherwise, they will not function as intended.

Installation Instructions:
#1 Clone the github repository
#2 Open the project on your preferred Java IDE
#3 Run Main.java

Usage:

The Animal simulation allows the user to make an animal do a Trick or to just exist in the Zoo. Either of these are subject to at-chance events of injury (existing in zoo)  or hunger (doing a trick). The Animal can also become tired. It is the user's job to help the veterinarian and zookeeper to help fix the animals, by budgeting and stocking for the inventory, and thus helping their animal to continue thriving. 

The Habitat simulation allows the user to make a habitat, and open/close it and add animals to the habitat. When the habitat is open, it is subject to possible damage,  which the User can go in and fix (by calling the zookeeper). When the habitat is closed, the user has to feed all the animals (which requires stocking from the inventory). In both of these cases the Animals are set to active or inactive (with a boolean value). Lastly, when adding an animal to the habitat, the program does a special check to ensure that the Animal is actually compatible (terrestrial with terrestrial, aquatic with aquatic, general animal objects go with anything).

Manager class, and some of the functionalities of other Staff classes, are used in Manager Game. The game simulates the resource management that a manager must perform in order to keep the zoo running smoothly. Holistically, the manager is allowed a budget and a weekly income, they must manage their staff team such that they do not go over budget until their tracking period is up. Managers may earn bonuses for the zoo if their schedule for that week is perfect, meaning it does not contains any overtime shifts or uncovered shifts. There are three levels within the game; the budget, weekly income, bonuses, as well as the tracking period adjust appropriately for each week. Budget and weekly income, as well as the man hour needed update dynamically each week. 

The schedule simulation

The zoobuilder simulation

Contributions:

Nam Vo - Implemented Attraction, Inventory, Item classes. Implemented Schedule Game Menu & Schedule Game classes.

Thaneesha Singh - Implemented Animal & Habitat classes. Implemented AquaticAnimals, SkyAnimal, and TerrestialAnimals subclasses. Implemented Animal 

Bao Tran - 

Angelica Perez - 


