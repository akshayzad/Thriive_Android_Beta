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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.fragments.NotificationFragment;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.CircularSeekBar;
import com.thriive.app.utilities.PreciseCountdown;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.clevertap.android.sdk.Utils.runOnUiThread;
import static com.thriive.app.NotificationListActivity.TAG;

public class PendingNotificationAdapter extends RecyclerView.Adapter<PendingNotificationAdapter.RecyclerAdapterHolder> {
    private Context context;
    private List<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList;
    private Fragment fragment;
    long progress = 0;
    private long seconds = 0, minutes = 0, hours = 0, call_time;
    private PreciseCountdown preciseCountdown, endCountDown;


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
        @BindView(R.id.label_timer)
        TextView txtTimer;
        @BindView(R.id.img_user)
        CircleImageView img_user;
        @BindView(R.id.layout_timer)
        RelativeLayout layout_timer;
        @BindView(R.id.card_view_layout)
        CardView card_view_layout;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public PendingNotificationAdapter(Context context, Fragment fragment, List<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList) {
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
        this.fragment = fragment;
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

        if (item.getSel_meeting().isFlag_accepted() && item.getSel_meeting().isFlag_giver_prop_time()
                && !item.getSel_meeting().isFlag_req_accept_prop_time()){
            holder.layout_timer.setVisibility(View.GONE);
            holder.card_view_layout.setBackground(context.getResources().getDrawable(R.drawable.bg_outline_nottification));
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
            arrayList.addAll(item.getMeetingTag());
            ArrayList<String> combine_array = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++){
                if (!arrayList.get(i).equals("")){
                    combine_array.add(arrayList.get(i));
                }
            }
            HashSet hs = new HashSet();
            hs.addAll(combine_array); // demoArrayList= name of arrayList from which u want to remove duplicates
            combine_array.clear();
            combine_array.addAll(hs);
            FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
            holder.rv_tags.setLayoutManager(gridLayout);
            holder.rv_tags.setAdapter(new ExpertiseAdapter(context, combine_array));
            holder.txt_reason.setText("Meeting for "+item.getMeetingReason());
            //setProgress();
            ArrayList<String> ex_array = new ArrayList<>();

            ex_array.addAll(item.getRequestorDesignationTags());
            ex_array.addAll(item.getRequestorExperienceTags());
            FlexboxLayoutManager gridLayout1 = new FlexboxLayoutManager(context);
            holder.rv_experience.setLayoutManager(gridLayout1);
            holder.rv_experience.setAdapter(new ExperienceAdapter(context, ex_array));
            // holder.circularSeekbar.setMax(TimeUnit.MINUTES.toMillis(7));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*if (context instanceof NotificationListActivity) {
                        ((NotificationListActivity) view.getContext()).show_giver_detail_pop_up(item);
                    }*/
                    ((NotificationFragment) fragment).show_giver_detail_pop_up(item);

                }
            });
        }else {
            holder.layout_timer.setVisibility(View.VISIBLE);
            holder.card_view_layout.setBackground(context.getResources().getDrawable(R.drawable.bg_disable_outline));
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
            ArrayList<String> combine_array = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++){
                if (!arrayList.get(i).equals("")){
                    combine_array.add(arrayList.get(i));
                }
            }
            HashSet hs = new HashSet();
            hs.addAll(combine_array); // demoArrayList= name of arrayList from which u want to remove duplicates
            combine_array.clear();
            combine_array.addAll(hs);
            FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
            holder.rv_tags.setLayoutManager(gridLayout);
            holder.rv_tags.setAdapter(new ExpertiseAdapter(context, combine_array));
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
                    /*if (context instanceof NotificationListActivity) {
                        ((NotificationListActivity) view.getContext()).detailsMeeting(item);
                    }*/
                    ((NotificationFragment) fragment).detailsMeeting(item);

                }
            });

            holder.btn_meeting_decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*if (context instanceof NotificationListActivity) {
                        ((NotificationListActivity) view.getContext()).getDeclineMeeting(item.getMeetingCode());
                    }*/
                    ((NotificationFragment) fragment).getDeclineMeeting(item.getMeetingCode());
                }
            });

            holder.btn_meeting_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*if (context instanceof NotificationListActivity) {
                        ((NotificationListActivity) view.getContext()).detailsMeeting(item);
                    }*/
                    ((NotificationFragment) fragment).detailsMeeting(item);
                }
            });

            try {
                String match_date = item.getSel_meeting().getDate_matched();
                final long millisToAdd = 43_200_000; //12 hours => 43,200,000

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date d = format.parse(match_date);
                d.setTime(d.getTime() + millisToAdd);
                Date abc =  d;

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String dateTime = dateFormat.format(abc);
                call_time = Utility.getTimeDifferenceWithCurrentTime(Utility.ConvertUTCToUserTimezone(dateTime));
                startTimer(holder);
                preciseCountdown.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }

    @SuppressLint("RestrictedApi")
    private void startTimer(PendingNotificationAdapter.RecyclerAdapterHolder holder) {
        // TimeUnit.MINUTES.toMillis(30)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                preciseCountdown = new PreciseCountdown(call_time, 1000, 0) {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onTick(long timeLeft) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                seconds ++;
                                if(seconds == 60) {
                                    seconds = 0;
                                    minutes ++;
                                }
                                if(minutes == 60) {
                                    seconds = 0;
                                    minutes = 0;
                                    hours ++;
                                }

                                NumberFormat f = new DecimalFormat("00");

                                long hour1 = (timeLeft / 3600000) % 24;
                                long min1 = (timeLeft / 60000) % 60;
                                long sec1 = (timeLeft / 1000) % 60;
                                if (timeLeft >= 0){
                                    holder.txtTimer.setText(f.format(hour1)
                                            + ":"  + f.format(min1));
                                }else {
                                    holder.txtTimer.setText("00"
                                            + ":"  + "00");
                                }

                            }
                        });
                    }
                    @Override
                    public void onFinished() {
                        Log.d(TAG, "RESTART");
                    }
                };
            }
        });

    }
}
