package com.thriive.app.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thriive.app.R;
import com.thriive.app.models.HomeDisplayPOJO;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturedThriiversAdapter extends RecyclerView.Adapter<FeaturedThriiversAdapter.RecyclerAdapterHolder> {

    private Context context;
    private List<HomeDisplayPOJO.EntityList> thriiversPOJOArrayList;

    public FeaturedThriiversAdapter(Context context, List<HomeDisplayPOJO.EntityList> thriiversPOJOArrayList, Fragment fragment) {
        this.context = context;
        this.thriiversPOJOArrayList = thriiversPOJOArrayList;
    }

    @NonNull
    @Override
    public RecyclerAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured_thriivers, parent, false);

        return new RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterHolder holder, int position) {

        holder.txtName.setText(thriiversPOJOArrayList.get(position).getEntityName());
        holder.txtDesignationName.setText(thriiversPOJOArrayList.get(position).getDesignationName());
        if (thriiversPOJOArrayList.get(position).getPicUrl().equals("")){
            Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_medium);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(context.getResources().getColor( R.color.terracota))
                    .useFont(typeface)
                    .fontSize(40) /* size in px */
                    .bold()
                    .toUpperCase()
                    .width(120)  // width in px
                    .height(120) // height in px
                    .endConfig()
                    .buildRect(Utility.getInitialsName(thriiversPOJOArrayList.get(position).getEntityName()) ,
                            context.getResources().getColor( R.color.pale48));

            holder.imgUser.setImageDrawable(drawable);
        } else {
            Glide.with(context)
                    .load(thriiversPOJOArrayList.get(position).getPicUrl())
                    .placeholder(R.drawable.profile_pic_card)
                    .into(holder.imgUser);
        }

    }

    @Override
    public int getItemCount() {
        return thriiversPOJOArrayList.size();
    }

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_user)
        ImageView imgUser;
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_designation_name)
        TextView txtDesignationName;

        public RecyclerAdapterHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
