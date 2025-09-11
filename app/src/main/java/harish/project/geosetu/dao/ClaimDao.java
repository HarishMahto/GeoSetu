package harish.project.geosetu.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import harish.project.geosetu.model.Claim;
import java.util.List;

@Dao
public interface ClaimDao {
    @Insert
    void insert(Claim claim);

    @Update
    void update(Claim claim);

    @Query("SELECT * FROM claims WHERE claimId = :claimId")
    LiveData<Claim> getClaimById(@NonNull String claimId);

    @Query("SELECT * FROM claims WHERE userId = :userId")
    LiveData<List<Claim>> getClaimsByUserId(int userId);

    @Query("SELECT * FROM claims WHERE status = :status")
    LiveData<List<Claim>> getClaimsByStatus(String status);

    @Query("SELECT * FROM claims")
    LiveData<List<Claim>> getAllClaims();

    @Query("SELECT COUNT(*) FROM claims WHERE status = :status")
    LiveData<Integer> getClaimCountByStatus(String status);

    @Query("SELECT COUNT(*) FROM claims")
    int getClaimCount();
}