package com.thriive.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchingTagAdapter extends RecyclerView.Adapter<MatchingTagAdapter.RecyclerAdapterHolder> {
private Context context;
private ArrayList<String> requesterPOJOArrayList;
int count = 0;

public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.txt_experience)
    TextView txt_experience;

    @BindView(R.id.txt_see_more)
    TextView txt_see_more;

    public RecyclerAdapterHolder(View itemView) {
        super( itemView );
        ButterKnife.bind(this, itemView);
    }
}
    public MatchingTagAdapter(Context context, ArrayList<String> requesterPOJOArrayList, int cnt){
        this.context = context;
        this.requesterPOJOArrayList = requesterPOJOArrayList;
        this.count = cnt;
    }
    @Override
    public  RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_matching_tag_adp, parent, false);
        return new RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterHolder holder,int position) {
        String item  = requesterPOJOArrayList.get(position);
        holder.txt_experience.setText(item);

        if (count > 2){
            if( position == getItemCount() - 1 ){
                holder.txt_see_more.setVisibility(View.VISIBLE);
                holder.txt_see_more.setText("+ "+  count + " more");
            }else {
                holder.txt_see_more.setVisibility(View.GONE);
            }
        }else {
            holder.txt_see_more.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return requesterPOJOArrayList.size();
    }
}