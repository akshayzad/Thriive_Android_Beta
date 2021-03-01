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

import com.bumptech.glide.Glide;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.ReasonListPOJO;
import com.thriive.app.models.ReasonListPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReasonListAdapter extends RecyclerView.Adapter<ReasonListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private Fragment fragment;
    private ArrayList<ReasonListPOJO> reasonList;
    private int currentItem = -1;
    private SharedData sharedData;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_reasonName)
        TextView txt_reasonName;
        @BindView(R.id.image)
        ImageView image;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public ReasonListAdapter(Context context, Fragment fragment, ArrayList<ReasonListPOJO> reasonList) {
        this.context = context;
        this.fragment = fragment;
        this.reasonList = reasonList;
        sharedData = new SharedData(context);
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
        Glide.with(context)
                .load(sharedData.getStringData(SharedData.API_URL) +"reason_button/"+ item.getRowcode() + ".png")
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                ((MeetingRequestFragment) fragment ).get_l1_attrib(""+item.getReasonId(), ""+item.getReasonName(),
                        sharedData.getStringData(SharedData.API_URL) +"reason_button/"+ item.getRowcode() + ".png");
            }
        });
    }

    @Override
    public int getItemCount() {
        return reasonList.size();
    }
}