package Data.Enums;

public enum BookingStatus {
    Confirmed("confirmed"),
    Checked_in("checked_in"),
    Checked_out("checked_out"),
    Cancelled("cancelled");

    private  String value;

    BookingStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
