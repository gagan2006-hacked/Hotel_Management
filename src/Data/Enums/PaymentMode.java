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

    public static PaymentMode setValue(String value) {
        PaymentMode mode=null;
        if (value.equalsIgnoreCase("cash")){
            mode=PaymentMode.Cash;
        } else if (value.equalsIgnoreCase("card")) {
            mode=PaymentMode.Card;
        } else if (value.equalsIgnoreCase("upi")) {
            mode=PaymentMode.Upi;
        } else if (value.equalsIgnoreCase("netbanking")) {
            mode=PaymentMode.Netbanking;
        }
        return mode;
    }
}
