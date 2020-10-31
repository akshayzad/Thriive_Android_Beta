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
import com.thriive.app.models.DomainListPOJO;
import com.thriive.app.models.SubDomainListPOJO;
import com.thriive.app.utilities.SharedData;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubDomainListAdapter extends RecyclerView.Adapter<SubDomainListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<SubDomainListPOJO> domainList;
    private Fragment fragment;
    private  int currentItem = -1;
    private ArrayList<DomainListPOJO> list;
    private SharedData sharedData;
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

    public SubDomainListAdapter(Context context, Fragment fragment, ArrayList<SubDomainListPOJO> domainList, ArrayList<DomainListPOJO> list) {
        this.context = context;
        this.domainList = domainList;
        this.fragment = fragment;
        this.list = list;
        sharedData = new SharedData(context);
    }

    @Override
    public SubDomainListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mr_label, parent, false);

        return new SubDomainListAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SubDomainListAdapter.RecyclerAdapterHolder holder, int position) {
        SubDomainListPOJO item = domainList.get(position);
        holder.txt_name.setText(item.getSubDomainName());
        if (sharedData.getIntData(SharedData.domainId) == item.getDomainId()
                && sharedData.getIntData(SharedData.subDomainId) == item.getSubDomainId()){
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
        } else {
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
        }
//        if(currentItem == position){
//            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
//        } else {
//            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
//        }

//        if (item.isSelect()){
//
//            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));
//        } else {
//            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItem = position;
                sharedData.addIntData(SharedData.subDomainId, item.getSubDomainId());
                sharedData.addIntData(SharedData.domainId, item.getDomainId());

               // notifyDataSetChanged();
                ((MeetingRequestFragment) fragment).setSubDomain(item.getDomainId(), item.getSubDomainId(), item.getSubDomainName());
                //   ((MeetingRequestFragment) fragment).refreshList();

            }
        });
    }

    @Override
    public int getItemCount() {
        return domainList.size();
    }
}
