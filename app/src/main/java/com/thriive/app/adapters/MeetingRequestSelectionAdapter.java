package com.thriive.app.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingRequestSelectionAdapter extends RecyclerView.Adapter<MeetingRequestSelectionAdapter.RecyclerAdapterHolder> {
    private Context context;
    private Fragment fragment;
    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList;
    private int currentItem = -1;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_expertise)
        TextView txt_expertise;
        @BindView(R.id.layout_select)
        LinearLayout layout_select;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public MeetingRequestSelectionAdapter(Context context, ArrayList<CommonRequesterPOJO> requesterPOJOArrayList) {
        this.context = context;
        this.fragment = fragment;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }

    public MeetingRequestSelectionAdapter(Context context, Fragment fragment, ArrayList<CommonRequesterPOJO> requesterPOJOArrayList) {
        this.context = context;
        this.fragment = fragment;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }

    @Override
    public MeetingRequestSelectionAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expertise, parent, false);

        return new MeetingRequestSelectionAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MeetingRequestSelectionAdapter.RecyclerAdapterHolder holder, int position) {
        CommonRequesterPOJO item = requesterPOJOArrayList.get(position);
        holder.txt_expertise.setText(item.getName());

        if(currentItem == position){
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));

        } else {
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                currentItem = position;
                notifyDataSetChanged();
              //  ((MeetingRequestFragment) fragment ).meetingRequestExpertise();
//
            }
        });

    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}