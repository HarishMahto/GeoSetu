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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import harish.project.geosetu.R;
import harish.project.geosetu.adapter.ClaimAdapter;
import harish.project.geosetu.viewmodel.ClaimViewModel;

public class DashboardFragment extends Fragment {
    private TextView tvWelcome, tvPendingCount, tvApprovedCount;
    private RecyclerView rvClaims;
    private MaterialCardView cardNewClaim;
    private FloatingActionButton fabNewClaim;
    private ClaimViewModel claimViewModel;
    private ClaimAdapter claimAdapter;
    private int userId;
    private String userRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        getUserData();
        initViews(view);
        setupViewModel();
        setupRecyclerView();
        setupClickListeners();
        
        return view;
    }

    private void getUserData() {
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getInt("user_id", -1);
            userRole = args.getString("user_role", "");
        }
    }

    private void initViews(View view) {
        tvWelcome = view.findViewById(R.id.tvWelcome);
        tvPendingCount = view.findViewById(R.id.tvPendingCount);
        tvApprovedCount = view.findViewById(R.id.tvApprovedCount);
        rvClaims = view.findViewById(R.id.rvClaims);
        cardNewClaim = view.findViewById(R.id.cardNewClaim);
        fabNewClaim = view.findViewById(R.id.fabNewClaim);
        
        // Set welcome message based on role
        if ("OFFICER".equals(userRole)) {
            tvWelcome.setText("Welcome, District Officer");
            cardNewClaim.setVisibility(View.GONE);
            fabNewClaim.setVisibility(View.GONE);
        } else {
            tvWelcome.setText("Welcome, Patta Holder");
            cardNewClaim.setVisibility(View.VISIBLE);
            fabNewClaim.setVisibility(View.VISIBLE);
        }
    }

    private void setupViewModel() {
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);
        
        // Load claims based on user role
        if ("OFFICER".equals(userRole)) {
            // Officer sees all claims
            claimViewModel.getAllClaims().observe(getViewLifecycleOwner(), claims -> {
                if (claims != null) {
                    claimAdapter.updateClaims(claims);
                }
            });
            
            // Show claim counts
            claimViewModel.getClaimCountByStatus("PENDING").observe(getViewLifecycleOwner(), count -> {
                tvPendingCount.setText(String.valueOf(count != null ? count : 0));
            });
            
            claimViewModel.getClaimCountByStatus("APPROVED").observe(getViewLifecycleOwner(), count -> {
                tvApprovedCount.setText(String.valueOf(count != null ? count : 0));
            });
        } else {
            // Patta holder sees only their claims
            claimViewModel.getClaimsByUserId(userId).observe(getViewLifecycleOwner(), claims -> {
                if (claims != null) {
                    claimAdapter.updateClaims(claims);
                    
                    // Count claims by status
                    long pendingCount = claims.stream().filter(c -> "PENDING".equals(c.getStatus())).count();
                    long approvedCount = claims.stream().filter(c -> "APPROVED".equals(c.getStatus())).count();
                    
                    tvPendingCount.setText(String.valueOf(pendingCount));
                    tvApprovedCount.setText(String.valueOf(approvedCount));
                }
            });
        }
    }

    private void setupRecyclerView() {
        claimAdapter = new ClaimAdapter(userRole, this::onClaimClick);
        rvClaims.setLayoutManager(new LinearLayoutManager(getContext()));
        rvClaims.setAdapter(claimAdapter);
    }

    private void setupClickListeners() {
        if (fabNewClaim != null) {
            fabNewClaim.setOnClickListener(v -> openNewClaimFragment());
        }
        
        if (cardNewClaim != null) {
            cardNewClaim.setOnClickListener(v -> openNewClaimFragment());
        }
    }

    private void onClaimClick(String claimId) {
        if ("OFFICER".equals(userRole)) {
            openClaimReviewFragment(claimId);
        } else {
            openClaimDetailsFragment(claimId);
        }
    }

    private void openNewClaimFragment() {
        ClaimSubmissionFragment fragment = new ClaimSubmissionFragment();
        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        fragment.setArguments(args);
        
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openClaimReviewFragment(String claimId) {
        ClaimReviewFragment fragment = new ClaimReviewFragment();
        Bundle args = new Bundle();
        args.putString("claim_id", claimId);
        args.putInt("user_id", userId);
        fragment.setArguments(args);
        
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openClaimDetailsFragment(String claimId) {
        ClaimDetailsFragment fragment = new ClaimDetailsFragment();
        Bundle args = new Bundle();
        args.putString("claim_id", claimId);
        args.putInt("user_id", userId);
        fragment.setArguments(args);
        
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}