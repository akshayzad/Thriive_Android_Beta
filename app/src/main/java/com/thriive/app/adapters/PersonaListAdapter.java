package com.thriive.app.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.PersonaListPOJO;
import com.thriive.app.models.PersonaListPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonaListAdapter extends RecyclerView.Adapter<PersonaListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private Fragment fragment;
    private ArrayList<PersonaListPOJO> personaList;
    private int currentItem = -1;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_reasonName)
        TextView txt_reasonName;
        @BindView(R.id.image)
        ImageView image;

        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public PersonaListAdapter(Context context, ArrayList<PersonaListPOJO> personaList) {
        this.context = context;
        this.fragment = fragment;
        this.personaList = personaList;
    }

    public PersonaListAdapter(Context context, Fragment fragment, ArrayList<PersonaListPOJO> personaList) {
        this.context = context;
        this.fragment = fragment;
        this.personaList = personaList;
    }

    @Override
    public PersonaListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reason_list, parent, false);

        return new PersonaListAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PersonaListAdapter.RecyclerAdapterHolder holder, int position) {
        PersonaListPOJO item = personaList.get(position);
        holder.txt_reasonName.setText(item.getPersonaName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
               // ((MeetingRequestFragment) fragment).reason_id = "" + item.getReasonId();
                ((MeetingRequestFragment) fragment).getMeta("" + item.getPersonaId(), ""+item.getPersonaName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return personaList.size();
    }
}