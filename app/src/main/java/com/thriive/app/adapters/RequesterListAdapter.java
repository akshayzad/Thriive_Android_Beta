package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.HomeActivity;
import com.thriive.app.R;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequesterListAdapter extends RecyclerView.Adapter<RequesterListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList;
    private BusinessProfessionAdapter businessProfessionAdapter;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_profession)
        public RecyclerView recycler_profession;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public RequesterListAdapter(Context context, ArrayList<CommonRequesterPOJO> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public RequesterListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_requester, parent, false);

        return new RequesterListAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RequesterListAdapter.RecyclerAdapterHolder holder,int position) {
        CommonRequesterPOJO item  = requesterPOJOArrayList.get(position);
        businessProfessionAdapter = new BusinessProfessionAdapter(context, requesterPOJOArrayList);
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.recycler_profession.setLayoutManager(gridLayout );
        holder.recycler_profession.setAdapter(businessProfessionAdapter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)view.getContext()).setMeetingFragment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}
