package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.R;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestedAdapter extends RecyclerView.Adapter<RequestedAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList;
    private BusinessProfessionAdapter businessProfessionAdapter;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_schedule)
        RecyclerView recycler_schedule;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public RequestedAdapter(Context context, ArrayList<CommonRequesterPOJO> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public RequestedAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_requested, parent, false);

        return new RequestedAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RequestedAdapter.RecyclerAdapterHolder holder,int position) {
        CommonRequesterPOJO item  = requesterPOJOArrayList.get(position);
        businessProfessionAdapter = new BusinessProfessionAdapter(context, requesterPOJOArrayList);
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.recycler_schedule.setLayoutManager(gridLayout );
        holder.recycler_schedule.setAdapter(businessProfessionAdapter);
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}