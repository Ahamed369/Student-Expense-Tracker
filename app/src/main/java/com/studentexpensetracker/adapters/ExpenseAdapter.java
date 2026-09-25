package com.studentexpensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studentexpensetracker.R;
import com.studentexpensetracker.models.Expense;
import com.studentexpensetracker.utils.CategoryUtils;

import java.util.List;
import java.util.Locale;

/**
 * ExpenseAdapter.java
 * ──────────────────────────────────────────────────────────────
 * RecyclerView Adapter for the Expense list (Function 1).
 *
 * Each row displays:
 *   - Category emoji icon (in a colored circle)
 *   - Expense title
 *   - Date
 *   - Amount in LKR
 *   - Delete button (trash icon)
 *
 * The delete action is handled via the OnDeleteClickListener interface
 * so the Fragment retains control of the database operation.
 * ──────────────────────────────────────────────────────────────
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    // ── Callback interface ─────────────────────────────────────
    public interface OnDeleteClickListener {
        void onDeleteClick(Expense expense, int position);
    }

    private final Context context;
    private final List<Expense> expenseList;
    private final OnDeleteClickListener deleteListener;

    // ── Constructor ───────────────────────────────────────────

    public ExpenseAdapter(Context context, List<Expense> expenseList,
                          OnDeleteClickListener deleteListener) {
        this.context        = context;
        this.expenseList    = expenseList;
        this.deleteListener = deleteListener;
    }

    // ── RecyclerView.Adapter overrides ─────────────────────────

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each expense row
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        // ── Category emoji icon ────────────────────────────────
        holder.tvCategoryIcon.setText(CategoryUtils.getEmoji(expense.getCategory()));
        // Tint the icon background with the category color
        holder.tvCategoryIcon.getBackground()
                .setTint(CategoryUtils.getColor(expense.getCategory()));

        // ── Title and date ─────────────────────────────────────
        holder.tvTitle.setText(expense.getTitle());
        holder.tvDate.setText(expense.getDate());
        holder.tvCategory.setText(expense.getCategory());

        // ── Amount formatted as LKR ────────────────────────────
        holder.tvAmount.setText(String.format(Locale.getDefault(),
                "LKR %.2f", expense.getAmount()));

        // ── Delete button ──────────────────────────────────────
        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_ID && deleteListener != null) {
                deleteListener.onDeleteClick(expense, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    // ── Public helpers ─────────────────────────────────────────

    /**
     * Removes an item at the given position and notifies the RecyclerView.
     * Call this after successfully deleting from the database.
     */
    public void removeItem(int position) {
        if (position >= 0 && position < expenseList.size()) {
            expenseList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, expenseList.size());
        }
    }

    /**
     * Replaces the entire data set (used when re-loading from DB).
     */
    public void updateData(List<Expense> newList) {
        expenseList.clear();
        expenseList.addAll(newList);
        notifyDataSetChanged();
    }

    // ── ViewHolder ─────────────────────────────────────────────

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView    tvCategoryIcon;
        TextView    tvTitle;
        TextView    tvDate;
        TextView    tvCategory;
        TextView    tvAmount;
        ImageButton btnDelete;

        ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryIcon = itemView.findViewById(R.id.tv_category_icon);
            tvTitle        = itemView.findViewById(R.id.tv_expense_title);
            tvDate         = itemView.findViewById(R.id.tv_expense_date);
            tvCategory     = itemView.findViewById(R.id.tv_expense_category);
            tvAmount       = itemView.findViewById(R.id.tv_expense_amount);
            btnDelete      = itemView.findViewById(R.id.btn_delete_expense);
        }
    }
}
