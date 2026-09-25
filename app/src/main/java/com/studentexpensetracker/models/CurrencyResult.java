package com.studentexpensetracker.models;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * ExchangeRateResponse.java
 * ──────────────────────────────────────────────────────────────
 * Maps the JSON response from ExchangeRate-API:
 *   GET https://api.exchangerate-api.com/v4/latest/LKR
 *
 * Sample response:
 * {
 *   "base": "LKR",
 *   "date": "2025-05-10",
 *   "rates": { "USD": 0.0031, "EUR": 0.0029, ... }
 * }
 * ──────────────────────────────────────────────────────────────
 */
class ExchangeRateResponse {

    @SerializedName("base")
    private String baseCurrency;

    @SerializedName("date")
    private String date;

    @SerializedName("rates")
    private Map<String, Double> rates;

    public String getBaseCurrency()           { return baseCurrency; }
    public String getDate()                   { return date; }
    public Map<String, Double> getRates()     { return rates; }
}


/**
 * CurrencyResult.java
 * ──────────────────────────────────────────────────────────────
 * Represents a single row in the currency conversion RecyclerView.
 * Combines data from both APIs:
 *   - ExchangeRate-API: converted amount and exchange rate
 *   - RestCountries API: country name, flag emoji
 * ──────────────────────────────────────────────────────────────
 */
public class CurrencyResult {

    private String currencyCode;      // e.g. "USD"
    private String currencyName;      // e.g. "US Dollar"
    private String countryName;       // e.g. "United States"
    private String flagEmoji;         // e.g. "🇺🇸"
    private double exchangeRate;      // Rate relative to LKR
    private double convertedAmount;   // Result of the conversion
    private boolean isLoading;        // True while fetching country data

    // ── Constructor ───────────────────────────────────────────

    public CurrencyResult(String currencyCode, String currencyName, String flagEmoji,
                          double exchangeRate, double convertedAmount) {
        this.currencyCode     = currencyCode;
        this.currencyName     = currencyName;
        this.flagEmoji        = flagEmoji;
        this.exchangeRate     = exchangeRate;
        this.convertedAmount  = convertedAmount;
        this.isLoading        = false;
    }

    // ── Getters & Setters ─────────────────────────────────────

    public String getCurrencyCode()                    { return currencyCode; }
    public void setCurrencyCode(String currencyCode)   { this.currencyCode = currencyCode; }

    public String getCurrencyName()                    { return currencyName; }
    public void setCurrencyName(String currencyName)   { this.currencyName = currencyName; }

    public String getCountryName()                     { return countryName; }
    public void setCountryName(String countryName)     { this.countryName = countryName; }

    public String getFlagEmoji()                   { return flagEmoji; }
    public void setFlagEmoji(String flagEmoji)     { this.flagEmoji = flagEmoji; }

    public double getExchangeRate()                      { return exchangeRate; }
    public void setExchangeRate(double exchangeRate)     { this.exchangeRate = exchangeRate; }

    public double getConvertedAmount()                        { return convertedAmount; }
    public void setConvertedAmount(double convertedAmount)    { this.convertedAmount = convertedAmount; }

    public boolean isLoading()                 { return isLoading; }
    public void setLoading(boolean loading)    { this.isLoading = loading; }
}
