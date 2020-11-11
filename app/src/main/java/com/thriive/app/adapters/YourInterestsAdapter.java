package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.models.CommonInterestsPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YourInterestsAdapter extends RecyclerView.Adapter<YourInterestsAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonInterestsPOJO.InterestsList> interestsLists;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_objective)
        public TextView txt_name;
        @BindView(R.id.layout_select)
        LinearLayout layout_select;
        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public YourInterestsAdapter(Context context, ArrayList<CommonInterestsPOJO.InterestsList> interestsLists) {
        this.context = context;
        this.interestsLists = interestsLists;
    }

    @Override
    public YourInterestsAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_objectives_list, parent, false);

        return new YourInterestsAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final YourInterestsAdapter.RecyclerAdapterHolder holder, int position) {
        CommonInterestsPOJO.InterestsList item = interestsLists.get(position);
        holder.txt_name.setText(item.getDomainName());
        if(item.getSelected()){
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.bg_darkseacolor_fifty));
        } else {
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_select_experience));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getSelected()){
                    item.setSelected(false);
                } else {
                    item.setSelected(true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return interestsLists.size();
    }
}
