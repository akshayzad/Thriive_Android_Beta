package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpertiseAdapter extends RecyclerView.Adapter<ExpertiseAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_expertise)
        TextView txt_expertise;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this, itemView);
        }
    }
    public ExpertiseAdapter(Context context, ArrayList<CommonRequesterPOJO> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public ExpertiseAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_experties, parent, false);

        return new ExpertiseAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExpertiseAdapter.RecyclerAdapterHolder holder,int position) {
        CommonRequesterPOJO item  = requesterPOJOArrayList.get(position);
        holder.txt_expertise.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}