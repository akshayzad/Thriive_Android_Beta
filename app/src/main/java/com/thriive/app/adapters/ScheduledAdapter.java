package com.thriive.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingAvailabilityFragment;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduledAdapter extends RecyclerView.Adapter<ScheduledAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList;
    private BusinessProfessionAdapter businessProfessionAdapter;
    private Fragment fragment;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_profession)
        public RecyclerView recycler_profession;
        @BindView(R.id.layout_avail)
        LinearLayout layout_avail;
        @BindView(R.id.layout_join)
        LinearLayout layout_join;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public ScheduledAdapter(Activity context, Fragment fragment, ArrayList<CommonRequesterPOJO> requesterPOJOArrayList){
        this.context = context;
        this.fragment = fragment;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public ScheduledAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scheduled, parent, false);

        return new ScheduledAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ScheduledAdapter.RecyclerAdapterHolder holder,int position) {
        CommonRequesterPOJO item  = requesterPOJOArrayList.get(position);
        businessProfessionAdapter = new BusinessProfessionAdapter(context, requesterPOJOArrayList);
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.recycler_profession.setLayoutManager(gridLayout );
        holder.recycler_profession.setAdapter(businessProfessionAdapter);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  (MeetingsFragment).callFragment();
            }
        });

        holder.layout_avail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetingAvailabilityFragment addPhotoBottomDialogFragment =
                        (MeetingAvailabilityFragment) MeetingAvailabilityFragment.newInstance();
                addPhotoBottomDialogFragment.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(),
                        "MeetingAvailabilityFragment");

                //
            }
        });

        holder.layout_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MeetingJoinActivity.class);
                view.getContext().startActivity(intent);
                //
            }
        });
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}