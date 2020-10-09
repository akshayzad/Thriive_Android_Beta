package com.thriive.app.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.NotificationListActivity;
import com.thriive.app.R;
import com.thriive.app.RequestMeetingGuideActivity;
import com.thriive.app.adapters.DomainAdapter;
import com.thriive.app.adapters.ExpertiseListAdapter;
import com.thriive.app.adapters.PersonaListAdapter;
import com.thriive.app.adapters.ReasonListAdapter;
import com.thriive.app.adapters.RegionAdapter;
import com.thriive.app.adapters.SubDomainListAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonDomainPOJO;
import com.thriive.app.models.CommonMetaPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonPersonaPOJO;
import com.thriive.app.models.CommonReasonPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.CountryListPOJO;
import com.thriive.app.models.DomainListPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.ExpertiseListPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PersonaListPOJO;
import com.thriive.app.models.ReasonListPOJO;
import com.thriive.app.models.SubDomainListPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;


import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class MeetingRequestFragment extends BottomSheetDialogFragment {

    @BindView(R.id.rv_persona)
    RecyclerView rv_persona;
    @BindView(R.id.rv_expertise)
    RecyclerView rv_expertise;
    @BindView(R.id.rv_domain)
    RecyclerView rv_domain;
    @BindView(R.id.rv_subdomain)
    RecyclerView rv_subdomain;
    @BindView(R.id.rv_region)
    RecyclerView rv_region;

    @BindView(R.id.layout_reason)
    LinearLayout layout_reason;
    @BindView(R.id.layout_persona)
    LinearLayout layout_persona;

    @BindView(R.id.layout_meeting_preference)
    LinearLayout layout_meeting_preference;
    @BindView(R.id.layout_expertise)
    LinearLayout layout_expertise;
    @BindView(R.id.layout_domain)
    LinearLayout layout_domain;
    @BindView(R.id.layout_subdomain)
    LinearLayout layout_subdomain;
    @BindView(R.id.layout_region)
    LinearLayout layout_region;
    @BindView(R.id.txt_lpersona)
    TextView txt_lpersona;


    @BindView(R.id.edt_domain)
    EditText edt_domain;

    private APIInterface apiInterface;

    private SharedData sharedData;
    private  KProgressHUD progressHUD;
    public  static  String TAG = MeetingRequestFragment.class.getName();

    private ArrayList<CommonRequesterPOJO> arrayList = new ArrayList<>();
    Unbinder unbinder;
    private LoginPOJO.ReturnEntity loginPOJO;

//    "requestor_id":3,
//            "reason_id":1,
//            "giver_persona_id":3,
//            "sel_domain_id":3,
//            "sel_sub_domain_id":2,
//            "sel_expertise_id":1
    public String reasonId = "",requestorId, giverPersonaId, domainId ="",
        subDomainId ="", expertiseId  = "", reasonName, regionId = "", personaName, sdomainName;

    private String  personaId = "";
    @BindView(R.id.txt_reason)
    TextView txt_reason;
    @BindView(R.id.txt_persona)
    TextView txt_persona;
    @BindView(R.id.layout_lreason)
    LinearLayout layout_lreason;
    @BindView(R.id.layout_lpersona)
    LinearLayout layout_lpersona;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.txt_domains)
    TextView txt_domains;
    @BindView(R.id.label_domain)
    TextView label_domain;
    @BindView(R.id.label_expertise)
    TextView label_expertise;
    @BindView(R.id.layout_ldomain)
    LinearLayout layout_ldomain;
    @BindView(R.id.layout_lexpertise)
    LinearLayout layout_lexpertise;
    @BindView(R.id.rv_reason)
    RecyclerView rv_reason;
    @BindView(R.id.txt_expertise)
    TextView txt_expertise;
    @BindView(R.id.label_region)
    TextView label_region;
    @BindView(R.id.label_noDomain)
    TextView label_noDomain;
    private DomainAdapter domainAdapter;


    public MeetingRequestFragment() {
        // Required empty public constructor
    }

    public static MeetingRequestFragment newInstance() {
        return new MeetingRequestFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_request, container, false);
        unbinder = ButterKnife.bind(this, view);

        sharedData = new SharedData(getActivity());
        apiInterface = APIClient.getApiInterface();
        loginPOJO = Utility.getLoginData(getActivity());

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
        getReason();

        edt_domain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getSearchDomain(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return  view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style. AppBottomSheetDialogTheme);
    }

    @OnClick({R.id.btn_request_meeting, R.id.img_close, R.id.layout_lpersona, R.id.layout_lreason, R.id.layout_ldomain, R.id.layout_lexpertise})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_request_meeting:
                getSubmitRequestMeeting();
                // successDialog();
                break;

            case R.id.img_close:
                dismiss();
                //onCancel();
                break;

            case R.id.layout_lreason:
                init();
                getReason();
                break;

            case R.id.layout_lpersona:
                getPersona(reasonId,reasonName);
                break;

            case R.id.layout_ldomain:

                getMeta(personaId, personaName);

                break;

            case R.id.layout_lexpertise:
                getMeta(personaId, personaName);
                break;

        }
    }

    private void getSearchDomain(String s) {
//        progressHUD = KProgressHUD.create(getActivity())
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setCancellable(false)
//                .show();
        sharedData.addIntData(SharedData.domainId, 0);
        sharedData.addIntData(SharedData.subDomainId, 0);
        Call<CommonDomainPOJO> call = apiInterface.getSearchDomain(loginPOJO.getActiveToken(),s,"10", "0");
        call.enqueue(new Callback<CommonDomainPOJO>() {
            @Override
            public void onResponse(Call<CommonDomainPOJO> call, Response<CommonDomainPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonDomainPOJO reasonPOJO = response.body();
                    //  progressHUD.dismiss();
                    //  Log.d(TAG,""+reasonPOJO.getMrParams().getReasonName());
                    if (reasonPOJO.getOK()) {
                        //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        // Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if (reasonPOJO.getDomainList() != null){
                        if (reasonPOJO.getDomainList().size() == 0 ){
                            label_noDomain.setVisibility(View.VISIBLE);
                        } else {
                            label_noDomain.setVisibility(View.GONE);
                        }

                    } else {
                        label_noDomain.setVisibility(View.VISIBLE);
                    }

//                    txt_persona.setText(""+persona_name);
//                    if (reasonPOJO.getMrParams().getFlagExpertise()){
//                        layout_expertise.setVisibility(View.VISIBLE);
//                    } else {
//                        layout_expertise.setVisibility(View.GONE);
//                    }

                    domainAdapter = new DomainAdapter(getActivity(), MeetingRequestFragment.this,
                            (ArrayList<DomainListPOJO>) reasonPOJO.getDomainList());
                    rv_domain.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rv_domain.setAdapter(domainAdapter);





                    setMeta();

                }
            }
            @Override
            public void onFailure(Call<CommonDomainPOJO> call, Throwable t) {
                //  progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSubmitRequestMeeting() {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getSaveMeetingRequest(loginPOJO.getActiveToken(),
                loginPOJO.getEntityId()
                ,reasonId, personaId, domainId, subDomainId, expertiseId, regionId);
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMessage());
                    if (reasonPOJO.getOK()) {
                        successDialog();
                      //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }



                }
            }
            @Override
            public void onFailure(Call<CommonPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void init(){
        //selected tag
        layout_lreason.setVisibility(View.GONE);
        layout_lpersona.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.GONE);
        layout_lexpertise.setVisibility(View.GONE);


        //labels
        layout_reason.setVisibility(View.VISIBLE);
        layout_persona.setVisibility(View.GONE);
        label_expertise.setVisibility(View.GONE);
        label_domain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
        label_noDomain.setVisibility(View.GONE);
        layout_domain.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);

        //rv
        rv_reason.setVisibility(View.VISIBLE);
        rv_persona.setVisibility(View.GONE);
        rv_expertise.setVisibility(View.GONE);
        rv_domain.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);

        layout_subdomain.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
    }

    private void getReason() {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonReasonPOJO> call = apiInterface.getReason(loginPOJO.getActiveToken(),""+loginPOJO.getEntityId(),
                loginPOJO.getEntityName(), +loginPOJO.getReqPersonaId(),
                loginPOJO.getReqPersonaName());
        call.enqueue(new Callback<CommonReasonPOJO>() {
            @Override
            public void onResponse(Call<CommonReasonPOJO> call, Response<CommonReasonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonReasonPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMrParams().getReasonName());
                    if (reasonPOJO.getOK()) {
                      //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                       // Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    rv_reason.setVisibility(View.VISIBLE);
                    rv_reason.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    rv_reason.setAdapter(new ReasonListAdapter(getActivity(), MeetingRequestFragment.this,
                            (ArrayList<ReasonListPOJO>) reasonPOJO.getMrParams().getReasonList()));

                    FlexboxLayoutManager manager = new FlexboxLayoutManager(getContext());
                    manager.setFlexWrap(FlexWrap.WRAP);
                    manager.setJustifyContent(JustifyContent.FLEX_START);
                    rv_region.setLayoutManager(manager );

                //    rv_region.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                    rv_region.setAdapter(new RegionAdapter(getActivity(), MeetingRequestFragment.this,
                            (ArrayList<CountryListPOJO>) reasonPOJO.getMrParams().getCountryList()));


                }
            }
            @Override
            public void onFailure(Call<CommonReasonPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getPersona(String reason_id, String reason_name) {
        txt_reason.setText(""+reason_name);
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        reasonId = reason_id;
        reasonName = reason_name;
        Call<CommonPersonaPOJO> call = apiInterface.getPersona(loginPOJO.getActiveToken(),""+loginPOJO.getEntityId(),
                loginPOJO.getEntityName(), ""+loginPOJO.getReqPersonaId(),
                loginPOJO.getReqPersonaName(), reason_id);
        call.enqueue(new Callback<CommonPersonaPOJO>() {
            @Override
            public void onResponse(Call<CommonPersonaPOJO> call, Response<CommonPersonaPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPersonaPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMrParams().getReasonName());
                    if (reasonPOJO.getOK()) {
                      //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                     //   Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    rv_persona.setVisibility(View.VISIBLE);
                    rv_persona.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    rv_persona.setAdapter(new PersonaListAdapter(getActivity(), MeetingRequestFragment.this,
                            (ArrayList<PersonaListPOJO>) reasonPOJO.getMrParams().getPersonaList()));

                    setPersona();

                }
            }
            @Override
            public void onFailure(Call<CommonPersonaPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setPersona() {
        txt_lpersona.setText(getResources().getString(R.string.label_meeting_request) + " for "+ reasonName + "?");

        //selected tag
        layout_lreason.setVisibility(View.VISIBLE);
        layout_lpersona.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.GONE);
        layout_lexpertise.setVisibility(View.GONE);
        label_noDomain.setVisibility(View.GONE);
        //labels
        layout_reason.setVisibility(View.GONE);
        layout_persona.setVisibility(View.VISIBLE);
        label_expertise.setVisibility(View.GONE);
        label_domain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);


        layout_domain.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        //rv
        rv_reason.setVisibility(View.GONE);
        rv_persona.setVisibility(View.VISIBLE);
        rv_expertise.setVisibility(View.GONE);
        rv_domain.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);


        layout_meeting_preference.setVisibility(View.GONE);

    }


    public void getMeta(String persona_id, String persona_name) {
        // ddapter.re();omainA
        txt_persona.setText(""+persona_name);
        edt_domain.setText("");
        sharedData.addIntData(SharedData.domainId, 0);
        sharedData.addIntData(SharedData.subDomainId, 0);
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        personaId = persona_id;
        personaName = persona_name;
        if (domainAdapter != null){
            domainAdapter.clearData();
        }

        Call<CommonMetaPOJO> call = apiInterface.getMeta(loginPOJO.getActiveToken(), ""+loginPOJO.getEntityId(),
                loginPOJO.getEntityName(), ""+loginPOJO.getReqPersonaId(),
                loginPOJO.getReqPersonaName(), reasonId, persona_id);
        call.enqueue(new Callback<CommonMetaPOJO>() {
            @Override
            public void onResponse(Call<CommonMetaPOJO> call, Response<CommonMetaPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonMetaPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMrParams().getReasonName());
                    if (reasonPOJO.getOK()) {
                      //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                      //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    if (reasonPOJO.getMrParams().getFlagDomain()){
                        label_domain.setText(getResources().getText(R.string.tag_domain) + " "+ persona_name + "?");
                        layout_domain.setVisibility(View.VISIBLE);
                        rv_domain.setVisibility(View.VISIBLE);
                        label_domain.setVisibility(View.VISIBLE);
                        domainAdapter = new DomainAdapter(getActivity(), MeetingRequestFragment.this,
                                (ArrayList<DomainListPOJO>) reasonPOJO.getMrParams().getDomainList());
                        rv_domain.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_domain.setAdapter(domainAdapter);
                    } else {
                        label_domain.setVisibility(View.GONE);
                        layout_domain.setVisibility(View.GONE);
                    }

                    if (domainAdapter != null) {
                        domainAdapter.notifyDataSetChanged();
                    }

                    if (reasonPOJO.getMrParams().getFlagExpertise()){
                        label_expertise.setText(getResources().getText(R.string.tag_expertise) + " "+ persona_name + "?");
                        layout_expertise.setVisibility(View.VISIBLE);
                        rv_expertise.setVisibility(View.VISIBLE);
                        label_expertise.setVisibility(View.VISIBLE);
                        layout_region.setVisibility(View.VISIBLE);
                        rv_region.setVisibility(View.VISIBLE);
                        FlexboxLayoutManager manager = new FlexboxLayoutManager(getContext());
                        manager.setFlexWrap(FlexWrap.WRAP);
                        manager.setJustifyContent(JustifyContent.FLEX_START);
                        rv_expertise.setLayoutManager(manager );
                        rv_expertise.setAdapter(new ExpertiseListAdapter(getActivity(),
                                 MeetingRequestFragment.this, (ArrayList<ExpertiseListPOJO>) reasonPOJO.getMrParams().getExpertiseList()));

                    } else {
                        layout_expertise.setVisibility(View.GONE);
                        label_expertise.setVisibility(View.GONE);
                    }

                    if (!reasonPOJO.getMrParams().getFlagDomain() && !reasonPOJO.getMrParams().getFlagExpertise())
                    {
                        label_domain.setText(getResources().getText(R.string.tag_domain) + " " + persona_name + "?");
                        label_domain.setVisibility(View.VISIBLE);
                        layout_domain.setVisibility(View.VISIBLE);
                        rv_domain.setVisibility(View.VISIBLE);

                    }
                    label_noDomain.setVisibility(View.GONE);
                    setMeta();

                }
            }
            @Override
            public void onFailure(Call<CommonMetaPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMeta() {
        layout_lreason.setVisibility(View.VISIBLE);
        layout_lpersona.setVisibility(View.VISIBLE);
        layout_ldomain.setVisibility(View.GONE);
        layout_lexpertise.setVisibility(View.GONE);
       // label_noDomain.setVisibility(View.GONE);
        //labels
        layout_reason.setVisibility(View.GONE);
        layout_persona.setVisibility(View.GONE);
//        label_expertise.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);

        rv_persona.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);

        layout_subdomain.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);

    }



    public void setMeeting(Integer d_id, Integer s_id, String subDomainName) {
        Utility.hideKeyboard(getActivity());
       // scrollView.scrollTo(0,scrollView.getBottom());
        subDomainId = ""+s_id;
        domainId = ""+d_id;
        sdomainName = subDomainName;
        txt_domains.setText(subDomainName);
        label_domain.setVisibility(View.GONE);
        label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName);
        layout_domain.setVisibility(View.GONE);
        rv_persona.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.VISIBLE);
        layout_region.setVisibility(View.VISIBLE);
        rv_region.setVisibility(View.VISIBLE);
        label_region.setVisibility(View.VISIBLE);
        label_noDomain.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.VISIBLE);
    }

    public void getRegion_Id(Integer countryId) {
        regionId = "" + countryId;
    }

    public void setExpertise(Integer expertise_id, String expertiseName) {
        label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName);
        txt_expertise.setText(""+expertiseName);
        label_expertise.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_region.setVisibility(View.VISIBLE);
        rv_region.setVisibility(View.VISIBLE);
        layout_meeting_preference.setVisibility(View.VISIBLE);
        layout_lexpertise.setVisibility(View.VISIBLE);
        label_region.setVisibility(View.VISIBLE);
        expertiseId = ""+expertise_id;
    }


    public void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_request_meeting_success, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        TextView label_title = view1.findViewById(R.id.label_title);
//        Hey Samir, Thriive is now at work to find you the right business guru.
//
//                Expect a notification for your match within 48 hrs."
        label_title.setText("Hey "+ loginPOJO.getFirstName() + ", " + getResources().getString(R.string.label_success_meeting));
        //    tv_msg.setText("Session Added Successfully.");
        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);
        label_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedData.addBooleanData(SharedData.MEETING_BOOKED, true);
                EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_BOOK));
                dialogs.dismiss();
                dismiss();
            }
        });
        dialogs.show();
    }

}