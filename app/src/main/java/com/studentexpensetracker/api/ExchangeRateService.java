package com.studentexpensetracker.api;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit service interface for the ExchangeRate-API.
 * Returns live exchange rates based on LKR (Sri Lankan Rupee).
 */
public interface ExchangeRateService {
    /**
     * Fetches all exchange rates relative to the given base currency.
     * Example: GET /v4/latest/LKR
     * Response keys: "base", "date", "rates" (Map of currency codes to rates)
     */
    @GET("v4/latest/{base}")
    Call<JsonObject> getLatestRates(@Path("base") String baseCurrency);
}
