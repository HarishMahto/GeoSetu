package harish.project.geosetu.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import harish.project.geosetu.dao.ClaimDao;
import harish.project.geosetu.dao.DSSRecommendationDao;
import harish.project.geosetu.dao.NotificationDao;
import harish.project.geosetu.dao.UserDao;
import harish.project.geosetu.model.Claim;
import harish.project.geosetu.model.DSSRecommendation;
import harish.project.geosetu.model.Notification;
import harish.project.geosetu.model.User;

@Database(
    entities = {User.class, Claim.class, DSSRecommendation.class, Notification.class},
    version = 2,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ClaimDao claimDao();
    public abstract DSSRecommendationDao dssRecommendationDao();
    public abstract NotificationDao notificationDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "fra_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() // For demo purposes only
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}