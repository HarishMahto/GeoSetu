package harish.project.geosetu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import harish.project.geosetu.R;
import harish.project.geosetu.model.Notification;
import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<Notification> notifications = new ArrayList<>();

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void updateNotifications(List<Notification> newNotifications) {
        this.notifications = newNotifications;
        notifyDataSetChanged();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvMessage, tvTimestamp, tvType;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvType = itemView.findViewById(R.id.tvType);
        }

        public void bind(Notification notification) {
            tvTitle.setText(notification.getTitle());
            tvMessage.setText(notification.getMessage());
            tvTimestamp.setText(notification.getTimestamp());
            tvType.setText(notification.getType());

            // Set background based on read status
            if (!notification.isRead()) {
                itemView.setBackgroundColor(itemView.getContext().getColor(R.color.background_light));
            } else {
                itemView.setBackgroundColor(itemView.getContext().getColor(android.R.color.transparent));
            }

            // Set type icon
            String typeIcon = getTypeIcon(notification.getType());
            tvType.setText(typeIcon + " " + notification.getType());
        }

        private String getTypeIcon(String type) {
            switch (type) {
                case "CLAIM_UPDATE":
                    return "📋";
                case "DSS_RECOMMENDATION":
                    return "⭐";
                default:
                    return "📢";
            }
        }
    }
}