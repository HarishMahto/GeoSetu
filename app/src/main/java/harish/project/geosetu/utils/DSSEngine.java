package harish.project.geosetu.utils;

import harish.project.geosetu.model.Claim;
import harish.project.geosetu.model.DSSRecommendation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DSSEngine {
    
    public static List<DSSRecommendation> generateRecommendations(Claim claim) {
        List<DSSRecommendation> recommendations = new ArrayList<>();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        
        // Rule 1: PM-KISAN eligibility
        if (claim.getLandArea() >= 2.0) {
            DSSRecommendation pmKisan = new DSSRecommendation(
                claim.getClaimId(),
                "PM-KISAN",
                "Pradhan Mantri Kisan Samman Nidhi",
                "Farmland >= 2 acres",
                "₹6000 per year in 3 installments",
                true,
                currentDate,
                "Land area " + claim.getLandArea() + " acres >= 2.0 threshold"
            );
            recommendations.add(pmKisan);
        }
        
        // Rule 2: Jal Jeevan Mission eligibility (simulate based on village)
        if (!claim.getVillage().equals("Khargone")) { // Simulate no water source
            DSSRecommendation jalJeevan = new DSSRecommendation(
                claim.getClaimId(),
                "Jal Jeevan Mission",
                "Har Ghar Jal Scheme",
                "No water source within 500m",
                "Piped water connection to household",
                true,
                currentDate,
                "Village " + claim.getVillage() + " flagged for water access gap"
            );
            recommendations.add(jalJeevan);
        }
        
        // Rule 3: MGNREGA eligibility for community forest
        if (claim.getClaimType().equals("CFR") && claim.getLandArea() >= 3.0) {
            DSSRecommendation mgnrega = new DSSRecommendation(
                claim.getClaimId(),
                "MGNREGA",
                "Mahatma Gandhi National Rural Employment Guarantee Act",
                "Community forest area >= 3 acres",
                "100 days guaranteed employment for pond construction",
                true,
                currentDate,
                "CFR area " + claim.getLandArea() + " acres >= 3.0"
            );
            recommendations.add(mgnrega);
        }
        
        // Rule 4: Forest Conservation Scheme
        if (claim.getClaimType().equals("IFR") || claim.getClaimType().equals("CFR")) {
            DSSRecommendation forestConservation = new DSSRecommendation(
                claim.getClaimId(),
                "Forest Conservation Scheme",
                "Incentive for forest protection",
                "Individual or Community Forest Rights",
                "₹2000 per acre annually for forest conservation",
                true,
                currentDate,
                "Claim type " + claim.getClaimType() + " qualifies"
            );
            recommendations.add(forestConservation);
        }
        
        // Rule 5: Tribal Development Scheme
        if (claim.getTribe().equals("Gond") || claim.getTribe().equals("Bhil")) {
            DSSRecommendation tribalDev = new DSSRecommendation(
                claim.getClaimId(),
                "Tribal Development Scheme",
                "Special assistance for scheduled tribes",
                "Belongs to Scheduled Tribe",
                "Educational and livelihood support",
                true,
                currentDate,
                "Tribe " + claim.getTribe() + " is eligible"
            );
            recommendations.add(tribalDev);
        }
        
        return recommendations;
    }
}