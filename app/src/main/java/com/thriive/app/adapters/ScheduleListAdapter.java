package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList;
    private SharedData sharedData;


    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.label_name)
        TextView labelName;
        @BindView(R.id.label_date)
        TextView labelDate;
        @BindView(R.id.img_user)
        CircleImageView img_user;
        @BindView(R.id.txt_reason)
        TextView txt_reason;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this, itemView);
        }
    }
    public ScheduleListAdapter(Context context, ArrayList<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
        sharedData = new SharedData(context);
    }
    @Override
    public ScheduleListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_scheduled, parent, false);

        return new ScheduleListAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ScheduleListAdapter.RecyclerAdapterHolder holder,int position) {
        CommonMeetingListPOJO.MeetingListPOJO item  = requesterPOJOArrayList.get(position);
        holder.txt_reason.setText(item.getMeetingReason());
        if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID)))
        {
            holder.labelName.setText(item.getGiverName());
            Glide.with(context)
                    .load(item.getGiverPicUrl())
                    .into(holder.img_user);

        } else {
            holder.labelName.setText(item.getRequestorName());
            Glide.with(context)
                    .load(item.getRequestorPicUrl())
                    .into(holder.img_user);
        }
        holder.labelDate.setText(Utility.getMeetingDate(item.getPlanStartTime(), item.getPlanEndTime()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utility.saveMeetingDetailsData(context, item);
                MeetingDetailsFragment addPhotoBottomDialogFragment =
                        (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
                addPhotoBottomDialogFragment.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}