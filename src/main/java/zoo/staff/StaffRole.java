package zoo.staff;

// The jobs a staff member can have. Each Staff subclass reports one of these from getRole().
public enum StaffRole {
    ZOOKEEPER("Zookeeper"),
    VETERINARIAN("Veterinarian"),
    MAINTENANCE("Maintenance"),
    MANAGER("Manager");

    private final String displayName;

    StaffRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
