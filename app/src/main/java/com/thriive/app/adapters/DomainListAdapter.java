package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.DomainListPOJO;
import com.thriive.app.models.DomainListPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DomainListAdapter extends RecyclerView.Adapter<DomainListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private ArrayList<DomainListPOJO> domainList;
    private Fragment fragment;
    private int currentItem = -1;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        public TextView txt_name;
        @BindView(R.id.layout_select)
        LinearLayout layout_select;
        public RecyclerAdapterHolder(View itemView) {
            super( itemView );
            ButterKnife.bind(this, itemView);
        }
    }
    public DomainListAdapter(Context context, Fragment fragment, ArrayList<DomainListPOJO> domainList){
        this.context = context;
        this.domainList = domainList;
        this.fragment = fragment;
    }
    @Override
    public DomainListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mr_label, parent, false);

        return new DomainListAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DomainListAdapter.RecyclerAdapterHolder holder,int position) {
        DomainListPOJO item  = domainList.get(position);
        holder.txt_name.setText(item.getDomainName());
        if(currentItem == position){
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.outline_circle_tarcoto));

        } else {
            holder.layout_select.setBackground(context.getResources().getDrawable(R.drawable.background_expertise));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItem = position;

                notifyDataSetChanged();
              //  ((MeetingRequestFragment) fragment).getSubDomainList(item.getSubDomainList());

            }
        });
    }

    @Override
    public int getItemCount() {
        return domainList.size();
    }
}