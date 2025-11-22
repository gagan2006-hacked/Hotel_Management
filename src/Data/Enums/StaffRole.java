package Data.Enums;

public enum StaffRole {
    Manager("Manager"),Receptionist("Receptionist"),Cleaner("Cleaner");

    private String value;

   StaffRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static StaffRole setValue(String value) {
        StaffRole role=null;
        if (value.equalsIgnoreCase("Manager")){
            role=StaffRole.Manager;
        } else if (value.equalsIgnoreCase("Receptionist")) {
            role=StaffRole.Receptionist;
        }
        else if (value.equalsIgnoreCase("Cleaner")){
            role=StaffRole.Cleaner;
        }
        return role;
    }
}
