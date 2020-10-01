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
        @BindView(R.id.layout_tags)
        FlexboxLayout layout_tags;
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
        holder.txt_reason.setText(item.getReasonName());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(item.getDomainTags());
        arrayList.addAll(item.getSubDomainTags());
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(context);
        holder.rv_tags.setLayoutManager(gridLayout );
        holder.rv_tags.setAdapter(new ExperienceAdapter(context, arrayList));


//        if (!item.getDomainName().equals("")){
//            TextView valueTV = new TextView(context);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(5, 5, 5, 5);
//            valueTV.setLayoutParams(params);
//            Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_regular);
//            valueTV.setTypeface(typeface);
//            valueTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            valueTV.setTextColor(context.getColor(R.color.slateGrey));
//            valueTV.setBackground(context.getDrawable(R.drawable.outline_circle_gray));
//            valueTV.setText(item.getDomainName() + "");
//            valueTV.setTextSize(11);
//            holder.layout_tags.addView(valueTV);
//
//        } else {
//            TextView valueTV = new TextView(context);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(5, 5, 5, 5);
//            valueTV.setLayoutParams(params);
//            holder.layout_tags.addView(valueTV);
//        }
//
//        if (!item.getSubDomainName().equals("")){
//            TextView valueTV = new TextView(context);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(5, 5, 5, 5);
//            valueTV.setLayoutParams(params);
//            Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_regular);
//            valueTV.setTypeface(typeface);
//            valueTV.setTextSize(11);
//            valueTV.setTextColor(context.getColor(R.color.slateGrey));
//            valueTV.setText(item.getSubDomainName() );
//            valueTV.setGravity(View.TEXT_ALIGNMENT_CENTER);
//            valueTV.setBackground(context.getDrawable(R.drawable.outline_circle_gray));
//            holder.layout_tags.addView(valueTV);
//        } else {
//            TextView valueTV = new TextView(context);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(5, 10, 5, 10);
//            valueTV.setLayoutParams(params);
//            holder.layout_tags.addView(valueTV);
//        }
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}