package com.thriive.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
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
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.fragments.MeetingsFragment;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SchedulePagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<CommonMeetingListPOJO.MeetingListPOJO> arrayList;
    private SharedData sharedData;
    private Fragment fragment;

    public SchedulePagerAdapter(Context context, Fragment fragment, ArrayList<CommonMeetingListPOJO.MeetingListPOJO> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragment = fragment;
        sharedData = new SharedData(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public  Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_scheduled, container, false);
        CommonMeetingListPOJO.MeetingListPOJO item  = arrayList.get(position);
        LinearLayout layout_avail = view.findViewById(R.id.layout_avail);
        LinearLayout layout_join = view.findViewById(R.id.layout_join);
        TextView txt_giverName = view.findViewById(R.id.txt_giverName);
        TextView txt_profession = view.findViewById(R.id.txt_profession);
        TextView txt_dateTime = view.findViewById(R.id.txt_dateTime);
        CircleImageView img_giver = view.findViewById(R.id.img_giver);
        RecyclerView rv_tags = view.findViewById(R.id.rv_tags);
        TextView txt_reason = view.findViewById(R.id.txt_reason);
        ImageView iv_linkdin = view.findViewById(R.id.iv_linkdin);
        ImageView iv_email = view.findViewById(R.id.iv_email);
        ImageView iv_calender= view.findViewById(R.id.iv_calender);
        TextView txt_tag = view.findViewById(R.id.txt_tag);
        txt_tag.setText("Areas of expertise");
        txt_reason.setText("Meeting for "+ item.getMeetingReason());
        txt_dateTime.setText(Utility.getScheduledMeetingDate(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()),
                Utility.ConvertUTCToUserTimezone(item.getPlanEndTime())));
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        rv_tags.setLayoutManager(gridLayout);
//        if (item.getMeetingTag() != null){
//            rv_tags.setAdapter(new ExperienceAdapter(context, (ArrayList<String>) item.getMeetingTag()));
//        }

        if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
            ArrayList <String> arrayList = new ArrayList<>();

            arrayList.addAll(item.getGiverExpertiseTags());
            arrayList.addAll(item.getMeetingTag());
            rv_tags.setAdapter(new ExperienceAdapter(context, arrayList));
            txt_giverName.setText("with "+item.getGiverName());
            sharedData.addStringData(SharedData.CALLING_NAME, item.getGiverName());
            try{
                if (item.getGiverDesignationTags().size() > 0){
                    txt_profession.setText(item.getGiverDesignationTags().get(0));
                } else {
                    txt_profession.setText("");
                }
            } catch (Exception e) {
                e.getMessage();
            }
            if (item.getGiverPicUrl().equals("")){
                try {
                    Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
                    TextDrawable drawable = null;
                    drawable = TextDrawable.builder()
                            .beginConfig()
                            .textColor(context.getResources().getColor(R.color.darkGreyBlue))
                            .useFont(typeface)
                            .fontSize(55) /* size in px */
                            .bold()
                            .toUpperCase()
                            .width(130)  // width in px
                            .height(130) // height in px
                            .endConfig()
                            .buildRect(Utility.getInitialsName(item.getGiverName()) , context.getResources().getColor(R.color.whiteTwo));

                    img_giver.setImageDrawable(drawable);
               //     if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                } catch (Exception e ){
                    e.getMessage();
                }
            } else {
                img_giver.setMinimumWidth(120);
                img_giver.setMaxHeight(120);
                img_giver.setMinimumHeight(120);
                img_giver.setMaxWidth(120);
                Glide.with(context)
                        .load(item.getGiverPicUrl())
                        .into(img_giver);
            }
        } else {
            txt_giverName.setText("with " +item.getRequestorName());
            ArrayList <String> arrayList = new ArrayList<>();
            arrayList.addAll(item.getRequestorExpertiseTags());
            arrayList.addAll(item.getMeetingTag());
            rv_tags.setAdapter(new ExperienceAdapter(context, arrayList));
            sharedData.addStringData(SharedData.CALLING_NAME, item.getRequestorName());
            try {
                if (item.getRequestorDesignationTags().size() > 0) {
                    txt_profession.setText(item.getRequestorDesignationTags().get(0));
                } else {
                    txt_profession.setText("");
                }
            } catch (Exception e) {
                e.getMessage();
            }
            if (item.getRequestorPicUrl().equals("")){
                Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
                TextDrawable drawable = null;
                drawable = TextDrawable.builder()
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

                img_giver.setImageDrawable(drawable);
            } else {
                img_giver.setMinimumWidth(120);
                img_giver.setMaxHeight(120);
                img_giver.setMinimumHeight(120);
                img_giver.setMaxWidth(120);
                Glide.with(context)
                        .load(item.getRequestorPicUrl())
                        .into(img_giver);
            }
        }
        view.setOnClickListener(new View.OnClickListener() {
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
        img_giver.setOnClickListener(new View.OnClickListener() {
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
        layout_avail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
                    ((MeetingsFragment) fragment).getMeetingSlot(item.getMeetingCode(), item.getGiverPersonaTags().get(0),
                            item.getMeetingReason(), item.getGiverCountryName(), item.getRequestorCountryName());

                } else {
                    ((MeetingsFragment) fragment).getMeetingSlot(item.getMeetingCode(), item.getRequestorPersonaTags().get(0),
                            item.getMeetingReason(), item.getRequestorCountryName() , item.getGiverCountryName());

                }
            }
        });

        layout_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.getCallJoin(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()))) {
                    if (Utility.getCallEdJoinJoin(Utility.ConvertUTCToUserTimezone(item.getPlanEndTime()))){
                        final Toast toast = Toast.makeText(context, "Meeting session has been ended",Toast.LENGTH_SHORT);
                        toast.show();
                        new CountDownTimer(2000, 1000)
                        {
                            public void onTick(long millisUntilFinished) {toast.show();}
                            public void onFinish() {toast.cancel();}
                        }.start();
                    } else {

                        ((MeetingsFragment) fragment).startMeeting(item.getMeetingId());
                    }
                 //   startMeeting();
                } else {
                    final Toast toast = Toast.makeText(context, "Meeting is yet to start",Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(2000, 1000)
                    {
                        public void onTick(long millisUntilFinished) {toast.show();}
                        public void onFinish() {toast.cancel();}
                    }.start();
             //   ..    Toast.makeText(context, "Meeting is yet to start", Toast.LENGTH_LONG).show();

                    //      Toast.makeText(context, ""+Utility.getCallJoin(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime())), Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
                    if (item.getGiverEmailId().equals("")){
                        Toast.makeText(context, "Sorry email not found", Toast.LENGTH_SHORT).show();

                    } else {
                        try {
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
                        } catch (Exception e){
                            e.getMessage();
                        }

                    }

                } else {
                    if (item.getRequestorEmailId().equals("")){
                        Toast.makeText(context, "Sorry email not found", Toast.LENGTH_SHORT).show();

                    } else {
                        try {
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
                        } catch (Exception e){
                            e.getMessage();
                        }

                    }
                }
            }
        });
        iv_linkdin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
                    if (item.getGiverLinkedinUrl().equals("")){
                        Toast.makeText(context, "Sorry linkedin not found", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(item.getGiverLinkedinUrl()));
                            context.startActivity(intent);
                        } catch (Exception e) {
                        }
                    }
                } else {
                    if (item.getRequestorLinkedinUrl().equals("")){
                        Toast.makeText(context, "Sorry linkedin not found", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(item.getRequestorLinkedinUrl()));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            //    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getRequestorLinkedinUrl())));
                        }
                    }
                }
            }
        });
        iv_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "";
                if (item.getRequestorId().equals(sharedData.getIntData(SharedData.USER_ID))) {
                    title = "Thriive Meet with "+ item.getGiverName() ;
                } else {
                    title = "Thriive Meet with "+ item.getRequestorName() ;
                }
                SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                long lnsTime = 0, lneTime = 0;
                Date dateObject;
                try {
                    dateObject = in_format.parse(Utility.ConvertUTCToUserTimezone(item.getPlanStartTime()));
                    lnsTime = dateObject.getTime();
                    Log.e("null", Long.toString(lnsTime));

                    dateObject = in_format.parse(Utility.ConvertUTCToUserTimezone(item.getPlanEndTime()));
                    lneTime = dateObject.getTime();
                    Log.e("null", Long.toString(lneTime));

                    ((MeetingsFragment)fragment).getAddCalenderEvent(title, item.getMeetingReason(), lnsTime, lneTime);

                }

                catch (java.text.ParseException e) {
                    // TODO Auto-generated catch block
                }

            }
        });

        container.addView(view);
        return view;
    }

    public String getDate(int i ){
        return arrayList.get(i).getPlanStartTime();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
