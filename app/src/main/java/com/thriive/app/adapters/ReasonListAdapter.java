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
import com.thriive.app.models.ReasonListPOJO;
import com.thriive.app.models.ReasonListPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReasonListAdapter extends RecyclerView.Adapter<ReasonListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private Fragment fragment;
    private ArrayList<ReasonListPOJO> reasonList;
    private int currentItem = -1;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_reasonName)
        TextView txt_reasonName;
//        @BindView(R.id.layout_select)
//        LinearLayout layout_select;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ReasonListAdapter(Context context, ArrayList<ReasonListPOJO> reasonList) {
        this.context = context;
        this.fragment = fragment;
        this.reasonList = reasonList;
    }

    public ReasonListAdapter(Context context, Fragment fragment, ArrayList<ReasonListPOJO> reasonList) {
        this.context = context;
        this.fragment = fragment;
        this.reasonList = reasonList;
    }

    @Override
    public ReasonListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reason_list, parent, false);

        return new ReasonListAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReasonListAdapter.RecyclerAdapterHolder holder, int position) {
        ReasonListPOJO item = reasonList.get(position);
        holder.txt_reasonName.setText(item.getReasonName());

//        if(currentItem == position){
//            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
//
//        } else {
//            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                ((MeetingRequestFragment) fragment ).getPersona(""+item.getReasonId(), ""+item.getReasonName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return reasonList.size();
    }
}