package com.studentexpensetracker.models;

/**
 * Expense.java
 * ──────────────────────────────────────────────
 * POJO model representing a single expense record.
 * Maps directly to the 'expenses' SQLite table.
 * ──────────────────────────────────────────────
 */
public class Expense {

    private int    id;
    private String title;
    private double amount;
    private String category;
    private String date;    // Format: "yyyy-MM-dd"
    private String notes;

    // ── Constructors ──────────────────────────────────────────

    public Expense() {}

    public Expense(String title, double amount, String category, String date, String notes) {
        this.title    = title;
        this.amount   = amount;
        this.category = category;
        this.date     = date;
        this.notes    = notes;
    }

    // ── Getters & Setters ─────────────────────────────────────

    public int getId()                { return id; }
    public void setId(int id)         { this.id = id; }

    public String getTitle()               { return title; }
    public void setTitle(String title)     { this.title = title; }

    public double getAmount()              { return amount; }
    public void setAmount(double amount)   { this.amount = amount; }

    public String getCategory()                  { return category; }
    public void setCategory(String category)     { this.category = category; }

    public String getDate()              { return date; }
    public void setDate(String date)     { this.date = date; }

    public String getNotes()               { return notes; }
    public void setNotes(String notes)     { this.notes = notes; }
}
