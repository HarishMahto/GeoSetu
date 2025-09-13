package harish.project.geosetu;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import harish.project.geosetu.fragment.DashboardFragment;
import harish.project.geosetu.fragment.FRAAtlasFragment;
import harish.project.geosetu.fragment.DSSFragment;
import harish.project.geosetu.fragment.ProfileFragment;
import harish.project.geosetu.database.AppDatabase;
import harish.project.geosetu.utils.DataInitializer;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private int userId;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Seed initial data if needed
        try {
            DataInitializer.initializeData(getApplicationContext());
            Log.d("MainActivity","DataInitializer invoked");
        } catch (Exception e) {
            Log.e("MainActivity","DataInitializer error", e);
        }

        getUserData();
        initViews();
        setupBottomNavigation();
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
        }
    }

    private void getUserData() {
        SharedPreferences prefs = getSharedPreferences("FRA_PREFS", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);
        userRole = prefs.getString("user_role", "");
    }

    private void initViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_dashboard) {
                selectedFragment = new DashboardFragment();
            } else if (itemId == R.id.nav_atlas) {
                selectedFragment = new FRAAtlasFragment();
            } else if (itemId == R.id.nav_dss) {
                selectedFragment = new DSSFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }
            
            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            Bundle args = new Bundle();
            args.putInt("user_id", userId);
            args.putString("user_role", userRole);
            fragment.setArguments(args);
            
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}