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
}
