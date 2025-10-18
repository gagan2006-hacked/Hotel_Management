package Data.Insertion;

import java.time.LocalDateTime;

/*
| Field            | Type         | Description    |
| ---------------- | ------------ | -------------- |
| customer_id (PK) | INT          | Customer ID    |
| name             | VARCHAR(50)  | Customer name  |
| email            | VARCHAR(100) | Email          |
| phone            | VARCHAR(20)  | Contact number |
| address          | VARCHAR(255) | Address        |
| created_at       | DATETIME     | Created time   |
**/
public class Customer {
    private int customer_id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime created_at;

    public Customer(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
