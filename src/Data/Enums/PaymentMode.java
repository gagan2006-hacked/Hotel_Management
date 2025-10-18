package Data.Enums;

public enum PaymentMode {
    Cash("cash"),Card("card"),Upi("upi"),Netbanking("netbanking");
    private String value;

    PaymentMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
