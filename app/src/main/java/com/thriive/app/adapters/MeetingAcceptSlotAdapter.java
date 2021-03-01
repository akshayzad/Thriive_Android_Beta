package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.fragments.NotificationFragment;
import com.thriive.app.models.MeetingDetailPOJO;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingAcceptSlotAdapter extends RecyclerView.Adapter<MeetingAcceptSlotAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<MeetingDetailPOJO> childArrayList;
    Fragment fragment;
    int currentItem = -1;
    List<CardView> cardViewList = new ArrayList<>();
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

    public MeetingAcceptSlotAdapter(Context context, Fragment fragment, ArrayList<MeetingDetailPOJO> childArrayList){
        this.context = context;
        this.fragment = fragment;
        this.childArrayList = childArrayList;
    }
    @Override
    public MeetingAcceptSlotAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_overview_selected_slot, parent, false);

        return new MeetingAcceptSlotAdapter.RecyclerAdapterHolder(itemView);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MeetingAcceptSlotAdapter.RecyclerAdapterHolder holder, int position) {
        MeetingDetailPOJO item  = childArrayList.get(position);
        holder.txt_time.setText(Utility.ConvertUTCToUserTimezoneWithTime( item.getSlot_from_date()));
        holder.txt_date.setText(Utility.ConvertUTCToUserTimezoneForSlot(item.getSlot_from_date()));
        holder.txt_day.setText(Utility.ConvertUTCToUserTimezoneForSlotDay(item.getSlot_from_date()));

        if (currentItem == position) {
            holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
        } else {
            holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
        }

        if (!cardViewList.contains(holder.card_slots)) {
            cardViewList.add(holder.card_slots);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (context instanceof NotificationListActivity) {
                    currentItem = position;
                     Activity activity = Utility.unwrap(view.getContext());
//                    ((NotificationListActivity)  activity).acceptMeetingSlot(item);
                    notifyDataSetChanged();
                }*/
                for(CardView cardView : cardViewList){
                    holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
                }
                holder.card_slots.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
                currentItem = position;
                ((NotificationFragment)  fragment).acceptMeetingSlot(item);
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
