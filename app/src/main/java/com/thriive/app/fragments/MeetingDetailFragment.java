package com.thriive.app.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.R;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.models.MeetingDetailPOJO;
import com.thriive.app.adapters.MeetingDetailSlotAdapter;
import com.thriive.app.models.PendingMeetingRequestPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MeetingDetailFragment extends BottomSheetDialogFragment {

    public static PendingMeetingRequestPOJO.MeetingRequestList item;
    Unbinder unbinder;

    @BindView(R.id.txt_persona)
    TextView txt_persona;
    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;
    @BindView(R.id.txt_reason)
    TextView txt_reason;
    @BindView(R.id.txt_tags)
    TextView txt_tags;
    @BindView(R.id.txt_details)
    TextView txt_details;
    @BindView(R.id.txt_region)
    TextView txt_region;
    @BindView(R.id.rv_slots)
    RecyclerView rv_slots;



    public MeetingDetailFragment() {
        // Required empty public constructor
    }

    public static MeetingDetailFragment newInstance() {
        return new MeetingDetailFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.SheetDialog);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout)
                        dialog.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //      behavior.setPeekHeight(0); // Remove this line to hide a dark background if you manually hide the dialog.
            }
        });
        init();

        return  view;
    }

    public void init(){
        if (item != null){
            txt_persona.setText("with "+item.getGiverPersonaName());
            txt_reason.setText("For "+item.getReasonName());
            //txt_tags.setText(""+item.getMeetingLabel());
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.addAll(item.getDomainTags());
            arrayList.addAll(item.getSubDomainTags());
            arrayList.addAll(item.getExpertiseTags());
            FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getActivity());
            rv_tags.setLayoutManager(gridLayout );
            if (arrayList.size() > 0){
                txt_tags.setVisibility(View.VISIBLE);
            } else {
                txt_tags.setVisibility(View.GONE);
            }

            txt_region.setText(""+item.getSel_meeting().getCountry_name());
            rv_tags.setAdapter(new ExperienceAdapter(getActivity(), arrayList));

            rv_slots.setLayoutManager(new GridLayoutManager(getActivity(), 3));

            if (item.getSlot_list().size() > 0){
                ArrayList<MeetingDetailPOJO> dialog_new_time = new ArrayList<>();
                for (int i = 0; i < 3 ; i++) {
                    MeetingDetailPOJO data = item.getSlot_list().get(i);
                    dialog_new_time.add(data);
                }
                rv_slots.setAdapter(new MeetingDetailSlotAdapter(getActivity(),
                        (ArrayList<MeetingDetailPOJO>) dialog_new_time));
            }

            txt_details.setText(""+item.getSel_meeting().getReq_text());
        }

    }

    @OnClick({R.id.img_close, })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                dismiss();
                break;
        }
    }
}
