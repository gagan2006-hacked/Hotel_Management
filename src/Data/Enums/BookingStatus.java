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

    public static BookingStatus setValue(String value) {
        BookingStatus status=null;
        if (value.equalsIgnoreCase("confirmed")){
            status=BookingStatus.Confirmed;
        } else if (value.equalsIgnoreCase("checked_in")) {
            status=BookingStatus.Checked_in;
        } else if (value.equalsIgnoreCase("checked_out")) {
            status=BookingStatus.Checked_out;
        }else if (value.equalsIgnoreCase("cancelled")){
            status=BookingStatus.Cancelled;
        }
        return status;
    }
}
