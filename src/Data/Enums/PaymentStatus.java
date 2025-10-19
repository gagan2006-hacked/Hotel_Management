package Data.Enums;

public enum PaymentStatus {
    Pending("pending"),Completed("completed"),Failed("failed");
    private String value;

    public String getValue() {
        return value;
    }

    PaymentStatus(String value) {
        this.value = value;
    }

    public static PaymentStatus setValue(String value) {
        PaymentStatus status=null;
        if (value.equalsIgnoreCase("pending")){
            status=PaymentStatus.Pending;
        } else if (value.equalsIgnoreCase("completed")) {
            status=PaymentStatus.Completed;
        } else if (value.equalsIgnoreCase("failed")) {
            status=PaymentStatus.Completed;
        }
        return status;
    }
}
