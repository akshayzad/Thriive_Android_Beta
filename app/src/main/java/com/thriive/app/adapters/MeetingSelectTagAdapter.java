package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingSelectTagAdapter extends RecyclerView.Adapter<MeetingSelectTagAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<String> arrayList;
    private ArrayList<String> matchList;
    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_expertise)
        TextView txt_expertise;
        @BindView(R.id.layout_select)
        LinearLayout layout_select;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this, itemView);
        }
    }
    public MeetingSelectTagAdapter(Context context, ArrayList<String> arrayList, ArrayList<String> meetingTag){
        this.context = context;
        this.arrayList = arrayList;
        this.matchList = meetingTag;
    }
    @Override
    public MeetingSelectTagAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_experties, parent, false);

        return new MeetingSelectTagAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MeetingSelectTagAdapter.RecyclerAdapterHolder holder,int position) {
        String item  = arrayList.get(position);
        holder.txt_expertise.setText(item);
        int j=0;
        for (int i = 0; i < matchList.size(); i++){
            if (item.equals(matchList.get(i))){
                j++;
            }
            /*if (item.equals(matchList.get(i))){
                holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_darkseagreen));
                holder.txt_expertise.setTextColor(context.getResources().getColor(R.color.darkSeaGreen));
            } else {
                holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_gray));
                holder.txt_expertise.setTextColor(context.getResources().getColor(R.color.darkGrey));
            }*/
        }

        if (j>0){
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_darkseagreen));
            holder.txt_expertise.setTextColor(context.getResources().getColor(R.color.darkSeaGreen));
        } else {
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_gray));
            holder.txt_expertise.setTextColor(context.getResources().getColor(R.color.darkGrey));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}