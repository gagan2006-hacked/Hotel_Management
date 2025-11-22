🏨 Hotel Management System (Java + Swing + JDBC + MySQL)

A complete Hotel Management System built using Java, Swing/AWT, and MySQL.
This desktop application helps manage all essential hotel operations, including:

Customer records

Room management

Staff management

Bookings

Payments

Reports

The project follows a clean layered architecture for easy maintenance and scalability.

🚀 Features

✔ Customer Management

Add, edit, and delete customer records

Customer listing with JTable

Real-time search functionality

✔ Room Management

Add/update room details

Check room availability

Update room status (Available / Occupied / Maintenance)

✔ Booking Management

Create and update bookings

View bookings by customer

View today's bookings

Complete check-in/check-out flow

✔ Staff Management

Add new staff members

Update staff info

Activate/Deactivate staff

Search staff records

✔ Payment Management

Add new payment records

Filter by mode (UPI, Cash, Card)

Auto-calculate revenue

✔ Reports

Monthly revenue & expense calculation using MySQL Stored Procedure

Easily extendable to PDF/Excel exports

🛠 Tech Stack
| Component     | Technology        |
| ------------- | ----------------- |
| Language      | Java              |
| GUI Framework | Swing / AWT       |
| Database      | MySQL             |
| Connectivity  | JDBC              |
| IDE           | IntelliJ IDEA     |
| Build System  | Manual Java build |

📂 Project Folder Structure
Hotel_Management/
│
├── src/
│   ├── DAO/              # Data Access Objects (CRUD operations, SQL queries)
│   ├── Data/             # POJO model classes (Customer, Room, Staff, Payment, Booking)
│   ├── DBConnection/     # MySQL connection logic
│   └── Service/          # Business logic layer
│
├── assets/               # (Optional) Icons and images
├── README.md             # Documentation
└── .gitignore            # Git ignore rules

🗃 Database Setup (MySQL)
1️⃣ Create database
CREATE DATABASE hotel;

2️⃣ Create a dedicated application user

⚠️ Never use root for applications.

CREATE USER 'hotelAdmin'@'localhost' IDENTIFIED BY 'YourStrongPassword#123';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON hotel.* TO 'hotelAdmin'@'localhost';
FLUSH PRIVILEGES;

3️⃣ Import your SQL tables

Place sanitized SQL setup files in a folder like:

/sql/schema.sql


⚠ Do NOT upload real customer/staff data.

🔐 Safe Database Connection (Highly Important)

Create db.properties (but NEVER upload this to GitHub):

db.url=jdbc:mysql://localhost:3306/hotel
db.user=hotelAdmin
db.password=YourStrongPassword#123


Load it in Java:

public class AccessLevel {
    private static final String admin = ""; here
    private static final String url = "";
    private static final String adHashPass = "";


    public static String getAdHashPass() {
        return adHashPass;
    }

    public static String getUrl() {
        return url;
    }

    public static String getAdmin() {
        return admin;
    }
    
}


If using direct constants, update them in:

src/DBConnection/AccessLevel.java

🧪 How to Run the Project

Install MySQL

Create the database and user

Run all SQL tables and procedures

Add database credentials into db.properties (NOT uploaded to GitHub)

Open the project in IntelliJ

Run the main class from the src/ directory

🧱 Future Enhancements

Admin login system

Export reports to PDF or Excel

Add Room Service / Inventory module

Multi-user access

REST API for web version

Migration to Spring Boot

Convert to Maven/Gradle project

📜 License

This project is open for use and modification.
You may fork, extend, or contribute as needed.

🤝 Contributions

Pull requests are welcome!
If you want to improve a module, create an issue.

👨‍💻 Author

Gagan Vishwanath
Java Developer | CS Student | Cybersecurity & DSA Learner

⭐ If you like this project

Please consider giving it a Star on GitHub!

