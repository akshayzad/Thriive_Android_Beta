package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.thriive.app.adapters.CountryListAdapter;
import com.thriive.app.adapters.YourExpertiseAdapter;
import com.thriive.app.adapters.YourInterestsAdapter;
import com.thriive.app.adapters.YourObjectivesAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonCountryPOJO;
import com.thriive.app.models.CommonExpertisePOJO;
import com.thriive.app.models.CommonInterestsPOJO;
import com.thriive.app.models.CommonObjectivesPOJO;
import com.thriive.app.models.CommonPOJO;
import com.thriive.app.models.CountryListPOJO;
import com.thriive.app.models.ExpertiseBodyPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.ObjectiveBodyPOJO;
import com.thriive.app.utilities.SharedData;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesActivity extends AppCompatActivity {
    private LoginPOJO.ReturnEntity loginPOJO;

    private KProgressHUD progressHUD;
    private APIInterface apiInterface;
    private SharedData sharedData;
    private String title = "";

    public static final String TAG = PreferencesActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        ButterKnife.bind(this);

        sharedData = new SharedData(getApplicationContext());
        loginPOJO = Utility.getLoginData(getApplicationContext());

        apiInterface = APIClient.getApiInterface();

    }
    @OnClick({R.id.img_close, R.id.view_interest, R.id.view_expertise, R.id.view_objective})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;

            case R.id.view_expertise:
                title = getResources().getString(R.string.expertise);
                getExpertise();
              //  dialog_prefrences(getResources().getString(R.string.expertise));
                break;


            case R.id.view_interest:
                title = getResources().getString(R.string.interest);
                getInterests();
            //    dialog_prefrences(getResources().getString(R.string.interest));
                break;

            case R.id.view_objective:
                title = getResources().getString(R.string.objectives);
                getObjectives();
               // dialog_prefrences(getResources().getString(R.string.objectives));
                break;

        }
    }

    private void getObjectives() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonObjectivesPOJO> call = apiInterface.getObjectives(sharedData.getStringData(SharedData.API_URL) +
                    "api/profile/get-objective", loginPOJO.getActiveToken(), loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonObjectivesPOJO>() {
                @Override
                public void onResponse(Call<CommonObjectivesPOJO> call, Response<CommonObjectivesPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonObjectivesPOJO pojo = response.body();
                        progressHUD.dismiss();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {
                                if (pojo.getResult() != null){
                                    setObjectives(pojo.getResult());
                                }

                              //  Toast.makeText(getApplicationContext(), ""+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonObjectivesPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(PreferencesActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    private void setObjectives(List<CommonObjectivesPOJO.ObjectivesList> result) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_preferences, null);
        BottomSheetDialog dialog = new BottomSheetDialog(PreferencesActivity.this,R.style.SheetDialog);
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
        RecyclerView rv_region = dialogView.findViewById(R.id.rv_region);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        Button btn_submit = dialogView.findViewById(R.id.btn_submit);
        TextView label_title = dialogView.findViewById(R.id.label_title);
        dialog.setContentView(dialogView);
        label_title.setText("Your "+title);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(getApplicationContext());
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setJustifyContent(JustifyContent.CENTER);
        rv_region.setLayoutManager(manager );

        YourObjectivesAdapter adapter = new YourObjectivesAdapter(PreferencesActivity.this,
                (ArrayList<CommonObjectivesPOJO.ObjectivesList>) result);
        //    rv_region.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        rv_region.setAdapter(adapter);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Gson gson = new GsonBuilder().create();
//                JsonArray myCustomArray = gson.toJsonTree(adapter.getObjectivesLists()).getAsJsonArray();
//
//                String string  = myCustomArray.toString().trim();
//                Log.d(TAG, "object " + string);

                ObjectiveBodyPOJO bodyPOJO = new ObjectiveBodyPOJO();
                bodyPOJO.setRowcode(loginPOJO.getRowcode());
                bodyPOJO.setResult(adapter.getObjectivesLists());
                dialog.dismiss();

                saveObjectives(bodyPOJO);

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

    private void saveObjectives(ObjectiveBodyPOJO bodyPOJO) {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.saveObjectives(sharedData.getStringData(SharedData.API_URL) +
                    "api/profile/save-objective", loginPOJO.getActiveToken(),bodyPOJO);
            call.enqueue(new Callback<CommonPOJO>() {
                @Override
                public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonPOJO pojo = response.body();
                        progressHUD.dismiss();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {
                                Toast.makeText(getApplicationContext(), ""+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(PreferencesActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    private void getExpertise() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonExpertisePOJO> call = apiInterface.getExpertise(sharedData.getStringData(SharedData.API_URL) +
                    "api/profile/get-expertise", loginPOJO.getActiveToken(), loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonExpertisePOJO>() {
                @Override
                public void onResponse(Call<CommonExpertisePOJO> call, Response<CommonExpertisePOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonExpertisePOJO pojo = response.body();
                        progressHUD.dismiss();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {
                                if (pojo.getResult() != null){
                                    setExpertise(pojo.getResult());
                                }

                                // Toast.makeText(getApplicationContext(), ""+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonExpertisePOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(PreferencesActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    private void setExpertise(List<CommonExpertisePOJO.ExpertiseList> result) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_preferences, null);
        BottomSheetDialog dialog = new BottomSheetDialog(PreferencesActivity.this,R.style.SheetDialog);
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
        RecyclerView rv_region = dialogView.findViewById(R.id.rv_region);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        Button btn_submit = dialogView.findViewById(R.id.btn_submit);
        TextView label_title = dialogView.findViewById(R.id.label_title);
        dialog.setContentView(dialogView);
        label_title.setText("Your "+title);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(getApplicationContext());
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setJustifyContent(JustifyContent.CENTER);
        rv_region.setLayoutManager(manager );

        YourExpertiseAdapter adapter = new YourExpertiseAdapter(PreferencesActivity.this,
                (ArrayList<CommonExpertisePOJO.ExpertiseList>) result);
        //    rv_region.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        rv_region.setAdapter(adapter);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(adapter.getExpertiseLists()).getAsJsonArray();

                String string  = myCustomArray.toString().trim();
                Log.d(TAG, "getExpertiseLists " + string);
                dialog.dismiss();
                ExpertiseBodyPOJO  expertiseBodyPOJO  = new ExpertiseBodyPOJO();
                expertiseBodyPOJO.setRowcode(loginPOJO.getRowcode());
                expertiseBodyPOJO.setResult(adapter.getExpertiseLists());
                saveExpertise(expertiseBodyPOJO);
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
    private void saveExpertise(ExpertiseBodyPOJO expertiseBodyPOJO) {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonPOJO> call = apiInterface.saveExpertise(sharedData.getStringData(SharedData.API_URL) +
                    "api/profile/save-expertise", loginPOJO.getActiveToken(), expertiseBodyPOJO);
            call.enqueue(new Callback<CommonPOJO>() {
                @Override
                public void onResponse(Call<CommonPOJO> call, Response<CommonPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonPOJO pojo = response.body();
                        progressHUD.dismiss();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {
                                Toast.makeText(getApplicationContext(), ""+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(PreferencesActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }


    private void getInterests() {
        try {
            progressHUD = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .show();
            Call<CommonInterestsPOJO> call = apiInterface.getInterests(sharedData.getStringData(SharedData.API_URL) +
                    "api/profile/get-interest", loginPOJO.getActiveToken(), loginPOJO.getRowcode());
            call.enqueue(new Callback<CommonInterestsPOJO>() {
                @Override
                public void onResponse(Call<CommonInterestsPOJO> call, Response<CommonInterestsPOJO> response) {
                    if(response.isSuccessful()) {
                        Log.d(TAG, response.toString());
                        CommonInterestsPOJO pojo = response.body();
                        progressHUD.dismiss();
                        if (pojo != null){
                            Log.d(TAG,""+pojo.getMessage());
                            if (pojo.getOK()) {
                                if (pojo.getResult() != null){
                                    setInterests(pojo.getResult());
                                }

                                Toast.makeText(getApplicationContext(), ""+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), " "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
                @Override
                public void onFailure(Call<CommonInterestsPOJO> call, Throwable t) {
                    progressHUD.dismiss();
                    Toast.makeText(PreferencesActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            e.getMessage();
        }
    }

    private void setInterests(List<CommonInterestsPOJO.InterestsList> result) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_preferences, null);
        BottomSheetDialog dialog = new BottomSheetDialog(PreferencesActivity.this,R.style.SheetDialog);

        RecyclerView rv_region = dialogView.findViewById(R.id.rv_region);
        ImageView img_close = dialogView.findViewById(R.id.img_close);
        Button btn_submit = dialogView.findViewById(R.id.btn_submit);
        TextView label_title = dialogView.findViewById(R.id.label_title);
        dialog.setContentView(dialogView);
        label_title.setText("Your "+title);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(getApplicationContext());
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setJustifyContent(JustifyContent.CENTER);
        rv_region.setLayoutManager(manager );

        YourInterestsAdapter adapter = new YourInterestsAdapter(PreferencesActivity.this,
                (ArrayList<CommonInterestsPOJO.InterestsList>) result);
        //    rv_region.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        rv_region.setAdapter(adapter);


        btn_submit.setOnClickListener(new View.OnClickListener() {
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