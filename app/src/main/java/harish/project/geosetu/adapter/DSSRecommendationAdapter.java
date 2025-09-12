package harish.project.geosetu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import harish.project.geosetu.R;
import harish.project.geosetu.model.DSSRecommendation;
import java.util.ArrayList;
import java.util.List;

public class DSSRecommendationAdapter extends RecyclerView.Adapter<DSSRecommendationAdapter.RecommendationViewHolder> {
    private List<DSSRecommendation> recommendations = new ArrayList<>();
    private ActionListener actionListener;

    public interface ActionListener {
        void onGenerateClaim(DSSRecommendation rec);
        void onForward(DSSRecommendation rec);
    }

    public void setActionListener(ActionListener listener) { this.actionListener = listener; }

    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dss_recommendation, parent, false);
        return new RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        DSSRecommendation recommendation = recommendations.get(position);
        holder.bind(recommendation, actionListener);
    }

    @Override
    public int getItemCount() {
        return recommendations.size();
    }

    public void updateRecommendations(List<DSSRecommendation> newRecommendations) {
        this.recommendations = newRecommendations;
        notifyDataSetChanged();
    }

    public void addRecommendations(List<DSSRecommendation> newRecommendations) {
        int startPosition = this.recommendations.size();
        this.recommendations.addAll(newRecommendations);
        notifyItemRangeInserted(startPosition, newRecommendations.size());
    }

    static class RecommendationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSchemeName, tvSchemeDescription, tvEligibilityCriteria, tvBenefits, tvClaimId, tvEligibilityStatus, tvExplanation;
        private Button btnGenerateClaim, btnForward;

        public RecommendationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSchemeName = itemView.findViewById(R.id.tvSchemeName);
            tvSchemeDescription = itemView.findViewById(R.id.tvSchemeDescription);
            tvEligibilityCriteria = itemView.findViewById(R.id.tvEligibilityCriteria);
            tvBenefits = itemView.findViewById(R.id.tvBenefits);
            tvClaimId = itemView.findViewById(R.id.tvClaimId);
            tvEligibilityStatus = itemView.findViewById(R.id.tvEligibilityStatus);
            tvExplanation = itemView.findViewById(R.id.tvExplanation);
            btnGenerateClaim = itemView.findViewById(R.id.btnGenerateClaim);
            btnForward = itemView.findViewById(R.id.btnForward);
        }

        public void bind(DSSRecommendation recommendation, ActionListener actionListener) {
            tvSchemeName.setText(recommendation.getSchemeName());
            tvSchemeDescription.setText(recommendation.getSchemeDescription());
            tvEligibilityCriteria.setText("Criteria: " + recommendation.getEligibilityCriteria());
            tvBenefits.setText("Benefits: " + recommendation.getBenefits());
            tvClaimId.setText("For Claim: " + recommendation.getClaimId());
            if (recommendation.getExplanation() != null && !recommendation.getExplanation().isEmpty()) {
                tvExplanation.setVisibility(View.VISIBLE);
                tvExplanation.setText("Why: " + recommendation.getExplanation());
            } else {
                tvExplanation.setVisibility(View.GONE);
            }
            if (recommendation.isEligible()) {
                tvEligibilityStatus.setText("✅ ELIGIBLE");
                tvEligibilityStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
                btnGenerateClaim.setEnabled(true);
                btnForward.setEnabled(true);
            } else {
                tvEligibilityStatus.setText("❌ NOT ELIGIBLE");
                tvEligibilityStatus.setTextColor(itemView.getContext().getColor(android.R.color.holo_red_dark));
                btnGenerateClaim.setEnabled(false);
                btnForward.setEnabled(false);
            }
            btnGenerateClaim.setOnClickListener(v -> {
                if (actionListener != null && recommendation.isEligible()) {
                    actionListener.onGenerateClaim(recommendation);
                }
            });
            btnForward.setOnClickListener(v -> {
                if (actionListener != null && recommendation.isEligible()) {
                    actionListener.onForward(recommendation);
                }
            });
        }
    }
}