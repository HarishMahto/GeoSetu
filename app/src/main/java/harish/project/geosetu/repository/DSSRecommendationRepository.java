package harish.project.geosetu.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import harish.project.geosetu.api.DSSApiService;
import harish.project.geosetu.api.RetrofitClient;
import harish.project.geosetu.database.AppDatabase;
import harish.project.geosetu.dao.DSSRecommendationDao;
import harish.project.geosetu.model.DSSRecommendation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DSSRecommendationRepository {
    private final DSSRecommendationDao dao;
    private final DSSApiService api;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public DSSRecommendationRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        dao = db.dssRecommendationDao();
        api = RetrofitClient.getInstance().create(DSSApiService.class);
    }

    public LiveData<List<DSSRecommendation>> getAllRecommendations() { return dao.getAllRecommendations(); }

    public LiveData<List<DSSRecommendation>> getRecommendationsForClaim(String claimId) { return dao.getRecommendationsByClaimId(claimId); }

    public void refreshRecommendationsForUser(int userId) {
        api.getRecommendations(userId).enqueue(new Callback<List<DSSRecommendation>>() {
            @Override public void onResponse(Call<List<DSSRecommendation>> call, Response<List<DSSRecommendation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executor.execute(() -> {
                        for (DSSRecommendation r : response.body()) {
                            dao.insert(r);
                        }
                    });
                }
            }
            @Override public void onFailure(Call<List<DSSRecommendation>> call, Throwable t) {
                // Log/ignore for now
            }
        });
    }

    public void insert(DSSRecommendation rec) { executor.execute(() -> dao.insert(rec)); }
}

