package com.thriive.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
        @BindView(R.id.txt_reason)
        TextView txt_reason;
        @BindView(R.id.iv_linkdin)
        ImageView iv_linkdin;
        @BindView(R.id.iv_email)
        ImageView iv_email;
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
                        .textColor(context.getResources().getColor(R.color.whiteTwo))
                        .useFont(typeface)
                        .fontSize(40)/* size in px */
                        .bold()
                        .toUpperCase()
                        .width(100)  // width in px
                        .height(100) // height in px
                        .endConfig()
                        .buildRect(Utility.getInitialsName(item.getGiverName()) , context.getResources().getColor(R.color.butterscotch));
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
            if (item.getRequestorPicUrl().equals("")){
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
        holder.txt_reason.setText("Meeting for "+ item.getMeetingReason());
        holder.iv_linkdin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
                    if (item.getGiverLinkedinUrl().equals("")){
                        Toast.makeText(context, "Sorry linkedin not found", Toast.LENGTH_SHORT).show();

                    } else {
                        try {

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getGiverLinkedinUrl()));
                            intent.setPackage("com.linkedin.android");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            view.getContext().startActivity(intent);
                        } catch (Exception e) {
                            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getGiverLinkedinUrl())));
                        } finally {

                        }

                    }

                } else {
                    if (item.getRequestorLinkedinUrl().equals("")){
                        Toast.makeText(context, "Sorry linkedin not found", Toast.LENGTH_SHORT).show();

                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getRequestorLinkedinUrl()));
                            intent.setPackage("com.linkedin.android");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            view.getContext().startActivity(intent);
                        } catch (Exception e) {
                            view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getRequestorLinkedinUrl())));
                        } finally {

                        }
                    }
                }

            }
        });

        holder.iv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
                    if (item.getGiverEmailId().equals("")){
                        Toast.makeText(context, "Sorry email not found", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getGiverEmailId()});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                        final PackageManager pm = context.getPackageManager();
                        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                        ResolveInfo best = null;
                        for(final ResolveInfo info : matches)
                            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                                best = info;
                        if (best != null)
                            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

                        view.getContext().startActivity(emailIntent);

//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("*/*");
//                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{ meetingListPOJO.getGiverEmailId()});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "");
//                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    getActivity().startActivity(intent);
//                }
                    }

                } else {
                    if (item.getRequestorEmailId().equals("")){
                        Toast.makeText(context, "Sorry email not found", Toast.LENGTH_SHORT).show();

                    } else {

                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ item.getRequestorEmailId()});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                        final PackageManager pm = context.getPackageManager();
                        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                        ResolveInfo best = null;
                        for(final ResolveInfo info : matches)
                            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                                best = info;
                        if (best != null)
                            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

                        view.getContext().startActivity(emailIntent);

//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("*/*");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{meetingListPOJO.getRequestorEmailId()});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "");
//                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    getActivity().startActivity(intent);
//                }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPOJOS.size();
    }
}