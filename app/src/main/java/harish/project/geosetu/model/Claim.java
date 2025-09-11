package harish.project.geosetu.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "claims")
public class Claim {
    @PrimaryKey
    @NonNull
    public String claimId;
    public int userId;
    public String applicantName;
    public String village;
    public String tribe;
    public String claimType; // IFR, CR, CFR
    public double landArea;
    public String documentPath;
    public String status; // PENDING, APPROVED, REJECTED
    public String submissionDate;
    public String reviewDate;
    public String reviewerComments;
    public String geoJsonData;

    public Claim() {}

    @Ignore
    public Claim(@NonNull String claimId, int userId, String applicantName, String village, String tribe, 
                 String claimType, double landArea, String documentPath, String status, String submissionDate) {
        this.claimId = claimId;
        this.userId = userId;
        this.applicantName = applicantName;
        this.village = village;
        this.tribe = tribe;
        this.claimType = claimType;
        this.landArea = landArea;
        this.documentPath = documentPath;
        this.status = status;
        this.submissionDate = submissionDate;
    }

    // Getters and setters
    @NonNull
    public String getClaimId() { return claimId; }
    public void setClaimId(@NonNull String claimId) { this.claimId = claimId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    
    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }
    
    public String getTribe() { return tribe; }
    public void setTribe(String tribe) { this.tribe = tribe; }
    
    public String getClaimType() { return claimType; }
    public void setClaimType(String claimType) { this.claimType = claimType; }
    
    public double getLandArea() { return landArea; }
    public void setLandArea(double landArea) { this.landArea = landArea; }
    
    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) { this.documentPath = documentPath; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(String submissionDate) { this.submissionDate = submissionDate; }
    
    public String getReviewDate() { return reviewDate; }
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }
    
    public String getReviewerComments() { return reviewerComments; }
    public void setReviewerComments(String reviewerComments) { this.reviewerComments = reviewerComments; }
    
    public String getGeoJsonData() { return geoJsonData; }
    public void setGeoJsonData(String geoJsonData) { this.geoJsonData = geoJsonData; }
}