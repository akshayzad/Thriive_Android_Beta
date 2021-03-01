package com.thriive.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.AttributeL1ListPOJO;
import com.thriive.app.models.AttributeL2ListPOJO;
import com.thriive.app.utilities.SharedData;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AttributeL2ListAdapter extends RecyclerView.Adapter<AttributeL2ListAdapter.RecyclerAdapterHolder> {

    private Context context;
    private Fragment fragment;
    private ArrayList<AttributeL2ListPOJO> reasonList;
    private int currentItem = -1;
    private SharedData sharedData;
    String hexCode = "";

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_hexCode)
        TextView text_hexCode;
        @BindView(R.id.text_l1_attrib_name)
        TextView text_l1_attrib_name;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public AttributeL2ListAdapter(Context context, Fragment fragment, ArrayList<AttributeL2ListPOJO> reasonList, String hexCode) {
        this.context = context;
        this.fragment = fragment;
        this.reasonList = reasonList;
        this.hexCode = hexCode;
        sharedData = new SharedData(context);
    }

    @Override
    public RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attrib_l1, parent, false);

        return new RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterHolder holder, int position) {
        AttributeL2ListPOJO item = reasonList.get(position);
        holder.text_l1_attrib_name.setText(item.getL2_attrib_name());
        if (!hexCode.equals("")){
            holder.text_hexCode.setBackgroundColor(Color.parseColor(hexCode));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
              //  ((MeetingRequestFragment) fragment ).getPersona(""+item.getL2_attrib_id(), ""+item.getL2_attrib_name());
//                ((MeetingRequestFragment) fragment).getMetaDomain("" + item.getL2_attrib_id(), ""+item.getL2_attrib_name());
                ((MeetingRequestFragment) fragment).setDomainOnclickNext();
            }
        });

    }

    @Override
    public int getItemCount() {
        return reasonList.size();
    }
}