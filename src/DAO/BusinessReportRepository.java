package DAO;

import DBConnection.ConnectionMangement;
import Data.Insertion.BusinessReport;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


/*
| Method                                                                      | Returns                | Purpose                                                                         |
| --------------------------------------------------------------------------- | ---------------------- | ------------------------------------------------------------------------------- |
| `addReport(BusinessReport report)`                                          | `boolean`              | Insert a new business_report row (committed). Sets `report_id` on success.      |
| `getReportById(int reportId)`                                               | `BusinessReport`       | Retrieve a single report by primary key.                                        |
| `getReportByYearMonth(Year year, Month month)`                              | `BusinessReport`       | Retrieve report for a specific year+month.                                      |
| `getAllReports()`                                                           | `List<BusinessReport>` | List all reports ordered by year/month.                                         |
| `updateFinancials(int reportId, double totalRevenue, double totalExpenses)` | `boolean`              | Update revenue/expenses and recompute growth (uses previous-month revenue).     |
| `deleteReport(int reportId)`                                                | `boolean`              | Delete report row.                                                              |
| `getAnnualSummary(Year year)`                                               | `BusinessReport`       | Aggregate annual totals (returns a BusinessReport-like object with month=null). |
| *(internal)* `getPreviousMonthRevenue(Connection, int)`                     | `double`               | Helper to fetch previous month revenue used for growth calc.                    |
| *(internal)* `calculateGrowthRate(double, double)`                          | `double`               | Helper to compute growth % safely.                                              |
| *(internal)* `extractReportFromResultSet(ResultSet)`                        | `BusinessReport`       | Maps a ResultSet row to BusinessReport object.                                  |

**/
/**
 * BusinessReportRepository
 * - follows the style of CustomerRepository (explicit transaction handling)
 * - uses the business_report table:
 *   (report_id, year, month, total_revenue, total_expenses, profit (generated), loss (generated), growth_rate, created_at)
 */
public class BusinessReportRepository {
    private final ConnectionMangement management;

    public BusinessReportRepository() {
        this.management = new ConnectionMangement();
    }

