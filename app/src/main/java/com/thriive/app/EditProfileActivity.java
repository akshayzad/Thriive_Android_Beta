package com.thriive.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.adapters.CountryListAdapter;
import com.thriive.app.adapters.RegionAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.fragments.MeetingRequestFragment;
import com.thriive.app.models.CommonCountryPOJO;
import com.thriive.app.models.CommonEntityImagePOJO;
import com.thriive.app.models.CommonEntityPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.CountryListPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.SiliCompressor;
import com.thriive.app.utilities.Utility;

import com.thriive.app.utilities.Validation;
import com.thriive.app.utilities.image_cropper.CropImage;
import com.thriive.app.utilities.progressdialog.KProgressHUD;
import com.thriive.app.utilities.textdrawable.TextDrawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSIONS_REQUEST =  100;
    private static final int GALLERY_PERMISSIONS_REQUEST = 200;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.img_profile)
    CircleImageView img_profile;
    @BindView(R.id.txt_profession)
    TextView txt_profession;
    @BindView(R.id.txt_email)
    TextView txt_email;
    @BindView(R.id.txt_gender)
    TextView txt_gender;
    @BindView(R.id.img_rateApp)
    ImageView img_rateApp;
    @BindView(R.id.txt_phoneNumber)
    TextView txt_phoneNumber;
    @BindView(R.id.txt_password)
    TextView txt_password;
    @BindView(R.id.txt_region)
    TextView txt_region;
    @BindView(R.id.rv_tags)
    RecyclerView rv_tags;

    private SharedData sharedData;

    private String fName = "", lName = "";
    private LoginPOJO.ReturnEntity loginPOJO;

    private KProgressHUD progressHUD;
    private APIInterface apiInterface;

    public static final String TAG = EditProfileActivity.class.getName();
    private Uri cameraUri = null;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        sharedData = new SharedData(getApplicationContext());
        loginPOJO = Utility.getLoginData(getApplicationContext());

        apiInterface = APIClient.getApiInterface();

        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getApplicationContext());
        rv_tags.setLayoutManager(gridLayout );
       // rv_tags.setAdapter(new PersonaTagsAdapter(getApplicationContext(),loginPOJO.()));


        setData();
    }


    private void setData() {
        try {
            loginPOJO = Utility.getLoginData(getApplicationContext());
            fName = loginPOJO.getFirstName();
            lName = loginPOJO.getLastName();
            txt_name.setText(fName + " "+ lName);
            txt_profession.setText(loginPOJO.getDesignationName());
            txt_email.setText(loginPOJO.getEmailId());
            //txt_gender.setText(loginPOJO.);
            txt_phoneNumber.setText(loginPOJO.getPhone());
            txt_password.setText(loginPOJO.getEntityPassword());
            txt_region.setText(loginPOJO.getCountryName());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_edit_profile);
            requestOptions.error(R.drawable.ic_edit_profile);
            Log.d(TAG, " "+ loginPOJO.getPicUrl());
            if (loginPOJO.getPicUrl().equals("")){
                try {
                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
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
                            .buildRect(Utility.getInitialsName(fName +" "+ lName) ,getResources().getColor( R.color.pale48));
                    img_profile.setImageDrawable(drawable);
                } catch (Exception e){
                    e.getMessage();
                }

            } else {
                img_profile.setMinimumWidth(60);
                img_profile.setMaxHeight(60);
                img_profile.setMinimumHeight(60);
                img_profile.setMaxWidth(60);
                Glide.with(this)
                        .load(loginPOJO.getPicUrl())
                        .into(img_profile);
            }
        } catch (Exception e){
            e.getMessage();
        }


    }

    @OnClick({R.id.lname, R.id.img_close, R.id.img_rateApp, R.id.ib_photo, R.id.lChangePassword, R.id.lRegion, R.id.lDesignation, R.id.img_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lname:
                //();
                editProfile();
                break;

            case R.id.img_close:
                finish();
                break;


            case R.id.img_rateApp:
                rateTheApp();
                break;

            case R.id.ib_photo:

               editProfilePhoto();

                break;


            case R.id.lChangePassword:
                editChangePassword();
                break;

            case R.id.lRegion:
                getRegionList();

                break;

            case R.id.lDesignation:
                dialogEditDesignation();
                break;

            case R.id.img_profile:
                editProfilePhoto();

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeCameraPhoto();
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                    try {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
                        alertDialogBuilder.setTitle("Allow permission to use this feature");
                        alertDialogBuilder
                                .setMessage("Click on SETTINGS button then Goto > Permissions > Camera  &  Storage permission are allow.")
                                .setCancelable(false)
                                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 1000);
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        try {
                            alertDialog.show();
                        } catch (Exception e){
                            e.getMessage();
                        }
                    } catch (Exception e){
                        e.getMessage();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Allow Permission to use this feature.", Toast.LENGTH_SHORT).show();
                }

                break;

            case GALLERY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeGalleryPhoto();
                }  else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                    try {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
                        alertDialogBuilder.setTitle("Allow permission to use this feature");
                        alertDialogBuilder
                                .setMessage("Click on SETTINGS button then Goto > Permissions > Camera  &  Storage permission are allow.")
                                .setCancelable(false)
                                .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 1000);
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        try {
                            alertDialog.show();
                        } catch (Exception e){
                            e.getMessage();
                        }
                    } catch (Exception e){
                        e.getMessage();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Allow Permission to use this feature.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case 1:
                    CropImage.activity(cameraUri).setAspectRatio(4, 4)
                            .start(this);

                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    final Uri imgResult = result.getUri();
                    String path = imgResult.getPath();
                    File imgFile = new File(SiliCompressor.with(this).compress(path,
                            new File("/storage/emulated/0/Thriive/.nomedia"), true));

                   // File imgFile = new File(path);
                    if (imgFile.exists() && imgFile.length() > 0) {
                        Bitmap bm = BitmapFactory.decodeFile(String.valueOf(imgFile));
                        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
                        String base64Image = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);
                        Log.d(TAG, "base64Image "+base64Image.trim().toString());
                        Log.d(TAG, "base64Image "+bm.toString());
                        Glide.with(this)
                               .load(bm)
                                .into(img_profile);
                        getEntityPhoto(base64Image.trim());
                    }

                    break;



                case 2:
                    if (data != null) {
                        CropImage.activity(data.getData()).setAspectRatio(4, 4)
                                .start(this);

                    }

                    break;
            }
        }
    }

    private void getEntityPhoto(String base64FromFile) {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonEntityImagePOJO> call = apiInterface.getUploadEntityPhoto(sharedData.getStringData(SharedData.API_URL) + "api/Entity/upload-entity-photo",loginPOJO.getActiveToken(),
                    loginPOJO.getRowcode(), base64FromFile);
            call.enqueue(new Callback<CommonEntityImagePOJO>() {
                @Override
                public void onResponse(Call<CommonEntityImagePOJO> call, Response<CommonEntityImagePOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonEntityImagePOJO pojo = response.body();
                        progressHUD.dismiss();
                        Log.d(TAG,"Edit Image "+pojo.getMessage() + " " + pojo.getOK());
                        if (pojo != null){
                            if (pojo.getOK()) {
                                //Toast.makeText(getApplicationContext(), "success"+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                                if (pojo.getPicUrl() != null){
                                    Log.d(TAG, pojo.getPicUrl());
                                  //  loginPOJO.setPicUrl("");
                                    loginPOJO.setPicUrl(pojo.getPicUrl());

                                    Utility.saveLoginData(getApplicationContext(), loginPOJO);
                                    setData();
                                 // Toast.makeText(EditProfileActivity.this, "Data is given " + pojo.getPicUrl(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EditProfileActivity.this, "Data not send ", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonEntityImagePOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(EditProfileActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }


    }

    public void editChangePassword() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        BottomSheetDialog dialog = new BottomSheetDialog(EditProfileActivity.this,R.style.SheetDialog);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        dialog.setContentView(dialogView);

        EditText edt_oldPassword  = dialogView.findViewById(R.id.edt_oldPassword);
        EditText edt_newPassword  = dialogView.findViewById(R.id.edt_newPassword);
        EditText edt_confirmPassword  = dialogView.findViewById(R.id.edt_confirmPassword);

        Button btn_submit  = dialogView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.hasText(edt_oldPassword) && Validation.hasText(edt_newPassword) && Validation.hasText(edt_confirmPassword)) {
                    if (edt_oldPassword.getText().toString().equals(loginPOJO.getEntityPassword())) {
                        if (edt_newPassword.getText().toString().equals(edt_confirmPassword.getText().toString())) {
                            dialog.dismiss();
                            getChangePassword(edt_confirmPassword.getText().toString());
                        } else {
                            Toast.makeText(EditProfileActivity.this, "New and confirm password did not match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Old password did not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Fill required field", Toast.LENGTH_SHORT).show();
                }

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

    private void getChangePassword(String password){
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.getChangePassword(sharedData.getStringData(SharedData.API_URL) +
                    "api/AppLogin/change-password", loginPOJO.getPrimaryLoginKey(), password);
            call.enqueue(new Callback<CommonPOJO>() {
                @Override
                public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonPOJO commonPOJO = response.body();
                        progressHUD.dismiss();
                        try {
                            if (commonPOJO != null){
                                if (commonPOJO.getOK()) {
                                    loginPOJO.setEntityPassword(password);
                                    Toast.makeText(EditProfileActivity.this, ""+commonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
//                                    sharedData.addBooleanData(SharedData.isFirstVisit, false);
//                                    sharedData.addBooleanData(SharedData.isLogged, false);
//                                    sharedData.clearPref(getApplicationContext());
//                                    Utility.clearLogin(getApplicationContext());
//                                    Utility.clearMeetingDetails(getApplicationContext());
//                                    Handler mHandler = new Handler();
//                                    mHandler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            progressHUD.dismiss();
//                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                            startActivity(intent);
//                                            finishAffinity();
//                                        }
//                                    }, 2000);
                                    Utility.saveLoginData(getApplicationContext(),loginPOJO);

                                    setData();
                                } else {
                                    Toast.makeText(EditProfileActivity.this, ""+commonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
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
                    //   Toast.makeText(LoginAccountActivity.this, Utility.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }


    }


    public void editProfilePhoto() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_profile, null);
        BottomSheetDialog dialog = new BottomSheetDialog(EditProfileActivity.this,R.style.SheetDialog);

        ImageView img_close = dialogView.findViewById(R.id.img_close);

        TextView txt_new = dialogView.findViewById(R.id.txt_new);
        TextView txt_gallery = dialogView.findViewById(R.id.txt_gallery);
        TextView txt_remove = dialogView.findViewById(R.id.txt_remove);
        dialog.setContentView(dialogView);
        txt_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                checkPermissionCamera();
            }
        });
        txt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                checkPermissionGallery();
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

    private boolean checkPermissionGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,}, GALLERY_PERMISSIONS_REQUEST);
            } else {
                takeGalleryPhoto();
                //return true;
            }
        } else {
            takeGalleryPhoto();
            //return true;
        }
        return false;
    }

    private void takeGalleryPhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 2);
    }


    public boolean checkPermissionCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CAMERA_PERMISSIONS_REQUEST);
            } else {
                takeCameraPhoto();
                //return true;
            }
        } else {
            takeCameraPhoto();
            // return true;
        }
        return false;
    }

    private void takeCameraPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = createMediaFile();
            if (photoFile != null) {
                cameraUri = FileProvider.getUriForFile(this,
                        getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createMediaFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = null;
        try {
            file = File.createTempFile(fileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPhotoPath = file.getAbsolutePath();
        return file;
    }

    public void dialogEditDesignation() {

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_designation, null);
        BottomSheetDialog dialog = new BottomSheetDialog(EditProfileActivity.this,R.style.SheetDialog);

        dialog.setContentView(dialogView);

        ImageView img_close = dialogView.findViewById(R.id.img_close);
        Button   button = dialogView.findViewById(R.id.btn_submit);
        EditText editText = dialogView.findViewById(R.id.edt_designation);
        editText.setText(loginPOJO.getDesignationName());
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.hasText(editText)){
                    dialog.dismiss();
                    getEditDesignation(editText.getText().toString());
                } else {
                    // Toast.makeText(LoginActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                }

            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();



    }

    private void getEditDesignation(String toString) {
        try{
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonEntityPOJO> call = apiInterface.getSaveEntityDesignation(sharedData.getStringData(SharedData.API_URL) + "api/Entity/save-entity-designation", loginPOJO.getActiveToken(),
                    loginPOJO.getRowcode(), toString);
            call.enqueue(new Callback<CommonEntityPOJO>() {
                @Override
                public void onResponse(Call<CommonEntityPOJO> call, Response<CommonEntityPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonEntityPOJO reasonPOJO = response.body();
                        progressHUD.dismiss();
                        if (reasonPOJO != null){
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (reasonPOJO.getOK()) {
                                if (reasonPOJO.getEntityObject() != null){
                                    Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                    Utility.saveLoginData(getApplicationContext(), reasonPOJO.getEntityObject());
                                    setData();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonEntityPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(EditProfileActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }


    public void getRegionList() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonCountryPOJO> call = apiInterface.getCountryList(sharedData.getStringData(SharedData.API_URL) +
                    "api/Entity/get-country-list", loginPOJO.getActiveToken());
            call.enqueue(new Callback<CommonCountryPOJO>() {
                @Override
                public void onResponse(Call<CommonCountryPOJO> call, Response<CommonCountryPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonCountryPOJO reasonPOJO = response.body();
                        progressHUD.dismiss();

                        if (reasonPOJO != null){
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (reasonPOJO.getOK()) {
                                if (reasonPOJO.getCountryList() != null){
                                    editRegion(reasonPOJO.getCountryList());
                                }

                                //  Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonCountryPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(EditProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }


    }


    public void editRegion(List<CountryListPOJO> listPOJO) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_region, null);
        BottomSheetDialog dialog = new BottomSheetDialog(EditProfileActivity.this,R.style.SheetDialog);
        dialogView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    dialogView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    dialogView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                //BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = (FrameLayout)
                        dialog.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //      behavior.setPeekHeight(0); // Remove this line to hide a dark background if you manually hide the dialog.
            }
        });

        EditText edt_region = dialogView.findViewById(R.id.edt_region);
        RecyclerView rv_region = dialogView.findViewById(R.id.rv_region);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        Button btn_submit = dialogView.findViewById(R.id.btn_submit);
        dialog.setContentView(dialogView);

        CountryListAdapter countryListAdapter = new CountryListAdapter(getApplicationContext(), (ArrayList<CountryListPOJO>) listPOJO);
        FlexboxLayoutManager manager = new FlexboxLayoutManager(getApplicationContext());
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setJustifyContent(JustifyContent.CENTER);
        rv_region.setLayoutManager(manager );
        //    rv_region.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        rv_region.setAdapter(countryListAdapter);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countryListAdapter.getCountryCode() == 0){
                    Toast.makeText(EditProfileActivity.this, "Select region first", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    getEditRegion(countryListAdapter.getCountryCode(),countryListAdapter.getCountryName() );

                }
            }
        });
        edt_region.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ((CountryListAdapter) countryListAdapter).filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    private void getEditRegion(int countryCode, String countryName) {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonEntityPOJO> call = apiInterface.getSaveEntityRegion(sharedData.getStringData(SharedData.API_URL) +
                    "api/Entity/save-entity-country", loginPOJO.getActiveToken(),
                    loginPOJO.getRowcode(), countryCode, countryName);
            call.enqueue(new Callback<CommonEntityPOJO>() {
                @Override
                public void onResponse(Call<CommonEntityPOJO> call, Response<CommonEntityPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonEntityPOJO reasonPOJO = response.body();
                        progressHUD.dismiss();
                        try {
                            Log.d(TAG,""+reasonPOJO.getMessage());
                            if (reasonPOJO.getOK()) {
                                if (reasonPOJO.getEntityObject() != null){
                                    Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                    Utility.saveLoginData(getApplicationContext(), reasonPOJO.getEntityObject());
                                    setData();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e ){
                            e.getMessage();
                        }

                    }
                }
                @Override
                public void onFailure(Call<CommonEntityPOJO> call, Throwable t) {
                    progressHUD.dismiss();
               //     Toast.makeText(EditProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }


    }


    public void editProfile() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
        BottomSheetDialog dialog = new BottomSheetDialog(EditProfileActivity.this,R.style.SheetDialog);

        EditText edt_lName = dialogView.findViewById(R.id.edt_lName);
        EditText edt_fName = dialogView.findViewById(R.id.edt_fName);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        Button button = dialogView.findViewById(R.id.btn_next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.hasText(edt_fName) && Validation.hasText(edt_lName)){
                    dialog.dismiss();
                    getChangeEntity(edt_fName.getText().toString(), edt_lName.getText().toString());
                }
            }
        });

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

    private void getChangeEntity(String f_name, String l_name){

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonEntityPOJO> call = apiInterface.getSaveEntityName(sharedData.getStringData(SharedData.API_URL) +
                "api/Entity/save-entity-name", loginPOJO.getActiveToken(),
                loginPOJO.getRowcode(), f_name, l_name);
        call.enqueue(new Callback<CommonEntityPOJO>() {
            @Override
            public void onResponse(Call<CommonEntityPOJO> call, Response<CommonEntityPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonEntityPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    if (reasonPOJO != null){
                        Log.d(TAG,""+reasonPOJO.getMessage());
                        if (reasonPOJO.getOK()) {
                            if (reasonPOJO.getEntityObject() != null){
                                Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                                Utility.saveLoginData(getApplicationContext(), reasonPOJO.getEntityObject());
                                setData();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonEntityPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(EditProfileActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void rateTheApp() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.SheetDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_rate_app, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog dialog = dialogBuilder.create();
        RatingBar rating_bar = dialogView.findViewById(R.id.rating_bar);
        TextView txt_rate = dialogView.findViewById(R.id.txt_rate);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
              //  Toast.makeText(EditProfileActivity.this, "" + v, Toast.LENGTH_SHORT).show();
                if (v == 3.0){
                    txt_rate.setText("Satisfied");
                } else if (v == 4.0){
                    txt_rate.setText("Happy");
                } else if (v == 5.0){
                    txt_rate.setText("Very Happy");
                }
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        dialog.dismiss();
                        getSaveAppReview(txt_rate.getText().toString(), ""+v);
                    }
                }, 2000);

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

    public void getSaveAppReview(String review_text, String review_int) {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonPOJO> call = apiInterface.getSaveAppReview(sharedData.getStringData(SharedData.API_URL) + "api/meeting/save-app-review", loginPOJO.getActiveToken(),
                 loginPOJO.getRowcode(), review_text ,review_text, Integer.parseInt(review_int));
        call.enqueue(new Callback<CommonPOJO>() {
            @Override
            public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    CommonPOJO reasonPOJO = response.body();
                    progressHUD.dismiss();
                    Log.d(TAG,""+reasonPOJO.getMessage());
                    if (reasonPOJO.getOK()) {
                        Toast.makeText(getApplicationContext(), ""+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), " "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<CommonPOJO> call, Throwable t) {
                progressHUD.dismiss();
               // Toast.makeText(EditProfileActivity.this, " ", Toast.LENGTH_SHORT).show();
            }
        });

    }

}