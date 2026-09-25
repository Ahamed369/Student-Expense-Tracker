package com.studentexpensetracker.fragments;

import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studentexpensetracker.R;
import com.studentexpensetracker.adapters.BudgetAdapter;
import com.studentexpensetracker.database.DatabaseHelper;
import com.studentexpensetracker.models.Budget;
import com.studentexpensetracker.utils.CategoryUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BudgetFragment extends Fragment {

    // ── UI elements ────────────────────────────────────────────
    private RecyclerView         recyclerView;
    private BudgetAdapter        adapter;
    private FloatingActionButton fabSetBudget;
    private TextView             tvMonth;
    private Spinner              spMonth;
    private TextView             tvTotalBudget;
    private TextView             tvTotalSpent;
    private TextView             tvTotalRemaining;

    // ── Data ────────────────────────────────────────────────────
    private List<Budget>   budgetList;
    private DatabaseHelper dbHelper;

    // ── Current month context ──────────────────────────────────
    private int currentMonth;
    private int currentYear;

    // ── Factory ───────────────────────────────────────────────
    public static BudgetFragment newInstance() {
        return new BudgetFragment();
    }

    // ── Lifecycle ──────────────────────────────────────────────

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── Bind views ─────────────────────────────────────────
        recyclerView     = view.findViewById(R.id.rv_budgets);
        fabSetBudget     = view.findViewById(R.id.fab_set_budget);
        tvMonth          = view.findViewById(R.id.tv_budget_month);
        spMonth          = view.findViewById(R.id.sp_budget_month);
        tvTotalBudget    = view.findViewById(R.id.tv_summary_total_budget);
        tvTotalSpent     = view.findViewById(R.id.tv_summary_total_spent);
        tvTotalRemaining = view.findViewById(R.id.tv_summary_remaining);

        // ── Calendar context ───────────────────────────────────
        Calendar cal = Calendar.getInstance();
        currentMonth = cal.get(Calendar.MONTH) + 1;
        currentYear  = cal.get(Calendar.YEAR);

        // Set month/year header label
        tvMonth.setText(String.valueOf(currentYear));

        // ── Month spinner setup ────────────────────────────────
        String[] months = {"January","February","March","April","May","June",
                "July","August","September","October","November","December"};
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spMonth.setAdapter(monthAdapter);
        spMonth.setSelection(currentMonth - 1); // select current month

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentMonth = position + 1;
                loadBudgets();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ── DB + list setup ────────────────────────────────────
        dbHelper   = DatabaseHelper.getInstance(requireContext());
        budgetList = new ArrayList<>();

        // ── RecyclerView setup ─────────────────────────────────
        adapter = new BudgetAdapter(requireContext(), budgetList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // ── Load budgets ───────────────────────────────────────
        loadBudgets();

        // ── FAB: open Set Budget dialog ────────────────────────
        fabSetBudget.setOnClickListener(v -> showSetBudgetDialog());
    }

    // ── Load data ─────────────────────────────────────────────

    private void loadBudgets() {
        List<Budget> budgets = dbHelper.getBudgetsByMonth(currentMonth, currentYear);

        for (Budget b : budgets) {
            double spent = dbHelper.getTotalSpentByCategory(
                    b.getCategory(), currentMonth, currentYear);
            b.setSpentAmount(spent);
        }

        budgetList.clear();
        budgetList.addAll(budgets);
        adapter.notifyDataSetChanged();

        updateSummaryCard();
    }

    private void updateSummaryCard() {
        double totalBudget = 0, totalSpent = 0;
        for (Budget b : budgetList) {
            totalBudget += b.getBudgetAmount();
            totalSpent  += b.getSpentAmount();
        }
        double remaining = totalBudget - totalSpent;

        tvTotalBudget.setText(String.format(Locale.getDefault(), "LKR %.0f", totalBudget));
        tvTotalSpent.setText(String.format(Locale.getDefault(), "LKR %.0f", totalSpent));
        tvTotalRemaining.setText(String.format(Locale.getDefault(), "LKR %.0f", remaining));

        int remainColor = remaining >= 0
                ? requireContext().getColor(R.color.colorSuccess)
                : requireContext().getColor(R.color.colorDanger);
        tvTotalRemaining.setTextColor(remainColor);

        for (Budget b : budgetList) {
            if (b.getBudgetAmount() > 0 && b.getSpentPercentage() >= 80) {
                Toast.makeText(requireContext(),
                        "⚠️ " + b.getCategory() + " budget is " + b.getSpentPercentage() + "% used!",
                        Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    // ── Set Budget Dialog ──────────────────────────────────────

    private void showSetBudgetDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_set_budget, null);

        Spinner  spCategory     = dialogView.findViewById(R.id.sp_budget_category);
        EditText etBudgetAmount = dialogView.findViewById(R.id.et_budget_amount);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, CategoryUtils.ALL_CATEGORIES);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerAdapter);

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("Set Monthly Budget")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String category = spCategory.getSelectedItem().toString();
                    String amtStr   = etBudgetAmount.getText().toString().trim();

                    if (TextUtils.isEmpty(amtStr)) {
                        Toast.makeText(requireContext(),
                                "Please enter a budget amount.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double amount;
                    try {
                        amount = Double.parseDouble(amtStr);
                        if (amount <= 0) throw new NumberFormatException();
                    } catch (NumberFormatException ex) {
                        Toast.makeText(requireContext(),
                                "Enter a valid positive amount.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Budget budget = new Budget(category, amount, currentMonth, currentYear);
                    long result   = dbHelper.setBudget(budget);

                    if (result != -1) {
                        Toast.makeText(requireContext(),
                                "✅ Budget set for " + category, Toast.LENGTH_SHORT).show();
                        loadBudgets();
                    } else {
                        Toast.makeText(requireContext(),
                                "Error saving budget.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(android.graphics.Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(android.graphics.Color.BLACK);
    }

    // ── Helpers ───────────────────────────────────────────────

    private String getMonthName(int month) {
        String[] months = {"January","February","March","April","May","June",
                "July","August","September","October","November","December"};
        if (month >= 1 && month <= 12) return months[month - 1];
        return "Unknown";
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBudgets();
    }
}