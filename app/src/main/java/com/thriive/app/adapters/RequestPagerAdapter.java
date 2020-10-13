package com.thriive.app.adapters;

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
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.fragments.MeetingsFragment;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

public class RequestPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<PendingMeetingRequestPOJO.MeetingRequestList> arrayList;
    SharedData sharedData;
    private Fragment fragment;

    public RequestPagerAdapter(Context context, Fragment fragment, ArrayList<PendingMeetingRequestPOJO.MeetingRequestList> arrayList) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_requested, container, false);
        PendingMeetingRequestPOJO.MeetingRequestList item  = arrayList.get(position);

        TextView txt_persona = view.findViewById(R.id.txt_persona);
        RecyclerView rv_tags = view.findViewById(R.id.rv_tags);
        TextView txt_reason = view.findViewById(R.id.txt_reason);

        txt_persona.setText("with "+item.getGiverPersonaName());
        txt_reason.setText("Meeting for "+item.getReasonName());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(item.getDomainTags());
        arrayList.addAll(item.getSubDomainTags());
        arrayList.addAll(item.getExpertiseTags());
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        rv_tags.setLayoutManager(gridLayout );
        rv_tags.setAdapter(new ExperienceAdapter(context, arrayList));


        container.addView(view);

        return view;
    }
    public String getDate(int i ){
        return arrayList.get(i).getRequestDate();
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
