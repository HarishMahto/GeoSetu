package harish.project.geosetu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import harish.project.geosetu.R;
import harish.project.geosetu.adapter.DSSRecommendationAdapter;
import harish.project.geosetu.database.AppDatabase;

public class DSSFragment extends Fragment {
    private RecyclerView rvRecommendations;
    private DSSRecommendationAdapter recommendationAdapter;
    private AppDatabase database;
    private int userId;
    private String userRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dss, container, false);
        
        getUserData();
        initViews(view);
        setupDatabase();
        setupRecyclerView();
        loadRecommendations();
        
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
        rvRecommendations = view.findViewById(R.id.rvRecommendations);
    }

    private void setupDatabase() {
        database = AppDatabase.getDatabase(getContext());
    }

    private void setupRecyclerView() {
        recommendationAdapter = new DSSRecommendationAdapter();
        rvRecommendations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecommendations.setAdapter(recommendationAdapter);
    }

    private void loadRecommendations() {
        if ("OFFICER".equals(userRole)) {
            // Officers see all recommendations
            database.dssRecommendationDao().getAllRecommendations().observe(getViewLifecycleOwner(), recommendations -> {
                if (recommendations != null) {
                    recommendationAdapter.updateRecommendations(recommendations);
                }
            });
        } else {
            // Patta holders see recommendations for their approved claims
            database.claimDao().getClaimsByUserId(userId).observe(getViewLifecycleOwner(), claims -> {
                if (claims != null) {
                    for (var claim : claims) {
                        if ("APPROVED".equals(claim.getStatus())) {
                            database.dssRecommendationDao().getRecommendationsByClaimId(claim.getClaimId())
                                    .observe(getViewLifecycleOwner(), recommendations -> {
                                        if (recommendations != null) {
                                            recommendationAdapter.addRecommendations(recommendations);
                                        }
                                    });
                        }
                    }
                }
            });
        }
    }
}