package com.thriive.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.AttributeL1ListPOJO;
import com.thriive.app.models.ReasonListPOJO;
import com.thriive.app.utilities.SharedData;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AttributeL1ListAdapter extends RecyclerView.Adapter<AttributeL1ListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private Fragment fragment;
    private ArrayList<AttributeL1ListPOJO> reasonList;
    private int currentItem = -1;
    private SharedData sharedData;

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


    public AttributeL1ListAdapter(Context context, Fragment fragment, ArrayList<AttributeL1ListPOJO> reasonList) {
        this.context = context;
        this.fragment = fragment;
        this.reasonList = reasonList;
        sharedData = new SharedData(context);
    }

    @Override
    public AttributeL1ListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attrib_l1, parent, false);

        return new AttributeL1ListAdapter.RecyclerAdapterHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(final AttributeL1ListAdapter.RecyclerAdapterHolder holder, int position) {
        AttributeL1ListPOJO item = reasonList.get(position);
        holder.text_l1_attrib_name.setText(item.getL1_attrib_name());
        if (!item.getHexCode().equals("")){
            holder.text_hexCode.setBackgroundColor(Color.parseColor("#"+item.getHexCode()));
        }else {
            item.setHexCode("d4583d");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                ((MeetingRequestFragment) fragment ).getPersona(""+item.getL1_attrib__id(), ""+item.getL1_attrib_name(), "#"+item.getHexCode());
            }
        });

    }

    @Override
    public int getItemCount() {
        return reasonList.size();
    }
}