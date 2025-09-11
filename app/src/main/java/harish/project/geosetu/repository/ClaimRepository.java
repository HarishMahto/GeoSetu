package harish.project.geosetu.repository;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import harish.project.geosetu.dao.ClaimDao;
import harish.project.geosetu.database.AppDatabase;
import harish.project.geosetu.model.Claim;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClaimRepository {
    private ClaimDao claimDao;
    private ExecutorService executor;

    public ClaimRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        claimDao = database.claimDao();
        executor = Executors.newFixedThreadPool(4);
    }

    public void insert(Claim claim) {
        executor.execute(() -> claimDao.insert(claim));
    }

    public void update(Claim claim) {
        executor.execute(() -> claimDao.update(claim));
    }

    public LiveData<Claim> getClaimById(@NonNull String claimId) {
        return claimDao.getClaimById(claimId);
    }

    public LiveData<List<Claim>> getClaimsByUserId(int userId) {
        return claimDao.getClaimsByUserId(userId);
    }

    public LiveData<List<Claim>> getClaimsByStatus(String status) {
        return claimDao.getClaimsByStatus(status);
    }

    public LiveData<List<Claim>> getAllClaims() {
        return claimDao.getAllClaims();
    }

    public LiveData<Integer> getClaimCountByStatus(String status) {
        return claimDao.getClaimCountByStatus(status);
    }
}