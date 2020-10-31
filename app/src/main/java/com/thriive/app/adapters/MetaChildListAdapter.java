package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.MetaListPOJO;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MetaChildListAdapter extends RecyclerView.Adapter<MetaChildListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<MetaListPOJO.Child> childArrayList;
    Fragment fragment;
    int currentItem = -1;
    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        public TextView txt_name;
        @BindView(R.id.layout_select)
        LinearLayout layout_select;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this,itemView);
        }
    }
    public MetaChildListAdapter(Context context, Fragment fragment, ArrayList<MetaListPOJO.Child> childArrayList){
        this.context = context;
        this.fragment = fragment;
        this.childArrayList = childArrayList;
    }
    @Override
    public MetaChildListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mr_label, parent, false);

        return new MetaChildListAdapter.RecyclerAdapterHolder(itemView);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MetaChildListAdapter.RecyclerAdapterHolder holder, int position) {
        MetaListPOJO.Child item  = childArrayList.get(position);
        holder.txt_name.setText(item.getTagName());
        if (currentItem == position) {
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
        } else {
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItem = position;
                ((MeetingRequestFragment) fragment).setMeeting(item.getDomainId(), item.getSubDomainId(), item.getTagName());
                notifyDataSetChanged();

            }
        });
    }
    public void clearData() {
        childArrayList.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return childArrayList.size();
    }
}

