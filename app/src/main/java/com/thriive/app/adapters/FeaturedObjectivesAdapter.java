package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thriive.app.R;
import com.thriive.app.fragments.NewHomeFragment;
import com.thriive.app.models.ExpertiseListPOJO;
import com.thriive.app.models.FeaturedObjectivesPOJO;
import com.thriive.app.models.HomeDisplayPOJO;
import com.thriive.app.utilities.BoundedLinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturedObjectivesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    Fragment fragment;
    private List<HomeDisplayPOJO.FeaturedRequest> objectsList;
    private static int VIEW_TYPE_1 = 0;
    private static int VIEW_TYPE_2 = 1;

    public FeaturedObjectivesAdapter(Context context, List<HomeDisplayPOJO.FeaturedRequest> objectsList, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.objectsList = objectsList;
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_featured_objectives)
        LinearLayout layout_featured_objectives;
        @BindView(R.id.layout_description)
        BoundedLinearLayout layout_description;
        @BindView(R.id.img_featured_obj)
        ImageView img_fea_obj;
        @BindView(R.id.card_title)
        TextView card_title;
        @BindView(R.id.card_desc)
        TextView card_desc;
        public ViewHolder1(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_featured_objectives)
        LinearLayout layout_featured_objectives;
        @BindView(R.id.layout_description)
        BoundedLinearLayout layout_description;
        @BindView(R.id.img_featured_obj)
        ImageView img_fea_obj;
        @BindView(R.id.card_title)
        TextView card_title;
        @BindView(R.id.card_desc)
        TextView card_desc;
        public ViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_1) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_featured_objective_top, parent, false);

            return new ViewHolder1(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_featured_objective_down, parent, false);

            return new ViewHolder2(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == VIEW_TYPE_1){
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.card_title.setText(objectsList.get(position).getCardTitle());
            viewHolder1.card_desc.setText(objectsList.get(position).getCardText());
            if (position == 0) {
                viewHolder1.layout_featured_objectives.setBackgroundColor(context.getResources().getColor(R.color.featured_obj_bg_pink));
                viewHolder1.layout_description.setBackgroundColor(context.getResources().getColor(R.color.featured_obj_txt_bg_terracota));
                viewHolder1.img_fea_obj.setImageDrawable(context.getResources().getDrawable(R.drawable.img_featured_obj_1));
            } else if (position == 2){
                viewHolder1.layout_featured_objectives.setBackgroundColor(context.getResources().getColor(R.color.featured_obj_bg_gray));
                viewHolder1.layout_description.setBackgroundColor(context.getResources().getColor(R.color.featured_obj_txt_bg_black));
                viewHolder1.img_fea_obj.setImageDrawable(context.getResources().getDrawable(R.drawable.img_featured_obj_3));
            }
        } else if (holder.getItemViewType() == VIEW_TYPE_2){
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            viewHolder2.card_title.setText(objectsList.get(position).getCardTitle());
            viewHolder2.card_desc.setText(objectsList.get(position).getCardText());
            viewHolder2.card_title.setTextColor(context.getResources().getColor(R.color.colorBlack));
            viewHolder2.card_desc.setTextColor(context.getResources().getColor(R.color.colorBlack));
            viewHolder2.img_fea_obj.setImageDrawable(context.getResources().getDrawable(R.drawable.img_featured_obj_2));
            viewHolder2.layout_featured_objectives.setBackgroundColor(context.getResources().getColor(R.color.featured_obj_bg_cascade));
            viewHolder2.layout_description.setBackgroundColor(context.getResources().getColor(R.color.featured_obj_txt_bg_yellow));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewHomeFragment) fragment).setOnFeaturedObjectivesClicked(objectsList.get(position));
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPE_1;
        else if (position == 1)
            return VIEW_TYPE_2;
        else
            return VIEW_TYPE_1;
    }

    @Override
    public int getItemCount() {
        return objectsList.size();
    }

}
