package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.CountryListPOJO;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.RecyclerAdapterHolder> implements Filterable {
    private Context context;
    private ArrayList<CountryListPOJO> countryList;
    private ArrayList<CountryListPOJO> arraylist;
    private Fragment fragment;
    private int currentItem = -1;
    int countryCode  = 0;
    String countryName = "";

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_expertise)
        public TextView txt_expertise;
        @BindView(R.id.layout_select)
        LinearLayout layout_select;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public CountryListAdapter(Context context, ArrayList<CountryListPOJO> countryList) {
        this.context = context;
        this.countryList = countryList;
        this.arraylist = new ArrayList<CountryListPOJO>();
        this.arraylist.addAll(countryList);
    }

    @Override
    public CountryListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_experties, parent, false);

        return new CountryListAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CountryListAdapter.RecyclerAdapterHolder holder, int position) {
        CountryListPOJO item = countryList.get(position);
        holder.txt_expertise.setText(item.getCountryName());
        holder.txt_expertise.setBackground(null);
        holder.txt_expertise.setPadding(7,7,7,7);
        if (currentItem == position) {
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
        } else {
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItem = position;
                countryCode = item.getCountryId();
                countryName = item.getCountryName();
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }


    public String getCountryName(){

        return countryName;
    }
    public int getCountryCode(){

        return countryCode;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        countryList.clear();
        if (charText.length() == 0) {
            countryList.addAll(arraylist);
        } else {
            for (CountryListPOJO wp : arraylist) {
                if (wp.getCountryName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    countryList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

}