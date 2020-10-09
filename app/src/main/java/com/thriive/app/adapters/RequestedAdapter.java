package com.thriive.app.adapters;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.thriive.app.R;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestedAdapter extends RecyclerView.Adapter<RequestedAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<PendingMeetingRequestPOJO.MeetingRequestList> requesterPOJOArrayList;
    private BusinessProfessionAdapter businessProfessionAdapter;

    private  Fragment fragment;
    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_reason)
        TextView txt_reason;
        @BindView(R.id.txt_persona)
        TextView txt_persona;
        @BindView(R.id.rv_tags)
        RecyclerView rv_tags;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public RequestedAdapter(Context context, Fragment fragment, ArrayList<PendingMeetingRequestPOJO.MeetingRequestList> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
        this.fragment =fragment;
    }
    @Override
    public RequestedAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_requested, parent, false);

        return new RequestedAdapter.RecyclerAdapterHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final RequestedAdapter.RecyclerAdapterHolder holder,int position) {
        PendingMeetingRequestPOJO.MeetingRequestList item  = requesterPOJOArrayList.get(position);
        holder.txt_persona.setText(item.getGiverPersonaName());
        holder.txt_reason.setText("Meeting for "+item.getReasonName());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(item.getDomainTags());
        arrayList.addAll(item.getSubDomainTags());
        arrayList.addAll(item.getExpertiseTags());
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.rv_tags.setLayoutManager(gridLayout );
        holder.rv_tags.setAdapter(new ExperienceAdapter(context, arrayList));
    }

    public String getDate(int i ){
        return requesterPOJOArrayList.get(i).getRequestDate();
    }


    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}