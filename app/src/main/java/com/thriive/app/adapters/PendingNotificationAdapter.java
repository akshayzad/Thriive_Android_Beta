package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PendingNotificationAdapter extends RecyclerView.Adapter<PendingNotificationAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList;
    private BusinessProfessionAdapter businessProfessionAdapter;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_expertise)
        public RecyclerView rv_expertise;
        @BindView(R.id.rv_experience)
        public RecyclerView rv_experience;
        @BindView(R.id.btn_meeting_decline)
        public ImageButton btn_meeting_decline;
        @BindView(R.id.btn_meeting_accept)
        public ImageButton btn_meeting_accept;

        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public PendingNotificationAdapter(Context context, ArrayList<CommonRequesterPOJO> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public PendingNotificationAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pending_notification, parent, false);

        return new PendingNotificationAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PendingNotificationAdapter.RecyclerAdapterHolder holder,int position) {
        CommonRequesterPOJO item  = requesterPOJOArrayList.get(position);

        ArrayList<CommonRequesterPOJO> arrayList   = new ArrayList<>();
        arrayList.add(new CommonRequesterPOJO("Expert"));
        arrayList.add(new CommonRequesterPOJO("Data Analyst"));
        arrayList.add(new CommonRequesterPOJO("Marketing"));
        arrayList.add(new CommonRequesterPOJO("Business"));
        arrayList.add(new CommonRequesterPOJO("Data Science"));
        holder.rv_experience.setLayoutManager(new FlexboxLayoutManager(context) );
        holder.rv_experience.setAdapter(new ExperienceAdapter(context, arrayList));


        holder.rv_expertise.setLayoutManager(new FlexboxLayoutManager(context) );
        holder.rv_expertise.setAdapter(new ExpertiseAdapter(context, arrayList));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof NotificationListActivity){
                    ((NotificationListActivity)view.getContext()).detailsMeeting();
                    //((MyCoursesDetailsActivity)view.getContext()).checkPermissionForNotes(child.getNotesPdfPath());
                }

            }
        });

         holder.btn_meeting_decline.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (context instanceof  NotificationListActivity) {
                     ((NotificationListActivity) view.getContext()).meetingCancel();
                 }
             }
         });

        holder.btn_meeting_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof NotificationListActivity){
                    ((NotificationListActivity)view.getContext()).detailsMeeting();
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
