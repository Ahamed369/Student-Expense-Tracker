package com.studentexpensetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.studentexpensetracker.models.Budget;
import com.studentexpensetracker.models.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * DatabaseHelper.java
 * ─────────────────────────────────────────────────────────────────
 * Manages the SQLite database for the Student Expense Tracker app.
 * Handles creation of two tables:
 *   1. expenses  — stores all individual expense records
 *   2. budgets   — stores monthly budget limits per category
 *
 * On first install, dummy data is inserted automatically so the app
 * is never presented with an empty state.
 *
 * Function coverage: Functions 1, 2, 3 (all SQLite read/write ops)
 * ─────────────────────────────────────────────────────────────────
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // ── Database meta ──────────────────────────────────────────
    private static final String DATABASE_NAME = "expense_tracker.db";
    private static final int DATABASE_VERSION = 1;

    // ── Table: expenses ────────────────────────────────────────
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COL_EXP_ID       = "id";
    public static final String COL_EXP_TITLE    = "title";
    public static final String COL_EXP_AMOUNT   = "amount";
    public static final String COL_EXP_CATEGORY = "category";
    public static final String COL_EXP_DATE     = "date";      // stored as "yyyy-MM-dd"
    public static final String COL_EXP_NOTES    = "notes";

    // ── Table: budgets ─────────────────────────────────────────
    public static final String TABLE_BUDGETS = "budgets";
    public static final String COL_BUD_ID       = "id";
    public static final String COL_BUD_CATEGORY = "category";
    public static final String COL_BUD_AMOUNT   = "budget_amount";
    public static final String COL_BUD_MONTH    = "month";     // 1–12
    public static final String COL_BUD_YEAR     = "year";      // e.g. 2025

    // ── CREATE statements ──────────────────────────────────────
    private static final String CREATE_EXPENSES_TABLE =
            "CREATE TABLE " + TABLE_EXPENSES + " (" +
                    COL_EXP_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_EXP_TITLE    + " TEXT NOT NULL, " +
                    COL_EXP_AMOUNT   + " REAL NOT NULL, " +
                    COL_EXP_CATEGORY + " TEXT NOT NULL, " +
                    COL_EXP_DATE     + " TEXT NOT NULL, " +
                    COL_EXP_NOTES    + " TEXT" +
                    ")";

    private static final String CREATE_BUDGETS_TABLE =
            "CREATE TABLE " + TABLE_BUDGETS + " (" +
                    COL_BUD_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_BUD_CATEGORY + " TEXT NOT NULL, " +
                    COL_BUD_AMOUNT   + " REAL NOT NULL, " +
                    COL_BUD_MONTH    + " INTEGER NOT NULL, " +
                    COL_BUD_YEAR     + " INTEGER NOT NULL" +
                    ")";

    // ── Singleton ──────────────────────────────────────────────
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ── Lifecycle ──────────────────────────────────────────────

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EXPENSES_TABLE);
        db.execSQL(CREATE_BUDGETS_TABLE);
        Log.d(TAG, "Database tables created successfully.");
        // Insert dummy data so the app has content on first launch
        insertDummyData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables and recreate on version bump
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);
        onCreate(db);
    }

    // ── Dummy seed data ────────────────────────────────────────

    /**
     * Inserts sample expenses and budgets for the current month.
     * This ensures the Analytics and Budget screens are never blank.
     * All values are fictional/dummy data for demonstration purposes.
     */
    private void insertDummyData(SQLiteDatabase db) {
        // Get current month/year for realistic dummy data
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int month = cal.get(java.util.Calendar.MONTH) + 1;
        int year  = cal.get(java.util.Calendar.YEAR);

        // Helper to format today's date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());

        // ── Dummy Expenses ────────────────────────────────────
        String[][] expenses = {
                {"Lunch at Canteen",      "350.00",  "Food",           today,          "Rice and curry"},
                {"Bus Pass (Weekly)",     "450.00",  "Transport",      today,          "Weekly bus card"},
                {"Textbook - Java OOP",   "1800.00", "Education",      "2025-05-05",   "From university bookshop"},
                {"Movie Night",           "650.00",  "Entertainment",  "2025-05-07",   "With friends"},
                {"Pharmacy",              "320.00",  "Health",         "2025-05-06",   "Cold medicine"},
                {"Dinner (Kottu)",        "420.00",  "Food",           "2025-05-08",   ""},
                {"Three-wheel fare",      "200.00",  "Transport",      "2025-05-08",   ""},
                {"Stationery",            "150.00",  "Other",          "2025-05-04",   "Pens and notebooks"},
                {"Breakfast",             "180.00",  "Food",           "2025-05-09",   "String hoppers"},
                {"Online Course Fee",     "2500.00", "Education",      "2025-05-03",   "Udemy discount"},
        };

        for (String[] e : expenses) {
            ContentValues cv = new ContentValues();
            cv.put(COL_EXP_TITLE,    e[0]);
            cv.put(COL_EXP_AMOUNT,   Double.parseDouble(e[1]));
            cv.put(COL_EXP_CATEGORY, e[2]);
            cv.put(COL_EXP_DATE,     e[3]);
            cv.put(COL_EXP_NOTES,    e[4]);
            db.insert(TABLE_EXPENSES, null, cv);
        }

        // ── Dummy Budgets (current month) ─────────────────────
        String[][] budgets = {
                {"Food",          "5000.00"},
                {"Transport",     "3000.00"},
                {"Education",     "8000.00"},
                {"Entertainment", "2000.00"},
                {"Health",        "2000.00"},
                {"Other",         "1500.00"},
        };

        for (String[] b : budgets) {
            ContentValues cv = new ContentValues();
            cv.put(COL_BUD_CATEGORY, b[0]);
            cv.put(COL_BUD_AMOUNT,   Double.parseDouble(b[1]));
            cv.put(COL_BUD_MONTH,    month);
            cv.put(COL_BUD_YEAR,     year);
            db.insert(TABLE_BUDGETS, null, cv);
        }

        Log.d(TAG, "Dummy seed data inserted.");
    }

    // ══════════════════════════════════════════════════════════
    //  EXPENSE CRUD OPERATIONS  (Function 1)
    // ══════════════════════════════════════════════════════════

    /**
     * Inserts a new expense record into the database.
     * @return the row ID of the newly inserted row, or -1 on error.
     */
    public long addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EXP_TITLE,    expense.getTitle());
        cv.put(COL_EXP_AMOUNT,   expense.getAmount());
        cv.put(COL_EXP_CATEGORY, expense.getCategory());
        cv.put(COL_EXP_DATE,     expense.getDate());
        cv.put(COL_EXP_NOTES,    expense.getNotes());
        long id = db.insert(TABLE_EXPENSES, null, cv);
        db.close();
        Log.d(TAG, "Expense added with id=" + id);
        return id;
    }

    /**
     * Returns ALL expenses ordered by date DESC (most recent first).
     */
    public List<Expense> getAllExpenses() {
        List<Expense> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_EXPENSES,
                null,
                null, null, null, null,
                COL_EXP_DATE + " DESC"   // most recent first
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursorToExpense(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    /**
     * Returns expenses filtered by a date range (inclusive).
     * @param fromDate "yyyy-MM-dd"
     * @param toDate   "yyyy-MM-dd"
     */
    public List<Expense> getExpensesByDateRange(String fromDate, String toDate) {
        List<Expense> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_EXPENSES,
                null,
                COL_EXP_DATE + " BETWEEN ? AND ?",
                new String[]{fromDate, toDate},
                null, null,
                COL_EXP_DATE + " DESC"
        );
        if (cursor != null && cursor.moveToFirst()) {
            do { list.add(cursorToExpense(cursor)); } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    /**
     * Returns expenses for a specific month and year.
     * Uses SQLite's strftime to extract month/year from the date column.
     */
    public List<Expense> getExpensesByMonth(int month, int year) {
        List<Expense> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // strftime('%m', date) returns zero-padded month
        String monthStr = String.format(Locale.getDefault(), "%02d", month);
        String yearStr  = String.valueOf(year);
        Cursor cursor = db.query(
                TABLE_EXPENSES,
                null,
                "strftime('%m', " + COL_EXP_DATE + ") = ? AND strftime('%Y', " + COL_EXP_DATE + ") = ?",
                new String[]{monthStr, yearStr},
                null, null,
                COL_EXP_DATE + " DESC"
        );
        if (cursor != null && cursor.moveToFirst()) {
            do { list.add(cursorToExpense(cursor)); } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }

    /**
     * Deletes a single expense by its primary key.
     * @return number of rows deleted (1 on success, 0 if not found).
     */
    public int deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_EXPENSES, COL_EXP_ID + "=?",
                new String[]{String.valueOf(expenseId)});
        db.close();
        Log.d(TAG, "Deleted expense id=" + expenseId + ", rows=" + rows);
        return rows;
    }

    /**
     * Returns the total amount spent in a given category for the current month.
     * Used by Budget fragment to compare against budget limits.
     */
    public double getTotalSpentByCategory(String category, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String monthStr = String.format(Locale.getDefault(), "%02d", month);
        String yearStr  = String.valueOf(year);
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + COL_EXP_AMOUNT + ") FROM " + TABLE_EXPENSES +
                        " WHERE " + COL_EXP_CATEGORY + " = ?" +
                        " AND strftime('%m', " + COL_EXP_DATE + ") = ?" +
                        " AND strftime('%Y', " + COL_EXP_DATE + ") = ?",
                new String[]{category, monthStr, yearStr}
        );
        double total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.isNull(0) ? 0 : cursor.getDouble(0);
            cursor.close();
        }
        db.close();
        return total;
    }

    // ── Helper: map cursor row → Expense object ───────────────
    private Expense cursorToExpense(Cursor cursor) {
        Expense e = new Expense();
        e.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_EXP_ID)));
        e.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_TITLE)));
        e.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_EXP_AMOUNT)));
        e.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_CATEGORY)));
        e.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_DATE)));
        e.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_NOTES)));
        return e;
    }

    // ══════════════════════════════════════════════════════════
    //  BUDGET CRUD OPERATIONS  (Function 2)
    // ══════════════════════════════════════════════════════════

    /**
     * Inserts or updates a budget for a given category/month/year.
     * Uses INSERT OR REPLACE so calling this multiple times is safe.
     */
    public long setBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        // First check if a budget already exists for this category/month/year
        Cursor cursor = db.query(
                TABLE_BUDGETS, new String[]{COL_BUD_ID},
                COL_BUD_CATEGORY + "=? AND " + COL_BUD_MONTH + "=? AND " + COL_BUD_YEAR + "=?",
                new String[]{budget.getCategory(),
                        String.valueOf(budget.getMonth()),
                        String.valueOf(budget.getYear())},
                null, null, null
        );

        long result;
        ContentValues cv = new ContentValues();
        cv.put(COL_BUD_CATEGORY, budget.getCategory());
        cv.put(COL_BUD_AMOUNT,   budget.getBudgetAmount());
        cv.put(COL_BUD_MONTH,    budget.getMonth());
        cv.put(COL_BUD_YEAR,     budget.getYear());

        if (cursor != null && cursor.moveToFirst()) {
            // Update existing record
            int existingId = cursor.getInt(0);
            cursor.close();
            result = db.update(TABLE_BUDGETS, cv, COL_BUD_ID + "=?",
                    new String[]{String.valueOf(existingId)});
            Log.d(TAG, "Budget updated for category=" + budget.getCategory());
        } else {
            // Insert new record
            if (cursor != null) cursor.close();
            result = db.insert(TABLE_BUDGETS, null, cv);
            Log.d(TAG, "Budget inserted for category=" + budget.getCategory());
        }
        db.close();
        return result;
    }

    /**
     * Returns all budgets for a specific month and year.
     */
    public List<Budget> getBudgetsByMonth(int month, int year) {
        List<Budget> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_BUDGETS, null,
                COL_BUD_MONTH + "=? AND " + COL_BUD_YEAR + "=?",
                new String[]{String.valueOf(month), String.valueOf(year)},
                null, null, COL_BUD_CATEGORY + " ASC"
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Budget b = new Budget();
                b.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_BUD_ID)));
                b.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_BUD_CATEGORY)));
                b.setBudgetAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BUD_AMOUNT)));
                b.setMonth(cursor.getInt(cursor.getColumnIndexOrThrow(COL_BUD_MONTH)));
                b.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(COL_BUD_YEAR)));
                list.add(b);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return list;
    }
}
