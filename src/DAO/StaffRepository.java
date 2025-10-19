package DAO;

import DBConnection.ConnectionMangement;
import Data.Enums.StaffRole;
import Data.Enums.StaffStatus;
import Data.Insertion.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
| Method                                          | Purpose                                 |
| ----------------------------------------------- | --------------------------------------- |
| `addStaff(Staff staff)`                         | Insert a new staff member               |
| `updateStaffSalary(int id, double salary)`      | Update salary                           |
| `updateStaffStatus(int id, StaffStatus status)` | Activate/deactivate staff               |
| `deleteStaff(int id)`                           | Remove a staff record                   |
| `getStaffById(int id)`                          | Retrieve one staff record               |
| `getAllActiveStaff()`                           | List all active staff                   |
| `getStaffByRole(StaffRole role)`                | Filter by role (manager, cleaner, etc.) |
| `getAllStaff()`                                 | Get all staff records                   |

* */
public class StaffRepository {
    private final ConnectionMangement management;

    public StaffRepository() {
        this.management = new ConnectionMangement();
    }

    // ==========================================================
    // 1️⃣ Add New Staff
    // ==========================================================
    public boolean addStaff(Staff staff) {
        try (Connection connection = management.formConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO staff (name, role, salary, contact, hire_date, status) VALUES (?, ?, ?, ?, ?, ?)"
            );
            statement.setString(1, staff.getName());
            statement.setString(2, staff.getRole().getValue());
            statement.setDouble(3, staff.getSalary());
            statement.setString(4, staff.getContact());

            java.sql.Date sqlDate = new java.sql.Date(staff.getHire_data().getTime());
            statement.setDate(5, sqlDate);
            statement.setString(6, staff.getStatus().getValue());

            int ack = statement.executeUpdate();
            if (ack == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 2️⃣ Update Staff Salary
    // ==========================================================
    public boolean updateStaffSalary(int staffId, double newSalary) {
        try (Connection connection = management.formConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE staff SET salary = ? WHERE staff_id = ?"
            );
            statement.setDouble(1, newSalary);
            statement.setInt(2, staffId);

            int ack = statement.executeUpdate();
            if (ack == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 3️⃣ Update Staff Status (active/inactive)
    // ==========================================================
    public boolean updateStaffStatus(int staffId, StaffStatus status) {
        try (Connection connection = management.formConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE staff SET status = ? WHERE staff_id = ?"
            );
            statement.setString(1, status.getValue());
            statement.setInt(2, staffId);

            int ack = statement.executeUpdate();
            if (ack == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 4️⃣ Delete Staff
    // ==========================================================
    public boolean deleteStaff(int staffId) {
        try (Connection connection = management.formConnection()) {
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM staff WHERE staff_id = ?"
            );
            statement.setInt(1, staffId);

            int ack = statement.executeUpdate();
            if (ack == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 5️⃣ Get Staff By ID
    // ==========================================================
    public Staff getStaffById(int staffId) {
        try (Connection connection = management.formConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM staff WHERE staff_id = ?"
            );
            statement.setInt(1, staffId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String roleStr = rs.getString("role");
                StaffRole role = StaffRole.setValue(roleStr);

                double salary = rs.getDouble("salary");
                String contact = rs.getString("contact");
                Date hireDate = rs.getDate("hire_date");

                String statusStr = rs.getString("status");
                StaffStatus status = StaffStatus.setValue(statusStr);

                return new Staff(staffId, name, role, salary, contact, hireDate, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==========================================================
    // 6️⃣ Get All Active Staff
    // ==========================================================
    public List<Staff> getAllActiveStaff() {
        List<Staff> staffList = new ArrayList<>();
        try (Connection connection = management.formConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM staff WHERE status = 'active'"
            );
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("staff_id");
                String name = rs.getString("name");
                String roleStr = rs.getString("role");
                StaffRole role = StaffRole.setValue(roleStr);
                double salary = rs.getDouble("salary");
                String contact = rs.getString("contact");
                Date hireDate = rs.getDate("hire_date");
                String statusStr = rs.getString("status");
                StaffStatus status = StaffStatus.setValue(statusStr);

                staffList.add(new Staff(id, name, role, salary, contact, hireDate, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    // ==========================================================
    // 7️⃣ Filter Staff by Role
    // ==========================================================
    public List<Staff> getStaffByRole(StaffRole role) {
        List<Staff> staffList = new ArrayList<>();
        try (Connection connection = management.formConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM staff WHERE role = ?"
            );
            statement.setString(1, role.getValue());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("staff_id");
                String name = rs.getString("name");
                double salary = rs.getDouble("salary");
                String contact = rs.getString("contact");
                Date hireDate = rs.getDate("hire_date");
                String statusStr = rs.getString("status");
                StaffStatus status = StaffStatus.setValue(statusStr);

                staffList.add(new Staff(id, name, role, salary, contact, hireDate, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    // ==========================================================
    // 8️⃣ Get All Staff
    // ==========================================================
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        try (Connection connection = management.formConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM staff");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("staff_id");
                String name = rs.getString("name");
                String roleStr = rs.getString("role");
                StaffRole role = StaffRole.setValue(roleStr);
                double salary = rs.getDouble("salary");
                String contact = rs.getString("contact");
                Date hireDate = rs.getDate("hire_date");
                String statusStr = rs.getString("status");
                StaffStatus status = StaffStatus.setValue(statusStr);

                staffList.add(new Staff(id, name, role, salary, contact, hireDate, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
}
