package com.thriive.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
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


       //. final View view1 = layoutInflater.inflate(R.layout.dialog_edit_profile, null);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
        BottomSheetDialog dialog = new BottomSheetDialog(EditProfileActivity.this,R.style.SheetDialog);



        ImageView img_close = dialogView.findViewById(R.id.img_close);
        //    tv_msg.setText("Session Added Successfully.");
        dialog.setContentView(dialogView);
       // dialog.show();
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