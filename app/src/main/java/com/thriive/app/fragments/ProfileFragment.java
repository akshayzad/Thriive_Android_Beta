package com.thriive.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.CommonWebviewActivity;
import com.thriive.app.EditProfileActivity;
import com.thriive.app.HomeActivity;
import com.thriive.app.LoginActivity;
import com.thriive.app.MeetingsHistoryActivity;
import com.thriive.app.PreferencesActivity;
import com.thriive.app.ProfileActivity;
import com.thriive.app.R;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {


    private LoginPOJO.ReturnEntity loginPOJO;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_profession)
    TextView txt_profession;
    @BindView(R.id.img_profile)
    CircleImageView img_profile;

    private APIInterface apiInterface;
    private KProgressHUD progressHUD;
    Unbinder unbinder;

    private static final String TAG = ProfileActivity.class.getName();
    private SharedData sharedData;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        loginPOJO = Utility.getLoginData(getContext());
        apiInterface = APIClient.getApiInterface();

        sharedData = new SharedData(getContext());

//        txt_name.setText(loginPOJO.getFirstName() + " " + loginPOJO.getLastName());
//        txt_profession.setText(loginPOJO.getDesignationName());
//        if (loginPOJO.getPicUrl().equals("")){
//            Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.roboto_medium);
//            TextDrawable drawable = TextDrawable.builder()
//                    .beginConfig()
//                    .textColor(getResources().getColor( R.color.terracota))
//                    .useFont(typeface)
//                    .fontSize(70) /* size in px */
//                    .bold()
//                    .toUpperCase()
//                    .width(120)  // width in px
//                    .height(120) // height in px
//                    .endConfig()
//                    .buildRect(""+loginPOJO.getFirstName().charAt(0) ,getResources().getColor( R.color.pale48));
//            img_profile.setImageDrawable(drawable);
//        } else {
//            img_profile.setMinimumWidth(60);
//            img_profile.setMaxHeight(60);
//            img_profile.setMinimumHeight(60);
//            img_profile.setMaxWidth(60);
//            Glide.with(getActivity())
//                    .load(loginPOJO.getPicUrl())
//                    .into(img_profile);
//        }

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loginPOJO = Utility.getLoginData(getActivity());
        txt_name.setText(loginPOJO.getFirstName() + " " + loginPOJO.getLastName());
        txt_profession.setText(loginPOJO.getDesignationName());

        if (loginPOJO.getPicUrl().equals("")){
            Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.roboto_medium);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(getResources().getColor( R.color.terracota))
                    .useFont(typeface)
                    .fontSize(40) /* size in px */
                    .bold()
                    .toUpperCase()
                    .width(120)  // width in px
                    .height(120) // height in px
                    .endConfig()
                    .buildRect(Utility.getInitialsName(loginPOJO.getEntityName()) ,
                            getResources().getColor( R.color.pale48));

            img_profile.setImageDrawable(drawable);
        } else {
            img_profile.setMinimumWidth(60);
            img_profile.setMaxHeight(60);
            img_profile.setMinimumHeight(60);
            img_profile.setMaxWidth(60);
            Glide.with(this)
                    .load(loginPOJO.getPicUrl())
                    .into(img_profile);
        }

    }
    @OnClick({R.id.profile, R.id.preferences, R.id.history, R.id.txt_logout,R.id.service, R.id.policy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile:
                //();
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.preferences:

                Intent intent1 = new Intent(getContext(), PreferencesActivity.class);
                startActivity(intent1);
                break;

            case R.id.history:

                Intent intent2 = new Intent(getContext(), MeetingsHistoryActivity.class);
                startActivity(intent2);
                //  signInWithLinkLined();
                break;

            case R.id.txt_logout:
                logoutApp();
                break;

            case R.id.service:
                Intent intent3 = new Intent(getContext(), CommonWebviewActivity.class);
                intent3.putExtra("intent_type", Utility.TERMS);
                startActivity(intent3);
                break;

            case R.id.policy:
                Intent intent4 = new Intent(getContext(), CommonWebviewActivity.class);
                intent4.putExtra("intent_type", Utility.PRIVACY_POLICY);
                startActivity(intent4);
                break;

        }
    }

    public void logoutApp() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(),R.style.SheetDialog);


        ImageView img_close = dialogView.findViewById(R.id.img_close);
        Button btn_yes = dialogView.findViewById(R.id.btn_yes);
        Button btn_no = dialogView.findViewById(R.id.btn_no);
        dialog.setContentView(dialogView);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getLogoutApp();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //   ratingDialog();
            }
        });
        dialog.show();
    }

    private void getLogoutApp() {
        try {
            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getLogout(sharedData.getStringData(SharedData.API_URL) +
                    "api/AppLogin/logout", loginPOJO.getActiveToken(), loginPOJO.getPrimaryLoginKey());
            call.enqueue(new Callback<CommonPOJO>() {
                @Override
                public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonPOJO reasonPOJO = response.body();
                        //   progressHUD.dismiss();
                        try {
                            if (reasonPOJO != null){
                                Log.d(TAG,""+reasonPOJO.getMessage());
                                if (reasonPOJO.getOK()) {
                                    sharedData.addBooleanData(SharedData.isFirstVisit, false);
                                    sharedData.addBooleanData(SharedData.isLogged, false);
                                    sharedData.clearPref(getContext());
                                    Utility.clearLogin(getContext());
                                    Utility.clearMeetingDetails(getContext());
                                    ((HomeActivity)getActivity()).getLogoutApp();
//                                    Handler mHandler = new Handler();
//                                    mHandler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            progressHUD.dismiss();
//                                            //  Toast.makeText(getContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(getContext(), LoginActivity.class);
//                                            startActivity(intent);
//
//
//                                        }
//                                    }, 2000);
                                } else {
                                    Toast.makeText(getContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (Exception e){
                            e.getMessage();
                        }

                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    // Toast.makeText(ProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }

    }

}