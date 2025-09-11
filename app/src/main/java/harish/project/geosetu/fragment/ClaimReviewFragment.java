package harish.project.geosetu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import harish.project.geosetu.R;
import harish.project.geosetu.model.Claim;
import harish.project.geosetu.viewmodel.ClaimViewModel;

public class ClaimReviewFragment extends Fragment {
    private TextView tvClaimId, tvApplicantName, tvVillage, tvTribe, tvClaimType, tvLandArea, tvSubmissionDate, tvDocumentPath;
    private EditText etReviewComments;
    private Button btnApprove, btnReject;
    private ClaimViewModel claimViewModel;
    private String claimId;
    private int userId;
    private Claim currentClaim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_claim_review, container, false);
        
        getArgumentData();
        initViews(view);
        setupViewModel();
        setupClickListeners();
        loadClaimDetails();
        
        return view;
    }

    private void getArgumentData() {
        Bundle args = getArguments();
        if (args != null) {
            claimId = args.getString("claim_id");
            userId = args.getInt("user_id", -1);
        }
    }

    private void initViews(View view) {
        tvClaimId = view.findViewById(R.id.tvClaimId);
        tvApplicantName = view.findViewById(R.id.tvApplicantName);
        tvVillage = view.findViewById(R.id.tvVillage);
        tvTribe = view.findViewById(R.id.tvTribe);
        tvClaimType = view.findViewById(R.id.tvClaimType);
        tvLandArea = view.findViewById(R.id.tvLandArea);
        tvSubmissionDate = view.findViewById(R.id.tvSubmissionDate);
        tvDocumentPath = view.findViewById(R.id.tvDocumentPath);
        etReviewComments = view.findViewById(R.id.etReviewComments);
        btnApprove = view.findViewById(R.id.btnApprove);
        btnReject = view.findViewById(R.id.btnReject);
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
        btnApprove.setOnClickListener(v -> reviewClaim("APPROVED"));
        btnReject.setOnClickListener(v -> reviewClaim("REJECTED"));
    }

    private void loadClaimDetails() {
        claimViewModel.getClaimById(claimId).observe(getViewLifecycleOwner(), claim -> {
            if (claim != null) {
                currentClaim = claim;
                displayClaimDetails(claim);
            }
        });
    }

    private void displayClaimDetails(Claim claim) {
        tvClaimId.setText("Claim ID: " + claim.getClaimId());
        tvApplicantName.setText("Applicant: " + claim.getApplicantName());
        tvVillage.setText("Village: " + claim.getVillage());
        tvTribe.setText("Tribe: " + claim.getTribe());
        tvClaimType.setText("Claim Type: " + claim.getClaimType());
        tvLandArea.setText("Land Area: " + claim.getLandArea() + " acres");
        tvSubmissionDate.setText("Submitted: " + claim.getSubmissionDate());
        tvDocumentPath.setText("Document: " + (claim.getDocumentPath() != null ? claim.getDocumentPath() : "No document"));
        
        // Disable buttons if already reviewed
        if (!"PENDING".equals(claim.getStatus())) {
            btnApprove.setEnabled(false);
            btnReject.setEnabled(false);
            etReviewComments.setText(claim.getReviewerComments());
            etReviewComments.setEnabled(false);
        }
    }

    private void reviewClaim(String status) {
        String comments = etReviewComments.getText().toString().trim();
        
        if (comments.isEmpty()) {
            Toast.makeText(getContext(), "Please enter review comments", Toast.LENGTH_SHORT).show();
            return;
        }
        
        claimViewModel.updateClaimStatus(claimId, status, comments, userId);
    }
}