package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingDetailsFragment;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

public class MeetingHistoryAdapter extends RecyclerView.Adapter<MeetingHistoryAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonRequesterPOJO> requesterPOJOArrayList;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
        }
    }
    public MeetingHistoryAdapter(Context context, ArrayList<CommonRequesterPOJO> requesterPOJOArrayList){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
    }
    @Override
    public MeetingHistoryAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_history, parent, false);

        return new MeetingHistoryAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MeetingHistoryAdapter.RecyclerAdapterHolder holder,int position) {
        CommonRequesterPOJO item  = requesterPOJOArrayList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MeetingDetailsFragment addPhotoBottomDialogFragment =
//                        (MeetingDetailsFragment) MeetingDetailsFragment.newInstance();
//                addPhotoBottomDialogFragment.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(),
//                        "add_photo_dialog_fragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}