package com.thriive.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.utilities.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity {
    @BindView(R.id.txt_name)
    TextView txt_name;

    private String fName, lName;
    private LoginPOJO loginPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        loginPOJO = Utility.getLoginData(getApplicationContext());

        fName = loginPOJO.getReturnEntity().getFirstName();
        lName = loginPOJO.getReturnEntity().getLastName();
        txt_name.setText(fName + " "+ lName);
    }

    @OnClick({R.id.img_edit_profile, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_edit_profile:
                //();
                editProfile();
                break;

            case R.id.img_close:
                finish();
                break;

        }
    }

    public void editProfile() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
        BottomSheetDialog dialog = new BottomSheetDialog(EditProfileActivity.this,R.style.SheetDialog);

        EditText edt_lName = dialogView.findViewById(R.id.edt_lName);
        EditText edt_fName = dialogView.findViewById(R.id.edt_fName);
        ImageView img_close = dialogView.findViewById(R.id.img_close);

        edt_fName.setText(fName);
        edt_lName.setText(lName);

        dialog.setContentView(dialogView);
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