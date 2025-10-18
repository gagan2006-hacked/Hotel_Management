package Data.Insertion;

import java.time.LocalDateTime;

/*
| Field         | Type         | Description                |
| ------------- | ------------ | -------------------------- |
| admin_id (PK) | INT          | Admin ID (only one record) |
| name          | VARCHAR(50)  | Admin name                 |
| email         | VARCHAR(100) | Admin login email          |
| password      | VARCHAR(255) | Hashed password            |
| created_at    | DATETIME     | Created time               |
| updated_at    | DATETIME     | Last update time           |
* */
public class Admin {
    private int admin;
    private String name;
    private String email;
    private String password;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Admin(int admin, String name, String email, String password, LocalDateTime created_at, LocalDateTime updated_at) {
        this.admin = admin;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
