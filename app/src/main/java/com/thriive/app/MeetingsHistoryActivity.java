package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.thriive.app.adapters.MeetingHistoryAdapter;
import com.thriive.app.models.CommonRequesterPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingsHistoryActivity extends AppCompatActivity {

    @BindView(R.id.rv_history)
    RecyclerView rv_history;

    private ArrayList<CommonRequesterPOJO>arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_history);
        ButterKnife.bind(this);

        arrayList.add(new CommonRequesterPOJO());
        arrayList.add(new CommonRequesterPOJO());
        arrayList.add(new CommonRequesterPOJO());

        rv_history.setAdapter(new MeetingHistoryAdapter(MeetingsHistoryActivity.this, arrayList));
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