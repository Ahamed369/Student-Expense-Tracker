package com.studentexpensetracker.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.studentexpensetracker.R;
import com.studentexpensetracker.database.DatabaseHelper;
import com.studentexpensetracker.models.Expense;
import com.studentexpensetracker.utils.CategoryUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * AnalyticsFragment.java
 * ════════════════════════════════════════════════════════════════
 * FUNCTION 3: Expense Analytics & Reports
 * ════════════════════════════════════════════════════════════════
 *
 * Features:
 *   ✓ PieChart — spending by category (MPAndroidChart)
 *   ✓ BarChart — daily spending trend for the current week
 *   ✓ Filter: This Week / This Month / All Time
 *   ✓ Stats: Total spent, highest category, daily average
 *   ✓ Top 3 spending categories
 *   ✓ All data read from SQLite (DatabaseHelper)
 *
 * Library: MPAndroidChart v3.1.0
 *   Source: https://github.com/PhilJay/MPAndroidChart
 *   License: Apache 2.0
 *
 * HCI principles applied:
 *   - Visual data representation (charts over numbers alone)
 *   - Filter radio group for user-driven date range
 *   - Color-coded categories consistent with the rest of the app
 *   - Stat cards for at-a-glance insights
 */
public class AnalyticsFragment extends Fragment {

    // ── Charts ─────────────────────────────────────────────────
    private PieChart pieChart;
    private BarChart barChart;

    // ── Stats cards ────────────────────────────────────────────
    private TextView tvTotalSpent;
    private TextView tvHighestCategory;
    private TextView tvDailyAverage;
    private TextView tvTop1;
    private TextView tvTop2;
    private TextView tvTop3;

    // ── Filter ─────────────────────────────────────────────────
    private RadioGroup rgFilter;
    private int        currentFilter = FILTER_MONTH; // default

    private static final int FILTER_WEEK  = 0;
    private static final int FILTER_MONTH = 1;
    private static final int FILTER_ALL   = 2;

    // ── Data ────────────────────────────────────────────────────
    private DatabaseHelper dbHelper;
    private List<Expense>  expenseList;

    // ── Factory ───────────────────────────────────────────────
    public static AnalyticsFragment newInstance() {
        return new AnalyticsFragment();
    }

    // ── Lifecycle ──────────────────────────────────────────────

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_analytics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── Bind views ─────────────────────────────────────────
        pieChart          = view.findViewById(R.id.pie_chart);
        barChart          = view.findViewById(R.id.bar_chart);
        tvTotalSpent      = view.findViewById(R.id.tv_analytics_total);
        tvHighestCategory = view.findViewById(R.id.tv_analytics_highest);
        tvDailyAverage    = view.findViewById(R.id.tv_analytics_avg);
        tvTop1            = view.findViewById(R.id.tv_top1);
        tvTop2            = view.findViewById(R.id.tv_top2);
        tvTop3            = view.findViewById(R.id.tv_top3);
        rgFilter          = view.findViewById(R.id.rg_filter);

        // ── Init DB ────────────────────────────────────────────
        dbHelper    = DatabaseHelper.getInstance(requireContext());
        expenseList = new ArrayList<>();

