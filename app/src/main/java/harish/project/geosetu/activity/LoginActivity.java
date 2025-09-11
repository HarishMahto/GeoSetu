package harish.project.geosetu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import harish.project.geosetu.MainActivity;
import harish.project.geosetu.R;
import harish.project.geosetu.model.User;
import harish.project.geosetu.utils.DataInitializer;
import harish.project.geosetu.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private LoginViewModel loginViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize data on first run
        DataInitializer.initializeData(this);

        initViews();
        setupViewModel();
        setupClickListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        sharedPreferences = getSharedPreferences("FRA_PREFS", MODE_PRIVATE);
    }

    private void setupViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        
        loginViewModel.getAuthenticatedUser().observe(this, user -> {
            if (user != null) {
                saveUserSession(user);
                navigateToDashboard(user);
            }
        });

        loginViewModel.getLoginError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            loginViewModel.authenticate(username, password);
        });
    }

    private void saveUserSession(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", user.getId());
        editor.putString("username", user.getUsername());
        editor.putString("user_role", user.getRole());
        editor.putString("user_name", user.getName());
        editor.apply();
    }

    private void navigateToDashboard(User user) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user_id", user.getId());
        intent.putExtra("user_role", user.getRole());
        startActivity(intent);
        finish();
    }
}