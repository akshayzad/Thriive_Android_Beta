package com.thriive.app.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.thriive.app.R;
import com.thriive.app.adapters.AttributeL1ListAdapter;
import com.thriive.app.adapters.AttributeL2ListAdapter;
import com.thriive.app.adapters.ExpertiseListAdapter;
import com.thriive.app.adapters.MeetingRequestTimeSlotAdapter;
import com.thriive.app.adapters.MetaChildListAdapter;
import com.thriive.app.adapters.MetaListAdapter;
import com.thriive.app.adapters.PersonaListAdapter;
import com.thriive.app.adapters.ReasonListAdapter;
import com.thriive.app.adapters.RegionAdapter;
import com.thriive.app.adapters.SchedulePagerAdapter;
import com.thriive.app.adapters.SelectedSlotsAdapter;
import com.thriive.app.adapters.SelectedSlotsOverviewAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.AttributeL1ListPOJO;
import com.thriive.app.models.AttributeL2ListPOJO;
import com.thriive.app.models.CommonAttributeL1POJO;
import com.thriive.app.models.CommonAttributeL2POJO;
import com.thriive.app.models.CommonDomainPOJO;
import com.thriive.app.models.CommonMatchingPOJO;
import com.thriive.app.models.CommonMetaPOJO;
import com.thriive.app.models.CommonPersonaPOJO;
import com.thriive.app.models.CommonReasonPOJO;
import com.thriive.app.models.CommonRequestTimeSlots;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.CountryListPOJO;
import com.thriive.app.models.EventBusPOJO;
import com.thriive.app.models.ExpertiseBodyPOJO;
import com.thriive.app.models.ExpertiseListPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.MeetingSlotBodyPOJO;
import com.thriive.app.models.MetaListPOJO;
import com.thriive.app.models.PersonaListPOJO;
import com.thriive.app.models.ReasonListPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;


