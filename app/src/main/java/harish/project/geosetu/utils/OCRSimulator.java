package harish.project.geosetu.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OCRSimulator {
    private static final String[] SAMPLE_NAMES = {
        "Ramesh Kumar", "Sunita Devi", "Mohan Singh", "Priya Sharma", "Rajesh Gond"
    };
    
    private static final String[] SAMPLE_VILLAGES = {
        "Khargone", "Barwani", "Betul", "Chhindwara", "Seoni"
    };
    
    private static final String[] SAMPLE_TRIBES = {
        "Gond", "Bhil", "Korku", "Baiga", "Sahariya"
    };
    
    private static final String[] CLAIM_TYPES = {
        "IFR", "CFR", "CR"
    };

    public static Map<String, String> simulateOCR(String documentPath) {
        Map<String, String> extractedData = new HashMap<>();
        Random random = new Random();
        
        // Simulate OCR extraction with random data
        extractedData.put("name", SAMPLE_NAMES[random.nextInt(SAMPLE_NAMES.length)]);
        extractedData.put("village", SAMPLE_VILLAGES[random.nextInt(SAMPLE_VILLAGES.length)]);
        extractedData.put("tribe", SAMPLE_TRIBES[random.nextInt(SAMPLE_TRIBES.length)]);
        extractedData.put("claimType", CLAIM_TYPES[random.nextInt(CLAIM_TYPES.length)]);
        extractedData.put("landArea", String.valueOf(1.0 + (random.nextDouble() * 4.0))); // 1-5 acres
        
        return extractedData;
    }
    
    public static String generateClaimId() {
        Random random = new Random();
        return "C" + (1000 + random.nextInt(9000));
    }
}