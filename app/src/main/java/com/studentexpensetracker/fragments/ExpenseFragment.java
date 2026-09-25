package com.studentexpensetracker.fragments;

import androidx.appcompat.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studentexpensetracker.R;
import com.studentexpensetracker.adapters.ExpenseAdapter;
import com.studentexpensetracker.database.DatabaseHelper;
import com.studentexpensetracker.models.Expense;
import com.studentexpensetracker.utils.CategoryUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.graphics.Color;

/**
 * ExpenseFragment.java
 * ════════════════════════════════════════════════════════════════
 * FUNCTION 1: Expense Management — Add / View / Delete Expenses
 * ════════════════════════════════════════════════════════════════
 *
 * Features:
 *   ✓ RecyclerView showing all expenses (SQLite READ)
 *   ✓ FAB → dialog to add a new expense (SQLite WRITE)
 *   ✓ Delete button on each item (SQLite DELETE)
 *   ✓ Swipe-left gesture also deletes an item
 *   ✓ Empty state message when no expenses exist
 *   ✓ Total spent summary card at the top
 *   ✓ Category spinner, DatePicker, notes field
 *
 * HCI principles applied:
 *   - Clear affordances (FAB icon, trash icon)
 *   - Confirmation dialog before delete
 *   - Immediate feedback via Toast messages
 *   - Empty state guidance text
 */
public class ExpenseFragment extends Fragment {

    // ── UI elements ────────────────────────────────────────────
    private RecyclerView           recyclerView;
    private ExpenseAdapter         adapter;
    private FloatingActionButton   fabAdd;
    private TextView               tvEmptyState;
    private TextView               tvTotalSpent;

    // ── Data ────────────────────────────────────────────────────
    private List<Expense>  expenseList;
    private DatabaseHelper dbHelper;

    // ── Selected date for new expense ──────────────────────────
    private String selectedDate;

    // ── Factory method ─────────────────────────────────────────
    public static ExpenseFragment newInstance() {
        return new ExpenseFragment();
    }

    // ── Lifecycle ──────────────────────────────────────────────

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── Bind views ─────────────────────────────────────────
        recyclerView  = view.findViewById(R.id.rv_expenses);
        fabAdd        = view.findViewById(R.id.fab_add_expense);
        tvEmptyState  = view.findViewById(R.id.tv_expense_empty);
        tvTotalSpent  = view.findViewById(R.id.tv_total_spent);

        // ── Init database ──────────────────────────────────────
        dbHelper    = DatabaseHelper.getInstance(requireContext());
        expenseList = new ArrayList<>();

        // ── Set up RecyclerView ────────────────────────────────
        setupRecyclerView();

        // ── Load data from SQLite ──────────────────────────────
        loadExpenses();

