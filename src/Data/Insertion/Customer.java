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

    public Customer(int customer_id, String name, String email, String phone, String address, LocalDateTime created_at) {
        this.customer_id = customer_id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.created_at = created_at;
    }

    public Customer(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
}
