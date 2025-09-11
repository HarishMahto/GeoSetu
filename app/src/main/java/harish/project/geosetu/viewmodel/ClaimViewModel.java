package harish.project.geosetu.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import harish.project.geosetu.model.Claim;
import harish.project.geosetu.model.DSSRecommendation;
import harish.project.geosetu.model.Notification;
import harish.project.geosetu.repository.ClaimRepository;
import harish.project.geosetu.database.AppDatabase;
import harish.project.geosetu.utils.DSSEngine;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClaimViewModel extends AndroidViewModel {
    private ClaimRepository claimRepository;
    private AppDatabase database;
    private MutableLiveData<String> operationResult;
    private ExecutorService executor;

    public ClaimViewModel(Application application) {
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

    public void updateClaimStatus(@NonNull String claimId, String status, String comments, int reviewerId) {
        executor.execute(() -> {
            try {
                Claim claim = database.claimDao().getClaimById(claimId).getValue();
                if (claim != null) {
                    claim.setStatus(status);
                    claim.setReviewerComments(comments);
                    claim.setReviewDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                    
                    claimRepository.update(claim);
                    
                    // Create notification for the claim owner
                    String notificationTitle = status.equals("APPROVED") ? "Claim Approved" : "Claim Rejected";
                    String notificationMessage = "Your claim " + claimId + " has been " + status.toLowerCase() + " by District Officer";
                    
                    Notification notification = new Notification(
                        claim.getUserId(),
                        notificationTitle,
                        notificationMessage,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()),
                        "CLAIM_UPDATE"
                    );
                    
                    database.notificationDao().insert(notification);
                    
                    // Generate DSS recommendations if approved
                    if (status.equals("APPROVED")) {
                        List<DSSRecommendation> recommendations = DSSEngine.generateRecommendations(claim);
                        for (DSSRecommendation rec : recommendations) {
                            database.dssRecommendationDao().insert(rec);
                        }
                        
                        // Create DSS notification
                        if (!recommendations.isEmpty()) {
                            Notification dssNotification = new Notification(
                                claim.getUserId(),
                                "DSS Recommendations Available",
                                "You are eligible for " + recommendations.size() + " government schemes",
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()),
                                "DSS_RECOMMENDATION"
                            );
                            database.notificationDao().insert(dssNotification);
                        }
                    }
                    
                    operationResult.postValue("Claim " + status.toLowerCase() + " successfully");
                }
            } catch (Exception e) {
                operationResult.postValue("Failed to update claim: " + e.getMessage());
            }
        });
    }
}