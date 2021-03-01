package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.fragments.MeetingsFragment;
import com.thriive.app.fragments.NextMeetingDetailsFragment;
import com.thriive.app.models.CommonRequestTimeSlots;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRescheduleSlotAdapter extends RecyclerView.Adapter<DetailRescheduleSlotAdapter.RecyclerAdapterHolder> {
    private Context context;
    private String date;
    private ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> childArrayList;
    private ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> slotsListPOJOS;
    Fragment fragment;
    int currentItem = -1;
    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        public TextView txt_name;
        @BindView(R.id.layout_select)
        LinearLayout layout_select;
        @BindView(R.id.card_slots)
        CardView card_slots;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }

    public DetailRescheduleSlotAdapter(Context context, Fragment fragment ,ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> childArrayList, String date){
        this.context = context;
        this.fragment = fragment;
        this.childArrayList = childArrayList;
        this.date = date;
    }
    @Override
    public DetailRescheduleSlotAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slot_label, parent, false);

        return new DetailRescheduleSlotAdapter.RecyclerAdapterHolder(itemView);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final DetailRescheduleSlotAdapter.RecyclerAdapterHolder holder, int position) {
        CommonRequestTimeSlots.EntitySlotsListPOJO item  = childArrayList.get(position);

        if (item.isIs_disabled()){
            holder.txt_name.setText(Utility.ConvertUTCToUserTimezoneForSlotTime( item.getFrom_hour() + ":" + item.getFrom_min()));
            holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.slot_disabled));
            holder.card_slots.setCardElevation(0);
            holder.txt_name.setTextColor(context.getResources().getColor(R.color.new_grey));
            ((MeetingDetailsFragment) fragment).showCustomToast(false);
        }else {
            holder.txt_name.setText(Utility.ConvertUTCToUserTimezoneForSlotTime( item.getFrom_hour() + ":" + item.getFrom_min()));
            holder.card_slots.setCardElevation(5);
            holder.txt_name.setTextColor(context.getResources().getColor(R.color.black_01));
            if (currentItem == position) {
                holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
            } else {
                holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentItem = position;
                    String time = Utility.ConvertUTCToUserTimezoneForSlotTime( item.getFrom_hour() + ":" + item.getFrom_min());
                    item.setSlot_from_date(Utility.ConvertUserTimezoneToUTC(date + "T" + time +":00") + "Z");
                    if (fragment instanceof MeetingDetailsFragment) {
                        ((MeetingDetailsFragment) fragment).setSelectedTimeSlot(item, Utility.ConvertUTCToUserTimezoneForSlotTime(item.getFrom_hour() + ":" + item.getFrom_min()));
                    } else {
                        ((NextMeetingDetailsFragment) fragment).setSelectedTimeSlot(item ,Utility.ConvertUTCToUserTimezoneForSlotTime( item.getFrom_hour() + ":" + item.getFrom_min()));
                    }
                    notifyDataSetChanged();
                }
            });
        }

    }

    public void clearData() {
        childArrayList.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return childArrayList.size();
    }
}
