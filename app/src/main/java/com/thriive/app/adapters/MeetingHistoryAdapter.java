package com.thriive.app.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
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
import com.thriive.app.utilities.textdrawable.TextDrawable;

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
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final MeetingHistoryAdapter.RecyclerAdapterHolder holder,int position) {
        CommonMeetingListPOJO.MeetingListPOJO item  = listPOJOS.get(position);
        Log.d("Rating", item.getRequestorResponseInt() +" "+item.getGiverResponseInt() );
        if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
            holder.txt_name.setText(item.getGiverName());
            Log.d("Rating", item.getGiverResponseInt() +" "+item.getGiverResponseInt().floatValue() );
//            holder.ratingBar.setStepSize(1.0f);
//            holder.ratingBar.setMax(5);
            holder.ratingBar.setRating((float)item.getRequestorResponseInt());
            holder.ratingBar.setIsIndicator(true);
            if (item.getGiverPicUrl().equals("")){
                Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(context.getColor(R.color.whiteTwo))
                        .useFont(typeface)
                        .fontSize(40)/* size in px */
                        .bold()
                        .toUpperCase()
                        .width(100)  // width in px
                        .height(100) // height in px
                        .endConfig()
                        .buildRect(Utility.getInitialsName(item.getGiverName()) , context.getColor(R.color.butterscotch));
                holder.img_user.setImageDrawable(drawable);
            } else {
                holder.img_user.setMaxWidth(60);
                holder.img_user.setMaxHeight(60);
                Glide.with(context)
                        .load(item.getGiverPicUrl())
                        .into(holder.img_user);
            }
        } else {
            Log.d("Rating", " "+item.getRequestorResponseInt().floatValue() );
//            holder.ratingBar.setStepSize(1.0f);
//            holder.ratingBar.setMax(5);
            holder.txt_name.setText(item.getRequestorName());
            holder.ratingBar.setRating((float)item.getGiverResponseInt());
            holder.ratingBar.setIsIndicator(true);
            if (item.getGiverPicUrl().equals("")){
                Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(context.getColor(R.color.whiteTwo))
                        .useFont(typeface)
                        .fontSize(40) /* size in px */
                        .bold()
                        .toUpperCase()
                        .width(100)  // width in px
                        .height(100) // height in px
                        .endConfig()
                        .buildRect(Utility.getInitialsName(item.getRequestorName()) , context.getColor(R.color.butterscotch));
                holder.img_user.setImageDrawable(drawable);
            } else {
                Glide.with(context)
                        .load(item.getRequestorPicUrl())
                        .into(holder.img_user);
            }
        }
        holder.txt_date.setText(Utility.getMeetingDate(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()),
                Utility.ConvertUTCToUserTimezone(item.getPlanEndTime())));
    }

    @Override
    public int getItemCount() {
        return listPOJOS.size();
    }
}