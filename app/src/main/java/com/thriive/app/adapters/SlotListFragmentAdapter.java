package com.thriive.app.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.fragments.MeetingsFragment;
import com.thriive.app.models.CommonEntitySlotsPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlotListFragmentAdapter extends RecyclerView.Adapter<SlotListFragmentAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonEntitySlotsPOJO.EntitySlotList> slotList;
    private SharedData sharedData;
    private Fragment fragment;
    private int select = -1;
    public String startTime = "", endTime = "", meetingReason = "", personaName = "";
    private String intent_type;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_date)
        ImageView img_date;
        @BindView(R.id.img_time)
        ImageView img_time;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.layout_select)
        LinearLayout layout_select;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public SlotListFragmentAdapter(Context context, Fragment fragment,  ArrayList<CommonEntitySlotsPOJO.EntitySlotList> slotList, String intent_type) {
        this.context = context;
        this.slotList = slotList;
        this.fragment = fragment;
        this.intent_type = intent_type;
        sharedData = new SharedData(context);
    }

    @Override
    public SlotListFragmentAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slot_list, parent, false);

        return new SlotListFragmentAdapter.RecyclerAdapterHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final SlotListFragmentAdapter.RecyclerAdapterHolder holder, int position) {
        CommonEntitySlotsPOJO.EntitySlotList item = slotList.get(position);
        if (select == position) {
            selectDate(holder.txt_date, holder.txt_time, holder.img_date, holder.img_time, holder.layout_select);
        } else {
            setUnSelectedDate(holder.txt_date, holder.txt_time, holder.img_date, holder.img_time, holder.layout_select);
        }
        holder.txt_date.setText(Utility.getSlotDate(Utility.ConvertUTCToUserTimezone(item.getSlotDate())));
        holder.txt_time.setText(Utility.getSlotTime(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()),
                Utility.ConvertUTCToUserTimezone(item.getPlanEndTime())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                startTime = Utility.ConvertUTCToUserTimezone(item.getPlanStartTime());
                endTime = Utility.ConvertUTCToUserTimezone(item.getPlanEndTime());
                //personaName = item.
              //  notifyDataSetChanged();
                if (intent_type.equals("MEETING")) {
                    ((MeetingsFragment) fragment).meetingConfirmation(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()),
                            Utility.ConvertUTCToUserTimezone(item.getPlanEndTime()));

                } else {
                    ((MeetingDetailsFragment) fragment).meetingConfirmation(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()),
                            Utility.ConvertUTCToUserTimezone(item.getPlanEndTime()));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    private void selectDate(TextView textDate, TextView textTime, ImageView imageDate, ImageView imageTime, LinearLayout linearLayout) {
        textDate.setTextColor(context.getResources().getColor(R.color.terracota));
        textTime.setTextColor(context.getResources().getColor(R.color.terracota));
        imageDate.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calender_t));
        imageTime.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_time_t));
        linearLayout.setBackground(context.getResources().getDrawable(R.drawable.rectangle_tarccoto_outline));

    }

    private void setUnSelectedDate(TextView textDate, TextView textTime, ImageView imageDate, ImageView imageTime, LinearLayout linearLayout) {
        textDate.setTextColor(context.getResources().getColor(R.color.darkGreyBlue));
        textTime.setTextColor(context.getResources().getColor(R.color.darkGreyBlue));
        imageDate.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calender));
        imageTime.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_time));
        linearLayout.setBackground(context.getResources().getDrawable(R.drawable.reactangle_grey_outline));

    }


}
