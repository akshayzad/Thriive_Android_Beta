package com.thriive.app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.thriive.app.utilities.Utility;
import com.thriive.app.utilities.progressdialog.KProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommonWebviewActivity extends AppCompatActivity {

    @BindView(R.id.label_name)
    TextView label_name;
    KProgressHUD progressHUD;
    @BindView(R.id.webView)
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_webview);
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("intent_type");

        progressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false);
        if (title.equals(Utility.TERMS)){
            label_name.setText(""+Utility.TERMS);
            webView.loadUrl("https://thriive.app/terms-of-service/");
            webView.getSettings().setJavaScriptEnabled(true);

        } else {
            label_name.setText(""+Utility.PRIVACY_POLICY);
            webView.loadUrl("https://thriive.app/privacy-policy/");
            webView.getSettings().setJavaScriptEnabled(true);

        }
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                try {
                    progressHUD.show();
                } catch (Exception e){
                    e.getMessage();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    progressHUD.dismiss();
                } catch (Exception e){
                    e.getMessage();
                }
                //progressHUD.dismiss();
            }
        });


//        Terms of Service -
//
//                https://thriive.app/terms-of-service/
//
//        Privacy Policy -
//
//                https://thriive.app/privacy-policy/



    }


    @OnClick({R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                //();
                onBackPressed();
                //editProfile();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        try {
            if (progressHUD != null){
                if (progressHUD.isShowing()){
                    progressHUD.dismiss();
                }
            }
            super.onBackPressed();
        } catch (Exception e){
            e.getMessage();
            super.onBackPressed();
        }

    }
}