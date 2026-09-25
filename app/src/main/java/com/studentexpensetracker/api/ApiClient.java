package com.studentexpensetracker.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiClient — Singleton factory for Retrofit instances
 * ──────────────────────────────────────────────────────────────
 * Provides singleton Retrofit instances for each external API.
 * Uses OkHttp with a logging interceptor for easier debugging.
 *
 * Two separate Retrofit instances are needed because both APIs
 * have different base URLs:
 *   - ExchangeRate-API : https://api.exchangerate-api.com/
 *   - RestCountries    : https://restcountries.com/
 *
 * Source: Retrofit documentation — https://square.github.io/retrofit/
 * ──────────────────────────────────────────────────────────────
 */
public class ApiClient {

    // ── Base URLs ──────────────────────────────────────────────
    private static final String EXCHANGE_RATE_BASE_URL = "https://api.exchangerate-api.com/";
    private static final String REST_COUNTRIES_BASE_URL = "https://restcountries.com/";

    // ── Singletons ─────────────────────────────────────────────
    private static Retrofit exchangeRateRetrofit;
    private static Retrofit restCountriesRetrofit;

    // ── OkHttp client (shared) ─────────────────────────────────

    /**
     * Builds a shared OkHttpClient with a logging interceptor.
     * Logging level is set to BODY so full request/response is visible in Logcat.
     */
    private static OkHttpClient buildOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }

    // ── Factory methods ────────────────────────────────────────

    /**
     * Returns (or lazily creates) the Retrofit instance for ExchangeRate-API.
     */
    public static ExchangeRateService getExchangeRateService() {
        if (exchangeRateRetrofit == null) {
            exchangeRateRetrofit = new Retrofit.Builder()
                    .baseUrl(EXCHANGE_RATE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(buildOkHttpClient())
                    .build();
        }
        return exchangeRateRetrofit.create(ExchangeRateService.class);
    }

    /**
     * Returns (or lazily creates) the Retrofit instance for RestCountries API.
     */
    public static RestCountriesService getRestCountriesService() {
        if (restCountriesRetrofit == null) {
            restCountriesRetrofit = new Retrofit.Builder()
                    .baseUrl(REST_COUNTRIES_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(buildOkHttpClient())
                    .build();
        }
        return restCountriesRetrofit.create(RestCountriesService.class);
    }
}
