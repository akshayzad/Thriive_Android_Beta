package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thriive.app.adapters.MeetingHistoryAdapter;
import com.thriive.app.adapters.RequesterListAdapter;
import com.thriive.app.adapters.ScheduleListAdapter;
import com.thriive.app.api.APIClient;
import com.thriive.app.api.APIInterface;
import com.thriive.app.models.CommonHomePOJO;
import com.thriive.app.models.CommonMeetingListPOJO;
import com.thriive.app.models.CommonMeetingPOJO;
import com.thriive.app.models.CommonRequesterPOJO;
import com.thriive.app.models.LoginPOJO;
import com.thriive.app.models.PendingMeetingRequestPOJO;
import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingsHistoryActivity extends AppCompatActivity {

    @BindView(R.id.rv_history)
    RecyclerView rv_history;
    @BindView(R.id.txt_noHistory)
    TextView txt_noHistory;

    private APIInterface apiInterface;
    private KProgressHUD progressHUD;
    private LoginPOJO.ReturnEntity loginPOJO;
    private static String TAG = MeetingsHistoryActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_history);
        ButterKnife.bind(this);
        loginPOJO = Utility.getLoginData(getApplicationContext());
        apiInterface = APIClient.getApiInterface();

        getMeetingHistory();


    }

    private void getMeetingHistory() {
        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .show();
        Call<CommonMeetingListPOJO> call = apiInterface.getMeetingHistory(loginPOJO.getActiveToken(),
                loginPOJO.getRowcode());
        call.enqueue(new Callback<CommonMeetingListPOJO>() {
            @Override
            public void onResponse(Call<CommonMeetingListPOJO> call, Response<CommonMeetingListPOJO> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    progressHUD.dismiss();
                    CommonMeetingListPOJO pojo = response.body();
                    Log.d(TAG,""+pojo.getMessage());
                    if (pojo.getOK()) {
                        if (pojo.getMeetingList() != null){
                            if (pojo.getMeetingList().size() == 0){
                                txt_noHistory.setVisibility(View.VISIBLE);
                            } else {
                                txt_noHistory.setVisibility(View.GONE);
                            }
                            rv_history.setAdapter(new MeetingHistoryAdapter(MeetingsHistoryActivity.this,pojo.getMeetingList()));
                        }

                        // recycler_requested.setAdapter(requestedAdapter);
                        Toast.makeText(getApplicationContext(), "Success "+pojo.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Failure "+pojo.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<CommonMeetingListPOJO> call, Throwable t) {
                //   progressHUD.dismiss();
                Toast.makeText(getApplicationContext(), "Getting Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;


        }
    }
}