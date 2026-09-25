package com.studentexpensetracker.utils;

import android.graphics.Color;

/**
 * CategoryUtils.java
 * ──────────────────────────────────────────────────────────────
 * Utility class providing consistent emoji icons and colors
 * for all expense categories across the entire application.
 *
 * Updated with high-contrast colors for better visibility.
 * ──────────────────────────────────────────────────────────────
 */
public class CategoryUtils {

    // ── Category constants ────────────────────────────────────
    public static final String CAT_FOOD          = "Food";
    public static final String CAT_TRANSPORT     = "Transport";
    public static final String CAT_EDUCATION     = "Education";
    public static final String CAT_ENTERTAINMENT = "Entertainment";
    public static final String CAT_HEALTH        = "Health";
    public static final String CAT_OTHER         = "Other";

    /** All categories in display order — used to populate Spinners */
    public static final String[] ALL_CATEGORIES = {
            CAT_FOOD, CAT_TRANSPORT, CAT_EDUCATION,
            CAT_ENTERTAINMENT, CAT_HEALTH, CAT_OTHER
    };

    /**
     * Returns the emoji icon for a given category.
     */
    public static String getEmoji(String category) {
        if (category == null) return "📦";
        switch (category) {
            case CAT_FOOD:          return "🍛";
            case CAT_TRANSPORT:     return "🚌";
            case CAT_EDUCATION:     return "📚";
            case CAT_ENTERTAINMENT: return "🎬";
            case CAT_HEALTH:        return "🏥";
            case CAT_OTHER:
            default:                return "📦";
        }
    }

    /**
     * Returns the accent color for a given category.
     * Updated to high-contrast versions.
     */
    public static int getColor(String category) {
        return Color.parseColor(getColorHex(category));
    }

    /**
     * Returns the category color as a hex string (for XML usage).
     * High-contrast palette synced with colors.xml.
     */
    public static String getColorHex(String category) {
        if (category == null) return "#607D8B";
        switch (category) {
            case CAT_FOOD:          return "#E64A19"; // Darker Deep Orange
            case CAT_TRANSPORT:     return "#1976D2"; // Darker Blue
            case CAT_EDUCATION:     return "#388E3C"; // Darker Green
            case CAT_ENTERTAINMENT: return "#7B1FA2"; // Darker Purple
            case CAT_HEALTH:        return "#D32F2F"; // Darker Red
            case CAT_OTHER:
            default:                return "#607D8B"; // Darker Blue Grey
        }
    }
}
