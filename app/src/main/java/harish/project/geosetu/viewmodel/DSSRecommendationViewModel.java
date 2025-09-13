package harish.project.geosetu.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import harish.project.geosetu.model.DSSRecommendation;
import harish.project.geosetu.repository.DSSRecommendationRepository;

public class DSSRecommendationViewModel extends AndroidViewModel {
    private final DSSRecommendationRepository repository;

    public DSSRecommendationViewModel(@NonNull Application application) {
        super(application);
        repository = new DSSRecommendationRepository(application);
    }

    public LiveData<List<DSSRecommendation>> getAllRecommendations() { return repository.getAllRecommendations(); }

    public LiveData<List<DSSRecommendation>> getRecommendationsForClaim(String claimId) { return repository.getRecommendationsForClaim(claimId); }

    public void refreshForUser(int userId) { repository.refreshRecommendationsForUser(userId); }
}

