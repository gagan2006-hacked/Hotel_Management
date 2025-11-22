package Data.Enums;

public enum StaffStatus {
    Active("active"),InActive("inactive");
    private String value;

    StaffStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static StaffStatus setValue(String value) {
        StaffStatus status=null;
        if (value.equalsIgnoreCase("active")){
            status=StaffStatus.Active;
        } else if (value.equalsIgnoreCase("inactive")) {
            status=StaffStatus.InActive;
        }
        return status;
    }
}