import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

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
    @BindView(R.id.detail_linear_layout)
    LinearLayout detail_linear_layout;

    @BindView(R.id.layout_summary)
    LinearLayout layout_summary;
    @BindView(R.id.layout_meeting_preference)
    LinearLayout layout_meeting_preference;
    @BindView(R.id.cardview_meeting_preference)
    CardView cardview_meeting_preference;
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
    @BindView(R.id.txt_select_one_optn)
    TextView txt_select_one_optn;
    @BindView(R.id.txt_toaster)
    TextView txt_toaster;
    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.edt_domain)
    EditText edt_domain;
    @BindView(R.id.detail_editText)
    EditText detail_editText;
    @BindView(R.id.detail_editText_disabled)
    EditText detail_editText_disabled;
    @BindView(R.id.slot_btn_next)
    Button slot_btn_next;

    @BindView(R.id.tag_summary_objective)
    TextView tag_summary_objective;
    @BindView(R.id.edit_summary)
    ImageView edit_summary;
    @BindView(R.id.img_summary_objective)
    ImageView img_summary_objective;
    @BindView(R.id.tag_summary_l1attribute)
    TextView tag_summary_l1attribute;
    @BindView(R.id.tag_summary_persona)
    TextView tag_summary_persona;
    @BindView(R.id.tag_summary_l2attribute)
    TextView tag_summary_l2attribute;
    @BindView(R.id.tag_summary_expertise)
    TextView tag_summary_expertise;
    @BindView(R.id.tag_summary_domain)
    TextView tag_summary_domain;
    @BindView(R.id.tag_summary_subdomain)
    TextView tag_summary_subdomain;

    @BindView(R.id.tag_overview_objective)
    TextView tag_overview_objective;
    @BindView(R.id.img_overview_objective)
    ImageView img_overview_objective;
    @BindView(R.id.tag_overview_l1attribute)
    TextView tag_overview_l1attribute;
    @BindView(R.id.tag_overview_persona)
    TextView tag_overview_persona;
    @BindView(R.id.tag_overview_l2attribute)
    TextView tag_overview_l2attribute;
    @BindView(R.id.tag_overview_expertise)
    TextView tag_overview_expertise;
    @BindView(R.id.tag_overview_domain)
    TextView tag_overview_domain;
    @BindView(R.id.tag_overview_subdomain)
    TextView tag_overview_subdomain;
    @BindView(R.id.tag_overview_region)
    TextView tag_overview_region;

    private APIInterface apiInterface;

    RelativeLayout containerView;
    private SharedData sharedData;
    private  KProgressHUD progressHUD;
    public  static  String TAG = MeetingRequestFragment.class.getName();

    private ArrayList<CommonRequesterPOJO> arrayList = new ArrayList<>();
    Unbinder unbinder;
    private LoginPOJO.ReturnEntity loginPOJO;


    public String reasonId = "",l1AttribId = "",l2AttribId = "",requestorId, giverPersonaId, domainId ="",helper_text,
            subDomainId ="", expertiseId  = "", selected_layout,reasonName,reasonImgUrl,l1AttribName,l2AttribName, regionId = "",str_detail = "",
            personaName , domainName = "", sdomainName = "", expertiseName = "", regionName = "",selectedDate = "",currentDate = "", hexCode = "",
            rowcode="";

    private String  personaId = "";
    public boolean isFromHomeScreen = false;
    public boolean isFirstTimeHomeScreen = false;
    public boolean isDomainClickedFromSummary = false;
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
    @BindView(R.id.rv_attribL1)
    RecyclerView rv_attribL1;
    @BindView(R.id.rv_attribL2)
    RecyclerView rv_attribL2;
    @BindView(R.id.txt_expertise)
    TextView txt_expertise;
    @BindView(R.id.label_region)
    TextView label_region;
    @BindView(R.id.txt_additional_details)
    TextView txt_additional_details;
    @BindView(R.id.label_noDomain)
    TextView label_noDomain;
    @BindView(R.id.txt_meetingCount)
    TextView txt_meetingCount;
    @BindView(R.id.txt_message)
    TextView txt_message;
    @BindView(R.id.txt_details_160)
    TextView txt_details_160;

    @BindView(R.id.layout_meta)
    LinearLayout layout_meta;
    @BindView(R.id.layout_overview)
    LinearLayout layout_overview;

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
    @BindView(R.id.txt_overview)
    TextView txt_overview;
    @BindView(R.id.txt_meeting_for)
    TextView txt_meeting_for;
    @BindView(R.id.text_l1_attribute)
    TextView text_l1_attribute;
    @BindView(R.id.txt_with_persona)
    TextView txt_with_persona;
    @BindView(R.id.txt_domain_sub_domain)
    TextView txt_domain_sub_domain;
    @BindView(R.id.txt_from_region)
    TextView txt_from_region;

    @BindView(R.id.txt_expetise)
    TextView txt_expetise;
    @BindView(R.id.txt_sub_domain)
    TextView txt_sub_domain;
    @BindView(R.id.txt_additional_details_count)
    TextView txt_additional_details_count;

    @BindView(R.id.layout_domain_sub_domain)
    LinearLayout layout_domain_sub_domain;
    @BindView(R.id.layout_sub_domain)
    LinearLayout layout_sub_domain;
    @BindView(R.id.layout_expetise)
    LinearLayout layout_expetise;

    @BindView(R.id.btn_skip)
    TextView btn_skip;
    @BindView(R.id.btn_expertise_skip)
    TextView btn_expertise_skip;
    @BindView(R.id.cv_meta)
    CardView cv_meta;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.img_stamp)
    ImageView img_stamp;


    View view;
    MetaListAdapter metaListAdapter;
    MetaChildListAdapter metaChildListAdapter;

    private CleverTapAPI cleverTap;
    ArrayList<MetaListPOJO.Child> tagLists = new ArrayList<>();

    private int dotsCount=5;    //No of tabs or images
    private ImageView[] dots;
    @BindView(R.id.viewPagerCountDots)
    LinearLayout linearLayout;
    @BindView(R.id.viewPagerCountDots1)
    LinearLayout linearLayout1;
    @BindView(R.id.viewPagerCountDots2)
    LinearLayout linearLayout2;
    @BindView(R.id.viewPagerCountDots3)
    LinearLayout linearLayout3;
    @BindView(R.id.viewPagerCountDots4)
    LinearLayout linearLayout4;

    @BindView(R.id.layout_date_time_slots)
    LinearLayout layout_date_time_slots;
    @BindView(R.id.cardview_time_slots)
    CardView cardview_time_slots;
    @BindView(R.id.overview_card)
    CardView overview_card;
    @BindView(R.id.rv_time_slots)
    RecyclerView rv_time_slots;
    @BindView(R.id.rv_select_time_slots)
    RecyclerView rv_select_time_slots;
    @BindView(R.id.rv_slots)
    RecyclerView rv_slots;
    @BindView(R.id.layout_circle)
    LinearLayout layout_circle;
    @BindView(R.id.txt_year)
    TextView txt_year;
    @BindView(R.id.txt_month)
    TextView txt_month;
    @BindView(R.id.txt_day)
    TextView txt_day;
    @BindView(R.id.img_info)
    ImageView img_info;
    @BindView(R.id.txt_timezone) TextView txtTimezone;


    boolean flag_domain_optional = false;
    boolean flag_expertise_optional = false;
    boolean flag_req_text = false;
    boolean is_l1_empty = false;
    boolean is_l2_flag = false;
    boolean flag_domain = false;
    boolean flag_expertise = false;
    boolean flag_l1 = false;
    boolean flag_l2 = false;
    boolean is_refresh_flag = false;
    boolean flag = false;
    Date date1;
    String year1, month1, day, newDate;
    Calendar calendar = Calendar.getInstance();
    ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> selectItem;
    ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> slotsListPOJOS;
    Set<CommonRequestTimeSlots.EntitySlotsListPOJO> set = new HashSet<>();
    ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO> slotsListPOJOS1;
    MeetingRequestTimeSlotAdapter meetingRequestTimeSlotAdapter;
    ArrayList<CountryListPOJO> region_array;
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
        view = inflater.inflate(R.layout.fragment_meeting_request, container, false);
        unbinder = ButterKnife.bind(this, view);

        containerView = view.findViewById(R.id.container);
        sharedData = new SharedData(getActivity());
        apiInterface = APIClient.getApiInterface();
        loginPOJO = Utility.getLoginData(getActivity());

        cleverTap = CleverTapAPI.getDefaultInstance(getActivity().getApplicationContext());
        slotsListPOJOS = new ArrayList<>();
        slotsListPOJOS.clear();
        slotsListPOJOS1 = new ArrayList<>();
        slotsListPOJOS1.clear();


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


        Bundle mArgs = getArguments();
        if (mArgs!=null) {
            if (!mArgs.isEmpty()) {
                isFromHomeScreen = true;
                isFirstTimeHomeScreen = true;
            }
        }
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

        detail_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (detail_editText.getText().toString().trim().length() > 0){
                    txt_additional_details_count.setText(detail_editText.getText().toString().trim().length() + "/" + "160");
                }else {
                    txt_additional_details_count.setText( "0/160");
                }
                if (regionId.length() > 0 && detail_editText.getText().length() > 0 ){
                    detail_editText.setBackground(getResources().getDrawable(R.drawable.bg_red_border));
                    btn_next.setBackground(getResources().getDrawable(R.drawable.filled_circle_terracota));
                }else {
                    detail_editText.setBackground(getResources().getDrawable(R.drawable.bg_grey_border));
                    btn_next.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //drawPageSelectionIndicators(-1);
        setIndicators(-1);


        return  view;
    }

    public void fromHomeScreen(){
        Bundle mArgs = getArguments();
        if (mArgs!=null) {
            if (!mArgs.isEmpty()) {
                reasonId = mArgs.getString("reason_id");
                reasonName = mArgs.getString("reason_name");
                l1AttribId = mArgs.getString("l1_attrib_id");
                l1AttribName = mArgs.getString("l1_attrib_name");
                l2AttribId = mArgs.getString("l2_attrib_id");
                l2AttribName = mArgs.getString("l2_attrib_name");
                personaId = mArgs.getString("giver_persona_id");
                personaName = mArgs.getString("giver_persona_name");
                domainId = mArgs.getString("domain_id");
                domainName = mArgs.getString("domain_name");
                subDomainId = mArgs.getString("sub_domain_id");
                sdomainName = mArgs.getString("sub_domain_name");
                expertiseId = mArgs.getString("expertise_id");
                expertiseName = mArgs.getString("expertise_name");
                rowcode = mArgs.getString("rowcode");
                flag_domain = mArgs.getBoolean("flag_domain", false);
                flag_expertise = mArgs.getBoolean("flag_expertise", false);
                flag_l1 = mArgs.getBoolean("flag_l1", false);
                flag_l2 = mArgs.getBoolean("flag_l2", false);
                flag_domain_optional = false;
                flag_expertise_optional = false;
                is_l2_flag = flag_l2;
                is_l1_empty = !flag_l1;
                flag_req_text = true;
                reasonImgUrl = sharedData.getStringData(SharedData.API_URL) +"reason_button/"+ rowcode + ".png";

                if (flag_domain_optional){
                    selected_layout = "step_6";
                }else {
                    if (subDomainId.equals("")){
                        selected_layout = "step_9";
                    }else{
                        selected_layout = "step_6";
                    }
                }

                setIndicators(4);
                selected_layout = "step_9";
                img_back.setVisibility(View.VISIBLE);
                layout_reason.setVisibility(View.GONE);
                rv_reason.setVisibility(View.GONE);
                label_region.setText(getResources().getString(R.string.tag_region) + " " + personaName + " to be from?");
                layout_region.setVisibility(View.VISIBLE);
                layout_summary.setVisibility(View.VISIBLE);
                rv_region.setVisibility(View.VISIBLE);
                label_region.setVisibility(View.VISIBLE);
                layout_meeting_preference.setVisibility(View.GONE);
                cardview_meeting_preference.setVisibility(View.GONE);
                cardview_time_slots.setVisibility(View.GONE);
                layout_date_time_slots.setVisibility(View.GONE);
                img_stamp.setVisibility(View.VISIBLE);
                layout_overview.setVisibility(View.GONE);
                setSummaryDetails();
                label_noDomain.setVisibility(View.GONE);
                setMeta();
                setHelperText();
            }
        }
    }


    public void init(){
        is_l2_flag = false;
        is_l1_empty = false;
        //selectItem.clear();
        txt_message.setText("You can request only "+ sharedData.getIntData(SharedData.MEETING_TOTAL) + " meetings a week.");
        int count_m = sharedData.getIntData(SharedData.MEETING_DONE) + 1;
        txt_meetingCount.setText(count_m+ "/" + sharedData.getIntData(SharedData.MEETING_TOTAL));
        //selected tag
        layout_lreason.setVisibility(View.GONE);
        layout_lpersona.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.GONE);
        layout_lsdomain.setVisibility(View.GONE);
        layout_lexpertise.setVisibility(View.GONE);
        img_back.setVisibility(View.GONE);
        setIndicators(-1);
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
        layout_summary.setVisibility(View.GONE);

        //rv
        rv_reason.setVisibility(View.VISIBLE);
        rv_persona.setVisibility(View.GONE);
        rv_attribL1.setVisibility(View.GONE);
        rv_attribL2.setVisibility(View.GONE);
        rv_expertise.setVisibility(View.GONE);
        rv_domain.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        rv_meta.setVisibility(View.GONE);
        rv_metaChild.setVisibility(View.GONE);

        layout_subdomain.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
        cardview_meeting_preference.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        img_stamp.setVisibility(View.VISIBLE);
        layout_overview.setVisibility(View.GONE);
        btn_meta.setVisibility(View.GONE);
        btn_skip.setVisibility(View.GONE);
    }


    private void getMetaV2(){

        Call<CommonMetaPOJO> call = apiInterface.getMetaList(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/get-meta-v2",loginPOJO.getActiveToken(), ""+loginPOJO.getEntityId(),
                loginPOJO.getEntityName(), ""+loginPOJO.getReqPersonaId(),
                loginPOJO.getReqPersonaName(), reasonId, personaId,l1AttribId,l2AttribId);
        call.enqueue(new Callback<CommonMetaPOJO>() {
            @Override
            public void onResponse(Call<CommonMetaPOJO> call, Response<CommonMetaPOJO> response) {
                CommonMetaPOJO reasonPOJO = response.body();
                if (reasonPOJO.getOK()) {

                    if (reasonPOJO.getMrParams().getFlagDomain()){
                        flag_domain = true;
                    }

                    if (reasonPOJO.getMrParams().getFlagExpertise()){
                        flag_expertise = true;
                    }

                    flag_req_text = reasonPOJO.getMrParams().getFlag_req_text();

                    if (!reasonPOJO.getMrParams().getFlagDomain() && !reasonPOJO.getMrParams().getFlagExpertise()) {
                        //drawPageSelectionIndicators(4);
                        flag_domain = false;
                        flag_expertise = false;
                        setIndicators(4);
                        selected_layout = "step_9";
                        label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
                        layout_region.setVisibility(View.VISIBLE);
                        layout_summary.setVisibility(View.VISIBLE);
                        rv_region.setVisibility(View.VISIBLE);
                        label_region.setVisibility(View.VISIBLE);
                        layout_meeting_preference.setVisibility(View.GONE);
                        cardview_meeting_preference.setVisibility(View.GONE);
                        cardview_time_slots.setVisibility(View.GONE);
                        layout_date_time_slots.setVisibility(View.GONE);
                        img_stamp.setVisibility(View.VISIBLE);
                        layout_overview.setVisibility(View.GONE);

                        label_noMeta.setVisibility(View.GONE);
                        layout_lexpertise.setVisibility(View.GONE);
                        layout_ldomain.setVisibility(View.GONE);
                        label_domain.setVisibility(View.GONE);
                        layout_persona.setVisibility(View.GONE);
                        rv_persona.setVisibility(View.GONE);
                        layout_meta.setVisibility(View.GONE);
                        setSummaryDetails();
                    } else {
                        setIndicators(2);
                        rv_attribL2.setVisibility(View.GONE);
//                        setAttributeL2(reasonPOJO);
//                            getMetaDomain("" +personaId, ""+personaName);
                        domainName = "";
                        domainId = "";
                        sdomainName = "";
                        subDomainId = "";
                        setDomainOnclickNext();
                    }

                }

            }

            @Override
            public void onFailure(Call<CommonMetaPOJO> call, Throwable t) {

            }
        });

    }


    private void setHelperText() {
        Call<CommonMetaPOJO> call = apiInterface.getMetaList(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/get-meta-v2",loginPOJO.getActiveToken(), ""+loginPOJO.getEntityId(),
                loginPOJO.getEntityName(), ""+loginPOJO.getReqPersonaId(),
                loginPOJO.getReqPersonaName(), reasonId, personaId,l1AttribId,l2AttribId);
        call.enqueue(new Callback<CommonMetaPOJO>() {
            @Override
            public void onResponse(Call<CommonMetaPOJO> call, Response<CommonMetaPOJO> response) {
                CommonMetaPOJO pojo = response.body();
                if (pojo.getOK()) {
                    helper_text = pojo.getMrParams().getHelper_text();
                    if (helper_text != null) {
                        detail_editText.setHint(helper_text);
                    }
                }

            }

            @Override
            public void onFailure(Call<CommonMetaPOJO> call, Throwable t) {

            }
        });
    }

    private void setClearDataDomain() {
//        label_domain.setText(getResources().getText(R.string.tag_domain) + " "+ personaName + "?");
        domainName = "";
        sdomainName = "";
       if (flag_domain_optional){
           btn_skip.setVisibility(View.VISIBLE);
       }else {
           btn_skip.setVisibility(View.GONE);
       }
        // layout_domain.setVisibility(View.GONE);
        // rv_domain.setVisibility(View.GONE);
//        label_domain.setVisibility(View.VISIBLE);
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
        layout_summary.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
//        label_domain.setVisibility(View.VISIBLE);
    }

    private void getReason() {
        String url = sharedData.getStringData(SharedData.API_URL);
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
                    layout_domain.setVisibility(View.GONE);
                    label_domain.setVisibility(View.GONE);
                    rv_reason.setVisibility(View.VISIBLE);
                    rv_reason.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    rv_reason.setAdapter(new ReasonListAdapter(getActivity(), MeetingRequestFragment.this,
                            (ArrayList<ReasonListPOJO>) reasonPOJO.getMrParams().getReasonList()));

                    FlexboxLayoutManager manager = new FlexboxLayoutManager(getContext());
                    manager.setFlexWrap(FlexWrap.WRAP);
                    manager.setJustifyContent(JustifyContent.FLEX_START);

                    region_array = new ArrayList<>();
                    region_array = removeDuplicates(
                            (ArrayList<CountryListPOJO>) reasonPOJO.getMrParams().getCountryList());
                    rv_region.setLayoutManager(manager );

                    //    rv_region.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
                    rv_region.setAdapter(new RegionAdapter(getActivity(), MeetingRequestFragment.this,
                            (ArrayList<CountryListPOJO>) region_array));
                }
                if (isFirstTimeHomeScreen) {
                    fromHomeScreen();
                    isFirstTimeHomeScreen = false;
                }
            }
            @Override
            public void onFailure(Call<CommonReasonPOJO> call, Throwable t) {
                progressHUD.dismiss();
                if (isFirstTimeHomeScreen) {
                    fromHomeScreen();
                }
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
        if (isFirstTimeHomeScreen) {
            fromHomeScreen();
        }
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

    public void get_l1_attrib(String reason_id, String reason_name, String reason_img_url) {
        HashMap<String, Object> mr1Event = new HashMap<String, Object>();
        mr1Event.put("meeting_objective", reason_name);
        cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Request_Step1, mr1Event);

        txt_reason.setText(""+reason_name);
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        selected_layout = "step_1";
        reasonName = reason_name;
        reasonId = reason_id;
        reasonImgUrl = reason_img_url;
        Call<CommonAttributeL1POJO> call = apiInterface.getAttributeL1(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/get-l1-attrib",loginPOJO.getActiveToken(),""+loginPOJO.getEntityId(),
                loginPOJO.getEntityName(), +loginPOJO.getReqPersonaId(),
                loginPOJO.getReqPersonaName(),reason_id);
        call.enqueue(new Callback<CommonAttributeL1POJO>() {
            @Override
            public void onResponse(Call<CommonAttributeL1POJO> call, Response<CommonAttributeL1POJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonAttributeL1POJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMrParams().getReasonName());
                    if (reasonPOJO.getOK()) {
                        //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        //   Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //drawPageSelectionIndicators(0);
                    if (reasonPOJO.getMrParams().getAttributeL1ListPOJOS().size() > 0 ){
                        setIndicators(0);
                        rv_attribL1.setVisibility(View.VISIBLE);
                        img_back.setVisibility(View.VISIBLE);
                        rv_attribL1.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_attribL1.setAdapter(new AttributeL1ListAdapter(getActivity(), MeetingRequestFragment.this,
                                (ArrayList<AttributeL1ListPOJO>) reasonPOJO.getMrParams().getAttributeL1ListPOJOS()));

                        setAttributeL1(reasonPOJO);
                    }else {
                        is_l1_empty = true;
                        getPersona("","","");
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonAttributeL1POJO> call, Throwable t) {
                progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPersona(String l1_attrib_id, String reason_name, String hex_code) {
        HashMap<String, Object> mr1Event = new HashMap<String, Object>();
        mr1Event.put("meeting_objective", reason_name);
        cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Request_Step1, mr1Event);
        if (!hex_code.equals("")){
            hexCode = hex_code;
        }

        if (is_l1_empty){
            selected_layout = "step_1";
        }else {
            selected_layout = "step_2";
        }
        txt_reason.setText(""+reason_name);
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();

        //selected_layout = "step_2";
        l1AttribName = reason_name;
        l1AttribId = l1_attrib_id;
        Call<CommonPersonaPOJO> call = apiInterface.getPersona(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/get-persona",loginPOJO.getActiveToken(),""+loginPOJO.getEntityId(),
                loginPOJO.getEntityName(), ""+loginPOJO.getReqPersonaId(),
                loginPOJO.getReqPersonaName(), reasonId, l1_attrib_id);
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

                    //drawPageSelectionIndicators(1);
                    if (reasonPOJO.getMrParams().getPersonaList().size() > 0){
                        setIndicators(1);
                        rv_persona.setVisibility(View.VISIBLE);
                        img_back.setVisibility(View.VISIBLE);
                        rv_persona.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_persona.setAdapter(new PersonaListAdapter(getActivity(), MeetingRequestFragment.this,
                                (ArrayList<PersonaListPOJO>) reasonPOJO.getMrParams().getPersonaList(),hexCode));

                        setPersona(reasonPOJO);
                    }else {
                        getAttributeL2(personaId,reason_name);
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonPersonaPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAttributeL2(String persona_id, String reason_name) {
        HashMap<String, Object> mr1Event = new HashMap<String, Object>();
        mr1Event.put("meeting_objective", reason_name);
        cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Request_Step1, mr1Event);

        txt_reason.setText(""+reason_name);
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        personaName = reason_name;
        personaId = persona_id;
        is_l2_flag = false;
        Call<CommonAttributeL2POJO> call = apiInterface.getAttributeL2(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/get-l2-attrib",loginPOJO.getActiveToken(),""+loginPOJO.getEntityId(),
                loginPOJO.getEntityName(), ""+loginPOJO.getReqPersonaId(),
                loginPOJO.getReqPersonaName(), reasonId, l1AttribId, persona_id);
        call.enqueue(new Callback<CommonAttributeL2POJO>() {
            @Override
            public void onResponse(Call<CommonAttributeL2POJO> call, Response<CommonAttributeL2POJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonAttributeL2POJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    if (reasonPOJO.getOK()) {
                        //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        //   Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if (reasonPOJO.getMrParams().getAttributeL2ListPOJOS().size() > 0){
                        selected_layout = "step_3";
                        is_l2_flag = true;
                        expertiseName="";
                        expertiseId="";
                        setIndicators(2);
                        rv_attribL2.setVisibility(View.VISIBLE);
                        img_back.setVisibility(View.VISIBLE);
                        rv_attribL2.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_attribL2.setAdapter(new AttributeL2ListAdapter(getActivity(), MeetingRequestFragment.this,
                                (ArrayList<AttributeL2ListPOJO>) reasonPOJO.getMrParams().getAttributeL2ListPOJOS(),hexCode));
                        setAttributeL2(reasonPOJO);
                    }else {
                        //drawPageSelectionIndicators(2);
                        if (is_l2_flag){
                            selected_layout = "step_3";
                            rv_attribL2.setVisibility(View.VISIBLE);
                            img_back.setVisibility(View.VISIBLE);
                        }else{

                            getMetaV2();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonAttributeL2POJO> call, Throwable t) {
                progressHUD.dismiss();
                //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAttributeL1(CommonAttributeL1POJO reasonPOJO) {
        if (reasonPOJO.getMrParams().getL1_screen_title() != null){
            txt_lpersona.setText(reasonPOJO.getMrParams().getL1_screen_title());
        }else {
            txt_lpersona.setText("Select the kind of " + reasonName + " you are looking for");
        }
        //txt_lpersona.setText("Select the kind of " + reasonName + " you are looking for");
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
        layout_summary.setVisibility(View.GONE);
        //rv
        rv_reason.setVisibility(View.GONE);
        rv_persona.setVisibility(View.GONE);
        rv_attribL2.setVisibility(View.GONE);
        rv_expertise.setVisibility(View.GONE);
        rv_domain.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        rv_meta.setVisibility(View.GONE);
        rv_metaChild.setVisibility(View.GONE);
        cv_meta.setVisibility(View.GONE);

        btn_meta.setVisibility(View.GONE);
        btn_skip.setVisibility(View.GONE);


        layout_meeting_preference.setVisibility(View.GONE);
        cardview_meeting_preference.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        img_stamp.setVisibility(View.VISIBLE);
        layout_overview.setVisibility(View.GONE);

    }

    private void setPersona(CommonPersonaPOJO reasonPOJO) {
        if (reasonPOJO.getMrParams().getPersona_screen_title() != null){
            txt_lpersona.setText(reasonPOJO.getMrParams().getPersona_screen_title());
        }else {
            txt_lpersona.setText(getResources().getString(R.string.label_meeting_request_persona) + " " + reasonName + "?");
        }
        isFromHomeScreen = false;
        expertiseId="";
        expertiseName="";
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
        rv_attribL1.setVisibility(View.GONE);
        rv_attribL2.setVisibility(View.GONE);


        layout_domain.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        layout_summary.setVisibility(View.GONE);
        //rv
        rv_reason.setVisibility(View.GONE);
        rv_persona.setVisibility(View.VISIBLE);

        rv_attribL2.setVisibility(View.GONE);
        rv_expertise.setVisibility(View.GONE);
        rv_domain.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        rv_meta.setVisibility(View.GONE);
        rv_metaChild.setVisibility(View.GONE);
        cv_meta.setVisibility(View.GONE);

        btn_meta.setVisibility(View.GONE);
        btn_skip.setVisibility(View.GONE);

        layout_meeting_preference.setVisibility(View.GONE);
        cardview_meeting_preference.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        img_stamp.setVisibility(View.VISIBLE);
        layout_overview.setVisibility(View.GONE);
    }

    private void setAttributeL2(CommonAttributeL2POJO reasonPOJO) {
        if (reasonPOJO.getMrParams().getL2_screen_title() != null){
            txt_lpersona.setText( reasonPOJO.getMrParams().getL2_screen_title());
        }else {
            txt_lpersona.setText(getResources().getString(R.string.label_meeting_request_persona) + " " + reasonName + " partnership?");
        }

        expertiseId="";
        expertiseName="";

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
        rv_attribL1.setVisibility(View.GONE);

        layout_domain.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_subdomain.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        layout_summary.setVisibility(View.GONE);
        //rv
        rv_reason.setVisibility(View.GONE);
        rv_persona.setVisibility(View.GONE);
        rv_expertise.setVisibility(View.GONE);
        rv_domain.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        rv_meta.setVisibility(View.GONE);
        rv_metaChild.setVisibility(View.GONE);
        cv_meta.setVisibility(View.GONE);

        btn_meta.setVisibility(View.GONE);
        btn_skip.setVisibility(View.GONE);


        layout_meeting_preference.setVisibility(View.GONE);
        cardview_meeting_preference.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        img_stamp.setVisibility(View.VISIBLE);
        layout_overview.setVisibility(View.GONE);

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
                                rv_domain.setLayoutManager(new LinearLayoutManager(getActivity()));
                                // rv_domain.setAdapter(domainAdapter);

                            } else {
                                label_noDomain.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e){
                            e.getMessage();
                        }
                        layout_region.setVisibility(View.GONE);
                        layout_summary.setVisibility(View.GONE);
                        rv_region.setVisibility(View.GONE);
                        label_region.setVisibility(View.GONE);
                        layout_meeting_preference.setVisibility(View.GONE);
                        cardview_meeting_preference.setVisibility(View.GONE);
                        cardview_time_slots.setVisibility(View.GONE);
                        layout_date_time_slots.setVisibility(View.GONE);
                        img_stamp.setVisibility(View.VISIBLE);
                        layout_overview.setVisibility(View.GONE);

                        setMeta();

                    }
                }
                @Override
                public void onFailure(Call<CommonDomainPOJO> call, Throwable t) {
                    //  progressHUD.dismiss();
                    //  Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
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
                                    btn_meta.setVisibility(View.GONE);
                                } else {
                                    label_noMeta.setVisibility(View.GONE);
                                }

                                metaListAdapter = new MetaListAdapter(getActivity(), MeetingRequestFragment.this,
                                        (ArrayList<MetaListPOJO.TagList>) reasonPOJO.getTagList(), isDomainClickedFromSummary);
                                FlexboxLayoutManager manager = new FlexboxLayoutManager(getContext());
                                manager.setFlexWrap(FlexWrap.WRAP);
                                manager.setJustifyContent(JustifyContent.FLEX_START);
                                rv_meta.setLayoutManager(manager );

//                                rv_meta.setLayoutManager(new LinearLayoutManager(getActivity()));
                                rv_meta.setAdapter(metaListAdapter);

                                if (isDomainClickedFromSummary) {
                                    getTagList(reasonPOJO.getTagList().get(0).getChildren(), reasonPOJO.getTagList().get(0).getDomainId(),
                                            reasonPOJO.getTagList().get(0).getSubDomainId(), reasonPOJO.getTagList().get(0).getTagName());
                                }

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
        layout_summary.setVisibility(View.GONE);

        rv_persona.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        rv_attribL2.setVisibility(View.GONE);
        layout_subdomain.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
        cardview_meeting_preference.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        img_stamp.setVisibility(View.VISIBLE);
        layout_overview.setVisibility(View.GONE);
        btn_meta.setVisibility(View.GONE);
        btn_skip.setVisibility(View.GONE);

    }

    public void setNewDomain(Integer expertise_id, String expertise_name) {
        /*if (is_l2_flag){
            selected_layout = "step_5";
        }else {
            if (expertise_id == 0){selected_layout = "step_3";}else{ selected_layout = "step_4";}
        }*/

        expertiseId = ""+expertise_id;
        expertiseName = expertise_name;
    }

    public void setDomain(Integer expertise_id, String expertise_name) {

//        if (selected_layout.equals("step_6")){
            if (tagLists.size() > 0){
                rv_meta.setVisibility(View.VISIBLE);
                cv_meta.setVisibility(View.VISIBLE);
            }else {
                cv_meta.setVisibility(View.GONE);
                btn_meta.setVisibility(View.GONE);
            }
            btn_meta.setVisibility(View.VISIBLE);
//        }

        if (is_l2_flag){
            selected_layout = "step_4";
        }else {
            selected_layout = "step_3";
            /*if (flag_domain_optional){
                selected_layout = "step_3";
            }else {
                if (flag_expertise_optional){
                    selected_layout = "step_4";
                }else {
                    if (expertise_id == 0){selected_layout = "step_3";}else{ selected_layout = "step_4";}
                }

            }*/
        }

        label_domain.setText(getResources().getText(R.string.tag_domain) + " "+ personaName + "?");
        layout_domain.setVisibility(View.GONE);
        rv_domain.setVisibility(View.GONE);

        if (flag_domain_optional){
            btn_skip.setVisibility(View.VISIBLE);

            layout_meta.setVisibility(View.VISIBLE);
            label_domain.setVisibility(View.VISIBLE);
            cv_meta.setVisibility(View.GONE);
            btn_meta.setVisibility(View.GONE);
            rv_meta.setVisibility(View.GONE);
        }else {
            btn_skip.setVisibility(View.GONE);

            layout_meta.setVisibility(View.VISIBLE);
            label_domain.setVisibility(View.VISIBLE);
            rv_meta.setVisibility(View.VISIBLE);
            rv_select_time_slots.setVisibility(View.GONE);
        }

        if (!domainId.equals("")){
            btn_meta.setVisibility(View.VISIBLE);
        }else {
            btn_meta.setVisibility(View.GONE);
        }

        /*if (!rv_meta.isComputingLayout()){
            btn_meta.setVisibility(View.GONE);
        }*/

        label_domain.setVisibility(View.VISIBLE);
        label_noMeta.setVisibility(View.GONE);
        layout_lexpertise.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        label_expertise.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        layout_summary.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        layout_overview.setVisibility(View.GONE);

        img_stamp.setVisibility(View.VISIBLE);

        expertiseId = ""+expertise_id;
        expertiseName = expertise_name;

    }

    public void setDomainOnclickNext(){

        selected_layout = "step_4";
        Utility.hideKeyboard(getActivity());
        setIndicators(3);
        label_domain.setText(getResources().getText(R.string.tag_domain) + " "+ personaName + "?");
        layout_domain.setVisibility(View.GONE);
        rv_domain.setVisibility(View.GONE);
        layout_reason.setVisibility(View.GONE);
        layout_persona.setVisibility(View.GONE);
        rv_persona.setVisibility(View.GONE);
        rv_attribL2.setVisibility(View.GONE);
        label_domain.setVisibility(View.VISIBLE);
        layout_meta.setVisibility(View.VISIBLE);
        edt_meta.setText("");
        if (flag_domain_optional){
            btn_skip.setVisibility(View.VISIBLE);
        }else {
            btn_skip.setVisibility(View.GONE);
        }
        rv_meta.setVisibility(View.VISIBLE);
        rv_select_time_slots.setVisibility(View.GONE);
        label_noMeta.setVisibility(View.GONE);
        layout_lexpertise.setVisibility(View.GONE);

        layout_expertise.setVisibility(View.GONE);
        label_expertise.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        layout_summary.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
        cardview_meeting_preference.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        img_stamp.setVisibility(View.VISIBLE);
        layout_overview.setVisibility(View.GONE);
    }

    private void setBackButtonEvent(){
        if (selected_layout.equals("step_1")){

            refreshRegionView();
            init();
            getReason();
        }else if (selected_layout.equals("step_2")){
            refreshRegionView();
            get_l1_attrib(reasonId,reasonName,reasonImgUrl);
        }else if (selected_layout.equals("step_3")){
            refreshRegionView();
            getPersona(l1AttribId,l1AttribName,"");
        }else if (selected_layout.equals("step_4")){
            /*if(is_l2_flag){
                refreshRegionView();
                getAttributeL2(personaId,personaName);
            }else {
                if (isFromHomeScreen){
                    getMetaDomain(personaId, personaName);
                    setExpertiseViewOnBack();
                }else {
                    setExpertiseViewOnBack();
                }
            }*/

            if(is_l2_flag){
                refreshRegionView();
                getAttributeL2(personaId,personaName);
            }else {
                if (isFromHomeScreen){
                    if (expertiseId.equals("")) {
                        getSearchMetaList(domainName);
                        edt_meta.setText(domainName);
                        setDomain(0, expertiseName);
                    } else {
                        getSearchMetaList(domainName);
                        edt_meta.setText(domainName);
                        setDomain(Integer.valueOf(expertiseId), expertiseName);
                    }
                }else {
                    if (expertiseId.equals("")) {
                        setDomain(0, expertiseName);
                    } else {
                        setDomain(Integer.valueOf(expertiseId), expertiseName);
                    }
                }
            }

        }else if (selected_layout.equals("step_5")){
            //refreshRegionView();
            //getMetaDomain(personaId,personaName);
            /*if (isFromHomeScreen){
                getMetaDomain(personaId, personaName);
                setExpertiseViewOnBack();
            }else {
                setExpertiseViewOnBack();
            }*/

            if (isFromHomeScreen){
                if (expertiseId.equals("")) {
                    getSearchMetaList(domainName);
                    edt_meta.setText(domainName);
                    setDomain(0, expertiseName);
                } else {
                    getSearchMetaList(domainName);
                    edt_meta.setText(domainName);
                    setDomain(Integer.valueOf(expertiseId), expertiseName);
                }
            }else {
                if (expertiseId.equals("")) {
                    setDomain(0, expertiseName);
                } else {
                    setDomain(Integer.valueOf(expertiseId), expertiseName);
                }
            }
        }else if (selected_layout.equals("step_6")){
            refreshRegionView();
            /*if (expertiseId.equals("")){
                setDomain(0,expertiseName);
            }else{
                setDomain(Integer.valueOf(expertiseId),expertiseName);
            }*/

            if (isFromHomeScreen){
                getMetaDomain(personaId, personaName);
                if (flag_expertise) {
                    setExpertiseViewOnBack();
                }
            }else {
                if (expertiseId.equals("")){
                    getMetaDomain(personaId, personaName);
                    if (flag_expertise) {
                        setExpertiseViewOnBack();
                    }
                } else {
                    setExpertiseViewOnBack();
                }
            }
        }else if (selected_layout.equals("step_7")){
            if (domainName.equals("")){
                setMetaSubDomain();
            }else {
                setMetaSubDomain();
            }
            slotsListPOJOS.clear();
            rv_time_slots.removeAllViewsInLayout();

        }else if (selected_layout.equals("step_8")){
            refreshRegionView();
            getPersona(l1AttribId,l1AttribName,"");
        }else if (selected_layout.equals("step_9")){
            refreshRegionView();
            getPersona(l1AttribId,l1AttribName,"");
        }else if (selected_layout.equals("step_10")){
            setVisibleCalenderSlotView();
        }
    }

    private void setExpertiseViewOnBack(){
        setIndicators(2);
        if (flag_expertise) {
            if (is_l2_flag) {
                selected_layout = "step_5";
            } else {
                selected_layout = "step_4";
            }
        }
        if (isFromHomeScreen) {
            expertiseId = "";
            expertiseName = "";
        }
        layout_meta.setVisibility(View.GONE);
        label_domain.setVisibility(View.GONE);
        label_expertise.setText(getResources().getText(R.string.tag_expertise) + " "+ personaName + "?");
        layout_expertise.setVisibility(View.VISIBLE);
        if (flag_expertise_optional){
            btn_expertise_skip.setVisibility(View.VISIBLE);
        }else {
            btn_expertise_skip.setVisibility(View.GONE);
        }
        rv_expertise.setVisibility(View.VISIBLE);
        label_expertise.setVisibility(View.VISIBLE);

        layout_region.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
        layout_summary.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
        cardview_meeting_preference.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        img_stamp.setVisibility(View.VISIBLE);
        layout_overview.setVisibility(View.GONE);

        label_noDomain.setVisibility(View.GONE);
        setMeta();
    }

    public void getMetaDomain(String persona_id, String persona_name) {
        // ddapter.re();omainA
        HashMap<String, Object> mr2Event = new HashMap<String, Object>();
        mr2Event.put("requested_persona", persona_name);
        cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Request_Step2, mr2Event);

        label_noMeta.setVisibility(View.GONE);
        layout_lexpertise.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.GONE);
        label_domain.setVisibility(View.GONE);
        layout_persona.setVisibility(View.GONE);
        txt_persona.setText(""+persona_name);
        layout_meta.setVisibility(View.GONE);
//        edt_domain.setText("");
//        edt_meta.setText("");
        expertiseId = "";
        expertiseName = "";
        sharedData.addIntData(SharedData.domainId, 0);
        sharedData.addIntData(SharedData.subDomainId, 0);
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        //l2AttribId = persona_id;
        //selected_layout = "step_6";
        //personaName = persona_name;
        /*if (metaListAdapter != null){
            metaListAdapter.clearData();
        }
        if (metaChildListAdapter != null){
            metaChildListAdapter.clearData();
        }*/
        cv_meta.setVisibility(View.GONE);
        Log.d(TAG, reasonId + "persono " + persona_id);

        Call<CommonMetaPOJO> call = apiInterface.getMetaList(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/get-meta-v2",loginPOJO.getActiveToken(), ""+loginPOJO.getEntityId(),
                loginPOJO.getEntityName(), ""+loginPOJO.getReqPersonaId(),
                loginPOJO.getReqPersonaName(), reasonId, personaId,l1AttribId,l2AttribId);
        call.enqueue(new Callback<CommonMetaPOJO>() {
            @Override
            public void onResponse(Call<CommonMetaPOJO> call, Response<CommonMetaPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonMetaPOJO reasonPOJO = response.body();
                    Log.e(TAG, "metaDominOnResponse "+new Gson().toJson(response.body()));
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMrParams().getReasonName());
                    if (reasonPOJO.getOK()) {
                    } else {
                    }

                    flag_domain_optional = reasonPOJO.getMrParams().getFlag_domain_optional();
                    //flag_domain_optional = true;
                    flag_expertise_optional = reasonPOJO.getMrParams().getFlag_expertise_optional();
                    flag_req_text = reasonPOJO.getMrParams().getFlag_req_text();
                    if (flag_req_text){
                        detail_editText.setVisibility(View.VISIBLE);
                        txt_details_160.setVisibility(View.VISIBLE);
                        //btn_next.setBackground(getResources().getDrawable(R.drawable.filled_circle_terracota));
                    }else {
                        detail_editText.setVisibility(View.GONE);
                        txt_details_160.setVisibility(View.GONE);
                        //btn_next.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                    }
                    helper_text = reasonPOJO.getMrParams().getHelper_text();
                    if (helper_text != null) {
                        if (helper_text.length() > 0){
                            detail_editText.setHint(helper_text);
                        }
                    }

                    if (reasonPOJO.getMrParams().getFlagDomain() && reasonPOJO.getMrParams().getFlagExpertise()){
                        //drawPageSelectionIndicators(2);
                        setIndicators(2);
                        if (is_l2_flag){
                            selected_layout = "step_5";
                        }else {
                            selected_layout = "step_4";
                        }
                        flag_expertise = true;
                        flag_domain = true;
                        layout_meta.setVisibility(View.GONE);
                        label_domain.setVisibility(View.GONE);
                        label_expertise.setText(getResources().getText(R.string.tag_expertise) + " "+ personaName + "?");
                        layout_expertise.setVisibility(View.VISIBLE);
                        if (flag_expertise_optional){
                            btn_expertise_skip.setVisibility(View.VISIBLE);
                        }else {
                            btn_expertise_skip.setVisibility(View.GONE);
                        }
                        rv_expertise.setVisibility(View.VISIBLE);
                        label_expertise.setVisibility(View.VISIBLE);
                        FlexboxLayoutManager manager = new FlexboxLayoutManager(getContext());
                        manager.setFlexWrap(FlexWrap.WRAP);
                        manager.setJustifyContent(JustifyContent.FLEX_START);
                        rv_expertise.setLayoutManager(manager );
                        if (reasonPOJO.getMrParams().getExpertiseList() != null){
                            try {
                                rv_expertise.setAdapter(new ExpertiseListAdapter(getActivity(),
                                        MeetingRequestFragment.this, (ArrayList<ExpertiseListPOJO>) reasonPOJO.getMrParams().getExpertiseList(),reasonPOJO.getMrParams().getFlagDomain()));
                            } catch (Exception e){
                                e.getMessage();
                            }
                        }

                        layout_region.setVisibility(View.GONE);
                        rv_region.setVisibility(View.GONE);
                        label_region.setVisibility(View.GONE);
                        layout_summary.setVisibility(View.GONE);
                        layout_meeting_preference.setVisibility(View.GONE);
                        cardview_meeting_preference.setVisibility(View.GONE);
                        cardview_time_slots.setVisibility(View.GONE);
                        layout_date_time_slots.setVisibility(View.GONE);
                        img_stamp.setVisibility(View.VISIBLE);
                        layout_overview.setVisibility(View.GONE);

                        label_noDomain.setVisibility(View.GONE);
                        setMeta();

                        return;
                    }


                    if (reasonPOJO.getMrParams().getFlagExpertise()){
                        //selected_layout = "step_6";
                        //drawPageSelectionIndicators(2);
                        setIndicators(2);
                        label_expertise.setText(getResources().getText(R.string.tag_expertise) + " "+ persona_name + "?");
                        layout_expertise.setVisibility(View.VISIBLE);
                        flag_expertise = true;
                        if (flag_expertise_optional){
                            btn_expertise_skip.setVisibility(View.VISIBLE);
                        }else {
                            btn_expertise_skip.setVisibility(View.GONE);
                        }
                        rv_expertise.setVisibility(View.VISIBLE);
                        label_expertise.setVisibility(View.VISIBLE);
                        FlexboxLayoutManager manager = new FlexboxLayoutManager(getContext());
                        manager.setFlexWrap(FlexWrap.WRAP);
                        manager.setJustifyContent(JustifyContent.FLEX_START);
                        rv_expertise.setLayoutManager(manager );
                        if (reasonPOJO.getMrParams().getExpertiseList() != null){
                            try {
                                rv_expertise.setAdapter(new ExpertiseListAdapter(getActivity(),
                                        MeetingRequestFragment.this, (ArrayList<ExpertiseListPOJO>) reasonPOJO.getMrParams().getExpertiseList(),false));
                            } catch (Exception e){
                                e.getMessage();
                            }
                        }

                        layout_region.setVisibility(View.GONE);
                        layout_summary.setVisibility(View.GONE);
                        rv_region.setVisibility(View.GONE);
                        label_region.setVisibility(View.GONE);
                        layout_meeting_preference.setVisibility(View.GONE);
                        cardview_meeting_preference.setVisibility(View.GONE);
                        cardview_time_slots.setVisibility(View.GONE);
                        layout_date_time_slots.setVisibility(View.GONE);
                        img_stamp.setVisibility(View.VISIBLE);
                        layout_overview.setVisibility(View.GONE);

                        setMeta();
                     return;

                    } else {
                        flag_expertise = false;
                        layout_expertise.setVisibility(View.GONE);
                        label_expertise.setVisibility(View.GONE);
                        layout_region.setVisibility(View.GONE);
                        layout_summary.setVisibility(View.GONE);
                        rv_region.setVisibility(View.GONE);
                        label_region.setVisibility(View.GONE);
                        layout_meeting_preference.setVisibility(View.GONE);
                        cardview_meeting_preference.setVisibility(View.GONE);
                        cardview_time_slots.setVisibility(View.GONE);
                        layout_date_time_slots.setVisibility(View.GONE);
                        img_stamp.setVisibility(View.VISIBLE);
                        layout_overview.setVisibility(View.GONE);

                        if (!selected_layout.equals("step_6") && !selected_layout.equals("step_9")) {
                            selected_layout = "step_5";
                            label_region.setText(getResources().getString(R.string.tag_region) + " " + personaName + " to be from?");
                            layout_region.setVisibility(View.VISIBLE);
                            layout_summary.setVisibility(View.VISIBLE);
                            rv_region.setVisibility(View.VISIBLE);
                            label_region.setVisibility(View.VISIBLE);
                            setSummaryDetails();
                        } else if (reasonPOJO.getMrParams().getFlagDomain()){
                            setIndicators(2);
                            selected_layout = "step_8";
                            label_domain.setText(getResources().getText(R.string.tag_domain) + " "+ persona_name + "?");
                            layout_domain.setVisibility(View.GONE);
                            rv_domain.setVisibility(View.GONE);
                            label_domain.setVisibility(View.VISIBLE);
                            layout_meta.setVisibility(View.VISIBLE);
                            flag_domain = true;
                            if (flag_domain_optional){
                                btn_skip.setVisibility(View.VISIBLE);
                            }else {
                                btn_skip.setVisibility(View.GONE);
                            }
                            rv_meta.setVisibility(View.VISIBLE);
                            rv_select_time_slots.setVisibility(View.GONE);
                            label_noMeta.setVisibility(View.GONE);
                            layout_lexpertise.setVisibility(View.GONE);
    //                        rv_domain.setLayoutManager(new LinearLayoutManager(getActivity()));
    //                        rv_domain.setAdapter(domainAdapter);
                            setMeta();
                            return;
                        }

                    }


                    if (reasonPOJO.getMrParams().getFlagDomain()){
                        flag_domain = true;
                        //drawPageSelectionIndicators(2);
                        /*setIndicators(2);
                        selected_layout = "step_8";
                        label_domain.setText(getResources().getText(R.string.tag_domain) + " "+ persona_name + "?");
                        layout_domain.setVisibility(View.GONE);
                        rv_domain.setVisibility(View.GONE);
                        label_domain.setVisibility(View.VISIBLE);
                        layout_meta.setVisibility(View.VISIBLE);
                        flag_domain = true;
                        if (flag_domain_optional){
                            btn_skip.setVisibility(View.VISIBLE);
                        }else {
                            btn_skip.setVisibility(View.GONE);
                        }
                        rv_meta.setVisibility(View.VISIBLE);
                        rv_select_time_slots.setVisibility(View.GONE);
                        label_noMeta.setVisibility(View.GONE);
                        layout_lexpertise.setVisibility(View.GONE);
//                        rv_domain.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        rv_domain.setAdapter(domainAdapter);
                        setMeta();
                        return;*/
                    } else {
                        flag_domain = false;
                        /*label_domain.setVisibility(View.GONE);
                        layout_domain.setVisibility(View.GONE);
                        layout_meta.setVisibility(View.GONE);
                        layout_lexpertise.setVisibility(View.GONE);*/
                    }


                    if (!reasonPOJO.getMrParams().getFlagDomain() && !reasonPOJO.getMrParams().getFlagExpertise()) {
                        //drawPageSelectionIndicators(4);
                        flag_domain = false;
                        flag_expertise = false;
                        setIndicators(4);
                        selected_layout = "step_9";
                        label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
                        layout_region.setVisibility(View.VISIBLE);
                        layout_summary.setVisibility(View.VISIBLE);
                        rv_region.setVisibility(View.VISIBLE);
                        label_region.setVisibility(View.VISIBLE);
                        layout_meeting_preference.setVisibility(View.GONE);
                        cardview_meeting_preference.setVisibility(View.GONE);
                        cardview_time_slots.setVisibility(View.GONE);
                        layout_date_time_slots.setVisibility(View.GONE);
                        img_stamp.setVisibility(View.VISIBLE);
                        layout_overview.setVisibility(View.GONE);
                        setSummaryDetails();
                    }

                    label_noDomain.setVisibility(View.GONE);
                    setMeta();

                }
            }
            @Override
            public void onFailure(Call<CommonMetaPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMeta() {

        Utility.hideKeyboard(getActivity());
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

        rv_reason.setVisibility(View.GONE);
        rv_persona.setVisibility(View.GONE);
        rv_attribL2.setVisibility(View.GONE);
        //rv_region.setVisibility(View.GONE);

        layout_subdomain.setVisibility(View.GONE);
        //  layout_meeting_preference.setVisibility(View.GONE);
        btn_meta.setVisibility(View.GONE);
        btn_skip.setVisibility(View.GONE);

    }

    public void setSummaryDetails(){
        showSnackbar();
        tag_summary_objective.setVisibility(View.VISIBLE);
        tag_summary_objective.setText(reasonName);
        if (!l1AttribName.equals("")) {
            tag_summary_l1attribute.setVisibility(View.VISIBLE);
            tag_summary_l1attribute.setText(l1AttribName);
        } else {
            tag_summary_l1attribute.setVisibility(View.GONE);
        }
        if (!personaName.equals("")) {
            tag_summary_persona.setVisibility(View.VISIBLE);
            tag_summary_persona.setText(personaName);
        }else {
            tag_summary_persona.setVisibility(View.GONE);
        }
        Glide.with(getActivity())
                .load(reasonImgUrl)
                .into(img_summary_objective);
        if (!domainName.equals("")){
            tag_summary_domain.setVisibility(View.VISIBLE);
            tag_summary_domain.setText(domainName);
        }else {
            tag_summary_domain.setVisibility(View.GONE);
        }
        if (!sdomainName.equals("")){
            tag_summary_subdomain.setVisibility(View.VISIBLE);
            tag_summary_subdomain.setText(sdomainName);
        }else {
            tag_summary_subdomain.setVisibility(View.GONE);
        }
        if (!expertiseName.equals("")){
            tag_summary_expertise.setVisibility(View.VISIBLE);
            tag_summary_expertise.setText(expertiseName);
        }else {
            tag_summary_expertise.setVisibility(View.GONE);
        }

        if (!flag_domain){
            tag_summary_domain.setVisibility(View.GONE);
            tag_summary_subdomain.setVisibility(View.GONE);
        }

        if (isFromHomeScreen) {
            if (flag_domain_optional) {
                selected_layout = "step_6";
            } else {
                if (subDomainId.equals("")) {
                    selected_layout = "step_9";
                } else {
                    if (!flag_domain && !flag_expertise){
                        selected_layout = "step_9";
                    } else {
                        selected_layout = "step_6";
                    }
                }
            }
        }
    }


    @OnClick({R.id.edit_summary, R.id.tag_summary_l1attribute, R.id.tag_summary_persona,
            R.id.tag_summary_l2attribute, R.id.tag_summary_expertise, R.id.tag_summary_domain,
            R.id.tag_summary_subdomain})
    public void onSummaryClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_summary:
                refreshRegionView();
                init();
                getReason();
                break;
            case R.id.tag_summary_l1attribute:
                refreshRegionView();
                get_l1_attrib(reasonId,reasonName, reasonImgUrl);
                break;
            case R.id.tag_summary_persona:
                refreshRegionView();
                getPersona(l1AttribId,l1AttribName,"");
                break;
            case R.id.tag_summary_l2attribute:
                if(is_l2_flag){
                    refreshRegionView();
                    getAttributeL2(personaId,personaName);
                }else {
//                    setExpertiseViewOnBack();
                    getMetaDomain(personaId, personaName);
                    setExpertiseViewOnBack();
                }
                break;
            case R.id.tag_summary_expertise:
//                setExpertiseViewOnBack();
                if (isFromHomeScreen) {
                    getMetaDomain(personaId, personaName);
                    setExpertiseViewOnBack();
                }else {
                    setExpertiseViewOnBack();
                }
                break;
            case R.id.tag_summary_domain:
                refreshRegionView();
                if (expertiseId.equals("")){
                    isDomainClickedFromSummary = true;
                    setDomain(0,expertiseName);
                    edt_meta.setText(domainName);
                    getSearchMetaList(domainName);
                }else{
                    isDomainClickedFromSummary = true;
                    setDomain(Integer.valueOf(expertiseId),expertiseName);
                    edt_meta.setText(domainName);
                    getSearchMetaList(domainName);
                }
                break;
            case R.id.tag_summary_subdomain:
                refreshRegionView();
                if (expertiseId.equals("")){
                    isDomainClickedFromSummary = true;
                    setDomain(0,expertiseName);
                    edt_meta.setText(domainName);
                    getSearchMetaList(domainName);
                }else{
                    isDomainClickedFromSummary = true;
                    setDomain(Integer.valueOf(expertiseId),expertiseName);
                    edt_meta.setText(domainName);
                    getSearchMetaList(domainName);
                }
                break;
        }

    }


    private void showSnackbar(){

        /*Toast toast = Toast.makeText(getActivity(), "You can click on any tags to change selection.", Toast.LENGTH_SHORT);

        View toastView = toast.getView(); // This'll return the default View of the Toast.

//         And now you can get the TextView of the default View of the Toast.
        TextView toastMessage = (TextView) toastView.findViewById(R.id.message);
        toastMessage.setTextSize(15);
        toastMessage.setTextColor(getResources().getColor(R.color.colorWhite));
        toastMessage.setGravity(Gravity.CENTER);
        toastView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();*/

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.customToastRoot));

        TextView text = (TextView) layout.findViewById(R.id.toast_textview);
        text.setText("You can click on any tag to change selection.");

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }


    public void setMeeting(Integer d_id, Integer s_id, String subDomainName) {
        Utility.hideKeyboard(getActivity());
        HashMap<String, Object> mr3Event = new HashMap<String, Object>();
        mr3Event.put("domain_requested", domainName);
        mr3Event.put("subdomain_requested", subDomainName);
        mr3Event.put("expertise_requested", "");
        cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Request_Step3, mr3Event);


        // scrollView.scrollTo(0,scrollView.getBottom());
//        label_region.setText(getResources().getString(R.string.tag_domain) + " "+ personaName+"?");
        label_domain.setText(getResources().getString(R.string.tag_domain) + " "+ personaName+"?");
        txt_sdomains.setText(""+subDomainName);
        layout_lsdomain.setVisibility(View.VISIBLE);
        subDomainId = ""+s_id;
        domainId = ""+d_id;
        sdomainName = subDomainName;
        //  txt_domains.setText(subDomainName);
        label_domain.setVisibility(View.VISIBLE);
        layout_domain.setVisibility(View.GONE);
        rv_persona.setVisibility(View.GONE);
        rv_attribL2.setVisibility(View.GONE);
        layout_ldomain.setVisibility(View.VISIBLE);
        layout_region.setVisibility(View.GONE);
        layout_summary.setVisibility(View.GONE);
        rv_region.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
        label_noDomain.setVisibility(View.GONE);
        btn_meta.setVisibility(View.VISIBLE);
        btn_skip.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
        cardview_meeting_preference.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        img_stamp.setVisibility(View.VISIBLE);
        layout_overview.setVisibility(View.GONE);

    }

    public void getRegion_Id(Integer countryId, String countryName) {
        Utility.hideKeyboard(getActivity());
        if (layout_meeting_preference.getVisibility() == View.GONE){
            //layout_meeting_preference.setVisibility(View.VISIBLE);
            // btn_next.setBackground(getResources().getDrawable(R.drawable.filled_circle_terracota));
        }
        if (detail_linear_layout.getVisibility() == View.GONE){
            detail_linear_layout.setVisibility(View.VISIBLE);
        }
        if (flag_req_text){}else {btn_next.setBackground(getResources().getDrawable(R.drawable.filled_circle_terracota));}
        regionId = "" + countryId;
        regionName = countryName;
    }

    @OnClick({R.id.btn_request_meeting, R.id.img_close, R.id.layout_lpersona,
            R.id.layout_lreason, R.id.layout_ldomain, R.id.layout_lexpertise,
            R.id.btn_meta, R.id.layout_lsdomain,R.id.img_back,R.id.btn_skip,R.id.btn_next,
            R.id.slot_btn_next,R.id.img_forward,R.id.img_backward,R.id.layout_circle
            ,R.id.img_info,R.id.btn_for_expertise,R.id.btn_expertise_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_request_meeting:
                getSubmitRequestMeeting();
                // successDialog();
                break;

            case R.id.img_close:
                meetingConfirmation();
                //onCancel();
                break;

            case R.id.img_back:

                setBackButtonEvent();

                break;

            case R.id.layout_lreason:
                init();
                getReason();
                break;

            case R.id.layout_lpersona:
                getPersona(reasonId,reasonName,"");
                break;

            case R.id.layout_ldomain:
                getMetaDomain(personaId, personaName);
                break;

            case R.id.layout_lexpertise:
                getMetaDomain(personaId, personaName);
                break;
            case R.id.btn_meta:
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_meta.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    // TODO: handle exception
                }
//                setMetaSubDomain();
                getMetaDomain("" +personaId, ""+personaName);
                break;
            case R.id.layout_lsdomain:
                getTagList(tagLists, Integer.parseInt(domainId), Integer.parseInt(subDomainId), domainName);
                break;

            case R.id.btn_next:
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(detail_editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                detail_editText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                setVisibleTimeSlotsView();
                break;
            case R.id.slot_btn_next:
                if (slotsListPOJOS != null){
                    if (slotsListPOJOS.size() > 3){
                        Toast.makeText(getActivity(), "Please select only 3 slots", Toast.LENGTH_SHORT).show();
                    }else {
                        if (slotsListPOJOS.size() == 3){
                            setNextButtonEvent();
                        }else {
                            Toast.makeText(getActivity(), "Please select 3 slots", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.img_forward:
                nextDate();
                break;
            case R.id.img_backward:
                previousDate();
                break;
            case R.id.layout_circle:
                meetingEditDate();
                break;
            case R.id.img_info:
                showCustomToast();
                break;
            case R.id.btn_for_expertise:
                if (expertiseId.equals("")){
                    Toast.makeText(getActivity(), "Please Select Expertise", Toast.LENGTH_SHORT).show();
                }else {
//                    setDomainOnclickNext();
                    setMetaSubDomain();
                }
                break;

            case R.id.btn_skip:
                try {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_meta.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                expertiseName = "";
                setMetaSubDomain();
                break;

            case R.id.btn_expertise_skip:
//                setDomainOnclickNext();
                expertiseId = "";
                expertiseName = "";
                setMetaSubDomain();
                break;
        }
    }

    private void showCustomToast() {
        try {
            txt_toaster.setVisibility(View.VISIBLE);
            TimeZone timeZone = TimeZone.getDefault();
            Log.d(TAG, "time zone "+ timeZone.getID());
            String  time_stamp = "Limit of 1 time per day. Times are based on the region you’ve selected. Time is displayed in "+timeZone.getDisplayName();

            txt_toaster.setText(time_stamp);

        } catch(Exception e){
            e.getMessage();
        }

        txt_toaster.postDelayed(new Runnable() {
            public void run() {
                txt_toaster.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void nextDate(){

        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = df.format(calendar.getTime());
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format.parse(selectedDate);

            SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
            String year1 = dfYear.format(date1);

            SimpleDateFormat dfMonth = new SimpleDateFormat("dd MMM");
            String month1 = dfMonth.format(date1.getTime());

            SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
            String day = dfDay.format(date1.getTime());

            txt_year.setText(""+year1);
            txt_month.setText(month1);
            txt_day.setText(day);

            String utc_date = Utility.convertLocaleToUtc(selectedDate);
            if (utc_date != null && utc_date.length() > 0){
                is_refresh_flag = true;
                get_meeting_time_slots(utc_date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void previousDate(){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date current_date = format.parse(currentDate);
            Date selected_date = format.parse(selectedDate);
            if (selected_date.before(current_date) || selected_date.equals(current_date)){
                //Toast.makeText(getActivity(), "You can't select past date.", Toast.LENGTH_SHORT).show();
            }else {
                calendar.add(Calendar.DATE, -1);
                //calendar.se.setMinDate(System.currentTimeMillis() - 1000);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate = df.format(calendar.getTime());

                SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
                String year = dfYear.format(calendar.getTime());

                SimpleDateFormat dfMM = new SimpleDateFormat("dd MMM");
                String month = dfMM.format(calendar.getTime());

                SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
                String day = dfDay.format(calendar.getTime());

                txt_year.setText(year);
                txt_month.setText(month);
                txt_day.setText(day);

                String utc_date = Utility.convertLocaleToUtc(selectedDate);
                if (utc_date != null && utc_date.length() > 0){
                    is_refresh_flag = true;
                    get_meeting_time_slots(utc_date);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void meetingEditDate()  {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.dialog_select_slot_date, null);

        ImageView img_close = view1.findViewById(R.id.img_close);
        Button btn_done = view1.findViewById(R.id.btn_done);
        CalendarView calender_view = view1.findViewById(R.id.calender);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,+1);

        calender_view.setMinDate(calendar.getTime().getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(selectedDate);
            calender_view.setDate(date.getTime(),true,true);


            try {
                SimpleDateFormat formatt = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = formatt.parse(selectedDate);
                calendar.setTime(date1);
                SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
                year1 = dfYear.format(date1);

                SimpleDateFormat dfMonth = new SimpleDateFormat("dd MMM");
                month1 = dfMonth.format(date1.getTime());

                SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
                day = dfDay.format(date1.getTime());

                    txt_year.setText(""+year1);
                    txt_month.setText(month1);
                    txt_day.setText(day);

                newDate = selectedDate;

            } catch (ParseException e) {
                e.printStackTrace();
            }

//            Date maxDate = format.parse("2021-12-15");
//            calender_view.setMaxDate(maxDate.getTime());


        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("NEW_DATE", selectedDate);


        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);


        /*final String year1, month1, day;*/
        calender_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                month = month + 1;
                // output to log cat **not sure how to format year to two places here**
                newDate = year+"-"+month+"-"+date;
                Log.d("NEW_DATE", newDate);
//                selectedDate = newDate;
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    date1 = format.parse(newDate);
//                    calendar.setTime(date1);
                    SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
                    year1 = dfYear.format(date1);

                    SimpleDateFormat dfMonth = new SimpleDateFormat("dd MMM");
                    month1 = dfMonth.format(date1.getTime());

                    SimpleDateFormat dfDay = new SimpleDateFormat("EEE");
                    day = dfDay.format(date1.getTime());

                    /*txt_year.setText(""+year1);
                    txt_month.setText(month1);
                    txt_day.setText(day);*/

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                is_refresh_flag = true;
                if (year1 != null && month1 != null && day != null && newDate!= null && date1!= null) {
                    selectedDate = newDate;
                    calendar.setTime(date1);
                    txt_year.setText("" + year1);
                    txt_month.setText(month1);
                    txt_day.setText(day);
                }
                String utc_date = Utility.convertLocaleToUtc(selectedDate);
                if (utc_date != null && utc_date.length() > 0){
                    get_meeting_time_slots(utc_date);
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogs.dismiss();
            }
        });
        dialogs.show();

    }

    private void setVisibleTimeSlotsView(){
        selected_layout = "step_7";
        if (flag_req_text){
            if (regionId.equals("")){
                Toast.makeText(getActivity(), "Please select region", Toast.LENGTH_SHORT).show();
            }else if(detail_editText.getText().toString().trim().equals("")){
                if (helper_text != null) {
                    Toast.makeText(getActivity(), "Please Enter" + " " + helper_text, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please Enter Additional Details", Toast.LENGTH_SHORT).show();
                }
            } else {
                str_detail = detail_editText.getText().toString().trim();
                layout_overview.setVisibility(View.GONE);
                layout_meeting_preference.setVisibility(View.GONE);
                cardview_meeting_preference.setVisibility(View.GONE);
                img_stamp.setVisibility(View.GONE);
                layout_region.setVisibility(View.GONE);
                layout_summary.setVisibility(View.GONE);
                label_region.setVisibility(View.GONE);
                cardview_time_slots.setVisibility(View.VISIBLE);
                layout_date_time_slots.setVisibility(View.VISIBLE);

                calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE,1);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDate = df.format(calendar.getTime());
                currentDate = df.format(calendar.getTime());

                SimpleDateFormat dfYear = new SimpleDateFormat("yyyy", Locale.getDefault());
                String year = dfYear.format(calendar.getTime());

                SimpleDateFormat dfMM = new SimpleDateFormat("dd MMM", Locale.getDefault());
                String month = dfMM.format(calendar.getTime());

                SimpleDateFormat dfDay = new SimpleDateFormat("EEE", Locale.getDefault());
                String day = dfDay.format(calendar.getTime());

                txt_year.setText(year);
                txt_month.setText(month);
                txt_day.setText(day);
                txtTimezone.setText("Times are displayed in\n ("+TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)+") "+TimeZone.getDefault().getDisplayName());

                is_refresh_flag = false;
                String utc_date = Utility.convertLocaleToUtc(selectedDate);
                if (utc_date != null && utc_date.length() > 0){
                    get_meeting_time_slots(utc_date);
                }
            }
        }else {
            if (regionId.equals("")){
                Toast.makeText(getActivity(), "Please select region", Toast.LENGTH_SHORT).show();
            } else {
                layout_overview.setVisibility(View.GONE);
                layout_meeting_preference.setVisibility(View.GONE);
                cardview_meeting_preference.setVisibility(View.GONE);
                img_stamp.setVisibility(View.GONE);
                layout_region.setVisibility(View.GONE);
                layout_summary.setVisibility(View.GONE);
                label_region.setVisibility(View.GONE);
                detail_editText_disabled.setVisibility(View.GONE);
                txt_additional_details.setVisibility(View.GONE);
                cardview_time_slots.setVisibility(View.VISIBLE);
                layout_date_time_slots.setVisibility(View.VISIBLE);
            }
        }
    }

    private void get_meeting_time_slots(String utc_date) {
        try {
            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonRequestTimeSlots> call = apiInterface.getTimeSlots(sharedData.getStringData(SharedData.API_URL) +
                    "api/MRCalls/get-time-slots", loginPOJO.getActiveToken(), loginPOJO.getEntityId(), Integer.parseInt(regionId), utc_date);
            call.enqueue(new Callback<CommonRequestTimeSlots>() {
                @Override
                public void onResponse(Call<CommonRequestTimeSlots> call, Response<CommonRequestTimeSlots> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonRequestTimeSlots pojo = response.body();
                        progressHUD.dismiss();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {
                                if (pojo.getSlotsListPOJOS().size() > 0 ){
                                    rv_select_time_slots.setVisibility(View.VISIBLE);
                                    rv_select_time_slots.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                                    //setSlotRecycleView((ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>) pojo.getSlotsListPOJOS());
                                    meetingRequestTimeSlotAdapter = new MeetingRequestTimeSlotAdapter(getActivity(), MeetingRequestFragment.this,
                                            (ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>) pojo.getSlotsListPOJOS(),selectedDate);
                                    rv_select_time_slots.setAdapter(meetingRequestTimeSlotAdapter);
                                    meetingRequestTimeSlotAdapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(getContext(), ""+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        progressHUD.dismiss();
                        Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<CommonRequestTimeSlots> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    public void setSelectedTimeSlot(CommonRequestTimeSlots.EntitySlotsListPOJO slot,String time){
        selectItem = new ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>();
        if (slot != null){
            if (slotsListPOJOS.size() < 3){
                if (slotsListPOJOS.size()>0) {
                    for (int i = 0; i < slotsListPOJOS.size(); i++) {
                        if (slotsListPOJOS.get(i).getFor_date().contains(slot.getFor_date())) {
                            slotsListPOJOS.remove(i);
                        }
                    }
                    slotsListPOJOS.add(slot);
                } else slotsListPOJOS.add(slot);
            }else {
                for (int i = 0; i < slotsListPOJOS.size(); i++) {
                    if (slotsListPOJOS.get(i).getFor_date().contains(slot.getFor_date())) {
                        slotsListPOJOS.remove(i);
                    }
                }
                if (slotsListPOJOS.size() < 3) {
                    slotsListPOJOS.add(slot);
                }
            }


            int j=0;
            for(int i =0; i < slotsListPOJOS.size(); i++) {
                if (slot.getFor_date().equals(slotsListPOJOS.get(i).getFor_date())) {
                    j++;
                }
            }
            if (j==0 && slotsListPOJOS.size()>=3) {
                Toast.makeText(getActivity(), getActivity().getResources().getText(R.string.tag_slot_limit_reached), Toast.LENGTH_SHORT).show();
            }


            if (slotsListPOJOS.size() > 0){
                if (slotsListPOJOS.size() < 4){
                    if (slotsListPOJOS.size() == 3){
                        slot_btn_next.setBackground(getResources().getDrawable(R.drawable.filled_circle_terracota));
                    }
                    rv_time_slots.setVisibility(View.VISIBLE);
                    rv_time_slots.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    SelectedSlotsAdapter meetingRequestTimeSlotAdapter = new SelectedSlotsAdapter(getActivity(), MeetingRequestFragment.this,
                            (ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>) slotsListPOJOS);
                    rv_time_slots.setAdapter(meetingRequestTimeSlotAdapter);
                    meetingRequestTimeSlotAdapter.notifyDataSetChanged();

                }else {
                    //slot_btn_next.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                }
            }else {
                slot_btn_next.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
            }
        }
    }

    public void getSelectedTimeSlot(CommonRequestTimeSlots.EntitySlotsListPOJO slot, int pos){
        if (slot != null){

        }
    }

    public void removeSelectedTimeSlot(int pos){
        if (slotsListPOJOS.size() > 0){
            slotsListPOJOS.remove(pos);
            if (slotsListPOJOS.size() < 3){
                slot_btn_next.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
            }
        }
    }

    private void setNextButtonEvent(){
        selected_layout = "step_10";

        rv_slots.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv_slots.setAdapter(new SelectedSlotsOverviewAdapter(getActivity(), MeetingRequestFragment.this,
                (ArrayList<CommonRequestTimeSlots.EntitySlotsListPOJO>) slotsListPOJOS));

        str_detail = detail_editText.getText().toString().trim();
        layout_overview.setVisibility(View.VISIBLE);
        if (!hexCode.equals("")){
            overview_card.setCardBackgroundColor(Color.parseColor(hexCode));
        }
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.VISIBLE);
        cardview_meeting_preference.setVisibility(View.VISIBLE);
        img_stamp.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        layout_summary.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
        text_l1_attribute.setText(reasonName + " - "+ l1AttribName);
        txt_with_persona.setText("With a" + " " + personaName);
        layout_domain_sub_domain.setVisibility(View.VISIBLE);
        layout_sub_domain.setVisibility(View.VISIBLE);
        layout_expetise.setVisibility(View.VISIBLE);


        if (!domainName.equals("")){
            txt_domain_sub_domain.setText(domainName);
        }else {
            layout_domain_sub_domain.setVisibility(View.GONE);
        }

        if (!sdomainName.equals("")){
            txt_sub_domain.setText(sdomainName);
        }else {
            layout_sub_domain.setVisibility(View.GONE);
        }

        if (!expertiseName.equals("")){
            txt_expetise.setText(expertiseName);
        }else {
            layout_expetise.setVisibility(View.GONE);
        }

        //new design
        tag_overview_objective.setVisibility(View.VISIBLE);
        tag_overview_objective.setText(reasonName);
        if (!l1AttribName.equals("")) {
            tag_overview_l1attribute.setVisibility(View.VISIBLE);
            tag_overview_l1attribute.setText(l1AttribName);
        }else {
            tag_overview_l1attribute.setVisibility(View.GONE);
        }
        if (!personaName.equals("")) {
            tag_overview_persona.setVisibility(View.VISIBLE);
            tag_overview_persona.setText(personaName);
        }else {
            tag_overview_persona.setVisibility(View.GONE);
        }
        Glide.with(getActivity())
                .load(reasonImgUrl)
                .into(img_overview_objective);
        if (!domainName.equals("")){
            tag_overview_domain.setVisibility(View.VISIBLE);
            tag_overview_domain.setText(domainName);
        }else {
            tag_overview_domain.setVisibility(View.GONE);
        }
        if (!sdomainName.equals("")){
            tag_overview_subdomain.setVisibility(View.VISIBLE);
            tag_overview_subdomain.setText(sdomainName);
        }else {
            tag_overview_subdomain.setVisibility(View.GONE);
        }
        if (!expertiseName.equals("")){
            tag_overview_expertise.setVisibility(View.VISIBLE);
            tag_overview_expertise.setText(expertiseName);
        }else {
            tag_overview_expertise.setVisibility(View.GONE);
        }
        if (!flag_domain){
            tag_overview_domain.setVisibility(View.GONE);
            tag_overview_subdomain.setVisibility(View.GONE);
        }

        tag_overview_region.setVisibility(View.VISIBLE);
        tag_overview_region.setText(regionName);


        txt_from_region.setText("From " + regionName);
        detail_editText_disabled.setText(detail_editText.getText().toString());
    }

    private void refreshRegionView(){
        detail_editText_disabled.setText("");
       // detail_editText.setText("");
//        sdomainName = "";
//        domainName = "";
//        expertiseName = "";
        txt_domain_sub_domain.setVisibility(View.VISIBLE);
        detail_linear_layout.setVisibility(View.GONE);
        detail_editText.setText("");
        rv_region.setAdapter(new RegionAdapter(getActivity(), MeetingRequestFragment.this, region_array));
    }

    public void setVisibleCalenderSlotView(){
        selected_layout = "step_7";
        layout_overview.setVisibility(View.GONE);
        layout_meeting_preference.setVisibility(View.GONE);
        cardview_meeting_preference.setVisibility(View.GONE);
        img_stamp.setVisibility(View.GONE);
        layout_region.setVisibility(View.GONE);
        label_region.setVisibility(View.GONE);
        layout_summary.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.VISIBLE);
        layout_date_time_slots.setVisibility(View.VISIBLE);
    }

    private void setMetaSubDomain() {
        Utility.hideKeyboard(getActivity());
        if (flag_domain_optional){
            selected_layout = "step_6";
        }else {
            if (subDomainId.equals("")){selected_layout = "step_9";}else{selected_layout = "step_6";}
        }

        label_region.setText(getResources().getString(R.string.tag_region) + " "+ personaName+" to be from?");
        label_domain.setVisibility(View.GONE);
        setIndicators(4);
        label_expertise.setVisibility(View.GONE);
        layout_expertise.setVisibility(View.GONE);
        layout_region.setVisibility(View.VISIBLE);
        layout_summary.setVisibility(View.VISIBLE);
        rv_region.setVisibility(View.VISIBLE);
        layout_meeting_preference.setVisibility(View.GONE);
        cardview_meeting_preference.setVisibility(View.GONE);
        cardview_time_slots.setVisibility(View.GONE);
        layout_date_time_slots.setVisibility(View.GONE);
        img_stamp.setVisibility(View.VISIBLE);
        layout_overview.setVisibility(View.GONE);
        layout_lexpertise.setVisibility(View.GONE);
        label_region.setVisibility(View.VISIBLE);
        cv_meta.setVisibility(View.GONE);
        rv_meta.setVisibility(View.GONE);
        rv_metaChild.setVisibility(View.VISIBLE);
        btn_meta.setVisibility(View.GONE);
        btn_skip.setVisibility(View.GONE);
        //edt_meta.setText("");
        layout_meta.setVisibility(View.GONE);
        setSummaryDetails();
    }

    public void getTagList(List<MetaListPOJO.Child> children, Integer d_id, Integer s_id, String tagName) {
        HashMap<String, Object> mr3Event = new HashMap<String, Object>();
        mr3Event.put("domain_requested", tagName);
        mr3Event.put("subdomain_requested", tagName);
        mr3Event.put("expertise_requested", "");
        cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Request_Step3, mr3Event);

        layout_lsdomain.setVisibility(View.GONE);
        tagLists.clear();
        //rv_meta.setVisibility(View.GONE);
        if (children == null){
            Utility.hideKeyboard(getActivity());
            // scrollView.scrollTo(0,scrollView.getBottom());
//            label_region.setText(getResources().getString(R.string.tag_domain) + " "+ personaName+"?");
            label_domain.setText(getResources().getString(R.string.tag_domain) + " "+ personaName+"?");
            subDomainId = ""+s_id;
            domainId = ""+d_id;
            domainName = tagName;
            sdomainName = "";
            txt_domains.setText(tagName);
            flag_domain = true;
            label_domain.setVisibility(View.VISIBLE);
            layout_domain.setVisibility(View.GONE);
            rv_persona.setVisibility(View.GONE);
            rv_attribL2.setVisibility(View.GONE);
            layout_ldomain.setVisibility(View.VISIBLE);
            layout_region.setVisibility(View.GONE);
            rv_region.setVisibility(View.GONE);
            layout_summary.setVisibility(View.GONE);
            label_region.setVisibility(View.GONE);
            label_noDomain.setVisibility(View.GONE);
            layout_meeting_preference.setVisibility(View.GONE);
            cardview_meeting_preference.setVisibility(View.GONE);
            cardview_time_slots.setVisibility(View.GONE);
            layout_date_time_slots.setVisibility(View.GONE);
            img_stamp.setVisibility(View.VISIBLE);
            layout_overview.setVisibility(View.GONE);
            cv_meta.setVisibility(View.GONE);
            btn_meta.setVisibility(View.VISIBLE);
            btn_skip.setVisibility(View.GONE);

        } else if (children.size() == 0){
            Utility.hideKeyboard(getActivity());
            // scrollView.scrollTo(0,scrollView.getBottom());
//            label_region.setText(getResources().getString(R.string.tag_domain) + " "+ personaName+"?");
            label_domain.setText(getResources().getString(R.string.tag_domain) + " "+ personaName+"?");
            subDomainId = ""+s_id;
            domainId = ""+d_id;
            domainName = tagName;
            sdomainName = "";
            txt_domains.setText(tagName);
            flag_domain = true;
            label_domain.setVisibility(View.VISIBLE);
            layout_domain.setVisibility(View.GONE);
            rv_persona.setVisibility(View.GONE);
            rv_attribL2.setVisibility(View.GONE);
            layout_ldomain.setVisibility(View.VISIBLE);
            layout_region.setVisibility(View.GONE);
            rv_region.setVisibility(View.GONE);
            layout_summary.setVisibility(View.GONE);
            label_region.setVisibility(View.GONE);
            label_noDomain.setVisibility(View.GONE);
            layout_meeting_preference.setVisibility(View.GONE);
            cardview_meeting_preference.setVisibility(View.GONE);
            layout_overview.setVisibility(View.GONE);
            cv_meta.setVisibility(View.GONE);
            btn_meta.setVisibility(View.VISIBLE);
        } else {
            Utility.hideKeyboard(getActivity());
            // scrollView.scrollTo(0,scrollView.getBottom());
//            label_region.setText(getResources().getString(R.string.tag_domain) + " "+ personaName+"?");
            label_domain.setText(getResources().getString(R.string.tag_domain) + " "+ personaName+"?");
            subDomainId = ""+s_id;
            domainId = ""+d_id;
            domainName = tagName;
            sdomainName = "";
            txt_domains.setText(tagName);
            flag_domain = true;
            label_domain.setVisibility(View.VISIBLE);
            layout_domain.setVisibility(View.GONE);
            rv_persona.setVisibility(View.GONE);
            rv_attribL2.setVisibility(View.GONE);
            layout_ldomain.setVisibility(View.VISIBLE);
            layout_region.setVisibility(View.GONE);
            rv_region.setVisibility(View.GONE);
            layout_summary.setVisibility(View.GONE);
            label_region.setVisibility(View.GONE);
            label_noDomain.setVisibility(View.GONE);
            layout_meeting_preference.setVisibility(View.GONE);
            cardview_meeting_preference.setVisibility(View.GONE);
            cardview_time_slots.setVisibility(View.GONE);
            layout_date_time_slots.setVisibility(View.GONE);
            img_stamp.setVisibility(View.VISIBLE);
            layout_overview.setVisibility(View.GONE);
            cv_meta.setVisibility(View.VISIBLE);
            rv_metaChild.setVisibility(View.VISIBLE);
            btn_meta.setVisibility(View.VISIBLE);
            btn_skip.setVisibility(View.GONE);

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
        if (isDomainClickedFromSummary){
            isDomainClickedFromSummary = false;
            label_domain.setVisibility(View.VISIBLE);
            label_domain.setText(getResources().getText(R.string.tag_domain) + " "+ personaName + "?");
        }
    }

    private void getSubmitRequestMeeting(){

        MeetingSlotBodyPOJO expertiseBodyPOJO  = new MeetingSlotBodyPOJO();
        expertiseBodyPOJO.setRequestor_id(loginPOJO.getEntityId());
        expertiseBodyPOJO.setReason_id(Integer.parseInt(reasonId));
        expertiseBodyPOJO.setGiver_persona_id(personaId);
        if (flag_domain) {
            expertiseBodyPOJO.setSel_domain_id(domainId);
            expertiseBodyPOJO.setSel_sub_domain_id(subDomainId);
        }
        expertiseBodyPOJO.setSel_expertise_id(expertiseId);
        expertiseBodyPOJO.setSel_country_id(regionId);
        expertiseBodyPOJO.setL2_attrib_id(l2AttribId);
        expertiseBodyPOJO.setL1_attrib_id(l1AttribId);
        expertiseBodyPOJO.setReq_text(str_detail);
        expertiseBodyPOJO.setSlotsListPOJOS(slotsListPOJOS);

        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonMatchingPOJO> call = apiInterface.getSaveMeetingRequest(sharedData.getStringData(SharedData.API_URL) + "api/MRCalls/save-meeting-request",
                loginPOJO.getActiveToken(),
                expertiseBodyPOJO);
        call.enqueue(new Callback<CommonMatchingPOJO>() {
            @Override
            public void onResponse(Call<CommonMatchingPOJO> call, Response<CommonMatchingPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    //CommonPOJO reasonPOJO = response.body();
                    CommonMatchingPOJO matchingPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+matchingPOJO.getMessage());
                    if (matchingPOJO.getOK()) {
                        HashMap<String, Object> meeting_event = new HashMap<String, Object>();
                        meeting_event.put("meeting_request_id", matchingPOJO.getMr_id());
                        meeting_event.put("meeting_objective", reasonName);
                        meeting_event.put("requested_persona", personaName);
                        meeting_event.put("domain_requested", domainName);
                        meeting_event.put("subdomain_requested", sdomainName);
                        meeting_event.put("expertise_requested", expertiseName);
                        meeting_event.put("location_requested", regionName);
                        cleverTap.pushEvent(Utility.CLAVER_TAB_Meeting_Requested,meeting_event);

                        successDialog(response.body());
                        //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), ""+matchingPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonMatchingPOJO> call, Throwable t) {
                progressHUD.dismiss();
                //Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void successDialog(CommonMatchingPOJO response) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.SheetDialog);
//        LayoutInflater layoutInflater = this.getLayoutInflater();
//        final View view1 = layoutInflater.inflate(R.layout.dialog_request_meeting_success, null);
//        TextView label_close = view1.findViewById(R.id.label_close);
//        TextView label_title = view1.findViewById(R.id.label_title);
//
//        label_title.setText(Html.fromHtml("<b>Hey "+ loginPOJO.getFirstName() + ",</b> " + getResources().getString(R.string.label_success_meeting) + " "
//                + personaName + ".<br> <br /> "+ getResources().getString(R.string.label_success_meeting1) + "<b>  48 hrs. </b>"));
//        //    tv_msg.setText("Session Added Successfully.");
//        builder.setView(view1);
//        final AlertDialog dialogs = builder.create();
//        dialogs.setCancelable(false);
//        label_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sharedData.addBooleanData(SharedData.MEETING_BOOKED, true);
//                EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_BOOK));
//                dialogs.dismiss();
//                dismiss();
//                MatchingIntroFragment addPhotoBottomDialogFragment =
//                        (MatchingIntroFragment) MatchingIntroFragment.newInstance(response);
//                addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
//                        "MeetingRequestNewFragment");
//            }
//        });
//        dialogs.show();

        if (response != null){
            sharedData.addBooleanData(SharedData.MEETING_BOOKED, true);
            EventBus.getDefault().post(new EventBusPOJO(Utility.MEETING_BOOK));
            dismiss();
            MatchingIntroFragment addPhotoBottomDialogFragment =
                    (MatchingIntroFragment) MatchingIntroFragment.newInstance(response);
            addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                    "MeetingRequestNewFragment");
        }else {
            dismiss();
        }
    }

    public void setIndicators(int pos){
        if (pos == -1){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.item_unselected));
            linearLayout1.setBackground(getResources().getDrawable(R.drawable.item_unselected));
            linearLayout2.setBackground(getResources().getDrawable(R.drawable.item_unselected));
            linearLayout3.setBackground(getResources().getDrawable(R.drawable.item_unselected));
            linearLayout4.setBackground(getResources().getDrawable(R.drawable.item_unselected));
        }else if (pos == 0){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout1.setBackground(getResources().getDrawable(R.drawable.item_unselected));
            linearLayout2.setBackground(getResources().getDrawable(R.drawable.item_unselected));
            linearLayout3.setBackground(getResources().getDrawable(R.drawable.item_unselected));
            linearLayout4.setBackground(getResources().getDrawable(R.drawable.item_unselected));
        }else if (pos == 1){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout1.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout2.setBackground(getResources().getDrawable(R.drawable.item_unselected));
            linearLayout3.setBackground(getResources().getDrawable(R.drawable.item_unselected));
            linearLayout4.setBackground(getResources().getDrawable(R.drawable.item_unselected));
        }else if (pos == 2){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout1.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout2.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout3.setBackground(getResources().getDrawable(R.drawable.item_unselected));
            linearLayout4.setBackground(getResources().getDrawable(R.drawable.item_unselected));
        }else if (pos == 3){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout1.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout2.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout3.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout4.setBackground(getResources().getDrawable(R.drawable.item_unselected));
        }else if (pos == 4){
            linearLayout.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout1.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout2.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout3.setBackground(getResources().getDrawable(R.drawable.item_selected));
            linearLayout4.setBackground(getResources().getDrawable(R.drawable.item_selected));
        }

    }

    public void meetingConfirmation(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SheetDialog);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View view1 = layoutInflater.inflate(R.layout.meeting_req_close_pop_up, null);

        Button accept = view1.findViewById(R.id.btn_yes);
        Button decline = view1.findViewById(R.id.btn_no);


        builder.setView(view1);
        final AlertDialog dialogs = builder.create();
        dialogs.setCancelable(false);

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
                dismiss();

            }
        });
        dialogs.show();
    }

}