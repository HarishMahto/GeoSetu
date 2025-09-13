package harish.project.geosetu.api;

import java.util.List;
import harish.project.geosetu.model.DSSRecommendation;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DSSApiService {
    // Example endpoint: /dss/recommendations?userId=123
    @GET("dss/recommendations")
    Call<List<DSSRecommendation>> getRecommendations(@Query("userId") int userId);

    // Example endpoint: /dss/recommendations/claim/{claimId}
    @GET("dss/recommendations/claim/{claimId}")
    Call<List<DSSRecommendation>> getRecommendationsForClaim(@Path("claimId") String claimId);
}

