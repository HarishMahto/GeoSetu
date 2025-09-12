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

        if (claim.getLandArea() >= 2.0) {
            DSSRecommendation pmKisan = new DSSRecommendation(
                    claim.getClaimId(),
                    "PM-KISAN",
                    "Pradhan Mantri Kisan Samman Nidhi",
                    "Farmland ≥ 2 acres",
                    "₹6000 per year in 3 installments",
                    true,
                    currentDate
            );
            recommendations.add(pmKisan);
        }

        if (!"Khargone".equalsIgnoreCase(claim.getVillage())) {
            DSSRecommendation jalJeevan = new DSSRecommendation(
                    claim.getClaimId(),
                    "Jal Jeevan Mission",
                    "Har Ghar Jal Scheme",
                    "No water source within 500m",
                    "Piped water connection to household",
                    true,
                    currentDate
            );
            recommendations.add(jalJeevan);
        }

        if ("CFR".equalsIgnoreCase(claim.getClaimType()) && claim.getLandArea() >= 3.0) {
            DSSRecommendation mgnrega = new DSSRecommendation(
                    claim.getClaimId(),
                    "MGNREGA",
                    "Mahatma Gandhi National Rural Employment Guarantee Act",
                    "Community forest area ≥ 3 acres",
                    "100 days guaranteed employment for pond construction",
                    true,
                    currentDate
            );
            recommendations.add(mgnrega);
        }

        if ("IFR".equalsIgnoreCase(claim.getClaimType()) || "CFR".equalsIgnoreCase(claim.getClaimType())) {
            DSSRecommendation forestConservation = new DSSRecommendation(
                    claim.getClaimId(),
                    "Forest Conservation Scheme",
                    "Incentive for forest protection",
                    "Individual or Community Forest Rights",
                    "₹2000 per acre annually for forest conservation",
                    true,
                    currentDate
            );
            recommendations.add(forestConservation);
        }

        if ("Gond".equalsIgnoreCase(claim.getTribe()) || "Bhil".equalsIgnoreCase(claim.getTribe())) {
            DSSRecommendation tribalDev = new DSSRecommendation(
                    claim.getClaimId(),
                    "Tribal Development Scheme",
                    "Special assistance for scheduled tribes",
                    "Belongs to Scheduled Tribe (Gond/Bhil)",
                    "Educational and livelihood support",
                    true,
                    currentDate
            );
            recommendations.add(tribalDev);
        }

        return recommendations;
    }
}
