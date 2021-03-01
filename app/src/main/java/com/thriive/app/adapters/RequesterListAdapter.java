package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.HomeActivity;
import com.thriive.app.R;
import com.thriive.app.fragments.HomeFragment;
import com.thriive.app.fragments.MeetingsFragment;
import com.thriive.app.fragments.NewHomeFragment;
import com.thriive.app.models.PendingMeetingRequestPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequesterListAdapter extends RecyclerView.Adapter<RequesterListAdapter.RecyclerAdapterHolder> {

    private Context context;
    private ArrayList<PendingMeetingRequestPOJO.MeetingRequestList> requesterPOJOArrayList;
    private Fragment fragment;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_persona)
        TextView txt_persona;
        @BindView(R.id.txt_reason)
        TextView txt_reason;
        @BindView(R.id.label_shortlisting)
        TextView label_shortlisting;
        @BindView(R.id.rv_tags)
        RecyclerView rv_tags;
        @BindView(R.id.layout_requested)
        CardView layout_requested;
        @BindView(R.id.layout_region)
        LinearLayout layout_region;
        @BindView(R.id.txt_region)
        TextView txt_region;

        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public RequesterListAdapter(Context context,Fragment fragment, ArrayList<PendingMeetingRequestPOJO.MeetingRequestList> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
        this.fragment = fragment;
    }
    @Override
    public RequesterListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_requester, parent, false);

        return new RequesterListAdapter.RecyclerAdapterHolder(itemView);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final RecyclerAdapterHolder holder,int position) {
        PendingMeetingRequestPOJO.MeetingRequestList item  = requesterPOJOArrayList.get(position);

        if (item.getSel_meeting().isFlag_giver_prop_time()){
            holder.label_shortlisting.setText("New Time");
            holder.layout_requested.setBackground(context.getResources().getDrawable(R.drawable.outline_with_background));
        }else {
            holder.layout_requested.setBackground(context.getResources().getDrawable(R.drawable.background_with_plea));
            holder.label_shortlisting.setText("Submitted");
        }
        holder.txt_reason.setText("For "+item.getReasonName());
        holder.txt_persona.setText("with "+item.getGiverPersonaName());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(item.getDomainTags());
        arrayList.addAll(item.getSubDomainTags());
        arrayList.addAll(item.getExpertiseTags());
        if (arrayList.size() < 2){
            holder.layout_region.setVisibility(View.VISIBLE);
            holder.txt_region.setText(item.getSel_meeting().getCountry_name());
        }else {
            holder.layout_region.setVisibility(View.GONE);
        }
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.rv_tags.setLayoutManager(gridLayout);
        holder.rv_tags.setAdapter(new ExperienceAdapter(context, arrayList));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getSel_meeting().isFlag_giver_prop_time()){
//                    ((HomeFragment) fragment).setMeetingNewTime(item);
                    ((NewHomeFragment) fragment).setMeetingNewTime(item);
                }else {
                    ((HomeActivity)view.getContext()).setMeetingFragment(item);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}
