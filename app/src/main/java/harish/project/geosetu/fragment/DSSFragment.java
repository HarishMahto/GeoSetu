package harish.project.geosetu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import harish.project.geosetu.R;
import harish.project.geosetu.adapter.DSSRecommendationAdapter;
import harish.project.geosetu.database.AppDatabase;
import harish.project.geosetu.model.Claim;
import harish.project.geosetu.model.DSSRecommendation;
import harish.project.geosetu.model.Notification;
import harish.project.geosetu.viewmodel.DSSRecommendationViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class DSSFragment extends Fragment {
    private RecyclerView rvRecommendations;
    private DSSRecommendationAdapter recommendationAdapter;
    private AppDatabase database;
    private int userId;
    private String userRole;
    private DSSRecommendationViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dss, container, false);
        getUserData();
        initViews(view);
        setupDatabase();
        seedDummyIfEmpty();
        setupViewModel();
        setupRecyclerView();
        loadRecommendations();
        refreshRemote();
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

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DSSRecommendationViewModel.class);
    }

    private void setupRecyclerView() {
        recommendationAdapter = new DSSRecommendationAdapter();
        recommendationAdapter.setActionListener(new DSSRecommendationAdapter.ActionListener() {
            @Override
            public void onGenerateClaim(DSSRecommendation rec) {
                createNotification(userId, "Claim Generation", "Generated welfare claim for scheme: " + rec.getSchemeName());
                Toast.makeText(getContext(), "Claim generated (stub)", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onForward(DSSRecommendation rec) {
                createNotification(userId, "Scheme Forwarded", "Forwarded to scheme office: " + rec.getSchemeName());
                Toast.makeText(getContext(), "Forwarded (stub)", Toast.LENGTH_SHORT).show();
            }
        });
        rvRecommendations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecommendations.setAdapter(recommendationAdapter);
    }

    private void loadRecommendations() {
        if ("OFFICER".equals(userRole)) {
            database.dssRecommendationDao().getAllRecommendations().observe(getViewLifecycleOwner(), recommendations -> {
                if (recommendations != null) {
                    recommendationAdapter.updateRecommendations(recommendations);
                }
            });
        } else {
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

    private void refreshRemote() {
        viewModel.refreshForUser(userId);
    }

    private void createNotification(int targetUserId, String title, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Notification notification = new Notification(targetUserId, title, message, timestamp, "DSS_RECOMMENDATION");
        database.notificationDao().insert(notification);
    }

    private void seedDummyIfEmpty() {
        try {
            int count = database.dssRecommendationDao().getRecommendationCount();
            if (count == 0) {
                // Create a dummy approved claim (so PATTA_HOLDER users also see data)
                String claimId = "DUMMY-" + UUID.randomUUID().toString().substring(0,8);
                Claim dummyClaim = new Claim(claimId, userId <= 0 ? 9999 : userId, "Demo Applicant", "DemoVillage", "DemoTribe", "IFR", 2.2, "/documents/demo.pdf", "APPROVED", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                database.claimDao().insert(dummyClaim);

                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                DSSRecommendation r1 = new DSSRecommendation(claimId, "PM-KISAN", "Pradhan Mantri Kisan Samman Nidhi", "Land >= 2 acres", "₹6000/year", true, today, "Dummy seed: land 2.2 acres");
                DSSRecommendation r2 = new DSSRecommendation(claimId, "Jal Jeevan Mission", "Har Ghar Jal", "Water gap detected", "Household tap", true, today, "Dummy seed: no water source flagged");
                DSSRecommendation r3 = new DSSRecommendation(claimId, "MGNREGA", "Rural Employment", "Community effort eligible", "100 days work", true, today, "Dummy seed: demo eligibility");
                database.dssRecommendationDao().insert(r1);
                database.dssRecommendationDao().insert(r2);
                database.dssRecommendationDao().insert(r3);
            }
        } catch (Exception e) {
            // Silent fail; optional log
        }
    }
}