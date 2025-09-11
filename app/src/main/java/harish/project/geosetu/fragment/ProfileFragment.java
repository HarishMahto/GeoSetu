package harish.project.geosetu.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import harish.project.geosetu.R;
import harish.project.geosetu.activity.LoginActivity;
import harish.project.geosetu.adapter.NotificationAdapter;
import harish.project.geosetu.database.AppDatabase;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    private TextView tvUserName, tvUserRole, tvUserVillage;
    private RecyclerView rvNotifications;
    private Button btnLogout;
    private NotificationAdapter notificationAdapter;
    private AppDatabase database;
    private int userId;
    private String userRole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        getUserData();
        initViews(view);
        setupDatabase();
        setupRecyclerView();
        loadUserProfile();
        loadNotifications();
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
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserRole = view.findViewById(R.id.tvUserRole);
        tvUserVillage = view.findViewById(R.id.tvUserVillage);
        rvNotifications = view.findViewById(R.id.rvNotifications);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void setupDatabase() {
        database = AppDatabase.getDatabase(getContext());
    }

    private void setupRecyclerView() {
        notificationAdapter = new NotificationAdapter();
        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotifications.setAdapter(notificationAdapter);
    }

    private void loadUserProfile() {
        database.userDao().getUserById(userId).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tvUserName.setText(user.getName());
                tvUserRole.setText(user.getRole().equals("OFFICER") ? "District Officer" : "Patta Holder");
                tvUserVillage.setText(user.getVillage().isEmpty() ? "District Office" : user.getVillage());
            }
        });
    }

    private void loadNotifications() {
        database.notificationDao().getNotificationsByUserId(userId).observe(getViewLifecycleOwner(), notifications -> {
            if (notifications != null) {
                notificationAdapter.updateNotifications(notifications);
            }
        });
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        // Clear user session
        SharedPreferences prefs = getActivity().getSharedPreferences("FRA_PREFS", MODE_PRIVATE);
        prefs.edit().clear().apply();
        
        // Navigate to login
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}