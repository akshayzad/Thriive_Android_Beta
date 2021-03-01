package com.thriive.app.adapters;

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
import com.thriive.app.models.PersonaListPOJO;
import com.thriive.app.models.PersonaListPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonaListAdapter extends RecyclerView.Adapter<PersonaListAdapter.RecyclerAdapterHolder> {
    private Context context;
    private Fragment fragment;
    private ArrayList<PersonaListPOJO> personaList;
    private int currentItem = -1;
    private SharedData sharedData;
    String hexCode = "";

    public static class RecyclerAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_l1_attrib_name)
        TextView txt_reasonName;
        @BindView(R.id.text_hexCode)
        TextView text_hexCode;


        public RecyclerAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public PersonaListAdapter(Context context, Fragment fragment, ArrayList<PersonaListPOJO> personaList, String hexCode) {
        this.context = context;
        this.fragment = fragment;
        this.personaList = personaList;
        this.hexCode = hexCode;
        sharedData = new SharedData(context);
    }

    @Override
    public PersonaListAdapter.RecyclerAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attrib_l1, parent, false);

        return new PersonaListAdapter.RecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PersonaListAdapter.RecyclerAdapterHolder holder, int position) {
        PersonaListPOJO item = personaList.get(position);
        holder.txt_reasonName.setText(item.getPersonaName());
        if (!hexCode.equals("")){
            holder.text_hexCode.setBackgroundColor(Color.parseColor(hexCode));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
               // ((MeetingRequestFragment) fragment).reason_id = "" + item.getReasonId();
                //((MeetingRequestFragment) fragment).getMetaDomain("" + item.getPersonaId(), ""+item.getPersonaName());
                ((MeetingRequestFragment) fragment).getAttributeL2("" + item.getPersonaId(), ""+item.getPersonaName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return personaList.size();
    }
}