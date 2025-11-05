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
    private int admin_id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    public Admin(){

    }
    public Admin(int admin, String name, String email, String password, LocalDateTime created_at, LocalDateTime updated_at) {
        this.admin_id = admin;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setAdmin_id(int admin) {
        this.admin_id = admin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