        // ── Filter radio group listener ────────────────────────
        rgFilter.check(R.id.rb_this_month); // Default selection
        rgFilter.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_this_week)  currentFilter = FILTER_WEEK;
            else if (checkedId == R.id.rb_this_month) currentFilter = FILTER_MONTH;
            else                                       currentFilter = FILTER_ALL;
            loadAndRender();
        });

        // ── Initial render ─────────────────────────────────────
        loadAndRender();
    }

    // ── Data loading & rendering orchestration ─────────────────

    /**
     * Loads expenses from SQLite based on the current filter,
     * then updates all charts and stat cards.
     */
    private void loadAndRender() {
        expenseList = loadExpensesForFilter();

        renderPieChart();
        renderBarChart();
        updateStatCards();
    }

    /**
     * Reads expenses from SQLite for the currently selected filter.
     * @return filtered list of Expense objects
     */
    private List<Expense> loadExpensesForFilter() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();

        switch (currentFilter) {
            case FILTER_WEEK: {
                // This week: from last Sunday to today
                Calendar start = (Calendar) cal.clone();
                start.set(Calendar.DAY_OF_WEEK, start.getFirstDayOfWeek());
                return dbHelper.getExpensesByDateRange(
                        sdf.format(start.getTime()), sdf.format(cal.getTime()));
            }
            case FILTER_MONTH: {
                // This calendar month
                int month = cal.get(Calendar.MONTH) + 1;
                int year  = cal.get(Calendar.YEAR);
                return dbHelper.getExpensesByMonth(month, year);
            }
            case FILTER_ALL:
            default: {
                return dbHelper.getAllExpenses();
            }
        }
    }

    // ── PieChart ───────────────────────────────────────────────

    /**
     * Builds a PieChart showing spending breakdown by category.
     * Uses category colors from CategoryUtils for consistency.
     *
     * Library: MPAndroidChart PieChart
     * Source: https://github.com/PhilJay/MPAndroidChart/wiki/PieChart
     */
    private void renderPieChart() {
        // Aggregate spending by category
        Map<String, Float> categoryTotals = new HashMap<>();
        for (Expense e : expenseList) {
            String cat  = e.getCategory();
            float  prev = categoryTotals.containsKey(cat) ? categoryTotals.get(cat) : 0f;
            categoryTotals.put(cat, prev + (float) e.getAmount());
        }

        if (categoryTotals.isEmpty()) {
            pieChart.setNoDataText("No expenses in this period.");
            pieChart.invalidate();
            return;
        }

        // Build PieEntry list
        List<PieEntry> entries = new ArrayList<>();
        List<Integer>  colors  = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
            colors.add(CategoryUtils.getColor(entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        dataSet.setSliceSpace(3f);      // small gap between slices

        PieData pieData = new PieData(dataSet);

        // ── Chart configuration ────────────────────────────────
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setCenterText("Spending\nby Category");
        pieChart.setCenterTextSize(13f);
        pieChart.setEntryLabelTextSize(11f);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.getDescription().setEnabled(false);

        // Legend at the bottom
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setTextSize(11f);

        pieChart.animateY(800);
        pieChart.invalidate(); // Redraw
    }

    // ── BarChart ───────────────────────────────────────────────

    /**
     * Builds a BarChart showing daily spending for the last 7 days.
     * X-axis labels are "Mon", "Tue", etc.
     *
     * Library: MPAndroidChart BarChart
     * Source: https://github.com/PhilJay/MPAndroidChart/wiki/BarChart
     */
    private void renderBarChart() {
        SimpleDateFormat sdf    = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat dayFmt = new SimpleDateFormat("EEE", Locale.getDefault());

        // Get the last 7 days (today = index 6)
        Calendar cal = Calendar.getInstance();
        String[] dayLabels  = new String[7];
        float[]  dayAmounts = new float[7];

        for (int i = 6; i >= 0; i--) {
            dayLabels[6 - i]  = dayFmt.format(cal.getTime());
            String dateKey     = sdf.format(cal.getTime());
            dayAmounts[6 - i] = 0f;

            // Sum expenses for this day
            for (Expense e : expenseList) {
                if (e.getDate().equals(dateKey)) {
                    dayAmounts[6 - i] += (float) e.getAmount();
                }
            }
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        // Build BarEntry list
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, dayAmounts[i]));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Daily Spending (LKR)");
        dataSet.setColor(getResources().getColor(R.color.colorPrimary, null));
        dataSet.setValueTextColor(Color.DKGRAY);
        dataSet.setValueTextSize(9f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);

        // ── Chart configuration ────────────────────────────────
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dayLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(10f);

        barChart.getAxisLeft().setTextSize(9f);
        barChart.getAxisLeft().setDrawGridLines(true);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(true);
        barChart.animateY(600);
        barChart.invalidate();
    }

    // ── Stat cards ─────────────────────────────────────────────

    /**
     * Computes and displays:
     *   - Total spent in the period
     *   - Highest spending category
     *   - Daily average spend
     *   - Top 3 categories (by amount)
     */
    private void updateStatCards() {
        if (expenseList.isEmpty()) {
            tvTotalSpent.setText("LKR 0.00");
            tvHighestCategory.setText("N/A");
            tvDailyAverage.setText("LKR 0.00");
            tvTop1.setText("—");
            tvTop2.setText("—");
            tvTop3.setText("—");
            return;
        }

        // ── Total ──────────────────────────────────────────────
        double total = 0;
        for (Expense e : expenseList) total += e.getAmount();
        tvTotalSpent.setText(String.format(Locale.getDefault(), "LKR %.2f", total));

        // ── Category totals ────────────────────────────────────
        Map<String, Double> catTotals = new HashMap<>();
        for (Expense e : expenseList) {
            catTotals.merge(e.getCategory(), e.getAmount(), Double::sum);
        }

        // Find highest spending category
        String topCat = null;
        double topAmt = 0;
        for (Map.Entry<String, Double> entry : catTotals.entrySet()) {
            if (entry.getValue() > topAmt) {
                topAmt = entry.getValue();
                topCat = entry.getKey();
            }
        }
        if (topCat != null) {
            tvHighestCategory.setText(CategoryUtils.getEmoji(topCat) + " " + topCat);
        }

        // ── Daily average ──────────────────────────────────────
        // Count distinct dates in the dataset
        java.util.Set<String> distinctDates = new java.util.HashSet<>();
        for (Expense e : expenseList) distinctDates.add(e.getDate());
        int    days      = Math.max(1, distinctDates.size());
        double dailyAvg  = total / days;
        tvDailyAverage.setText(String.format(Locale.getDefault(), "LKR %.2f", dailyAvg));

        // ── Top 3 categories ───────────────────────────────────
        // Sort entries by amount descending
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(catTotals.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        String[] topViews = new String[3];
        for (int i = 0; i < 3; i++) {
            if (i < sorted.size()) {
                Map.Entry<String, Double> e = sorted.get(i);
                topViews[i] = (i + 1) + ". " + CategoryUtils.getEmoji(e.getKey())
                        + " " + e.getKey()
                        + " — LKR " + String.format(Locale.getDefault(), "%.0f", e.getValue());
            } else {
                topViews[i] = "—";
            }
        }
        tvTop1.setText(topViews[0]);
        tvTop2.setText(topViews[1]);
        tvTop3.setText(topViews[2]);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAndRender();
    }
}
