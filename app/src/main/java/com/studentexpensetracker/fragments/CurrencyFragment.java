package com.studentexpensetracker.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.studentexpensetracker.R;
import com.studentexpensetracker.adapters.CurrencyAdapter;
import com.studentexpensetracker.api.ApiClient;
import com.studentexpensetracker.models.CurrencyResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * CurrencyFragment.java
 * ════════════════════════════════════════════════════════════════
 * FUNCTION 4: Currency Converter & Exchange Rate (External APIs)
 * ════════════════════════════════════════════════════════════════
 *
 * Features:
 *   ✓ Input: LKR amount entered by user
 *   ✓ Output: Converted values in USD, EUR, GBP, AUD, JPY
 *   ✓ API #1: ExchangeRate-API (free, no key)
 *             GET https://api.exchangerate-api.com/v4/latest/LKR
 *   ✓ API #2: RestCountries API (free, no key)
 *             GET https://restcountries.com/v3.1/currency/{code}
 *   ✓ RecyclerView with flag emoji, code, country, rate, converted amount
 *   ✓ Graceful fallback to hardcoded cached rates if offline
 *   ✓ Loading indicator while APIs are called
 *   ✓ "Last updated" date from ExchangeRate-API response
 *
 * Implementation notes:
 *   - Retrofit handles both API calls asynchronously on background threads
 *   - UI updates happen on the main thread via runOnUiThread() inside callbacks
 *   - Fallback rates used when device has no internet connection
 *   - Country info (name, flag) from RestCountries enriches the result list
 *
 * Sources:
 *   - ExchangeRate-API: https://api.exchangerate-api.com/
 *   - RestCountries: https://restcountries.com/
 *   - Retrofit: https://square.github.io/retrofit/
 * ════════════════════════════════════════════════════════════════
 */
public class CurrencyFragment extends Fragment {

    // ── Target currencies for conversion ───────────────────────
    private static final String[] TARGET_CURRENCIES = {"USD", "EUR", "GBP", "AUD", "JPY"};

    // ── UI elements ────────────────────────────────────────────
    private EditText    etLkrAmount;
    private Button      btnConvert;
    private RecyclerView recyclerView;
    private ProgressBar  progressBar;
    private TextView     tvStatus;
    private TextView     tvLastUpdated;

    // ── Adapter & data ─────────────────────────────────────────
    private CurrencyAdapter    adapter;
    private List<CurrencyResult> resultList;

    // ── Cached offline rates (approximates as of 2025) ─────────
    // Source: manually noted for demonstration / offline fallback
    private static final Map<String, Double> FALLBACK_RATES = new HashMap<String, Double>() {{
        put("USD", 0.0031);  // 1 LKR ≈ 0.0031 USD
        put("EUR", 0.0028);  // 1 LKR ≈ 0.0028 EUR
        put("GBP", 0.0024);  // 1 LKR ≈ 0.0024 GBP
        put("AUD", 0.0047);  // 1 LKR ≈ 0.0047 AUD
        put("JPY", 0.4600);  // 1 LKR ≈ 0.46  JPY
    }};

    // ── Country metadata for fallback (flag emoji + name) ──────
    private static final Map<String, String[]> FALLBACK_COUNTRY_INFO = new HashMap<String, String[]>() {{
        put("USD", new String[]{"US Dollar",       "United States",   "🇺🇸"});
        put("EUR", new String[]{"Euro",            "European Union",  "🇪🇺"});
        put("GBP", new String[]{"British Pound",   "United Kingdom",  "🇬🇧"});
        put("AUD", new String[]{"Australian Dollar","Australia",      "🇦🇺"});
        put("JPY", new String[]{"Japanese Yen",    "Japan",           "🇯🇵"});
    }};

    // ── Factory ───────────────────────────────────────────────
    public static CurrencyFragment newInstance() {
        return new CurrencyFragment();
    }

    // ── Lifecycle ──────────────────────────────────────────────

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── Bind views ─────────────────────────────────────────
        etLkrAmount  = view.findViewById(R.id.et_lkr_amount);
        btnConvert   = view.findViewById(R.id.btn_convert);
        recyclerView = view.findViewById(R.id.rv_currency_results);
        progressBar  = view.findViewById(R.id.pb_currency_loading);
        tvStatus     = view.findViewById(R.id.tv_currency_status);
        tvLastUpdated = view.findViewById(R.id.tv_last_updated);

