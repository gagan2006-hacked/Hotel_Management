package Data.Enums;

public enum StaffRole {
    Manager("Manager"),Receptionist("receptionist"),Cleaner("cleaner");

    private String value;

   StaffRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
