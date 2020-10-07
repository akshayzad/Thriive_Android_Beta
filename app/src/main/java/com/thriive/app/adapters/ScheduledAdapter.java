package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.fragments.MeetingsFragment;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduledAdapter extends RecyclerView.Adapter<ScheduledAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList;
    private Fragment fragment;

    private  SharedData sharedData;
    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_tags)
        public FlexboxLayout layout_tags;
        @BindView(R.id.layout_avail)
        LinearLayout layout_avail;
        @BindView(R.id.layout_join)
        LinearLayout layout_join;
        @BindView(R.id.txt_giverName)
        TextView txt_giverName;
        @BindView(R.id.txt_profession)
        TextView txt_profession;
        @BindView(R.id.txt_dateTime)
        TextView txt_dateTime;
        @BindView(R.id.img_giver)
        CircleImageView img_giver;
        @BindView(R.id.rv_tags)
        RecyclerView rv_tags;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public ScheduledAdapter(Activity context, Fragment fragment, ArrayList<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList){
        this.context = context;
        this.fragment = fragment;
        sharedData = new SharedData(context);
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public ScheduledAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scheduled, parent, false);

        return new ScheduledAdapter.RecyclerAdapterHolder(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ScheduledAdapter.RecyclerAdapterHolder holder,int position) {
        CommonMeetingListPOJO.MeetingListPOJO item  = requesterPOJOArrayList.get(position);
        ArrayList<String> arrayList = new ArrayList<>();
        if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID)))
        {
            arrayList.addAll(item.getGiverDomainTags());
            arrayList.addAll(item.getGiverSubDomainTags());
            holder.txt_giverName.setText(item.getGiverName());
            sharedData.addStringData(SharedData.CALLING_NAME, item.getGiverName());
            if (item.getGiverDesignationTags().size() > 0){
                holder.txt_profession.setText(item.getGiverDesignationTags().get(0));
            } else {
                holder.txt_profession.setText("");
            }

            Glide.with(context)
                    .load(item.getGiverPicUrl())
                    .into(holder.img_giver);

        } else {
            arrayList.addAll(item.getRequestorDomainTags());
            arrayList.addAll(item.getRequestorSubDomainTags());
            holder.txt_giverName.setText(item.getRequestorName());
            sharedData.addStringData(SharedData.CALLING_NAME, item.getRequestorName());
            if (item.getRequestorDesignationTags().size() > 0) {
                holder.txt_profession.setText(item.getRequestorDesignationTags().get(0));
            } else {
                holder.txt_profession.setText("");
            }
            Glide.with(context)
                    .load(item.getRequestorPicUrl())
                    .into(holder.img_giver);
        }
        holder.txt_dateTime.setText(Utility.getMeetingDate(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()),
                Utility.ConvertUTCToUserTimezone(item.getPlanEndTime())));

        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.rv_tags.setLayoutManager(gridLayout );
        holder.rv_tags.setAdapter(new ExperienceAdapter(context, arrayList));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.saveMeetingDetailsData(context, item);
                MeetingDetailsFragment addPhotoBottomDialogFragment =
                        (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
                addPhotoBottomDialogFragment.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(),
                        "MeetingDetailsFragment");
              //  (MeetingsFragment).callFragment();
            }
        });
        holder.img_giver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.saveMeetingDetailsData(context, item);
                MeetingDetailsFragment addPhotoBottomDialogFragment =
                        (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
                addPhotoBottomDialogFragment.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(),
                        "MeetingDetailsFragment");
                //  (MeetingsFragment).callFragment();
            }
        });
        holder.layout_avail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MeetingsFragment) fragment).getMeetingSlot(item.getMeetingCode());
            }
        });

        holder.layout_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 ((MeetingsFragment) fragment).startMeeting(item.getMeetingId());

            }
        });
    }

    public String getDate(int i ){
       return requesterPOJOArrayList.get(i).getPlanStartTime();
    }
    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}