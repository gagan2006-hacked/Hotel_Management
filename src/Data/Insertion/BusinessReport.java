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
}
