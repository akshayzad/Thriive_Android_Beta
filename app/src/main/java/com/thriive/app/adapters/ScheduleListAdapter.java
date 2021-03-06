package com.thriive.app.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
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
import com.thriive.app.utilities.textdrawable.TextDrawable;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ScheduleListAdapter.RecyclerAdapterHolder holder,int position) {
        CommonMeetingListPOJO.MeetingListPOJO item  = requesterPOJOArrayList.get(position);
        holder.txt_reason.setText("For " + item.getMeetingReason());
        if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID)))
        {
            holder.labelName.setText("with "+item.getGiverName());
            if (item.getGiverPicUrl().equals("")){
                Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(context.getResources().getColor(R.color.darkGreyBlue))
                        .useFont(typeface)
                        .fontSize(55) /* size in px */
                        .bold()
                        .toUpperCase()
                        .width(130)  // width in px
                        .height(130) // height in px
                        .endConfig()
                        .buildRect(Utility.getInitialsName(item.getGiverName()) , context.getResources().getColor(R.color.whiteTwo));
                holder.img_user.setImageDrawable(drawable);
            } else {
                holder.img_user.setMaxWidth(80);
                holder.img_user.setMaxHeight(80);
                Glide.with(context)
                        .load(item.getGiverPicUrl())
                        .into(holder.img_user);
            }
//            Glide.with(context)
//                    .load(item.getGiverPicUrl())
//                    .into(holder.img_user);

        } else {
            holder.labelName.setText("with " + item.getRequestorName());
            if (item.getRequestorPicUrl().equals("")){
                Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(context.getResources().getColor(R.color.darkGreyBlue))
                        .useFont(typeface)
                        .fontSize(50) /* size in px */
                        .bold()
                        .toUpperCase()
                        .width(130)  // width in px
                        .height(130) // height in px
                        .endConfig()
                        .buildRect(Utility.getInitialsName(item.getRequestorName()) , context.getResources().getColor(R.color.whiteTwo));
                holder.img_user.setImageDrawable(drawable);
            } else {
                holder.img_user.setMaxWidth(80);
                holder.img_user.setMaxHeight(80);
                Glide.with(context)
                        .load(item.getRequestorPicUrl())
                        .into(holder.img_user);
            }
//            Glide.with(context)
//                    .load(item.getRequestorPicUrl())
//                    .into(holder.img_user);
        }
        holder.labelDate.setText(Utility.getMeetingDate(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()),
                Utility.ConvertUTCToUserTimezone(item.getPlanEndTime())));
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
        holder.img_user.setOnClickListener(new View.OnClickListener() {
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