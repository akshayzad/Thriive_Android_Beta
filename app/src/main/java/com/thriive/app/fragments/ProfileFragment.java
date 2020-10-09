package com.thriive.app.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.EditProfileActivity;
import com.thriive.app.HomeActivity;
import com.thriive.app.LoginActivity;
import com.thriive.app.MeetingsHistoryActivity;
import com.thriive.app.PreferencesActivity;
import com.thriive.app.ProfileActivity;
import com.thriive.app.R;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
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

        txt_name.setText(loginPOJO.getFirstName() + " " + loginPOJO.getLastName());
        txt_profession.setText(loginPOJO.getDesignationName());
        if (loginPOJO.getPicUrl().equals("")){
            Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.roboto_medium);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(getResources().getColor( R.color.terracota))
                    .useFont(typeface)
                    .fontSize(70) /* size in px */
                    .bold()
                    .toUpperCase()
                    .width(120)  // width in px
                    .height(120) // height in px
                    .endConfig()
                    .buildRect(""+loginPOJO.getFirstName().charAt(0) ,getResources().getColor( R.color.pale48));
            img_profile.setImageDrawable(drawable);
        } else {
            img_profile.setMinimumWidth(60);
            img_profile.setMaxHeight(60);
            img_profile.setMinimumHeight(60);
            img_profile.setMaxWidth(60);
            Glide.with(getActivity())
                    .load(loginPOJO.getPicUrl())
                    .into(img_profile);
        }

        return  view;
    }

    @OnClick({R.id.profile, R.id.preferences, R.id.history, R.id.txt_logout})
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
        }
    }

    public void logoutApp() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_logout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(),R.style.SheetDialog);


        ImageView img_close = dialogView.findViewById(R.id.img_close);
        Button btn_yes = dialogView.findViewById(R.id.btn_yes);
        Button btn_no = dialogView.findViewById(R.id.btn_no);
        dialog.setContentView(dialogView);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                ((HomeActivity)getActivity()).getLogoutApp();
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

}