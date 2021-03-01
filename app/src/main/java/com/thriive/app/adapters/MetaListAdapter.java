package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.MetaListPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MetaListAdapter extends RecyclerView.Adapter<MetaListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<MetaListPOJO.TagList> tagLists;
    private Fragment fragment;
    private int currentItem = -1;
    private boolean isFromSummary;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        public TextView txt_name;
        @BindView(R.id.layout_select)
        LinearLayout layout_select;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public MetaListAdapter(Context context, Fragment fragment, ArrayList<MetaListPOJO.TagList> tagLists, boolean isFromSummary) {
        this.context = context;
        this.tagLists = tagLists;
        this.fragment = fragment;
        this.isFromSummary = isFromSummary;
    }

    @Override
    public MetaListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mr_label, parent, false);

        return new MetaListAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MetaListAdapter.RecyclerAdapterHolder holder, int position) {
        MetaListPOJO.TagList item = tagLists.get(position);
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
                ((MeetingRequestFragment) fragment).getTagList(item.getChildren(), item.getDomainId(),
                        item.getSubDomainId(), item.getTagName());

                notifyDataSetChanged();

            }
        });
    }

    public void clearData() {
        tagLists.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return tagLists.size();
    }
}
