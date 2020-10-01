package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.DomainListPOJO;
import com.thriive.app.models.SubDomainListPOJO;
import com.thriive.app.utilities.RecyclerTouchListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DomainAdapter extends RecyclerView.Adapter<DomainAdapter.RecyclerAdapterHolder>{
private Context context;
private ArrayList<DomainListPOJO> domainList;
private Fragment fragment;
private int currentItem=-1;
private SubDomainListAdapter adapter;

    public void clearData() {
        domainList.clear();
        notifyDataSetChanged();
    }

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.txt_name)
    public TextView txt_name;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;



    public RecyclerAdapterHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}

    public DomainAdapter(Context context, Fragment fragment, ArrayList<DomainListPOJO> domainList) {
        this.context = context;
        this.domainList = domainList;
        this.fragment = fragment;
    }

    @Override
    public DomainAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_domain_list, parent, false);

        return new DomainAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DomainAdapter.RecyclerAdapterHolder holder, int position) {
        DomainListPOJO item = domainList.get(position);
        holder.txt_name.setText(item.getDomainName());
        if (domainList.size() > 3){
            holder.rv_list.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));
        } else {
            holder.rv_list.setLayoutManager(new GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false));
        }
        adapter = new SubDomainListAdapter(context, fragment, (ArrayList<SubDomainListPOJO>) item.getSubDomainList(), domainList);
        holder.rv_list.setAdapter(adapter);


    }

    @Override
    public int getItemCount() {
        return domainList.size();
    }

}