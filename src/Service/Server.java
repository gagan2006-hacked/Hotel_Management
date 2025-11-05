package Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import DAO.*;
import Data.Insertion.*;
import DBConnection.ConnectionMangement;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server.java
 * Lightweight HTTP server integrating with existing JDBC DAOs.
 * Place this file in Service package. Ensure Gson is on the classpath.
 */
public class Server {
    private static final int DEFAULT_PORT = 8080;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

            // Register endpoints
            server.createContext("/login", new LoginHandler());
            server.createContext("/book-room", new BookingHandler());
            server.createContext("/get-customer-history", new CustomerHistoryHandler());
            server.createContext("/get-payments", new PaymentHandler());
            server.createContext("/report", new ReportHandler());

            server.setExecutor(Executors.newFixedThreadPool(10));

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Stopping HTTP server...");
                server.stop(3);
                LOGGER.info("HTTP server stopped.");
            }));

            server.start();
            LOGGER.info("Server started on port " + port);
            System.out.println("✅ Server running at http://localhost:" + port);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to start HTTP server", e);
            System.err.println("Failed to start server: " + e.getMessage());
            System.exit(1);
        }
    }

    /* ---------------------- Utilities ---------------------- */

    private static void addCorsHeaders(Headers headers) {
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.add("Access-Control-Max-Age", "3600");
    }

    private static boolean handleOptions(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            addCorsHeaders(exchange.getResponseHeaders());
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return true;
        }
        addCorsHeaders(exchange.getResponseHeaders());
        return false;
    }

    private static String readRequestBody(InputStream is) throws IOException {
        if (is == null) return "";
        byte[] bytes = is.readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static void sendJson(HttpExchange exchange, Object payload, int status) throws IOException {
        String response = gson.toJson(payload);
        byte[] respBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        addCorsHeaders(exchange.getResponseHeaders());
        exchange.sendResponseHeaders(status, respBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(respBytes);
        } finally {
            exchange.close();
        }
    }

    private static void sendError(HttpExchange exchange, String message, int status) throws IOException {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", message);
        error.put("status", status);
        error.put("timestamp", Instant.now().toString());
        sendJson(exchange, error, status);
    }

    private static void sendSuccess(HttpExchange exchange, Object data) throws IOException {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("success", true);
        resp.put("data", data);
        resp.put("timestamp", Instant.now().toString());
        sendJson(exchange, resp, 200);
    }

    /* ---------------------- Handlers ---------------------- */

    /**
     * LoginHandler
     * - Checks admin table for credentials (your DB has admin with email/password)
     * - Returns admin object (id, name, email) on success
     */
    static class LoginHandler implements HttpHandler {
        private final ConnectionMangement management = new ConnectionMangement();
        private final Logger logger = Logger.getLogger(LoginHandler.class.getName());

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendError(exchange, "Method not allowed", 405);
                return;
            }

            String body = readRequestBody(exchange.getRequestBody());
            if (body == null || body.isBlank()) {
                sendError(exchange, "Request body cannot be empty", 400);
                return;
            }

            try {
                JsonObject json = JsonParser.parseString(body).getAsJsonObject();
                if (!json.has("username") || !json.has("password")) {
                    sendError(exchange, "username and password fields required", 400);
                    return;
                }
                String username = json.get("username").getAsString();
                String password = json.get("password").getAsString();

                // Query admin table (uses your ConnectionMangement)
                Admin admin = null;
                try (Connection conn = management.formConnection();
                     PreparedStatement ps = conn.prepareStatement("SELECT admin_id, name, email, created_at FROM admin WHERE email = ? AND password = ?")) {

                    ps.setString(1, username);
                    ps.setString(2, password); // if you hash passwords, hash before comparing
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            admin = new Admin();
                            admin.setAdmin_id(rs.getInt("admin_id"));
                            admin.setName(rs.getString("name"));
                            admin.setEmail(rs.getString("email"));
                            // Admin class may have other fields; set as needed
                        }
                    }
                } catch (SQLException e) {
                    logger.log(Level.WARNING, "DB error during admin login check", e);
                    sendError(exchange, "Database error during login", 500);
                    return;
                }

                if (admin != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("message", "Login successful");
                    data.put("admin", admin);
                    sendSuccess(exchange, data);
                } else {
                    sendError(exchange, "Invalid credentials", 401);
                }
            } catch (JsonSyntaxException e) {
                logger.log(Level.WARNING, "Malformed JSON in login request", e);
                sendError(exchange, "Invalid JSON format", 400);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected error in login handler", e);
                sendError(exchange, "Internal server error", 500);
            }
        }
    }

    /**
     * BookingHandler - expects Booking JSON (Data.Insertion.Booking) in POST
     * uses BookingRepository.addBooking(Booking)
     */
    static class BookingHandler implements HttpHandler {
        private final BookingRepository bookingRepo = new BookingRepository();
        private final Logger logger = Logger.getLogger(BookingHandler.class.getName());

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendError(exchange, "Method not allowed", 405);
                return;
            }

            String body = readRequestBody(exchange.getRequestBody());
            if (body == null || body.isBlank()) {
                sendError(exchange, "Request body cannot be empty", 400);
                return;
            }

            try {
                Booking booking = gson.fromJson(body, Booking.class);
                if (booking == null || booking.getCustomer_id() <= 0 || booking.getRoom_id() <= 0) {
                    sendError(exchange, "Invalid booking data: customer_id and room_id required", 400);
                    return;
                }

                boolean created;
                try {
                    created = bookingRepo.addBooking(booking);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "DB error creating booking", e);
                    sendError(exchange, "Database error creating booking", 500);
                    return;
                }

                Map<String, Object> result = new HashMap<>();
                result.put("created", created);
                result.put("booking", booking);
                sendSuccess(exchange, result);
            } catch (JsonSyntaxException e) {
                logger.log(Level.WARNING, "Malformed JSON in booking request", e);
                sendError(exchange, "Invalid JSON format", 400);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected error in booking handler", e);
                sendError(exchange, "Internal server error", 500);
            }
        }
    }

    /**
     * CustomerHistoryHandler - GET ?customerId=123
     * uses CustomerRepository.getCustomerBookingHistory(int)
     */
    static class CustomerHistoryHandler implements HttpHandler {
        private final CustomerRepository customerRepo = new CustomerRepository();
        private final Logger logger = Logger.getLogger(CustomerHistoryHandler.class.getName());

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendError(exchange, "Method not allowed", 405);
                return;
            }

            // parse query params
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = new HashMap<>();
            if (query != null && !query.isBlank()) {
                String[] pairs = query.split("&");
                for (String p : pairs) {
                    int idx = p.indexOf('=');
                    if (idx > 0) {
                        String k = p.substring(0, idx);
                        String v = p.substring(idx + 1);
                        params.put(k, v);
                    }
                }
            }

            if (!params.containsKey("customerId")) {
                sendError(exchange, "Missing customerId query parameter", 400);
                return;
            }

            int customerId;
            try {
                customerId = Integer.parseInt(params.get("customerId"));
            } catch (NumberFormatException e) {
                sendError(exchange, "customerId must be integer", 400);
                return;
            }

            try {
                List<?> history = customerRepo.getCustomerBookingHistory(customerId);
                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("customerId", customerId);
                payload.put("recordCount", history != null ? history.size() : 0);
                payload.put("history", history);
                sendSuccess(exchange, payload);
            } catch (Exception e) {
                logger.log(Level.WARNING, "DB error retrieving customer history", e);
                sendError(exchange, "Database error retrieving customer history", 500);
            }
        }
    }

    /**
     * PaymentHandler - GET returns all payments (uses PaymentRepository.getAllPayments())
     * If the repository method name differs, update the call accordingly.
     */
    static class PaymentHandler implements HttpHandler {
        private final PaymentRepository paymentRepo;
        private final Logger logger = Logger.getLogger(PaymentHandler.class.getName());

        PaymentHandler() {
            PaymentRepository tmp = null;
            try {
                tmp = new PaymentRepository(); // assumes no-arg constructor like CustomerRepository
            } catch (Exception e) {
                logger.log(Level.WARNING, "PaymentRepository no-arg constructor not available", e);
            }
            paymentRepo = tmp;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendError(exchange, "Method not allowed", 405);
                return;
            }

            if (paymentRepo == null) {
                sendError(exchange, "PaymentRepository not available - check constructor signature", 500);
                return;
            }

            try {
                // Expecting a method getAllPayments() or getAllPayment(); adapt if needed
                Object payments;
                try {
                    payments = paymentRepo.getAllPayments();
                } catch (NoSuchMethodError | AbstractMethodError ex) {
                    // Fallback: try alternate method names
                    try {
                        payments = paymentRepo.getPaymentsByBookingId(0); // unlikely - adjust/remove
                    } catch (Throwable t) {
                        throw new RuntimeException("payment repo method mismatch", t);
                    }
                }
                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("count", payments instanceof List ? ((List<?>) payments).size() : null);
                payload.put("payments", payments);
                sendSuccess(exchange, payload);
            } catch (Exception e) {
                logger.log(Level.WARNING, "DB error retrieving payments", e);
                sendError(exchange, "Database error retrieving payments", 500);
            }
        }
    }

    /**
     * ReportHandler - returns aggregated metrics using BusinessReportRepository
     * Uses getAllReports() and attempts some helper methods if present.
     */
    static class ReportHandler implements HttpHandler {
        private final BusinessReportRepository reportRepo = new BusinessReportRepository();
        private final Logger logger = Logger.getLogger(ReportHandler.class.getName());

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendError(exchange, "Method not allowed", 405);
                return;
            }

            try {
                Map<String, Object> report = new LinkedHashMap<>();

                // getAllReports present in your DAO
                try {
                    List<?> allReports = reportRepo.getAllReports();
                    report.put("allReportsCount", allReports != null ? allReports.size() : 0);
                    report.put("allReports", allReports);
                } catch (Exception e) {
                    logger.log(Level.FINE, "getAllReports failed", e);
                    report.put("allReports", null);
                }

                // Try to fetch totals if methods exist, otherwise null
                try {
                    Object totalRevenue = reportRepo.getTotalRevenue(); // if you implemented
                    report.put("totalRevenue", totalRevenue);
                } catch (Throwable t) {
                    report.put("totalRevenue", null);
                }

                try {
                    Object occupancy = reportRepo.calculateOccupancyRate(); // optional
                    report.put("occupancyRate", occupancy);
                } catch (Throwable t) {
                    report.put("occupancyRate", null);
                }

                sendSuccess(exchange, report);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unexpected error creating business report", e);
                sendError(exchange, "Internal server error generating report", 500);
            }
        }
    }
}
