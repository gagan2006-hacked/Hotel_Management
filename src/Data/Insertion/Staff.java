package Data.Insertion;

import Data.Enums.StaffRole;
import Data.Enums.StaffStatus;

import java.sql.Date;

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

    public Staff(int staff_id) {
        this.staff_id = staff_id;
    }

    public Staff(String name, StaffRole role, double salary, String contact, Date hire_data, StaffStatus status) {
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.contact = contact;
        this.hire_data = hire_data;
        this.status = status;
    }

    public Staff(int staff_id, String name, StaffRole role, double salary, String contact, Date hire_data, StaffStatus status) {
        this.staff_id = staff_id;
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.contact = contact;
        this.hire_data = hire_data;
        this.status = status;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public String getName() {
        return name;
    }

    public StaffRole getRole() {
        return role;
    }

    public double getSalary() {
        return salary;
    }

    public String getContact() {
        return contact;
    }

    public Date getHire_data() {
        return hire_data;
    }

    public StaffStatus getStatus() {
        return status;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setHire_data(Date hire_data) {
        this.hire_data = hire_data;
    }

    public void setStatus(StaffStatus status) {
        this.status = status;
    }
}
