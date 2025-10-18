package DBConnection;
public class AccessLevel {
    private static final String admin = "_0:1!|?>|?>|%#3$|";
    private static final String url = "jdbc:mysql://localhost:3306/hotel";
    private static final String adHashPass = "65%4|1/12|65%4|1/12|&9$<|S64|N8|N6|N6|N7|&34^~|)99&|~12#|198&5%|@12$9&|1/12|";


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

