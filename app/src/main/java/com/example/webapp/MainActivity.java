package com.example.webapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

public class MainActivity extends AppCompatActivity {


    WebView webView;

    Activity activity ;


    private KProgressHUD hud;
    LinearLayout linearLayout;
    ImageView imageView;

    SwipeRefreshLayout mySwipeRefreshLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView =(WebView)findViewById(R.id.webView1);
        linearLayout = findViewById(R.id.idLinearlayout);
        imageView = findViewById(R.id.idImageView);
        mySwipeRefreshLayout = findViewById(R.id.swipeContainer);



        activity = this;

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim);
        imageView.startAnimation(animation);



        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //hud.show();

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





        if(!isNetworkAvailable(this)) {

            NoInternetAlertDialog();
        }
        else {
            splashDismiss();
        }
        webView.loadUrl("http://jomeen.com/");
//        webView.loadUrl("https://www.kalerkantho.com/");



        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        webView.reload();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );



    }

    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 1000);
    }

    private void splashDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                linearLayout.setVisibility(View.GONE);

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


                hud = KProgressHUD.create(MainActivity.this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setWindowColor(getResources().getColor(R.color.loadcolor))
                        .setDimAmount(0.5f);

                //hud.show();
            }
        }, 3000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }


    public void NoInternetAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("No Internet Connection")
                .setMessage("Please connect your internet")
                .setIcon(R.drawable.no_internet_icon)
                .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reload();

                    }
                });
        builder.show();
    }

    public void ExitAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Do to want to close this App?")
               // .setMessage("Please connect your internet")
              //  .setIcon(R.drawable.no_internet_icon)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();

                    }
                }).setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               dialogInterface.dismiss();

            }
        });;

        builder.show();
    }



    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
