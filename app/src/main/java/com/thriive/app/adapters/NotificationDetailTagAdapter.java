package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationDetailTagAdapter extends RecyclerView.Adapter<NotificationDetailTagAdapter.RecyclerAdapterHolder> {
    private Context context;
    private List<String> requesterPOJOArrayList;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_expertise)
        TextView txt_expertise;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this, itemView);
        }
    }
    public NotificationDetailTagAdapter(Context context, List<String> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public NotificationDetailTagAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_experties, parent, false);

        return new NotificationDetailTagAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotificationDetailTagAdapter.RecyclerAdapterHolder holder,int position) {
        String item  = requesterPOJOArrayList.get(position);
        holder.txt_expertise.setText(item);
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}