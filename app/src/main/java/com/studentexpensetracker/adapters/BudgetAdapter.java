package com.studentexpensetracker.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.studentexpensetracker.R;
import com.studentexpensetracker.models.Budget;
import com.studentexpensetracker.utils.CategoryUtils;

import java.util.List;
import java.util.Locale;

/**
 * BudgetAdapter.java
 * ──────────────────────────────────────────────────────────────
 * RecyclerView Adapter for the monthly budget tracker (Function 2).
 *
 * Each card shows:
 *   - Category emoji + name
 *   - Spent amount vs budget limit
 *   - Remaining balance (green) or over-budget indicator (red)
 *   - ProgressBar with color-coded thresholds:
 *       < 60%  → green  (on track)
 *       60–80% → orange (caution)
 *       > 80%  → red    (warning — assignment requirement)
 *   - Percentage label
 * ──────────────────────────────────────────────────────────────
 */
public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private final Context      context;
    private final List<Budget> budgetList;

    public BudgetAdapter(Context context, List<Budget> budgetList) {
        this.context    = context;
        this.budgetList = budgetList;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        Budget budget  = budgetList.get(position);
        int    pct     = budget.getSpentPercentage();

        // ── Emoji icon and category name ───────────────────────
        holder.tvEmoji.setText(CategoryUtils.getEmoji(budget.getCategory()));
        holder.tvCategory.setText(budget.getCategory());

        // ── Spent / Budget amounts ─────────────────────────────
        holder.tvSpent.setText(String.format(Locale.getDefault(),
                "LKR %.0f spent", budget.getSpentAmount()));
        holder.tvBudgetLimit.setText(String.format(Locale.getDefault(),
                "of LKR %.0f", budget.getBudgetAmount()));

        // ── Remaining balance ──────────────────────────────────
        double remaining = budget.getRemaining();
        if (remaining >= 0) {
            holder.tvRemaining.setText(String.format(Locale.getDefault(),
                    "LKR %.0f left", remaining));
            holder.tvRemaining.setTextColor(
                    ContextCompat.getColor(context, R.color.colorSuccess));
        } else {
            holder.tvRemaining.setText(String.format(Locale.getDefault(),
                    "LKR %.0f over!", Math.abs(remaining)));
            holder.tvRemaining.setTextColor(
                    ContextCompat.getColor(context, R.color.colorDanger));
        }

        // ── ProgressBar (clamped to 100 for visual) ────────────
        holder.progressBar.setProgress(Math.min(pct, 100));

        // Color threshold logic (assignment requirement: alert at 80%)
        int color;
        if (pct < 60)       color = ContextCompat.getColor(context, R.color.colorSuccess);
        else if (pct < 80)  color = ContextCompat.getColor(context, R.color.colorWarning);
        else                color = ContextCompat.getColor(context, R.color.colorDanger);

        holder.progressBar.setProgressTintList(ColorStateList.valueOf(color));

        // ── Percentage label ───────────────────────────────────
        holder.tvPercentage.setText(pct + "%");
        holder.tvPercentage.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return budgetList != null ? budgetList.size() : 0;
    }

    /** Replace entire dataset and refresh the list */
    public void updateData(List<Budget> newList) {
        budgetList.clear();
        budgetList.addAll(newList);
        notifyDataSetChanged();
    }

    // ── ViewHolder ─────────────────────────────────────────────
    static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView    tvEmoji;
        TextView    tvCategory;
        TextView    tvSpent;
        TextView    tvBudgetLimit;
        TextView    tvRemaining;
        TextView    tvPercentage;
        ProgressBar progressBar;

        BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmoji      = itemView.findViewById(R.id.tv_budget_category_emoji);
            tvCategory   = itemView.findViewById(R.id.tv_budget_category_name);
            tvSpent      = itemView.findViewById(R.id.tv_budget_spent);
            tvBudgetLimit= itemView.findViewById(R.id.tv_budget_limit);
            tvRemaining  = itemView.findViewById(R.id.tv_budget_remaining);
            tvPercentage = itemView.findViewById(R.id.tv_budget_percentage);
            progressBar  = itemView.findViewById(R.id.progress_budget);
        }
    }
}
