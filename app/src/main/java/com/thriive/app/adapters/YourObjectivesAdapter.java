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
import com.thriive.app.models.CommonObjectivesPOJO;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YourObjectivesAdapter extends RecyclerView.Adapter<YourObjectivesAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<CommonObjectivesPOJO.ObjectivesList> objectivesLists;

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

    public YourObjectivesAdapter(Context context, ArrayList<CommonObjectivesPOJO.ObjectivesList> objectivesLists) {
        this.context = context;
        this.objectivesLists = objectivesLists;
    }

    @Override
    public YourObjectivesAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_objectives_list, parent, false);

        return new YourObjectivesAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final YourObjectivesAdapter.RecyclerAdapterHolder holder, int position) {
        CommonObjectivesPOJO.ObjectivesList item = objectivesLists.get(position);
        holder.txt_name.setText(item.getObjectiveName());
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



    public  ArrayList<CommonObjectivesPOJO.ObjectivesList> getObjectivesLists(){
        return  objectivesLists;
    }
    @Override
    public int getItemCount() {
        return objectivesLists.size();
    }
}
