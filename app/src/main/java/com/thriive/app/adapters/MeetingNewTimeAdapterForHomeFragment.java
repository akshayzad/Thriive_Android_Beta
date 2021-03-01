package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.HomeActivity;
import com.thriive.app.R;
import com.thriive.app.fragments.HomeFragment;
import com.thriive.app.fragments.NewHomeFragment;
import com.thriive.app.models.MeetingDetailPOJO;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingNewTimeAdapterForHomeFragment extends RecyclerView.Adapter<MeetingNewTimeAdapterForHomeFragment.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<MeetingDetailPOJO> childArrayList;
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

        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }

    public MeetingNewTimeAdapterForHomeFragment(Context context, Fragment fragment, ArrayList<MeetingDetailPOJO> childArrayList){
        this.context = context;
        this.childArrayList = childArrayList;
        this.fragment = fragment;
    }
    @Override
    public MeetingNewTimeAdapterForHomeFragment.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_overview_selected_slot, parent, false);

        return new MeetingNewTimeAdapterForHomeFragment.RecyclerAdapterHolder(itemView);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MeetingNewTimeAdapterForHomeFragment.RecyclerAdapterHolder holder, int position) {
        MeetingDetailPOJO item  = childArrayList.get(position);
        holder.txt_time.setText(Utility.ConvertUTCToUserTimezoneWithTime( item.getSlot_from_date()));
        holder.txt_date.setText(Utility.ConvertUTCToUserTimezoneForSlot(item.getSlot_from_date()));
        holder.txt_day.setText(Utility.ConvertUTCToUserTimezoneForSlotDay(item.getSlot_from_date()));
        if (currentItem == position) {
            holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
        } else {
            holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItem = position;
//                ((HomeFragment) fragment).acceptMeetingNewTime(item);
                ((NewHomeFragment) fragment).acceptMeetingNewTime(item);
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
