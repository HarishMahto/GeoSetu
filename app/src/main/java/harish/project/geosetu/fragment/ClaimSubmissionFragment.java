package harish.project.geosetu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import harish.project.geosetu.R;
import harish.project.geosetu.model.Claim;
import harish.project.geosetu.utils.OCRSimulator;
import harish.project.geosetu.viewmodel.ClaimViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ClaimSubmissionFragment extends Fragment {
    private EditText etName, etVillage, etTribe, etLandArea;
    private Spinner spinnerClaimType;
    private TextView tvSelectedDocument;
    private Button btnUploadDocument, btnSimulateOCR, btnSubmitClaim;
    private ClaimViewModel claimViewModel;
    private int userId;
    private String selectedDocumentPath;

    private ActivityResultLauncher<Intent> documentPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri documentUri = result.getData().getData();
                    if (documentUri != null) {
                        selectedDocumentPath = documentUri.toString();
                        tvSelectedDocument.setText("Document selected: " + documentUri.getLastPathSegment());
                        btnSimulateOCR.setEnabled(true);
                    }
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_claim_submission, container, false);
        
        getUserData();
        initViews(view);
        setupSpinner();
        setupViewModel();
        setupClickListeners();
        
        return view;
    }

    private void getUserData() {
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getInt("user_id", -1);
        }
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etVillage = view.findViewById(R.id.etVillage);
        etTribe = view.findViewById(R.id.etTribe);
        etLandArea = view.findViewById(R.id.etLandArea);
        spinnerClaimType = view.findViewById(R.id.spinnerClaimType);
        tvSelectedDocument = view.findViewById(R.id.tvSelectedDocument);
        btnUploadDocument = view.findViewById(R.id.btnUploadDocument);
        btnSimulateOCR = view.findViewById(R.id.btnSimulateOCR);
        btnSubmitClaim = view.findViewById(R.id.btnSubmitClaim);
        
        btnSimulateOCR.setEnabled(false);
    }

    private void setupSpinner() {
        String[] claimTypes = {"IFR (Individual Forest Rights)", "CFR (Community Forest Rights)", "CR (Community Rights)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, claimTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClaimType.setAdapter(adapter);
    }

    private void setupViewModel() {
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);
        
        claimViewModel.getOperationResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                if (result.contains("successfully")) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });
    }

    private void setupClickListeners() {
        btnUploadDocument.setOnClickListener(v -> openDocumentPicker());
        btnSimulateOCR.setOnClickListener(v -> simulateOCR());
        btnSubmitClaim.setOnClickListener(v -> submitClaim());
    }

    private void openDocumentPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        documentPickerLauncher.launch(Intent.createChooser(intent, "Select Document"));
    }

    private void simulateOCR() {
        if (selectedDocumentPath != null) {
            Map<String, String> extractedData = OCRSimulator.simulateOCR(selectedDocumentPath);
            
            // Auto-fill form fields with extracted data
            etName.setText(extractedData.get("name"));
            etVillage.setText(extractedData.get("village"));
            etTribe.setText(extractedData.get("tribe"));
            etLandArea.setText(extractedData.get("landArea"));
            
            // Set claim type in spinner
            String claimType = extractedData.get("claimType");
            String[] claimTypes = {"IFR", "CFR", "CR"};
            for (int i = 0; i < claimTypes.length; i++) {
                if (claimTypes[i].equals(claimType)) {
                    spinnerClaimType.setSelection(i);
                    break;
                }
            }
            
            Toast.makeText(getContext(), "OCR completed! Form auto-filled.", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitClaim() {
        String name = etName.getText().toString().trim();
        String village = etVillage.getText().toString().trim();
        String tribe = etTribe.getText().toString().trim();
        String landAreaStr = etLandArea.getText().toString().trim();
        String claimTypeSelection = spinnerClaimType.getSelectedItem().toString();
        
        if (name.isEmpty() || village.isEmpty() || tribe.isEmpty() || landAreaStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedDocumentPath == null) {
            Toast.makeText(getContext(), "Please upload a document", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            double landArea = Double.parseDouble(landAreaStr);
            String claimType = claimTypeSelection.substring(0, claimTypeSelection.indexOf(" "));
            
            String claimId = OCRSimulator.generateClaimId();
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            
            Claim claim = new Claim(claimId, userId, name, village, tribe, claimType, 
                                  landArea, selectedDocumentPath, "PENDING", currentDate);
            
            // Generate dummy GeoJSON for the claim
            String geoJson = generateDummyGeoJSON(village);
            claim.setGeoJsonData(geoJson);
            
            claimViewModel.insertClaim(claim);
            
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter a valid land area", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateDummyGeoJSON(String village) {
        // Generate dummy coordinates based on village name
        double baseLat = 22.0 + (village.hashCode() % 100) / 1000.0;
        double baseLng = 77.0 + (village.hashCode() % 100) / 1000.0;
        
        return String.format(Locale.US, 
            "{\"type\":\"Polygon\",\"coordinates\":[[[%.6f,%.6f],[%.6f,%.6f],[%.6f,%.6f],[%.6f,%.6f],[%.6f,%.6f]]]}",
            baseLng, baseLat,
            baseLng + 0.001, baseLat,
            baseLng + 0.001, baseLat + 0.001,
            baseLng, baseLat + 0.001,
            baseLng, baseLat
        );
    }
}