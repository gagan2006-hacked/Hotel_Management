package Data.Insertion;

import Data.Enums.StaffRole;
import Data.Enums.StaffStatus;

import java.util.Date;

/*
| Field         | Type                                                | Description       |
| ------------- | --------------------------------------------------- | ----------------- |
| staff_id (PK) | INT                                                 | Staff unique ID   |
| name          | VARCHAR(50)                                         | Full name         |
| role          | ENUM('manager','receptionist','cleaner','security') | Job role          |
| salary        | DECIMAL(10,2)                                       | Monthly salary    |
| contact       | VARCHAR(50)                                         | Phone/email       |
| hire_date     | DATE                                                | Join date         |
| status        | ENUM('active','inactive')                           | Employment status |
* */
public class Staff {
    private int staff_id;
    private  String name;
    private StaffRole role;
    private double salary;
    private String contact;
    private Date hire_data;
    private StaffStatus status;

    public Staff(String name, StaffRole role, double salary, String contact, Date hire_data, StaffStatus status) {
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.contact = contact;
        this.hire_data = hire_data;
        this.status = status;
    }
}
