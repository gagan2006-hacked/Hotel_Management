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

    public static StaffRole setValue(String value) {
        StaffRole role=null;
        if (value.equalsIgnoreCase("Manager")){
            role=StaffRole.Manager;
        } else if (value.equalsIgnoreCase("receptionist")) {
            role=StaffRole.Receptionist;
        }
        else if (value.equalsIgnoreCase("cleaner")){
            role=StaffRole.Cleaner;
        }
        return role;
    }
}
