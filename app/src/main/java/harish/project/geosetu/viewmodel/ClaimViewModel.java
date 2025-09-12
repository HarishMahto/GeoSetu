package harish.project.geosetu.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import harish.project.geosetu.database.AppDatabase;
import harish.project.geosetu.model.Claim;
import harish.project.geosetu.model.DSSRecommendation;
import harish.project.geosetu.model.Notification;
import harish.project.geosetu.repository.ClaimRepository;
import harish.project.geosetu.utils.DSSEngine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClaimViewModel extends AndroidViewModel {
    private final ClaimRepository claimRepository;
    private final AppDatabase database;
    private final MutableLiveData<String> operationResult;
    private final ExecutorService executor;

    public ClaimViewModel(@NonNull Application application) {
        super(application);
        claimRepository = new ClaimRepository(application);
        database = AppDatabase.getDatabase(application);
        operationResult = new MutableLiveData<>();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Claim>> getAllClaims() {
        return claimRepository.getAllClaims();
    }

    public LiveData<List<Claim>> getClaimsByUserId(int userId) {
        return claimRepository.getClaimsByUserId(userId);
    }

    public LiveData<List<Claim>> getClaimsByStatus(String status) {
        return claimRepository.getClaimsByStatus(status);
    }

    public LiveData<Claim> getClaimById(@NonNull String claimId) {
        return claimRepository.getClaimById(claimId);
    }

    public LiveData<Integer> getClaimCountByStatus(String status) {
        return claimRepository.getClaimCountByStatus(status);
    }

    public MutableLiveData<String> getOperationResult() {
        return operationResult;
    }

    public void insertClaim(Claim claim) {
        executor.execute(() -> {
            try {
                claimRepository.insert(claim);
                operationResult.postValue("Claim submitted successfully");
            } catch (Exception e) {
                operationResult.postValue("Failed to submit claim: " + e.getMessage());
            }
        });
    }

    /**
     * Requires ClaimDao to have:
     * @Query("SELECT * FROM claims WHERE claimId = :claimId LIMIT 1")
     * Claim getClaimByIdSync(String claimId);
     */
    public void updateClaimStatus(@NonNull String claimId, String status, String comments, int reviewerId) {
        executor.execute(() -> {
            try {
                String normalizedStatus = status == null ? "PENDING" : status.trim().toUpperCase(Locale.getDefault());
                String safeComments = comments == null ? "" : comments.trim();
                String reviewDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String nowTs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                Claim claim = database.claimDao().getClaimByIdSync(claimId);
                if (claim == null) {
                    operationResult.postValue("Failed to update claim: claim not found");
                    return;
                }

                database.runInTransaction(() -> {
                    claim.setStatus(normalizedStatus);
                    claim.setReviewerComments(safeComments);
                    claim.setReviewDate(reviewDate);
                    // reviewerId available if you later add it to Claim

                    claimRepository.update(claim);

                    String notificationTitle = "APPROVED".equals(normalizedStatus) ? "Claim Approved" : "Claim Rejected";
                    String notificationMessage = "Your claim " + claimId + " has been " + normalizedStatus.toLowerCase(Locale.getDefault()) + " by District Officer";

                    Notification notification = new Notification(
                            claim.getUserId(),
                            notificationTitle,
                            notificationMessage,
                            nowTs,
                            "CLAIM_UPDATE"
                    );
                    database.notificationDao().insert(notification);

                    if ("APPROVED".equals(normalizedStatus)) {
                        List<DSSRecommendation> recommendations = DSSEngine.generateRecommendations(claim);
                        for (DSSRecommendation rec : recommendations) {
                            database.dssRecommendationDao().insert(rec);
                        }
                        if (!recommendations.isEmpty()) {
                            Notification dssNotification = new Notification(
                                    claim.getUserId(),
                                    "DSS Recommendations Available",
                                    "You are eligible for " + recommendations.size() + " government schemes",
                                    nowTs,
                                    "DSS_RECOMMENDATION"
                            );
                            database.notificationDao().insert(dssNotification);
                        }
                    }
                });

                operationResult.postValue("Claim " + normalizedStatus.toLowerCase(Locale.getDefault()) + " successfully");
            } catch (Exception e) {
                operationResult.postValue("Failed to update claim: " + e.getMessage());
            }
        });
    }
}