        // ── FAB: open Add Expense dialog ───────────────────────
        fabAdd.setOnClickListener(v -> showAddExpenseDialog());
    }

    // ── RecyclerView setup ────────────────────────────────────

    private void setupRecyclerView() {
        adapter = new ExpenseAdapter(requireContext(), expenseList,
                (expense, position) -> confirmAndDelete(expense, position));

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Swipe-left gesture to delete (HCI: gesture-based interaction)
        // Source: Android ItemTouchHelper documentation
        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView rv,
                                         @NonNull RecyclerView.ViewHolder vh,
                                         @NonNull RecyclerView.ViewHolder target) {
                        return false; // No drag-and-drop needed
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int direction) {
                        int position = vh.getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Expense expense = expenseList.get(position);
                            confirmAndDelete(expense, position);
                        }
                    }
                };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView);
    }

    // ── Data loading ──────────────────────────────────────────

    /**
     * Reads all expenses from SQLite and refreshes the UI.
     * Also recalculates and displays the total spending summary.
     */
    private void loadExpenses() {
        List<Expense> fresh = dbHelper.getAllExpenses();
        expenseList.clear();
        expenseList.addAll(fresh);
        adapter.notifyDataSetChanged();

        // ── Update total spent card ────────────────────────────
        double total = 0;
        for (Expense e : expenseList) total += e.getAmount();
        tvTotalSpent.setText(String.format(Locale.getDefault(), "LKR %.2f", total));

        // ── Empty state visibility ─────────────────────────────
        tvEmptyState.setVisibility(expenseList.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(expenseList.isEmpty() ? View.GONE : View.VISIBLE);
    }

    // ── Add Expense Dialog ─────────────────────────────────────

    /**
     * Shows a dialog with fields:
     *   - Expense Title (EditText)
     *   - Amount       (EditText, numeric)
     *   - Category     (Spinner)
     *   - Date         (Button → DatePickerDialog)
     *   - Notes        (EditText, optional)
     */
    private void showAddExpenseDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_expense, null);

        // ── Bind dialog views ──────────────────────────────────
        EditText etTitle    = dialogView.findViewById(R.id.et_expense_title);
        EditText etAmount   = dialogView.findViewById(R.id.et_expense_amount);
        Spinner  spCategory = dialogView.findViewById(R.id.sp_expense_category);
        Button   btnDate    = dialogView.findViewById(R.id.btn_pick_date);
        EditText etNotes    = dialogView.findViewById(R.id.et_expense_notes);

        // ── Populate category spinner ──────────────────────────
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, CategoryUtils.ALL_CATEGORIES);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerAdapter);

        // ── Default date = today ───────────────────────────────
        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        btnDate.setText(selectedDate);

        // ── Date picker ────────────────────────────────────────
        btnDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (datePicker, year, month, day) -> {
                // month is 0-indexed in DatePicker, adjust by +1
                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                        year, month + 1, day);
                btnDate.setText(selectedDate);
            }, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // ── Build and show dialog ──────────────────────────────
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext(), android.R.style.Theme_Material_Light_Dialog_Alert)
                .setTitle("Add New Expense")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    saveExpense(etTitle, etAmount, spCategory, etNotes);
                })
                .setNegativeButton("Cancel", null)
                .create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(android.graphics.Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(android.graphics.Color.BLACK);
    }



    /**
     * Validates inputs and writes the new expense to SQLite.
     * Refreshes the RecyclerView list after saving.
     */
    private void saveExpense(EditText etTitle, EditText etAmount,
                             Spinner spCategory, EditText etNotes) {
        // ── Input validation ───────────────────────────────────
        String title = etTitle.getText().toString().trim();
        String amtStr = etAmount.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(requireContext(), "Please enter an expense title.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(amtStr)) {
            Toast.makeText(requireContext(), "Please enter the amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtStr);
            if (amount <= 0) throw new NumberFormatException("non-positive");
        } catch (NumberFormatException ex) {
            Toast.makeText(requireContext(), "Enter a valid positive amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ── Build Expense object ───────────────────────────────
        String category = spCategory.getSelectedItem().toString();
        String notes    = etNotes.getText().toString().trim();
        Expense expense = new Expense(title, amount, category, selectedDate, notes);

        // ── Write to SQLite ────────────────────────────────────
        long rowId = dbHelper.addExpense(expense);

        if (rowId != -1) {
            Toast.makeText(requireContext(), "✅ Expense saved!", Toast.LENGTH_SHORT).show();
            loadExpenses(); // Refresh list
        } else {
            Toast.makeText(requireContext(), "Error saving expense. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    // ── Delete ─────────────────────────────────────────────────

    /**
     * Shows a confirmation AlertDialog before deleting.
     * HCI principle: always confirm destructive actions.
     */
    private void confirmAndDelete(Expense expense, int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("Delete Expense")
                .setMessage("Delete \"" + expense.getTitle() + "\"? This cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    int deleted = dbHelper.deleteExpense(expense.getId());
                    if (deleted > 0) {
                        adapter.removeItem(position);
                        // Recalculate total after removal
                        double total = 0;
                        for (Expense e : expenseList) total += e.getAmount();
                        tvTotalSpent.setText(String.format(Locale.getDefault(),
                                "LKR %.2f", total));
                        // Show empty state if needed
                        tvEmptyState.setVisibility(expenseList.isEmpty() ? View.VISIBLE : View.GONE);
                        Toast.makeText(requireContext(), "Expense deleted.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // If triggered by swipe, restore the swiped item visually
                    adapter.notifyItemChanged(position);
                })
                .setOnCancelListener(d -> adapter.notifyItemChanged(position))
                .create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(android.graphics.Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(android.graphics.Color.BLACK);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data whenever the fragment becomes visible
        // (e.g. returning from another tab that may have modified data)
        loadExpenses();
    }
}
