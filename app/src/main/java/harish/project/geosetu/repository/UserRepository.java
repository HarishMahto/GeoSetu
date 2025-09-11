package harish.project.geosetu.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import harish.project.geosetu.dao.UserDao;
import harish.project.geosetu.database.AppDatabase;
import harish.project.geosetu.model.User;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private ExecutorService executor;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        userDao = database.userDao();
        executor = Executors.newFixedThreadPool(4);
    }

    public void insert(User user) {
        executor.execute(() -> userDao.insert(user));
    }

    public void update(User user) {
        executor.execute(() -> userDao.update(user));
    }

    public User authenticate(String username, String password) {
        return userDao.authenticate(username, password);
    }

    public LiveData<User> getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public LiveData<List<User>> getUsersByRole(String role) {
        return userDao.getUsersByRole(role);
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }
}