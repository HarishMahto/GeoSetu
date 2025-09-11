package harish.project.geosetu.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String title;
    public String message;
    public String timestamp;
    public boolean isRead;
    public String type; // CLAIM_UPDATE, DSS_RECOMMENDATION, GENERAL

    public Notification() {}

    @Ignore
    public Notification(int userId, String title, String message, String timestamp, String type) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.isRead = false;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}