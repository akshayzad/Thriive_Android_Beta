package com.thriive.app.fragments;

import android.os.Build;
import android.os.Bundle;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.thriive.app.R;
import com.thriive.app.adapters.DomainListAdapter;
import com.thriive.app.adapters.ExpertiseListAdapter;
import com.thriive.app.adapters.MetaChildListAdapter;
import com.thriive.app.adapters.MetaListAdapter;
import com.thriive.app.adapters.PersonaListAdapter;
import com.thriive.app.adapters.ReasonListAdapter;
import com.thriive.app.adapters.RegionAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonDomainPOJO;
import com.thriive.app.models.CommonMetaPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonPersonaPOJO;
import com.thriive.app.models.CommonReasonPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.CountryListPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.ExpertiseListPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.MetaListPOJO;
import com.thriive.app.models.PersonaListPOJO;
import com.thriive.app.models.ReasonListPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;


import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
    @BindView(R.id.txt_meetingCount)
    TextView txt_meetingCount;
    @BindView(R.id.txt_message)
    TextView txt_message;
    private DomainListAdapter domainAdapter;


    @BindView(R.id.layout_meta)
    LinearLayout layout_meta;
    @BindView(R.id.rv_meta)
    RecyclerView rv_meta;
    @BindView(R.id.edt_meta)
    EditText edt_meta;
    @BindView(R.id.label_noMeta)
    TextView label_noMeta;
    @BindView(R.id.rv_metaChild)
    RecyclerView rv_metaChild;

    @BindView(R.id.layout_lsdomain)
    LinearLayout layout_lsdomain;
    @BindView(R.id.txt_sdomains)
    TextView txt_sdomains;

    @BindView(R.id.btn_meta)
    TextView btn_meta;
    @BindView(R.id.cv_meta)
    CardView cv_meta;

    MetaListAdapter metaListAdapter;
    MetaChildListAdapter metaChildListAdapter;


    ArrayList<MetaListPOJO.Child> tagLists = new ArrayList<>();
    public MeetingRequestFragment() {
        // Required empty public constructor
    }

    public static MeetingRequestFragment newInstance() {
        return new MeetingRequestFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.SheetDialog);
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

        edt_meta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 2){
                    getSearchMetaList(charSequence.toString());
                }

                if (charSequence.toString().length() < 2){
                    setClearDataDomain();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return  view;
    }


    public void init(){
        txt_message.setText("You can request only "+ sharedData.getIntData(SharedData.MEETING_TOTAL) + " meetings a week.");
        int count_m = sharedData.getIntData(SharedData.MEETING_DONE) + 1;
        txt_meetingCount.setText(count_m+ "/" + sharedData.getIntData(SharedData.MEETING_TOTAL));
        //selected tag
        layout_lreason.setVisibility(View.GONE);
        layout_lpersona.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.GONE);
        layout_lsdomain.setVisibility(View.GONE);
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
        layout_meta.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);

        //rv
        rv_reason.setVisibility(View.VISIBLE);
        rv_persona.setVisibility(View.GONE);
        rv_expertise.setVisibility(View.GONE);
        rv_domain.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        rv_meta.setVisibility(View.GONE);
        rv_metaChild.setVisibility(View.GONE);

        layout_subdomain.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
        btn_meta.setVisibility(View.GONE);
    }



    private void setClearDataDomain() {
        label_domain.setText(getResources().getText(R.string.tag_domain) + " "+ personaName + "?");
       // layout_domain.setVisibility(View.GONE);
       // rv_domain.setVisibility(View.GONE);
        label_domain.setVisibility(View.VISIBLE);
        label_region.setVisibility(View.GONE);
       // layout_meta.setVisibility(View.VISIBLE);
      //  rv_meta.setVisibility(View.VISIBLE);
       // label_noMeta.setVisibility(View.GONE);
       // layout_lexpertise.setVisibility(View.GONE);


        if (metaListAdapter != null){
            metaListAdapter.clearData();
            metaListAdapter.notifyDataSetChanged();
        }
        if (metaChildListAdapter != null){
            metaChildListAdapter.clearData();
            metaChildListAdapter.notifyDataSetChanged();
        }
        cv_meta.setVisibility(View.GONE);
        btn_meta.setVisibility(View.GONE);
        label_noMeta.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.GONE);
        layout_lsdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
    }


    private void getReason() {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonReasonPOJO> call = apiInterface.getReason(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/get-reason",loginPOJO.getActiveToken(),""+loginPOJO.getEntityId(),
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

                    ArrayList<CountryListPOJO> region_array = removeDuplicates(
                            (ArrayList<CountryListPOJO>) reasonPOJO.getMrParams().getCountryList());
                    rv_region.setLayoutManager(manager );

                    //    rv_region.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                    rv_region.setAdapter(new RegionAdapter(getActivity(), MeetingRequestFragment.this,
                            (ArrayList<CountryListPOJO>) region_array));


                }
            }
            @Override
            public void onFailure(Call<CommonReasonPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<CountryListPOJO> removeDuplicates(ArrayList<CountryListPOJO> list) {
        ArrayList<String> countryListPOJOS = new ArrayList<>();
        ArrayList<CountryListPOJO> returnList = new ArrayList<>();

        for (int i = 0; i<list.size(); i++){
            CountryListPOJO pojo = list.get(i);
            if (!countryListPOJOS.contains(pojo.getCountryName().replace(" ", "").trim())){
                countryListPOJOS.add(pojo.getCountryName());
                returnList.add(pojo);
            }

        }

        //Set<String> noDuplication = new HashSet<String>(countryListPOJOS);

        return returnList;
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
        Call<CommonPersonaPOJO> call = apiInterface.getPersona(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/get-persona",loginPOJO.getActiveToken(),""+loginPOJO.getEntityId(),
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
        layout_lsdomain.setVisibility(View.GONE);
        //labels
        layout_reason.setVisibility(View.GONE);
        layout_persona.setVisibility(View.VISIBLE);
        label_expertise.setVisibility(View.GONE);
        label_domain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
        layout_meta.setVisibility(View.GONE);



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
        rv_meta.setVisibility(View.GONE);
        rv_metaChild.setVisibility(View.GONE);
        cv_meta.setVisibility(View.GONE);

        btn_meta.setVisibility(View.GONE);


        layout_meeting_preference.setVisibility(View.GONE);

    }

    private void getSearchDomain(String s) {
        try {
            sharedData.addIntData(SharedData.domainId, 0);
            sharedData.addIntData(SharedData.subDomainId, 0);
            Call<CommonDomainPOJO> call = apiInterface.getSearchDomain(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/search-domain",
                    loginPOJO.getActiveToken(),s,"10", "0");
            call.enqueue(new Callback<CommonDomainPOJO>() {
                @Override
                public void onResponse(Call<CommonDomainPOJO> call, Response<CommonDomainPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonDomainPOJO reasonPOJO = response.body();
                        try {
                            if (reasonPOJO.getDomainList() != null){
                                if (reasonPOJO.getDomainList().size() == 0 ){
                                    label_noDomain.setVisibility(View.VISIBLE);
                                } else {
                                    label_noDomain.setVisibility(View.GONE);
                                }
                               // domainAdapter = new DomainAdapter(getActivity(), MeetingRequestFragment.this,
                                   //     (ArrayList<DomainListPOJO>) reasonPOJO.getDomainList());
                                rv_domain.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rv_domain.setAdapter(domainAdapter);

                            } else {
                                label_noDomain.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }
                        layout_region.setVisibility(View.GONE);
                        rv_region.setVisibility(View.GONE);
                        label_region.setVisibility(View.GONE);
                        layout_meeting_preference.setVisibility(View.GONE);

                        setMeta();

                    }
                }
                @Override
                public void onFailure(Call<CommonDomainPOJO> call, Throwable t) {
                    //  progressHUD.dismiss();
                    //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }

    }

    private void getSearchMetaList(String s) {
        try {
            sharedData.addIntData(SharedData.domainId, 0);
            sharedData.addIntData(SharedData.subDomainId, 0);
            Call<MetaListPOJO> call = apiInterface.getSearchDomainV2(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/search-domain-v2", loginPOJO.getActiveToken(),s,"10", "0");
            call.enqueue(new Callback<MetaListPOJO>() {
                @Override
                public void onResponse(Call<MetaListPOJO> call, Response<MetaListPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        MetaListPOJO reasonPOJO = response.body();
                        try {
                            if (reasonPOJO.getTagList() != null){
                                if (reasonPOJO.getTagList().size() == 0 ){
                                    label_noMeta.setVisibility(View.VISIBLE);
                                } else {
                                    label_noMeta.setVisibility(View.GONE);
                                }

                                metaListAdapter = new MetaListAdapter(getActivity(), MeetingRequestFragment.this,
                                     (ArrayList<MetaListPOJO.TagList>) reasonPOJO.getTagList());
                                FlexboxLayoutManager manager = new FlexboxLayoutManager(getContext());
                                manager.setFlexWrap(FlexWrap.WRAP);
                                manager.setJustifyContent(JustifyContent.FLEX_START);
                                rv_meta.setLayoutManager(manager );

//                                rv_meta.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rv_meta.setAdapter(metaListAdapter);

                            } else {
                                label_noMeta.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }
                        setMetaDomain();

                    }
                }
                @Override
                public void onFailure(Call<MetaListPOJO> call, Throwable t) {
                    //  progressHUD.dismiss();
                    //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }

    }

    private void setMetaDomain() {
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
        btn_meta.setVisibility(View.GONE);

    }


    public void getMetaDomain(String persona_id, String persona_name) {
        // ddapter.re();omainA
        label_noMeta.setVisibility(View.GONE);
        layout_lexpertise.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.GONE);
        txt_persona.setText(""+persona_name);
        edt_domain.setText("");
        edt_meta.setText("");
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
        if (metaListAdapter != null){
            metaListAdapter.clearData();
        }
        if (metaChildListAdapter != null){
            metaChildListAdapter.clearData();
        }
        cv_meta.setVisibility(View.GONE);
        Log.d(TAG, reasonId + "persono " + persona_id);

        Call<CommonMetaPOJO> call = apiInterface.getMetaList(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/get-meta-v2",loginPOJO.getActiveToken(), ""+loginPOJO.getEntityId(),
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
                        layout_domain.setVisibility(View.GONE);
                        rv_domain.setVisibility(View.GONE);
                        label_domain.setVisibility(View.VISIBLE);
                        layout_meta.setVisibility(View.VISIBLE);
                        rv_meta.setVisibility(View.VISIBLE);
                        label_noMeta.setVisibility(View.GONE);
                        layout_lexpertise.setVisibility(View.GONE);
//                        domainAdapter = new DomainListAdapter(getActivity(), MeetingRequestFragment.this,
//                                (ArrayList<CommonMetaListPOJO.DomainList>) reasonPOJO.getMrParams().getDomainList());
//                        rv_domain.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        rv_domain.setAdapter(domainAdapter);
                    } else {
                        label_domain.setVisibility(View.GONE);
                        layout_domain.setVisibility(View.GONE);
                        layout_meta.setVisibility(View.GONE);
                        layout_lexpertise.setVisibility(View.GONE);
                    }

                    if (domainAdapter != null) {
                        domainAdapter.notifyDataSetChanged();
                    }

                    if (reasonPOJO.getMrParams().getFlagExpertise()){
                        label_expertise.setText(getResources().getText(R.string.tag_expertise) + " "+ persona_name + "?");
                        layout_expertise.setVisibility(View.VISIBLE);
                        rv_expertise.setVisibility(View.VISIBLE);
                        label_expertise.setVisibility(View.VISIBLE);
                        FlexboxLayoutManager manager = new FlexboxLayoutManager(getContext());
                        manager.setFlexWrap(FlexWrap.WRAP);
                        manager.setJustifyContent(JustifyContent.FLEX_START);
                        rv_expertise.setLayoutManager(manager );
                        if (reasonPOJO.getMrParams().getExpertiseList() != null){
                            try {
                                rv_expertise.setAdapter(new ExpertiseListAdapter(getActivity(),
                                        MeetingRequestFragment.this, (ArrayList<ExpertiseListPOJO>) reasonPOJO.getMrParams().getExpertiseList()));
                            } catch (Exception e){
                                e.getMessage();
                            }

                        }

                        layout_region.setVisibility(View.GONE);
                        rv_region.setVisibility(View.GONE);
                        label_region.setVisibility(View.GONE);
                        layout_meeting_preference.setVisibility(View.GONE);


                    } else {
                        layout_expertise.setVisibility(View.GONE);
                        label_expertise.setVisibility(View.GONE);


                        layout_region.setVisibility(View.GONE);
                        rv_region.setVisibility(View.GONE);
                        label_region.setVisibility(View.GONE);
                        layout_meeting_preference.setVisibility(View.GONE);

                    }

                    if (!reasonPOJO.getMrParams().getFlagDomain() && !reasonPOJO.getMrParams().getFlagExpertise()) {
                        label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
                        layout_region.setVisibility(View.VISIBLE);
                        rv_region.setVisibility(View.VISIBLE);
                        label_region.setVisibility(View.VISIBLE);
                        layout_meeting_preference.setVisibility(View.VISIBLE);

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
       // label_region.setVisibility(View.GONE);
        //layout_region.setVisibility(View.GONE);

        rv_persona.setVisibility(View.GONE);
        //rv_region.setVisibility(View.GONE);

        layout_subdomain.setVisibility(View.GONE);
      //  layout_meeting_preference.setVisibility(View.GONE);
        btn_meta.setVisibility(View.GONE);

    }

    public void setSubDomain(Integer d_id, Integer s_id, String subDomainName) {
        Utility.hideKeyboard(getActivity());
        // scrollView.scrollTo(0,scrollView.getBottom());
        label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
        subDomainId = ""+s_id;
        domainId = ""+d_id;
        sdomainName = subDomainName;
        txt_domains.setText(subDomainName);
        label_domain.setVisibility(View.GONE);
        layout_domain.setVisibility(View.GONE);
        rv_persona.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.VISIBLE);
        layout_region.setVisibility(View.VISIBLE);
        rv_region.setVisibility(View.VISIBLE);
        label_region.setVisibility(View.VISIBLE);
        label_noDomain.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
    }


    public void setMeeting(Integer d_id, Integer s_id, String subDomainName) {
        Utility.hideKeyboard(getActivity());
       // scrollView.scrollTo(0,scrollView.getBottom());
        label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
        txt_sdomains.setText(""+subDomainName);
        layout_lsdomain.setVisibility(View.VISIBLE);
        subDomainId = ""+s_id;
        domainId = ""+d_id;
       // sdomainName = subDomainName;
      //  txt_domains.setText(subDomainName);
        label_domain.setVisibility(View.GONE);
        layout_domain.setVisibility(View.GONE);
        rv_persona.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.VISIBLE);
        layout_region.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        label_region.setVisibility(View.VISIBLE);
        label_noDomain.setVisibility(View.GONE);
        btn_meta.setVisibility(View.VISIBLE);
        layout_meeting_preference.setVisibility(View.GONE);

    }

    public void getRegion_Id(Integer countryId) {
        if (layout_meeting_preference.getVisibility() == View.GONE){
            layout_meeting_preference.setVisibility(View.VISIBLE);
        }
        regionId = "" + countryId;
    }

    public void setExpertise(Integer expertise_id, String expertiseName) {
        label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
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


    @OnClick({R.id.btn_request_meeting, R.id.img_close, R.id.layout_lpersona,
            R.id.layout_lreason, R.id.layout_ldomain, R.id.layout_lexpertise, R.id.btn_meta, R.id.layout_lsdomain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_request_meeting:
                if (regionId.equals("")){
                    Toast.makeText(getActivity(), "Please select region", Toast.LENGTH_SHORT).show();
                } else {
                    getSubmitRequestMeeting();
                }

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

                getMetaDomain(personaId, personaName);

                break;

            case R.id.layout_lexpertise:
                getMetaDomain(personaId, personaName);
                break;
            case R.id.btn_meta:

                setMetaSubDomain();
                break;

            case R.id.layout_lsdomain:
                getTagList(tagLists, Integer.parseInt(domainId), Integer.parseInt(subDomainId), sdomainName);

                break;

        }
    }



    private void setMetaSubDomain() {
        label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
        label_expertise.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_region.setVisibility(View.VISIBLE);
        rv_region.setVisibility(View.VISIBLE);
        layout_meeting_preference.setVisibility(View.VISIBLE);
        layout_lexpertise.setVisibility(View.GONE);
        label_region.setVisibility(View.VISIBLE);
        cv_meta.setVisibility(View.GONE);
        rv_meta.setVisibility(View.GONE);
        rv_metaChild.setVisibility(View.VISIBLE);
        btn_meta.setVisibility(View.GONE);
        //edt_meta.setText("");
        layout_meta.setVisibility(View.GONE);

    }


    public void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_request_meeting_success, null);
        TextView label_close = view1.findViewById(R.id.label_close);
        TextView label_title = view1.findViewById(R.id.label_title);
//        Text should say
//        "Hey Samir,
//        Thriive is now at work to find you the right business guru.
//
//        Expect a meeting confirmation from your match within 48 hrs."

        label_title.setText(Html.fromHtml("<b>Hey "+ loginPOJO.getFirstName() + ",</b> " + getResources().getString(R.string.label_success_meeting) + " "
        + personaName + ".<br> <br /> "+ getResources().getString(R.string.label_success_meeting1) + "<b>  48 hrs. </b>"));
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
    private void getSubmitRequestMeeting() {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getSaveMeetingRequest(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/save-meeting-request", loginPOJO.getActiveToken(),
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
                        CleverTapAPI cleverTap = CleverTapAPI.getDefaultInstance(getActivity().getApplicationContext());
                        HashMap<String, Object> loginEvent = new HashMap<String, Object>();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        loginEvent.put("request_string", personaName + "-" + reasonName);
                        loginEvent.put("time_stamp", currentDateandTime);
                        loginEvent.put("requestor_email_id", loginPOJO.getEmailId());
                        cleverTap.pushEvent("Thriive_Meeting_Request",loginEvent);

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

    public void getTagList(List<MetaListPOJO.Child> children, Integer d_id, Integer s_id, String tagName) {

        layout_lsdomain.setVisibility(View.GONE);
        tagLists.clear();
        //rv_meta.setVisibility(View.GONE);
        if (children == null){
            Utility.hideKeyboard(getActivity());
            // scrollView.scrollTo(0,scrollView.getBottom());
            label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
            subDomainId = ""+s_id;
            domainId = ""+d_id;
            sdomainName = tagName;
            txt_domains.setText(tagName);
            label_domain.setVisibility(View.GONE);
            layout_domain.setVisibility(View.GONE);
            rv_persona.setVisibility(View.GONE);
            layout_ldomain.setVisibility(View.VISIBLE);
            layout_region.setVisibility(View.GONE);
            rv_region.setVisibility(View.GONE);
            label_region.setVisibility(View.VISIBLE);
            label_noDomain.setVisibility(View.GONE);
            layout_meeting_preference.setVisibility(View.GONE);
            cv_meta.setVisibility(View.GONE);
            btn_meta.setVisibility(View.VISIBLE);
        } else if (children.size() == 0){
            Utility.hideKeyboard(getActivity());
            // scrollView.scrollTo(0,scrollView.getBottom());
            label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
            subDomainId = ""+s_id;
            domainId = ""+d_id;
            sdomainName = tagName;
            txt_domains.setText(tagName);
            label_domain.setVisibility(View.GONE);
            layout_domain.setVisibility(View.GONE);
            rv_persona.setVisibility(View.GONE);
            layout_ldomain.setVisibility(View.VISIBLE);
            layout_region.setVisibility(View.GONE);
            rv_region.setVisibility(View.GONE);
            label_region.setVisibility(View.VISIBLE);
            label_noDomain.setVisibility(View.GONE);
            layout_meeting_preference.setVisibility(View.GONE);
            cv_meta.setVisibility(View.GONE);
            btn_meta.setVisibility(View.VISIBLE);
        } else {
            Utility.hideKeyboard(getActivity());
            // scrollView.scrollTo(0,scrollView.getBottom());
            label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
            subDomainId = ""+s_id;
            domainId = ""+d_id;
            sdomainName = tagName;
            txt_domains.setText(tagName);
            label_domain.setVisibility(View.GONE);
            layout_domain.setVisibility(View.GONE);
            rv_persona.setVisibility(View.GONE);
            layout_ldomain.setVisibility(View.VISIBLE);
            layout_region.setVisibility(View.GONE);
            rv_region.setVisibility(View.GONE);
            label_region.setVisibility(View.VISIBLE);
            label_noDomain.setVisibility(View.GONE);
            layout_meeting_preference.setVisibility(View.GONE);
            cv_meta.setVisibility(View.VISIBLE);
            rv_metaChild.setVisibility(View.VISIBLE);
            btn_meta.setVisibility(View.VISIBLE);

            tagLists.addAll(children);
            FlexboxLayoutManager manager = new FlexboxLayoutManager(getContext());
            manager.setFlexWrap(FlexWrap.WRAP);
            manager.setJustifyContent(JustifyContent.FLEX_START);
            rv_metaChild.setLayoutManager(manager );
            metaChildListAdapter = new MetaChildListAdapter(getActivity(), MeetingRequestFragment.this,
                    (ArrayList<MetaListPOJO.Child>) tagLists);
            //  rv_metaChild.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_metaChild.setAdapter(metaChildListAdapter);
        }
    }
}