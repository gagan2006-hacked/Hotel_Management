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
}
