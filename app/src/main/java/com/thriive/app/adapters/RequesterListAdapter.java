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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.HomeActivity;
import com.thriive.app.R;
import com.thriive.app.models.PendingMeetingRequestPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequesterListAdapter extends RecyclerView.Adapter<RequesterListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<PendingMeetingRequestPOJO.MeetingRequestList> requesterPOJOArrayList;


    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_persona)
        TextView txt_persona;
        @BindView(R.id.txt_reason)
        TextView txt_reason;
        @BindView(R.id.rv_tags)
        RecyclerView rv_tags;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public RequesterListAdapter(Context context, ArrayList<PendingMeetingRequestPOJO.MeetingRequestList> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
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
        holder.txt_reason.setText(item.getReasonName());
        holder.txt_persona.setText(item.getGiverPersonaName());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(item.getDomainTags());
        arrayList.addAll(item.getSubDomainTags());
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.rv_tags.setLayoutManager(gridLayout );
        holder.rv_tags.setAdapter(new ExperienceAdapter(context, arrayList));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)view.getContext()).setMeetingFragment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}
