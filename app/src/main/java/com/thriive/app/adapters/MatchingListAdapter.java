package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.HomeActivity;
import com.thriive.app.R;
import com.thriive.app.models.CommonMatchingPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchingListAdapter extends PagerAdapter {

    private Context context;
    private List<CommonMatchingPOJO.EntityListPOJO> arrayList;
    private Fragment fragment;

    public MatchingListAdapter(Context context, Fragment fragment, List<CommonMatchingPOJO.EntityListPOJO> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @NotNull
    @Override
    public  Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_matching, container, false);
        CommonMatchingPOJO.EntityListPOJO item  = arrayList.get(position);

        CircleImageView img_profile = view.findViewById(R.id.img_profile);
        TextView txt_entity_name = view.findViewById(R.id.txt_entity_name);
        TextView txt_designation_name = view.findViewById(R.id.txt_designation_name);
        //TextView txt_more = view.findViewById(R.id.txt_more);
        RecyclerView rv_tags = view.findViewById(R.id.rv_tags);
        RelativeLayout color_relative_layout = view.findViewById(R.id.color_relative_layout);

        if (position == 0){
            color_relative_layout.setBackgroundColor(context.getResources().getColor(R.color.terracota));
        } else if (position == 1){
            color_relative_layout.setBackgroundColor(context.getResources().getColor(R.color.butterscotch));

        } else if (position == 2){
            color_relative_layout.setBackgroundColor(context.getResources().getColor(R.color.darkSeaGreen));
        }

        if (item.getPic_url().equals("")){
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
                    .buildRect(Utility.getInitialsName(item.getEntity_name()) , context.getResources().getColor(R.color.whiteTwo));
            img_profile.setImageDrawable(drawable);
        } else {
            img_profile.setMaxWidth(80);
            img_profile.setMaxHeight(80);
            Glide.with(context)
                    .load(item.getPic_url())
                    .into(img_profile);
        }

        txt_entity_name.setText(item.getEntity_name());
        txt_designation_name.setText(item.getDesignation_name() + ", " + item.getCompany_name());
        //txt_tags.setText(""+item.getMeetingLabel());
        ArrayList<String> arrayList = new ArrayList<>();
        if (item.getDomainList() != null){
            arrayList.addAll(item.getDomainList());
        }

        if (item.getExpertiseList() != null){
            arrayList.addAll(item.getExpertiseList());
        }
        //arrayList.addAll(item.getSubDomainTags());

        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        rv_tags.setLayoutManager(gridLayout );
//        if (arrayList.size() > 0){
//            txt_tags.setVisibility(View.VISIBLE);
//        } else {
//            txt_tags.setVisibility(View.GONE);
//        }
        if (arrayList.size() > 0){

            if (arrayList.size() > 2){
                //txt_more.setVisibility(View.VISIBLE);
                ArrayList<String> arr1 = new ArrayList<>();
                for (int i = 0; i < 3 ; i++) {
                    arr1.add(arrayList.get(i));
                    rv_tags.setAdapter(new MatchingTagAdapter(context, arr1,arrayList.size()));
                }
            }else {
                //txt_more.setVisibility(View.GONE);
                rv_tags.setAdapter(new MatchingTagAdapter(context, arrayList,arrayList.size()));
            }
        }



        container.addView(view);

        return view;
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
