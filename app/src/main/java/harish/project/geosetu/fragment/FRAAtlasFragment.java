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
import com.google.android.material.tabs.TabLayout;
import harish.project.geosetu.R;
import harish.project.geosetu.adapter.ClaimAdapter;
import harish.project.geosetu.viewmodel.ClaimViewModel;

public class FRAAtlasFragment extends Fragment {
    private TextView tvMapPlaceholder;
    private TabLayout tabLayout;
    private RecyclerView rvMapClaims;
    private ClaimViewModel claimViewModel;
    private ClaimAdapter claimAdapter;
    private int userId;
    private String userRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fra_atlas, container, false);
        
        getUserData();
        initViews(view);
        setupViewModel();
        setupTabs();
        setupRecyclerView();
        
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
        tvMapPlaceholder = view.findViewById(R.id.tvMapPlaceholder);
        tabLayout = view.findViewById(R.id.tabLayout);
        rvMapClaims = view.findViewById(R.id.rvMapClaims);
    }

    private void setupViewModel() {
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("All Claims"));
        tabLayout.addTab(tabLayout.newTab().setText("Approved"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Assets"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadDataForTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        
        // Load initial data
        loadDataForTab(0);
    }

    private void setupRecyclerView() {
        claimAdapter = new ClaimAdapter(userRole, this::onClaimClick);
        rvMapClaims.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMapClaims.setAdapter(claimAdapter);
    }

    private void loadDataForTab(int position) {
        switch (position) {
            case 0: // All Claims
                claimViewModel.getAllClaims().observe(getViewLifecycleOwner(), claims -> {
                    if (claims != null) {
                        claimAdapter.updateClaims(claims);
                        updateMapPlaceholder("Showing all claims on map");
                    }
                });
                break;
            case 1: // Approved
                claimViewModel.getClaimsByStatus("APPROVED").observe(getViewLifecycleOwner(), claims -> {
                    if (claims != null) {
                        claimAdapter.updateClaims(claims);
                        updateMapPlaceholder("Showing approved claims (green polygons)");
                    }
                });
                break;
            case 2: // Pending
                claimViewModel.getClaimsByStatus("PENDING").observe(getViewLifecycleOwner(), claims -> {
                    if (claims != null) {
                        claimAdapter.updateClaims(claims);
                        updateMapPlaceholder("Showing pending claims (orange polygons)");
                    }
                });
                break;
            case 3: // Assets
                updateMapPlaceholder("Showing village assets: Water bodies, Farms, Forest patches");
                claimAdapter.updateClaims(java.util.Collections.emptyList());
                break;
        }
    }

    private void updateMapPlaceholder(String text) {
        tvMapPlaceholder.setText("🗺️ Interactive Map View\n\n" + text + 
                "\n\n• Click on polygons to view claim details" +
                "\n• Village boundaries shown in blue" +
                "\n• Forest areas in green shade" +
                "\n• Water bodies in blue" +
                "\n\n(Google Maps integration would be implemented here)");
    }

    private void onClaimClick(String claimId) {
        // Navigate to claim details or review based on user role
        if ("OFFICER".equals(userRole)) {
            openClaimReviewFragment(claimId);
        } else {
            openClaimDetailsFragment(claimId);
        }
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