    // Helper to convert java.time.Month -> DB string ("January", "February", ...)
    private String monthToDbString(Month month) {
        if (month == null) return null;
        String lower = month.name().toLowerCase();       // "january"
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1); // "January"
    }

    // Helper to convert DB month string ("January") -> java.time.Month
    private Month dbStringToMonth(String dbMonth) {
        if (dbMonth == null) return null;
        // convert "January" -> "JANUARY" and use Month.valueOf
        try {
            return Month.valueOf(dbMonth.toUpperCase());
        } catch (Exception ex) {
            return null;
        }
    }

    // ==========================================================
    // 1) Insert a new monthly report
    // ==========================================================
    public boolean addReport(BusinessReport report) {
        String sql = "INSERT INTO business_report (year, month, total_revenue, total_expenses, growth_rate, created_at) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            connection.setAutoCommit(false);

            ps.setInt(1, report.getYear().getValue());
            ps.setString(2, monthToDbString(report.getMonth()));
            ps.setDouble(3, report.getTotal_revenue());
            ps.setDouble(4, report.getTotal_expenses());
            ps.setDouble(5, report.getGrowth_rate());
            ps.setTimestamp(6, Timestamp.valueOf(report.getGenerated_at()));

            int ack = ps.executeUpdate();
            if (ack == 0) {
                connection.rollback();
                return false;
            }

            // set generated id back to object
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    report.setReport_id(keys.getInt(1));
                }
            }

            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 2) Get report by id
    // ==========================================================
    public BusinessReport getReportById(int reportId) {
        String sql = "SELECT * FROM business_report WHERE report_id = ?;";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            ps.setInt(1, reportId);
            ResultSet rs = ps.executeQuery();
            BusinessReport report = null;
            if (rs.next()) {
                report = extractReportFromResultSet(rs);
            }

            connection.commit();
            connection.close();
            return report;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==========================================================
    // 3) Get report by year + month
    // ==========================================================
    public BusinessReport getReportByYearMonth(Year year, Month month) {
        String sql = "SELECT * FROM business_report WHERE year = ? AND month = ?;";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            ps.setInt(1, year.getValue());
            ps.setString(2, monthToDbString(month));
            ResultSet rs = ps.executeQuery();
            BusinessReport report = null;
            if (rs.next()) {
                report = extractReportFromResultSet(rs);
            }

            connection.commit();
            connection.close();
            return report;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==========================================================
    // 4) List all reports (ordered by year desc, month)
    // ==========================================================
    public List<BusinessReport> getAllReports() {
        String sql = "SELECT * FROM business_report ORDER BY year DESC, FIELD(month, 'January','February','March','April','May','June','July','August','September','October','November','December');";
        List<BusinessReport> list = new ArrayList<>();
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            connection.setAutoCommit(false);

            while (rs.next()) {
                list.add(extractReportFromResultSet(rs));
            }

            connection.commit();
            connection.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ==========================================================
    // 5) Update financials (revenue, expenses) and optionally growth_rate
    //    This method recalculates profit_or_loss is done by DB (generated column).
    //    We compute growth_rate here based on previous month if available.
    // ==========================================================
    public boolean updateFinancials(int reportId, double totalRevenue, double totalExpenses) {
        String sql = "UPDATE business_report SET total_revenue = ?, total_expenses = ?, growth_rate = ? WHERE report_id = ?;";
        try (Connection connection = management.formConnection()) {
            connection.setAutoCommit(false);

            // get previous month revenue for growth calc
            double prevRevenue = getPreviousMonthRevenue(connection, reportId);
            double growth = calculateGrowthRate(totalRevenue, prevRevenue);

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setDouble(1, totalRevenue);
                ps.setDouble(2, totalExpenses);
                ps.setDouble(3, growth);
                ps.setInt(4, reportId);

                int ack = ps.executeUpdate();
                if (ack == 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 6) Delete a report by id
    // ==========================================================
    public boolean deleteReport(int reportId) {
        String sql = "DELETE FROM business_report WHERE report_id = ?;";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);
            ps.setInt(1, reportId);
            int ack = ps.executeUpdate();
            if (ack == 0) {
                connection.rollback();
                return false;
            }
            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 7) Get annual summary (total revenue, total expenses, profit) for a year
    // ==========================================================
    public BusinessReport getAnnualSummary(Year year) {
        String sql = "SELECT SUM(total_revenue) AS total_revenue, SUM(total_expenses) AS total_expenses FROM business_report WHERE year = ?;";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);
            ps.setInt(1, year.getValue());
            ResultSet rs = ps.executeQuery();

            BusinessReport summary = null;
            if (rs.next()) {
                double revenue = rs.getDouble("total_revenue");
                double expenses = rs.getDouble("total_expenses");
                double profit = revenue - expenses;
                // Use month = null and report_id = 0 for aggregate object
                summary = new BusinessReport(year, null, revenue, expenses, profit, 0.0, LocalDateTime.now());
            }

            connection.commit();
            connection.close();
            return summary;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==========================================================
    // Helper: get previous month's revenue using current report_id ordering
    // (returns 0.0 if not found)
    // ==========================================================
    private double getPreviousMonthRevenue(Connection connection, int currentReportId) {
        String sql = "SELECT total_revenue FROM business_report WHERE report_id < ? ORDER BY report_id DESC LIMIT 1;";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, currentReportId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ==========================================================
    // Helper: calculate growth rate
    // ==========================================================
    private double calculateGrowthRate(double currentRevenue, double previousRevenue) {
        if (previousRevenue <= 0) return 0.0;
        return ((currentRevenue - previousRevenue) / previousRevenue) * 100.0;
    }

    // ==========================================================
    // Helper: map ResultSet -> BusinessReport
    // ==========================================================
    private BusinessReport extractReportFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("report_id");
        Year year = Year.of(rs.getInt("year"));
        String monthStr = rs.getString("month"); // "January"
        Month month = dbStringToMonth(monthStr); // java.time.Month
        double revenue = rs.getDouble("total_revenue");
        double expenses = rs.getDouble("total_expenses");
        // profit and loss are generated columns in DB; compute to fill object consistently
        double profitOrLoss = revenue - expenses;
        double growth = rs.getDouble("growth_rate");
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime generatedAt = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();

        BusinessReport report = new BusinessReport(id, year, month, revenue, expenses, profitOrLoss, growth, generatedAt);
        return report;
    }
}
