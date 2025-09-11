package harish.project.geosetu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import harish.project.geosetu.R;
import harish.project.geosetu.model.Claim;
import java.util.ArrayList;
import java.util.List;

public class ClaimAdapter extends RecyclerView.Adapter<ClaimAdapter.ClaimViewHolder> {
    private List<Claim> claims = new ArrayList<>();
    private String userRole;
    private OnClaimClickListener listener;

    public interface OnClaimClickListener {
        void onClaimClick(String claimId);
    }

    public ClaimAdapter(String userRole, OnClaimClickListener listener) {
        this.userRole = userRole;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClaimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_claim, parent, false);
        return new ClaimViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimViewHolder holder, int position) {
        Claim claim = claims.get(position);
        holder.bind(claim);
    }

    @Override
    public int getItemCount() {
        return claims.size();
    }

    public void updateClaims(List<Claim> newClaims) {
        this.claims = newClaims;
        notifyDataSetChanged();
    }

    class ClaimViewHolder extends RecyclerView.ViewHolder {
        private TextView tvClaimId, tvApplicantName, tvVillage, tvClaimType, tvLandArea, tvStatus, tvSubmissionDate;

        public ClaimViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClaimId = itemView.findViewById(R.id.tvClaimId);
            tvApplicantName = itemView.findViewById(R.id.tvApplicantName);
            tvVillage = itemView.findViewById(R.id.tvVillage);
            tvClaimType = itemView.findViewById(R.id.tvClaimType);
            tvLandArea = itemView.findViewById(R.id.tvLandArea);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvSubmissionDate = itemView.findViewById(R.id.tvSubmissionDate);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onClaimClick(claims.get(position).getClaimId());
                }
            });
        }

        public void bind(Claim claim) {
            tvClaimId.setText("Claim ID: " + claim.getClaimId());
            tvApplicantName.setText("Applicant: " + claim.getApplicantName());
            tvVillage.setText("Village: " + claim.getVillage());
            tvClaimType.setText("Type: " + claim.getClaimType());
            tvLandArea.setText("Area: " + claim.getLandArea() + " acres");
            tvStatus.setText("Status: " + claim.getStatus());
            tvSubmissionDate.setText("Submitted: " + claim.getSubmissionDate());

            // Set status color
            int statusColor;
            switch (claim.getStatus()) {
                case "APPROVED":
                    statusColor = itemView.getContext().getColor(android.R.color.holo_green_dark);
                    break;
                case "REJECTED":
                    statusColor = itemView.getContext().getColor(android.R.color.holo_red_dark);
                    break;
                default:
                    statusColor = itemView.getContext().getColor(android.R.color.holo_orange_dark);
                    break;
            }
            tvStatus.setTextColor(statusColor);
        }
    }
}