package harish.project.geosetu.utils;

import android.content.Context;
import harish.project.geosetu.database.AppDatabase;
import harish.project.geosetu.model.Claim;
import harish.project.geosetu.model.DSSRecommendation;
import harish.project.geosetu.model.Notification;
import harish.project.geosetu.model.User;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataInitializer {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void initializeData(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);

        executor.execute(() -> {
            // Initialize users
            initializeUsers(database);

            // Initialize claims
            initializeClaims(database);

            // Initialize DSS recommendations
            initializeDSSRecommendations(database);

            // Initialize notifications
            initializeNotifications(database);
        });
    }

    private static void initializeUsers(AppDatabase database) {
        if (database.userDao().getUserCount() > 0) return;

        User officer1 = new User(1, "officer1", "password123", "OFFICER", "Rajesh Kumar", "District Office", "");
        User officer2 = new User(2, "officer2", "password123", "OFFICER", "Priya Sharma", "District Office", "");
        User patta1 = new User(3, "patta1", "password123", "PATTA_HOLDER", "Ramesh Gond", "Khargone", "Gond");
        User patta2 = new User(4, "patta2", "password123", "PATTA_HOLDER", "Sunita Bhil", "Barwani", "Bhil");
        User patta3 = new User(5, "patta3", "password123", "PATTA_HOLDER", "Mohan Korku", "Betul", "Korku");

        database.userDao().insert(officer1);
        database.userDao().insert(officer2);
        database.userDao().insert(patta1);
        database.userDao().insert(patta2);
        database.userDao().insert(patta3);
    }

    private static void initializeClaims(AppDatabase database) {
        if (database.claimDao().getClaimCount() > 0) return;

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Claim claim1 = new Claim("C1001", 3, "Ramesh Gond", "Khargone", "Gond", "IFR", 2.5,
                "/documents/claim1.pdf", "PENDING", currentDate);
        claim1.setGeoJsonData("{\"type\":\"Polygon\",\"coordinates\":[[[77.123,22.456],[77.124,22.456],[77.124,22.457],[77.123,22.457],[77.123,22.456]]]}");

        Claim claim2 = new Claim("C1002", 4, "Sunita Bhil", "Barwani", "Bhil", "CFR", 5.0,
                "/documents/claim2.pdf", "APPROVED", currentDate);
        claim2.setGeoJsonData("{\"type\":\"Polygon\",\"coordinates\":[[[76.234,21.567],[76.235,21.567],[76.235,21.568],[76.234,21.568],[76.234,21.567]]]}");
        claim2.setReviewDate(currentDate);
        claim2.setReviewerComments("All documents verified. Claim approved.");

        Claim claim3 = new Claim("C1003", 5, "Mohan Korku", "Betul", "Korku", "CR", 1.8,
                "/documents/claim3.pdf", "PENDING", currentDate);
        claim3.setGeoJsonData("{\"type\":\"Polygon\",\"coordinates\":[[[77.345,21.678],[77.346,21.678],[77.346,21.679],[77.345,21.679],[77.345,21.678]]]}");

        database.claimDao().insert(claim1);
        database.claimDao().insert(claim2);
        database.claimDao().insert(claim3);
    }

    private static void initializeDSSRecommendations(AppDatabase database) {
        if (database.dssRecommendationDao().getRecommendationCount() > 0) return;

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        DSSRecommendation rec1 = new DSSRecommendation("C1002", "PM-KISAN",
                "Pradhan Mantri Kisan Samman Nidhi", "Farmland > 2 acres",
                "₹6000 per year in 3 installments", true, currentDate);

        DSSRecommendation rec2 = new DSSRecommendation("C1002", "Jal Jeevan Mission",
                "Har Ghar Jal Scheme", "No water source within 500m",
                "Piped water connection to household", true, currentDate);

        DSSRecommendation rec3 = new DSSRecommendation("C1002", "MGNREGA",
                "Mahatma Gandhi National Rural Employment Guarantee Act", "Community forest area > 3 acres",
                "100 days guaranteed employment for pond construction", true, currentDate);

        database.dssRecommendationDao().insert(rec1);
        database.dssRecommendationDao().insert(rec2);
        database.dssRecommendationDao().insert(rec3);
    }

    private static void initializeNotifications(AppDatabase database) {
        if (database.notificationDao().getNotificationCount() > 0) return;

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Notification notif1 = new Notification(4, "Claim Approved",
                "Your claim C1002 has been approved by District Officer", currentDate, "CLAIM_UPDATE");

        Notification notif2 = new Notification(4, "DSS Recommendations Available",
                "You are eligible for 3 government schemes", currentDate, "DSS_RECOMMENDATION");

        Notification notif3 = new Notification(3, "Document Verification Required",
                "Additional documents needed for claim C1001", currentDate, "CLAIM_UPDATE");

        database.notificationDao().insert(notif1);
        database.notificationDao().insert(notif2);
        database.notificationDao().insert(notif3);
    }
}
