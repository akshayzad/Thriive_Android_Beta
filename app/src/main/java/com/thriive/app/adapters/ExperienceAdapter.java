package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<String> requesterPOJOArrayList;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_experience)
        TextView txt_experience;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this, itemView);
        }
    }
    public ExperienceAdapter(Context context, ArrayList<String> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public ExperienceAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_experience, parent, false);

        return new ExperienceAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExperienceAdapter.RecyclerAdapterHolder holder,int position) {
        String item  = requesterPOJOArrayList.get(position);
        holder.txt_experience.setText(item);
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}