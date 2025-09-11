package harish.project.geosetu.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "dss_recommendations")
public class DSSRecommendation {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String claimId;
    public String schemeName;
    public String schemeDescription;
    public String eligibilityCriteria;
    public String benefits;
    public boolean isEligible;
    public String recommendationDate;

    public DSSRecommendation() {}

    @Ignore
    public DSSRecommendation(String claimId, String schemeName, String schemeDescription, 
                           String eligibilityCriteria, String benefits, boolean isEligible, String recommendationDate) {
        this.claimId = claimId;
        this.schemeName = schemeName;
        this.schemeDescription = schemeDescription;
        this.eligibilityCriteria = eligibilityCriteria;
        this.benefits = benefits;
        this.isEligible = isEligible;
        this.recommendationDate = recommendationDate;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getClaimId() { return claimId; }
    public void setClaimId(String claimId) { this.claimId = claimId; }
    
    public String getSchemeName() { return schemeName; }
    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }
    
    public String getSchemeDescription() { return schemeDescription; }
    public void setSchemeDescription(String schemeDescription) { this.schemeDescription = schemeDescription; }
    
    public String getEligibilityCriteria() { return eligibilityCriteria; }
    public void setEligibilityCriteria(String eligibilityCriteria) { this.eligibilityCriteria = eligibilityCriteria; }
    
    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }
    
    public boolean isEligible() { return isEligible; }
    public void setEligible(boolean eligible) { isEligible = eligible; }
    
    public String getRecommendationDate() { return recommendationDate; }
    public void setRecommendationDate(String recommendationDate) { this.recommendationDate = recommendationDate; }
}