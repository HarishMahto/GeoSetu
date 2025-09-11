package harish.project.geosetu.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import harish.project.geosetu.model.DSSRecommendation;
import java.util.List;

@Dao
public interface DSSRecommendationDao {
    @Insert
    void insert(DSSRecommendation recommendation);

    @Query("SELECT * FROM dss_recommendations WHERE claimId = :claimId")
    LiveData<List<DSSRecommendation>> getRecommendationsByClaimId(@NonNull String claimId);

    @Query("SELECT * FROM dss_recommendations WHERE isEligible = 1")
    LiveData<List<DSSRecommendation>> getEligibleRecommendations();

    @Query("SELECT * FROM dss_recommendations")
    LiveData<List<DSSRecommendation>> getAllRecommendations();

    @Query("SELECT COUNT(*) FROM dss_recommendations")
    int getRecommendationCount();
}