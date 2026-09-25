package com.studentexpensetracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.studentexpensetracker.fragments.AnalyticsFragment;
import com.studentexpensetracker.fragments.BudgetFragment;
import com.studentexpensetracker.fragments.CurrencyFragment;
import com.studentexpensetracker.fragments.ExpenseFragment;

/**
 * MainActivity.java
 * ──────────────────────────────────────────────────────────────
 * The single Activity that hosts all four Fragments via a
 * BottomNavigationView (Material Design 3 component).
 *
 * Navigation tabs:
 *   1. 💸 Expenses   → ExpenseFragment    (Function 1)
 *   2. 💰 Budget     → BudgetFragment     (Function 2)
 *   3. 📊 Analytics  → AnalyticsFragment  (Function 3)
 *   4. 💱 Currency   → CurrencyFragment   (Function 4)
 *
 * Each Fragment is created once and reused via the back stack
 * (hide/show pattern) to preserve RecyclerView scroll position.
 *
 * HCI principles applied:
 *   - Persistent BottomNav = always-visible primary navigation
 *   - Active tab highlighted with accent color
 *   - Smooth fragment transitions (no jarring reloads)
 * ──────────────────────────────────────────────────────────────
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    // ── Fragment instances (kept alive for performance) ─────────
    private ExpenseFragment   expenseFragment;
    private BudgetFragment    budgetFragment;
    private AnalyticsFragment analyticsFragment;
    private CurrencyFragment  currencyFragment;

    // ── Reference to the currently shown fragment ───────────────
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ── Bind views ─────────────────────────────────────────
        bottomNav = findViewById(R.id.bottom_navigation);

        // ── Initialize all four fragments ──────────────────────
        expenseFragment   = ExpenseFragment.newInstance();
        budgetFragment    = BudgetFragment.newInstance();
        analyticsFragment = AnalyticsFragment.newInstance();
        currencyFragment  = CurrencyFragment.newInstance();

        // ── Add all fragments to the back stack at once ────────
        // Only the first (Expenses) is shown; others are hidden.
        // This "hide/show" pattern is more efficient than replace()
        // because it avoids recreating fragments on every tab switch.
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container, currencyFragment,  "currency").hide(currencyFragment);
        ft.add(R.id.fragment_container, analyticsFragment, "analytics").hide(analyticsFragment);
        ft.add(R.id.fragment_container, budgetFragment,    "budget").hide(budgetFragment);
        ft.add(R.id.fragment_container, expenseFragment,   "expense"); // shown by default
        ft.commit();

        activeFragment = expenseFragment;

        // ── BottomNavigationView listener ──────────────────────
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_expenses) {
                switchFragment(expenseFragment);
            } else if (id == R.id.nav_budget) {
                switchFragment(budgetFragment);
            } else if (id == R.id.nav_analytics) {
                switchFragment(analyticsFragment);
            } else if (id == R.id.nav_currency) {
                switchFragment(currencyFragment);
            }
            return true;
        });

        // ── Select default tab ─────────────────────────────────
        bottomNav.setSelectedItemId(R.id.nav_expenses);
    }

    /**
     * Hides the currently active fragment and shows the target fragment.
     * This is the recommended approach for BottomNavigationView with
     * Fragments to avoid recreating views on every tab switch.
     *
     * @param target The fragment to switch to.
     */
    private void switchFragment(Fragment target) {
        if (target == activeFragment) return; // Already showing — do nothing

        getSupportFragmentManager()
                .beginTransaction()
                .hide(activeFragment)
                .show(target)
                .commit();
        target.onResume();
        activeFragment = target;
    }
}
