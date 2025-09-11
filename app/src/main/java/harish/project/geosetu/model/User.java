package harish.project.geosetu.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    public int id;
    public String username;
    public String password;
    public String role; // "OFFICER" or "PATTA_HOLDER"
    public String name;
    public String village;
    public String tribe;

    public User() {}

    @Ignore
    public User(int id, String username, String password, String role, String name, String village, String tribe) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.village = village;
        this.tribe = tribe;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }
    
    public String getTribe() { return tribe; }
    public void setTribe(String tribe) { this.tribe = tribe; }
}