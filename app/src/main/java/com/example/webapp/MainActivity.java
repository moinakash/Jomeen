package com.example.webapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

public class MainActivity extends AppCompatActivity {


    WebView webView;

    Activity activity ;
    private ProgressDialog progDailog;

    private KProgressHUD hud;
    LinearLayout linearLayout;


    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView =(WebView)findViewById(R.id.webView1);
        linearLayout = findViewById(R.id.idLinearlayout);


        splashDismiss();


        ///////////////////////////////////////////////

        activity = this;

//        progDailog = ProgressDialog.show(activity, "Loading","Please wait...", true);
//        progDailog.setCancelable(false);


        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(new DialogInterface.OnCancelListener()
                {
                    @Override public void onCancel(DialogInterface
                                                           dialogInterface)
                    {
                        Toast.makeText(MainActivity.this, "You " +
                                "cancelled manually!", Toast
                                .LENGTH_SHORT).show();
                    }
                });

        hud.show();



        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                progDailog.show();
                hud.show();

                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
//                progDailog.dismiss();
                scheduleDismiss();
            }
        });

//        webView.loadUrl("https://google.com");
        webView.loadUrl("http://jomeen.com/");




        //////////////////////////////////////////////////////////
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
////        wb.getSettings().setPluginsEnabled(true);
//
//        if (Build.VERSION.SDK_INT < 8) {
//            webView.getSettings().setPluginsEnabled(true);
//        } else {
//            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        }
//
//
//        webView.setWebViewClient(new HelloWebViewClient());
////        wb.setWebViewClient(new WebViewClient() {
////            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
////                wb.loadUrl("file:///android_asset/myerrorpage.html");
////
////            }
////        });
//
//
//
//        webView.loadUrl("http://jomeen.com");
//
////        wb.loadUrl("https://www.google.com");
    }

    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 2000);
    }

    private void splashDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                linearLayout.setVisibility(View.GONE);
            }
        }, 2000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}
