package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.CircularSeekBar;
import com.thriive.app.utilities.PreciseCountdown;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.thriive.app.NotificationListActivity.TAG;

public class PendingNotificationAdapter extends RecyclerView.Adapter<PendingNotificationAdapter.RecyclerAdapterHolder> {
    private Context context;
    private List<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList;
    long progress = 0;
    PreciseCountdown preciseCountdown;
    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_tags)
        public RecyclerView rv_tags;
        @BindView(R.id.rv_experience)
        public RecyclerView rv_experience;
        @BindView(R.id.btn_meeting_decline)
        public ImageButton btn_meeting_decline;
        @BindView(R.id.btn_meeting_accept)
        public ImageButton btn_meeting_accept;
        @BindView(R.id.txt_persona)
        TextView txt_persona;
        @BindView(R.id.txt_profession)
        TextView txt_profession;
        @BindView(R.id.txt_objective)
        TextView txt_objective;
        @BindView(R.id.txt_reason)
        TextView txt_reason;
        @BindView(R.id.circular_seekbar)
        CircularSeekBar circularSeekbar;
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.img_user)
        CircleImageView img_user;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public PendingNotificationAdapter(Context context, List<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList) {
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }

    @Override
    public PendingNotificationAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pending_notification, parent, false);

        return new PendingNotificationAdapter.RecyclerAdapterHolder(itemView);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NewApi"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final PendingNotificationAdapter.RecyclerAdapterHolder holder, int position) {
        CommonMeetingListPOJO.MeetingListPOJO item = requesterPOJOArrayList.get(position);
        if (item.getRequestorPicUrl().equals("")){
            try {
                Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(context.getResources().getColor(R.color.darkGreyBlue))
                        .useFont(typeface)
                        .fontSize(55) /* size in px */
                        .bold()
                        .toUpperCase()
                        .width(130)  // width in px
                        .height(130) // height in px
                        .endConfig()
                        .buildRect(Utility.getInitialsName(item.getRequestorName()) , context.getResources().getColor(R.color.whiteTwo));
                holder.img_user.setImageDrawable(drawable);
            } catch (Exception e){
                e.getMessage();
            }

        } else {
            holder.img_user.setMinimumWidth(120);
            holder.img_user.setMaxHeight(120);
            holder.img_user.setMinimumHeight(120);
            holder.img_user.setMaxWidth(120);
            Glide.with(context)
                    .load(item.getRequestorPicUrl())
                    .into(holder.img_user);
        }
        try {
            holder.txt_profession.setText(""+item.getRequestorSubTitle());
//            if (item.getRequestorDesignationTags().size() == 0) {
//                holder.txt_profession.setText("");
//            } else {
//                holder.txt_profession.setText(item.getRequestorDesignationTags().get(0));
//            }
        } catch (Exception e){
            e.getMessage();
        }

        try {
            holder.txt_name.setText(Utility.getEncodedName(item.getRequestorName()));
        } catch (Exception e)
        {
            e.getMessage();
        }
        ArrayList<String> arrayList = new ArrayList<>();
       // arrayList.addAll(item.getRequestorDomainTags());
    //    arrayList.addAll(item.getRequestorSubDomainTags());
      //  arrayList.addAll(item.getRequestorExpertiseTags());
        arrayList.addAll(item.getMeetingTag());

        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.rv_tags.setLayoutManager(gridLayout);
        holder.rv_tags.setAdapter(new ExpertiseAdapter(context, arrayList));
        holder.txt_reason.setText("Meeting for "+item.getMeetingReason());
        // setProgress();
        ArrayList<String> ex_array = new ArrayList<>();
//        if (item.getRequestorDesignationTags() != null) {
//            for (int i = 0; i < item.getRequestorDesignationTags().size(); i++) {
//                if (i != 0) {
//                //    ex_array.add(item.getRequestorDesignationTags().get(i));
//                }
//            }
//        }
        ex_array.addAll(item.getRequestorDesignationTags());
        ex_array.addAll(item.getRequestorExperienceTags());
        FlexboxLayoutManager gridLayout1 = new FlexboxLayoutManager(context);
        holder.rv_experience.setLayoutManager(gridLayout1);
        holder.rv_experience.setAdapter(new ExperienceAdapter(context, ex_array));
       // holder.circularSeekbar.setMax(TimeUnit.MINUTES.toMillis(7));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof NotificationListActivity) {
                    ((NotificationListActivity) view.getContext()).detailsMeeting(item);
                    //((MyCoursesDetailsActivity)view.getContext()).checkPermissionForNotes(child.getNotesPdfPath());
                }

            }
        });

        holder.btn_meeting_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof NotificationListActivity) {
                    ((NotificationListActivity) view.getContext()).getDeclineMeeting(item.getMeetingCode());
                }
            }
        });

        holder.btn_meeting_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof NotificationListActivity) {
                    ((NotificationListActivity) view.getContext()).detailsMeeting(item);
                    //((MyCoursesDetailsActivity)view.getContext()).checkPermissionForNotes(child.getNotesPdfPath());
                }

            }
        });
    }
    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}
