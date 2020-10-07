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

import com.bumptech.glide.Glide;
import com.thriive.app.R;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.PersonaListPOJO;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonaTagsAdapter extends RecyclerView.Adapter<PersonaTagsAdapter.RecyclerAdapterHolder> {
    private Context context;
    private Fragment fragment;
    private ArrayList<PersonaListPOJO> personaList;
    private int currentItem = -1;

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_experience)
        TextView txt_experience;
        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public PersonaTagsAdapter(Context context, ArrayList<PersonaListPOJO> personaList) {
        this.context = context;
        this.fragment = fragment;
        this.personaList = personaList;
    }

    public PersonaTagsAdapter(Context context, Fragment fragment, ArrayList<PersonaListPOJO> personaList) {
        this.context = context;
        this.fragment = fragment;
        this.personaList = personaList;
    }

    @Override
    public PersonaTagsAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_experience, parent, false);

        return new PersonaTagsAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PersonaTagsAdapter.RecyclerAdapterHolder holder, int position) {
        PersonaListPOJO item = personaList.get(position);
        holder.txt_experience.setText(item.getPersonaName());

    }

    @Override
    public int getItemCount() {
        return personaList.size();
    }
}