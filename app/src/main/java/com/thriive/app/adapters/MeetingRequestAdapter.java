package com.thriive.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.SelectBusinessPOJO;
import com.thriive.app.models.SelectBusinessPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingRequestAdapter extends RecyclerView.Adapter<MeetingRequestAdapter.RecyclerAdapterHolder> {
    private Context context;
    private Fragment fragment;
    private ArrayList<SelectBusinessPOJO> requesterPOJOArrayList;
    private boolean isSelect =  false;
    private int currentItem = -1;



    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_select)
        RelativeLayout layout_select;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.layout_hide)
        RelativeLayout layout_hide;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public MeetingRequestAdapter(Context context, Fragment fragment,  ArrayList<SelectBusinessPOJO> requesterPOJOArrayList){
        this.context = context;
        this.fragment = fragment;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public MeetingRequestAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting_profession, parent, false);


        return new MeetingRequestAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MeetingRequestAdapter.RecyclerAdapterHolder holder,int position) {
        SelectBusinessPOJO item  = requesterPOJOArrayList.get(position);
        if(currentItem == position){
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.reactangle_terracota));
            holder.layout_hide.setVisibility(View.VISIBLE);
        } else {
            holder.layout_select.setBackground(null);
            holder.layout_hide.setVisibility(View.GONE);
        }
        holder.image.setImageDrawable(item.getDrawable());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                currentItem = position;
                notifyDataSetChanged();
                ((MeetingRequestFragment) fragment ).meetingRequestResult();

            }
        });
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}
