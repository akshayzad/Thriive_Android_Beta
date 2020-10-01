package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PendingNotificationAdapter extends RecyclerView.Adapter<PendingNotificationAdapter.RecyclerAdapterHolder> {
    private Context context;
    private List<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList;
    private BusinessProfessionAdapter businessProfessionAdapter;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_tags)
        public RecyclerView rv_tags;
        @BindView(R.id.rv_experience)
        public RecyclerView rv_experience;
        @BindView(R.id.rv_objective)
        public RecyclerView rv_objective;

        @BindView(R.id.btn_meeting_decline)
        public ImageButton btn_meeting_decline;
        @BindView(R.id.btn_meeting_accept)
        public ImageButton btn_meeting_accept;
        @BindView(R.id.txt_persona)
        TextView txt_persona;
        @BindView(R.id.progress)
        ProgressBar progress;
        @BindView(R.id.txt_objective)
        TextView txt_objective;
        @BindView(R.id.txt_reason)
        TextView txt_reason;

        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public PendingNotificationAdapter(Context context, List<CommonMeetingListPOJO.MeetingListPOJO> requesterPOJOArrayList){
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
    public void onBindViewHolder(final PendingNotificationAdapter.RecyclerAdapterHolder holder,int position) {
        CommonMeetingListPOJO.MeetingListPOJO item  = requesterPOJOArrayList.get(position);
        holder.txt_persona.setText(item.getRequestorDesignationTags().get(0));
        holder.progress.setMax(10);
        holder.progress.setProgress(7);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(item.getRequestorDomainTags());
        arrayList.addAll(item.getRequestorSubDomainTags());
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.rv_tags.setLayoutManager(gridLayout );
        holder.rv_tags.setAdapter(new ExperienceAdapter(context, arrayList));

        holder.txt_reason.setText(item.getMeetingReason());


//        if (item.getRequestorObjectiveTags().size() == 0) {
//            holder.txt_objective.setVisibility(View.GONE);
//        }else {
//            holder.txt_objective.setVisibility(View.VISIBLE);
//        }

        ArrayList<String> ex_array = new ArrayList<>();
        if (item.getRequestorDesignationTags() != null){
            for (int i = 0; i < item.getRequestorDesignationTags().size(); i++){
                if (i != 0){
                    ex_array.add(item.getRequestorDesignationTags().get(i));
                }
            }
        }
        ex_array.addAll(item.getRequestorExperienceTags());
        FlexboxLayoutManager gridLayout1 = new FlexboxLayoutManager(context);
        holder.rv_experience.setLayoutManager(gridLayout1);
        holder.rv_experience.setAdapter(new ExperienceAdapter(context, ex_array));

//        FlexboxLayoutManager gridLayout2 = new FlexboxLayoutManager(context);
//        holder.rv_objective.setLayoutManager(gridLayout2);
//        holder.rv_objective.setAdapter(new ExperienceAdapter(context, (ArrayList<String>) item.getRequestorObjectiveTags()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof NotificationListActivity){
                    ((NotificationListActivity)view.getContext()).detailsMeeting(item);
                    //((MyCoursesDetailsActivity)view.getContext()).checkPermissionForNotes(child.getNotesPdfPath());
                }

            }
        });

         holder.btn_meeting_decline.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (context instanceof  NotificationListActivity) {
                     ((NotificationListActivity) view.getContext()).getDeclineMeeting(item.getMeetingCode());
                 }
             }
         });

        holder.btn_meeting_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof NotificationListActivity){
                    ((NotificationListActivity)view.getContext()).detailsMeeting(item);
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
