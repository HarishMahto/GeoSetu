package harish.project.geosetu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import harish.project.geosetu.R;
import harish.project.geosetu.adapter.DSSRecommendationAdapter;
import harish.project.geosetu.database.AppDatabase;
import harish.project.geosetu.model.Claim;
import harish.project.geosetu.viewmodel.ClaimViewModel;

public class ClaimDetailsFragment extends Fragment {
    private TextView tvClaimId, tvApplicantName, tvVillage, tvTribe, tvClaimType, tvLandArea, tvStatus, tvSubmissionDate, tvReviewDate, tvReviewComments;
    private RecyclerView rvRecommendations;
    private ClaimViewModel claimViewModel;
    private DSSRecommendationAdapter recommendationAdapter;
    private AppDatabase database;
    private String claimId;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_claim_details, container, false);
        
        getArgumentData();
        initViews(view);
        setupDatabase();
        setupViewModel();
        setupRecyclerView();
        loadClaimDetails();
        loadRecommendations();
        
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
        tvStatus = view.findViewById(R.id.tvStatus);
        tvSubmissionDate = view.findViewById(R.id.tvSubmissionDate);
        tvReviewDate = view.findViewById(R.id.tvReviewDate);
        tvReviewComments = view.findViewById(R.id.tvReviewComments);
        rvRecommendations = view.findViewById(R.id.rvRecommendations);
    }

    private void setupDatabase() {
        database = AppDatabase.getDatabase(getContext());
    }

    private void setupViewModel() {
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);
    }

    private void setupRecyclerView() {
        recommendationAdapter = new DSSRecommendationAdapter();
        rvRecommendations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecommendations.setAdapter(recommendationAdapter);
    }

    private void loadClaimDetails() {
        claimViewModel.getClaimById(claimId).observe(getViewLifecycleOwner(), claim -> {
            if (claim != null) {
                displayClaimDetails(claim);
            }
        });
    }

    private void loadRecommendations() {
        database.dssRecommendationDao().getRecommendationsByClaimId(claimId)
                .observe(getViewLifecycleOwner(), recommendations -> {
                    if (recommendations != null) {
                        recommendationAdapter.updateRecommendations(recommendations);
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
        tvStatus.setText("Status: " + claim.getStatus());
        tvSubmissionDate.setText("Submitted: " + claim.getSubmissionDate());
        
        // Set status color
        int statusColor;
        switch (claim.getStatus()) {
            case "APPROVED":
                statusColor = getContext().getColor(android.R.color.holo_green_dark);
                break;
            case "REJECTED":
                statusColor = getContext().getColor(android.R.color.holo_red_dark);
                break;
            default:
                statusColor = getContext().getColor(android.R.color.holo_orange_dark);
                break;
        }
        tvStatus.setTextColor(statusColor);
        
        // Show review details if available
        if (claim.getReviewDate() != null && !claim.getReviewDate().isEmpty()) {
            tvReviewDate.setText("Reviewed: " + claim.getReviewDate());
            tvReviewDate.setVisibility(View.VISIBLE);
        } else {
            tvReviewDate.setVisibility(View.GONE);
        }
        
        if (claim.getReviewerComments() != null && !claim.getReviewerComments().isEmpty()) {
            tvReviewComments.setText("Comments: " + claim.getReviewerComments());
            tvReviewComments.setVisibility(View.VISIBLE);
        } else {
            tvReviewComments.setVisibility(View.GONE);
        }
    }
}