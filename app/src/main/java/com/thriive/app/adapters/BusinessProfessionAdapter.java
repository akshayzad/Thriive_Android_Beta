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

public class BusinessProfessionAdapter extends RecyclerView.Adapter<BusinessProfessionAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        public TextView txt_name;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this, itemView);
        }
    }
    public BusinessProfessionAdapter(Context context, ArrayList<CommonRequesterPOJO> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public BusinessProfessionAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profression, parent, false);

        return new BusinessProfessionAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BusinessProfessionAdapter.RecyclerAdapterHolder holder,int position) {
        CommonRequesterPOJO item  = requesterPOJOArrayList.get(position);

    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}