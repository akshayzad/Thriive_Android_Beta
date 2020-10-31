package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingTagAdapter extends RecyclerView.Adapter<MeetingTagAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<String> requesterPOJOArrayList;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_expertise)
        TextView txt_expertise;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this, itemView);
        }
    }
    public MeetingTagAdapter(Context context, ArrayList<String> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public MeetingTagAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting_tag, parent, false);

        return new MeetingTagAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MeetingTagAdapter.RecyclerAdapterHolder holder,int position) {
        String item  = requesterPOJOArrayList.get(position);
        holder.txt_expertise.setText(item);
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }

}