        // ── RecyclerView ───────────────────────────────────────
        resultList = new ArrayList<>();
        adapter    = new CurrencyAdapter(requireContext(), resultList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // ── Convert button ─────────────────────────────────────
        btnConvert.setOnClickListener(v -> handleConvert());

        // ── Show initial result with default 1000 LKR ─────────
        etLkrAmount.setText("1000");
        handleConvert();
    }

    // ── Conversion logic ──────────────────────────────────────

    private void handleConvert() {
        String amtStr = etLkrAmount.getText().toString().trim();
        if (TextUtils.isEmpty(amtStr)) {
            Toast.makeText(requireContext(), "Enter an LKR amount first.", Toast.LENGTH_SHORT).show();
            return;
        }

        double lkrAmount;
        try {
            lkrAmount = Double.parseDouble(amtStr);
            if (lkrAmount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Enter a valid positive amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check network availability
        if (isNetworkAvailable()) {
            fetchLiveRates(lkrAmount);
        } else {
            showOfflineFallback(lkrAmount);
        }
    }

    // ── API #1: ExchangeRate-API ────────────────────────────────

    /**
     * Calls the ExchangeRate-API to fetch live LKR-based exchange rates.
     * On success, builds the result list and triggers RestCountries API calls.
     * On failure, falls back to cached/hardcoded rates.
     *
     * Endpoint: GET https://api.exchangerate-api.com/v4/latest/LKR
     * Library: Retrofit 2.9.0
     */
    private void fetchLiveRates(double lkrAmount) {
        showLoading(true);
        tvStatus.setText("Fetching live rates…");

        ApiClient.getExchangeRateService().getLatestRates("LKR")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call,
                                           @NonNull Response<JsonObject> response) {
                        if (!isAdded()) return; // Fragment detached check

                        if (response.isSuccessful() && response.body() != null) {
                            JsonObject body = response.body();

                            // Parse the "date" field
                            String date = body.has("date")
                                    ? body.get("date").getAsString() : "Unknown";

                            // Parse the "rates" object
                            JsonObject rates = body.has("rates")
                                    ? body.get("rates").getAsJsonObject() : null;

                            if (rates != null) {
                                // Build initial result list with live exchange rates
                                List<CurrencyResult> newResults = new ArrayList<>();
                                for (String code : TARGET_CURRENCIES) {
                                    double rate = rates.has(code)
                                            ? rates.get(code).getAsDouble()
                                            : (FALLBACK_RATES.containsKey(code) ? FALLBACK_RATES.get(code) : 0);
                                    double converted = lkrAmount * rate;

                                    // Use fallback flag/name initially; API #2 will enrich these
                                    String[] info = FALLBACK_COUNTRY_INFO.get(code);
                                    String name    = info != null ? info[0] : code;
                                    String country = info != null ? info[1] : "";
                                    String flag    = info != null ? info[2] : "🏳";

                                    CurrencyResult result = new CurrencyResult(
                                            code, name, flag, rate, converted);
                                    result.setCountryName(country);
                                    newResults.add(result);
                                }

                                // Update UI on main thread
                                requireActivity().runOnUiThread(() -> {
                                    resultList.clear();
                                    resultList.addAll(newResults);
                                    adapter.notifyDataSetChanged();
                                    tvLastUpdated.setText("Last updated: " + date + " (live)");
                                    tvStatus.setText("✅ Live rates loaded.");
                                    showLoading(false);
                                });

                                // ── API #2: Enrich with RestCountries data ─────
                                // Called after ExchangeRate-API to add country name + flag
                                enrichWithCountryData(newResults);

                            } else {
                                // Rates object missing in response — use fallback
                                requireActivity().runOnUiThread(() ->
                                        showOfflineFallback(lkrAmount));
                            }
                        } else {
                            // Non-200 response — use fallback
                            requireActivity().runOnUiThread(() ->
                                    showOfflineFallback(lkrAmount));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                        if (!isAdded()) return;
                        // Network failure — fall back to cached rates
                        requireActivity().runOnUiThread(() ->
                                showOfflineFallback(lkrAmount));
                    }
                });
    }

    // ── API #2: RestCountries ──────────────────────────────────

