package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.CommonRequestTimeSlots;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedSlotsAdapter extends RecyclerView.Adapter<SelectedSlotsAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> childArrayList;
    Fragment fragment;
    int currentItem = -1;
    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_date)
        public TextView txt_date;

        @BindView(R.id.txt_day)
        public TextView txt_day;
        @BindView(R.id.txt_time)
        public TextView txt_time;

        @BindView(R.id.layout_select)
        CardView card_slots;

        @BindView(R.id.layout_remove_slot)
        RelativeLayout layout_remove_slot;

        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }

    public SelectedSlotsAdapter(Context context, Fragment fragment, ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> childArrayList){
        this.context = context;
        this.fragment = fragment;
        this.childArrayList = childArrayList;
    }
    @Override
    public RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_selected_time_slots, parent, false);

        return new  RecyclerAdapterHolder(itemView);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final RecyclerAdapterHolder holder, int position) {
        CommonRequestTimeSlots.EntitySlotsListPOJO item  = childArrayList.get(position);
        holder.txt_time.setText(Utility.ConvertUTCToUserTimezoneForSlotTime( item.getFrom_hour() + ":" + item.getFrom_min()));
        holder.txt_date.setText(Utility.ConvertUTCToUserTimezoneForSlot(item.getFor_date()));
        holder.txt_day.setText(Utility.ConvertUTCToUserTimezoneForSlotDay(item.getFor_date()));
        if (currentItem == position) {
            holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
            holder.layout_remove_slot.setVisibility(View.VISIBLE);
        } else {
            holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
            holder.layout_remove_slot.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItem = position;
//                 ((MeetingRequestFragment) fragment).getSelectedTimeSlot(item,position);
                 notifyDataSetChanged();
            }
        });

        holder.layout_remove_slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MeetingRequestFragment) fragment).removeSelectedTimeSlot(position);
                notifyDataSetChanged();
            }
        });
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

