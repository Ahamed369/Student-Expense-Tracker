package com.studentexpensetracker.api;

import com.google.gson.JsonArray;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit service interface for the RestCountries API.
 * Fetches country metadata (name, flag emoji) for a currency code.
 */
public interface RestCountriesService {
    /**
     * Returns a list of countries that use the specified currency.
     * We pick the first result to get the country name and flag.
     * Example: GET /v3.1/currency/USD
     */
    @GET("v3.1/currency/{code}")
    Call<JsonArray> getCountriesByCurrency(@Path("code") String currencyCode);
}
