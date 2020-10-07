package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thriive.app.R;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.CommonMeetingListPOJO.MeetingListPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingHistoryAdapter extends RecyclerView.Adapter<MeetingHistoryAdapter.RecyclerAdapterHolder> {
    private Context context;
    private List<MeetingListPOJO> listPOJOS;
    private SharedData sharedData;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.img_user)
        CircleImageView img_user;

        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this, itemView);
        }
    }
    public MeetingHistoryAdapter(Context context, List<CommonMeetingListPOJO.MeetingListPOJO> listPOJOS){
        this.context = context;
        this.listPOJOS = listPOJOS;
        sharedData = new SharedData(context);
    }
    @Override
    public MeetingHistoryAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_history, parent, false);

        return new MeetingHistoryAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MeetingHistoryAdapter.RecyclerAdapterHolder holder,int position) {
        CommonMeetingListPOJO.MeetingListPOJO item  = listPOJOS.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user);
        requestOptions.error(R.drawable.user);
        if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID)))
        {
            holder.txt_name.setText(item.getGiverName());

            Glide.with(context)
                    .load(item.getGiverPicUrl())
                    .apply(requestOptions)
                    .into(holder.img_user);

        } else {

            holder.txt_name.setText(item.getRequestorName());
            Glide.with(context)
                    .load(item.getRequestorPicUrl())
                    .apply(requestOptions)
                    .into(holder.img_user);
        }
        holder.txt_date.setText(Utility.getMeetingDate(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()),
                Utility.ConvertUTCToUserTimezone(item.getPlanEndTime())));
    }

    @Override
    public int getItemCount() {
        return listPOJOS.size();
    }
}