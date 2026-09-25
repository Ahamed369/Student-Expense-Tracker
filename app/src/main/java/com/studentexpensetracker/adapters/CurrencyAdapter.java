package com.studentexpensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studentexpensetracker.R;
import com.studentexpensetracker.models.CurrencyResult;

import java.util.List;
import java.util.Locale;

/**
 * CurrencyAdapter.java
 * ──────────────────────────────────────────────────────────────
 * RecyclerView Adapter for currency conversion results (Function 4).
 *
 * Displays one card per target currency:
 *   - Country flag emoji  (from RestCountries API — API #2)
 *   - Currency code       (e.g. USD, EUR)
 *   - Currency name + country (e.g. "US Dollar · United States")
 *   - Converted amount    (computed from ExchangeRate-API — API #1)
 *   - Exchange rate label (1 LKR = x CODE)
 * ──────────────────────────────────────────────────────────────
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private final Context             context;
    private final List<CurrencyResult> currencyList;

    public CurrencyAdapter(Context context, List<CurrencyResult> currencyList) {
        this.context      = context;
        this.currencyList = currencyList;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        CurrencyResult item = currencyList.get(position);

        // ── Flag emoji (from RestCountries API) ────────────────
        holder.tvFlag.setText(item.getFlagEmoji());

        // ── Currency code ──────────────────────────────────────
        holder.tvCode.setText(item.getCurrencyCode());

        // ── Currency name + country name ───────────────────────
        String label;
        if (item.getCountryName() != null && !item.getCountryName().isEmpty()) {
            label = item.getCurrencyName() + "\n" + item.getCountryName();
        } else {
            label = item.getCurrencyName();
        }
        holder.tvName.setText(label);

        // ── Converted amount (large, primary text) ─────────────
        holder.tvConverted.setText(
                String.format(Locale.getDefault(), "%.4f", item.getConvertedAmount()));

        // ── Exchange rate footnote ─────────────────────────────
        holder.tvRate.setText(
                String.format(Locale.getDefault(), "1 LKR = %.6f %s",
                        item.getExchangeRate(), item.getCurrencyCode()));
    }

    @Override
    public int getItemCount() {
        return currencyList != null ? currencyList.size() : 0;
    }

    /** Replace data and refresh list (called after API response) */
    public void updateData(List<CurrencyResult> newList) {
        currencyList.clear();
        currencyList.addAll(newList);
        notifyDataSetChanged();
    }

    // ── ViewHolder ─────────────────────────────────────────────
    static class CurrencyViewHolder extends RecyclerView.ViewHolder {
        TextView tvFlag;
        TextView tvCode;
        TextView tvName;
        TextView tvConverted;
        TextView tvRate;

        CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFlag      = itemView.findViewById(R.id.tv_currency_flag);
            tvCode      = itemView.findViewById(R.id.tv_currency_code);
            tvName      = itemView.findViewById(R.id.tv_currency_name);
            tvConverted = itemView.findViewById(R.id.tv_converted_amount);
            tvRate      = itemView.findViewById(R.id.tv_exchange_rate);
        }
    }
}
