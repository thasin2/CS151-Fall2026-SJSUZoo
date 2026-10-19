package zoo.game.manager;

// Random events that can happen at the start of a week in the manager game.
// Each event can happen at most once per week.
public enum ManagerEvent {
    ANIMAL_EMERGENCY,       // 1 or 2 sick animals: +1 vet shift each, and each one earns a vet the emergency bonus
    DANGEROUS_ANIMAL_WORK   // +3 zookeeper shifts with a dangerous animal, which earn hazard pay
}