    /**
     * For each currency result, calls RestCountries API to get:
     *   - Official country name
     *   - Flag emoji (Unicode, built from country alpha-2 code)
     *
     * This call is non-blocking — the list is already shown from API #1.
     * Each result is updated asynchronously and the adapter is notified.
     *
     * Endpoint: GET https://restcountries.com/v3.1/currency/{code}
     */
    private void enrichWithCountryData(List<CurrencyResult> results) {
        for (int i = 0; i < results.size(); i++) {
            final CurrencyResult result = results.get(i);
            final int index = i;

            ApiClient.getRestCountriesService()
                    .getCountriesByCurrency(result.getCurrencyCode())
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonArray> call,
                                               @NonNull Response<JsonArray> response) {
                            if (!isAdded() || !response.isSuccessful()
                                    || response.body() == null
                                    || response.body().size() == 0) return;

                            JsonObject firstCountry = response.body()
                                    .get(0).getAsJsonObject();

                            // Extract common name
                            String countryName = "";
                            if (firstCountry.has("name")) {
                                JsonObject nameObj = firstCountry.get("name").getAsJsonObject();
                                if (nameObj.has("common")) {
                                    countryName = nameObj.get("common").getAsString();
                                }
                            }

                            // Build flag emoji from country alpha-2 code
                            // Unicode flag emojis use regional indicator symbols
                            String flagEmoji = result.getFlagEmoji(); // keep existing as default
                            if (firstCountry.has("cca2")) {
                                String cca2 = firstCountry.get("cca2").getAsString().toUpperCase();
                                flagEmoji = countryCodeToFlagEmoji(cca2);
                            }

                            // Update the result object
                            result.setCountryName(countryName);
                            result.setFlagEmoji(flagEmoji);

                            // Notify adapter on main thread
                            final String finalFlag = flagEmoji;
                            final String finalCountry = countryName;
                            requireActivity().runOnUiThread(() -> {
                                result.setFlagEmoji(finalFlag);
                                result.setCountryName(finalCountry);
                                adapter.notifyItemChanged(index);
                            });
                        }

                        @Override
                        public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                            // Silently ignore — fallback data already set
                        }
                    });
        }
    }

    // ── Fallback (offline) ─────────────────────────────────────

    /**
     * Shows cached/hardcoded exchange rates when there is no internet connection.
     * The app must work offline as per the assignment requirements.
     * These rates were recorded manually for demonstration purposes.
     */
    private void showOfflineFallback(double lkrAmount) {
        showLoading(false);
        tvStatus.setText("📴 Offline — showing cached rates");
        tvLastUpdated.setText("Last updated: Cached (offline mode)");

        List<CurrencyResult> fallbackResults = new ArrayList<>();
        for (String code : TARGET_CURRENCIES) {
            double rate      = FALLBACK_RATES.containsKey(code) ? FALLBACK_RATES.get(code) : 0;
            double converted = lkrAmount * rate;
            String[] info    = FALLBACK_COUNTRY_INFO.get(code);
            String name      = info != null ? info[0] : code;
            String country   = info != null ? info[1] : "";
            String flag      = info != null ? info[2] : "🏳";

            CurrencyResult r = new CurrencyResult(code, name, flag, rate, converted);
            r.setCountryName(country);
            fallbackResults.add(r);
        }

        resultList.clear();
        resultList.addAll(fallbackResults);
        adapter.notifyDataSetChanged();

        Toast.makeText(requireContext(),
                "No internet — using offline rates.", Toast.LENGTH_LONG).show();
    }

    // ── Helpers ───────────────────────────────────────────────

    /**
     * Converts a 2-letter ISO country code (e.g. "US") to its flag emoji.
     * Uses Unicode Regional Indicator Symbols (U+1F1E6 to U+1F1FF).
     * Source: https://en.wikipedia.org/wiki/Regional_indicator_symbol
     */
    private String countryCodeToFlagEmoji(String countryCode) {
        if (countryCode == null || countryCode.length() != 2) return "🏳";
        int firstLetter  = Character.codePointAt(countryCode, 0) - 'A' + 0x1F1E6;
        int secondLetter = Character.codePointAt(countryCode, 1) - 'A' + 0x1F1E6;
        return new String(Character.toChars(firstLetter))
                + new String(Character.toChars(secondLetter));
    }

    /** Checks whether a network connection is currently available */
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /** Shows or hides the loading ProgressBar and convert button */
    private void showLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnConvert.setEnabled(!loading);
    }
}
