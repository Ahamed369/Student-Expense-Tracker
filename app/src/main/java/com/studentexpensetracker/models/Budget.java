package com.studentexpensetracker.models;

/**
 * Budget.java
 * ──────────────────────────────────────────────
 * POJO model representing a monthly budget limit
 * for a specific spending category.
 * Maps directly to the 'budgets' SQLite table.
 * ──────────────────────────────────────────────
 */
public class Budget {

    private int    id;
    private String category;
    private double budgetAmount;
    private double spentAmount;   // Calculated at runtime from expenses table (not stored)
    private int    month;
    private int    year;

    // ── Constructors ──────────────────────────────────────────

    public Budget() {}

    public Budget(String category, double budgetAmount, int month, int year) {
        this.category     = category;
        this.budgetAmount = budgetAmount;
        this.month        = month;
        this.year         = year;
    }

    // ── Getters & Setters ─────────────────────────────────────

    public int getId()                { return id; }
    public void setId(int id)         { this.id = id; }

    public String getCategory()                  { return category; }
    public void setCategory(String category)     { this.category = category; }

    public double getBudgetAmount()                     { return budgetAmount; }
    public void setBudgetAmount(double budgetAmount)    { this.budgetAmount = budgetAmount; }

    public double getSpentAmount()                   { return spentAmount; }
    public void setSpentAmount(double spentAmount)   { this.spentAmount = spentAmount; }

    public int getMonth()              { return month; }
    public void setMonth(int month)    { this.month = month; }

    public int getYear()               { return year; }
    public void setYear(int year)      { this.year = year; }

    /**
     * Returns the remaining budget (can be negative if overspent).
     */
    public double getRemaining() {
        return budgetAmount - spentAmount;
    }

    /**
     * Returns spending as a percentage of budget (0–100+).
     * Used to drive the ProgressBar in BudgetFragment.
     */
    public int getSpentPercentage() {
        if (budgetAmount <= 0) return 0;
        return (int) ((spentAmount / budgetAmount) * 100);
    }
}
