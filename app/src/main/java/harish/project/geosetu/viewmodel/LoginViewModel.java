package harish.project.geosetu.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import harish.project.geosetu.model.User;
import harish.project.geosetu.repository.UserRepository;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MutableLiveData<User> authenticatedUser;
    private MutableLiveData<String> loginError;
    private ExecutorService executor;

    public LoginViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        authenticatedUser = new MutableLiveData<>();
        loginError = new MutableLiveData<>();
        executor = Executors.newSingleThreadExecutor();
    }

    public MutableLiveData<User> getAuthenticatedUser() {
        return authenticatedUser;
    }

    public MutableLiveData<String> getLoginError() {
        return loginError;
    }

    public void authenticate(String username, String password) {
        executor.execute(() -> {
            try {
                User user = userRepository.authenticate(username, password);
                if (user != null) {
                    authenticatedUser.postValue(user);
                } else {
                    loginError.postValue("Invalid username or password");
                }
            } catch (Exception e) {
                loginError.postValue("Login failed: " + e.getMessage());
            }
        });
    }
}