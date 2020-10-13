package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.MeetingJoinActivity;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.fragments.MeetingsFragment;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.textdrawable.TextDrawable;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduledAdapter extends RecyclerView.Adapter<ScheduledAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList;
    private Fragment fragment;

    private  SharedData sharedData;
    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_tags)
        public FlexboxLayout layout_tags;
        @BindView(R.id.layout_avail)
        LinearLayout layout_avail;
        @BindView(R.id.layout_join)
        LinearLayout layout_join;
        @BindView(R.id.txt_giverName)
        TextView txt_giverName;
        @BindView(R.id.txt_profession)
        TextView txt_profession;
        @BindView(R.id.txt_dateTime)
        TextView txt_dateTime;
        @BindView(R.id.img_giver)
        CircleImageView img_giver;
        @BindView(R.id.rv_tags)
        RecyclerView rv_tags;
        @BindView(R.id.txt_reason)
        TextView txt_reason;
        @BindView(R.id.iv_linkdin)
        ImageView iv_linkdin;
        @BindView(R.id.iv_email)
        ImageView iv_email;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public ScheduledAdapter(Activity context, Fragment fragment, ArrayList<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList){
        this.context = context;
        this.fragment = fragment;
        sharedData = new SharedData(context);
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public ScheduledAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scheduled, parent, false);

        return new ScheduledAdapter.RecyclerAdapterHolder(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final ScheduledAdapter.RecyclerAdapterHolder holder,int position) {
        CommonMeetingListPOJO.MeetingListPOJO item  = requesterPOJOArrayList.get(position);
        ArrayList<String> arrayList = new ArrayList<>();
        if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID)))
        {
            arrayList.addAll(item.getGiverDomainTags());
            arrayList.addAll(item.getGiverSubDomainTags());
            holder.txt_giverName.setText("with "+item.getGiverName());
            sharedData.addStringData(SharedData.CALLING_NAME, item.getGiverName());
            try{
                if (item.getGiverDesignationTags().size() > 0){
                    holder.txt_profession.setText(item.getGiverDesignationTags().get(0));
                } else {
                    holder.txt_profession.setText("");
                }
            } catch (Exception e)
            {
                e.getMessage();
            }

          //  www.linkedin.com/in/sachchit-chaudhary-29464a14b
            if (item.getGiverPicUrl().equals("")){
                try {
                    Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(context.getColor(R.color.darkSeaGreen))
                            .useFont(typeface)
                            .fontSize(55) /* size in px */
                            .bold()
                            .toUpperCase()
                            .width(130)  // width in px
                            .height(130) // height in px
                            .endConfig()
                            .buildRect(Utility.getInitialsName(item.getGiverName()) , context.getColor(R.color.whiteTwo));
                    holder.img_giver.setImageDrawable(drawable);
                } catch (Exception e ){
                    e.getMessage();
                }

            } else {
                holder.img_giver.setMinimumWidth(120);
                holder.img_giver.setMaxHeight(120);
                holder.img_giver.setMinimumHeight(120);
                holder.img_giver.setMaxWidth(120);
                Glide.with(context)
                        .load(item.getGiverPicUrl())
                        .into(holder.img_giver);
            }


        } else {
            arrayList.addAll(item.getRequestorDomainTags());
            arrayList.addAll(item.getRequestorSubDomainTags());
            holder.txt_giverName.setText("with " +item.getRequestorName());
            sharedData.addStringData(SharedData.CALLING_NAME, item.getRequestorName());
            if (item.getRequestorDesignationTags().size() > 0) {
                holder.txt_profession.setText(item.getRequestorDesignationTags().get(0));
            } else {
                holder.txt_profession.setText("");
            }
            if (item.getRequestorPicUrl().equals("")){
                Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(context.getColor(R.color.darkGreyBlue))
                        .useFont(typeface)
                        .fontSize(55) /* size in px */
                        .bold()
                        .toUpperCase()
                        .width(130)  // width in px
                        .height(130) // height in px
                        .endConfig()
                        .buildRect(Utility.getInitialsName(item.getRequestorName()) , context.getColor(R.color.whiteTwo));
                holder.img_giver.setImageDrawable(drawable);
            } else {
                holder.img_giver.setMinimumWidth(120);
                holder.img_giver.setMaxHeight(120);
                holder.img_giver.setMinimumHeight(120);
                holder.img_giver.setMaxWidth(120);
                Glide.with(context)
                        .load(item.getRequestorPicUrl())
                        .into(holder.img_giver);
            }
        }
        holder.txt_reason.setText("Meeting for "+ item.getMeetingReason());
        holder.txt_dateTime.setText(Utility.getScheduledMeetingDate(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()),
                Utility.ConvertUTCToUserTimezone(item.getPlanEndTime())));

        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.rv_tags.setLayoutManager(gridLayout );
        holder.rv_tags.setAdapter(new ExperienceAdapter(context, arrayList));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.saveMeetingDetailsData(context, item);
                MeetingDetailsFragment addPhotoBottomDialogFragment =
                        (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
                addPhotoBottomDialogFragment.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(),
                        "MeetingDetailsFragment");
              //  (MeetingsFragment).callFragment();
            }
        });
        holder.img_giver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.saveMeetingDetailsData(context, item);
                MeetingDetailsFragment addPhotoBottomDialogFragment =
                        (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
                addPhotoBottomDialogFragment.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(),
                        "MeetingDetailsFragment");
                //  (MeetingsFragment).callFragment();
            }
        });
        holder.layout_avail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MeetingsFragment) fragment).getMeetingSlot(item.getMeetingCode());
            }
        });

        holder.layout_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 ((MeetingsFragment) fragment).startMeeting(item.getMeetingId());

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

                        context.startActivity(emailIntent);
//                        Intent intent = new Intent(Intent.ACTION_SEND);
//                        intent.setType("*/*");
//                        intent.putExtra(Intent.EXTRA_EMAIL, item.getGiverEmailId());
//                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
//                        if (intent.resolveActivity(context.getPackageManager()) != null) {
//                            context.startActivity(intent);
//                        }
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

                        context.startActivity(emailIntent);
                    }
                }
            }
        });
        holder.iv_linkdin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
                    if (item.getGiverLinkedinUrl().equals("")){
                        Toast.makeText(context, "Sorry linkedin not found", Toast.LENGTH_SHORT).show();
                        //www.linkedin.com/in/sachchit-chaudhary-29464a14b
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse("www.linkedin.com/in/sachchit-chaudhary-29464a14b"));
//                        context.startActivity(intent);
                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(item.getGiverLinkedinUrl()));
                            context.startActivity(intent);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getGiverLinkedinUrl()));
//                            intent.setPackage("com.linkedin.android");
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);
                        } catch (Exception e) {
                        }

                    }

                } else {
                    if (item.getRequestorLinkedinUrl().equals("")){
                        Toast.makeText(context, "Sorry linkedin not found", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse(item.getRequestorLinkedinUrl()));
//                            context.startActivity(intent);
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getRequestorLinkedinUrl()));
//                            intent.setPackage("com.linkedin.android");
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);
                        } catch (Exception e) {
                        //    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getRequestorLinkedinUrl())));
                        }
                    }
                }
            }
        });
    }

    public String getDate(int i ){
       return requesterPOJOArrayList.get(i).getPlanStartTime();
    }
    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}