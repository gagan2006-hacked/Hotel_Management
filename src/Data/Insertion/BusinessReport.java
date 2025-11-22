package Data.Insertion;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;

/*
| Field          | Type          | Description                  |
| -------------- | ------------- | ---------------------------- |
| report_id (PK) | INT           | Unique report ID             |
| year           | YEAR          | Year of the report           |
| month          | ENUM('January','February','March','April','May','June','July','August','September','October','November','December') | Month of the report |
| total_revenue  | DECIMAL(10,2) | All payments completed       |
| total_expenses | DECIMAL(10,2) | Staff salaries + maintenance |
| profit_or_loss | DECIMAL(10,2) | Revenue - expenses           |
| growth_rate    | DECIMAL(5,2)  | % change from last month     |
| generated_at   | DATETIME      | Date of report generation    |
* */
public class BusinessReport {
    private int report_id;
    private Year year;
    private Month month;
    private double total_revenue;
    private double total_expenses;
    private double profit_or_loss;
    private double growth_rate;
    private LocalDateTime generated_at;

    public BusinessReport(Year year, Month month, double total_revenue, double total_expenses, double profit_or_loss, double growth_rate, LocalDateTime generated_at) {
        this.year = year;
        this.month = month;
        this.total_revenue = total_revenue;
        this.total_expenses = total_expenses;
        this.profit_or_loss = profit_or_loss;
        this.growth_rate = growth_rate;
        this.generated_at = generated_at;
    }

    public BusinessReport(int report_id, Year year, Month month, double total_revenue, double total_expenses, double profit_or_loss, double growth_rate, LocalDateTime generated_at) {
        this.report_id = report_id;
        this.year = year;
        this.month = month;
        this.total_revenue = total_revenue;
        this.total_expenses = total_expenses;
        this.profit_or_loss = profit_or_loss;
        this.growth_rate = growth_rate;
        this.generated_at = generated_at;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public void setTotal_revenue(double total_revenue) {
        this.total_revenue = total_revenue;
    }

    public void setTotal_expenses(double total_expenses) {
        this.total_expenses = total_expenses;
    }

    public void setProfit_or_loss(double profit_or_loss) {
        this.profit_or_loss = profit_or_loss;
    }

    public void setGrowth_rate(double growth_rate) {
        this.growth_rate = growth_rate;
    }

    public void setGenerated_at(LocalDateTime generated_at) {
        this.generated_at = generated_at;
    }

    public int getReport_id() {
        return report_id;
    }

    public Year getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public double getTotal_revenue() {
        return total_revenue;
    }

    public double getTotal_expenses() {
        return total_expenses;
    }

    public double getProfit_or_loss() {
        return profit_or_loss;
    }

    public double getGrowth_rate() {
        return growth_rate;
    }

    public LocalDateTime getGenerated_at() {
        return generated_at;
    }
}
