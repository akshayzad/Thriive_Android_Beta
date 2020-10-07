package com.thriive.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.thriive.app.adapters.ExperienceAdapter;
import com.thriive.app.adapters.ExpertiseAdapter;
import com.thriive.app.adapters.PersonaTagsAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.CircleImageView;
import com.thriive.app.utilities.SiliCompressor;
import com.thriive.app.utilities.Utility;

import com.thriive.app.utilities.image_cropper.CropImage;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


    private String fName, lName;
    private LoginPOJO loginPOJO;

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

        loginPOJO = Utility.getLoginData(getApplicationContext());

        apiInterface = APIClient.getApiInterface();
        fName = loginPOJO.getReturnEntity().getFirstName();
        lName = loginPOJO.getReturnEntity().getLastName();
        txt_name.setText(fName + " "+ lName);
        txt_profession.setText(loginPOJO.getReturnEntity().getDesignationName());
        txt_email.setText(loginPOJO.getReturnEntity().getEmailId());
        //txt_gender.setText(loginPOJO.getReturnEntity().);
        txt_phoneNumber.setText(loginPOJO.getReturnEntity().getPhone());
        txt_password.setText(loginPOJO.getReturnEntity().getEntityPassword());
        txt_region.setText(loginPOJO.getReturnEntity().getCountryName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_edit_profile);
        requestOptions.error(R.drawable.ic_edit_profile);
        Glide.with(this)
                .load(loginPOJO.getReturnEntity().getPicUrl())
                .apply(requestOptions)
                .into(img_profile);
        FlexboxLayoutManager gridLayout = new FlexboxLayoutManager(getApplicationContext());
        rv_tags.setLayoutManager(gridLayout );
       // rv_tags.setAdapter(new PersonaTagsAdapter(getApplicationContext(),loginPOJO.getReturnEntity().()));


    }

    @OnClick({R.id.img_edit_profile, R.id.img_close, R.id.img_rateApp, R.id.ib_photo, R.id.iv_changePassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_edit_profile:
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


            case R.id.iv_changePassword:
                editChangePassword();
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
                    File compressedImageFile = new File(SiliCompressor.with(this).compress(path,
                            new File("/storage/emulated/0/Thriive/.nomedia"), true));

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


    public void editChangePassword() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        BottomSheetDialog dialog = new BottomSheetDialog(EditProfileActivity.this,R.style.SheetDialog);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
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
        Call<CommonPOJO> call = apiInterface.getSaveAppReview(loginPOJO.getReturnEntity().getActiveToken(),
                 loginPOJO.getReturnEntity().getRowcode(), review_text ,review_text, review_int);
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
                        Toast.makeText(getApplicationContext(), "Failure "+reasonPOJO.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<CommonPOJO> call, Throwable t) {
                progressHUD.dismiss();
                Toast.makeText(EditProfileActivity.this, "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

}