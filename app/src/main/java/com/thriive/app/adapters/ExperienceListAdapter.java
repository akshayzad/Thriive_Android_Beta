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

public class ExperienceListAdapter extends RecyclerView.Adapter<ExperienceListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<String> arrayList;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_experience)
        TextView txt_experience;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ExperienceListAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ExperienceListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_experience_list, parent, false);

        return new ExperienceListAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExperienceListAdapter.RecyclerAdapterHolder holder, int position) {
        String item = arrayList.get(position);
        holder.txt_experience.setText(item);
      //111  holder.txt_tag.setText("\");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}