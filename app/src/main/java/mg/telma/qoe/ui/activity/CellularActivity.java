package mg.telma.qoe.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import mg.telma.qoe.R;


public class CellularActivity extends BaseActivity {

    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private TextView mTextView;
    private Button mButton;
    private WebView mWebView;
    private String mURL;
    double startTime ;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = CellularActivity.this;
        final DecimalFormat dec = new DecimalFormat("#.##");

        // Specify the url to surf
        mURL = "http://www.youtube.com";

        // Get the widgets reference from XML layout
    /*    mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        mTextView = (TextView) findViewById(R.id.tv);
        mButton = (Button) findViewById(R.id.btn);
        mWebView = (WebView) findViewById(R.id.web_view);*/



        // Set a click listener for button widget
/*
        mButton.setOnClickListener(view -> {
            // Set a WebViewClient for WebView
            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon){
                    double startTime = System.currentTimeMillis();
                    CellularActivity.this.startTime = startTime;
                    // Page loading started
                    // Do something
                }
                @Override
                public void onPageFinished(WebView view, String url){
                    // Page loading finished
                    double duration = (System.currentTimeMillis() - CellularActivity.this.startTime) /1000 ;
                    System.out.println("startTime :" + dec.format(duration));
                    Toast.makeText(mContext,"Page Loaded. Time Loaded " + dec.format(duration), Toast.LENGTH_SHORT).show();
                }
            });

            // Set a WebChromeClient for WebView
            // Another way to determine when page loading finish
            mWebView.setWebChromeClient(new WebChromeClient(){
                public void onProgressChanged(WebView view, int newProgress){
                    mTextView.setText("Page loading : " + newProgress + "%");

                    if(newProgress == 100){
                        // Page loading finish
                        mTextView.setText("Page Loaded.");
                    }
                }
            });
            // Enable JavaScript
            mWebView.getSettings().setJavaScriptEnabled(true);

            // Load the url in the WebView
            mWebView.loadUrl(mURL);
        });*/
    }
}
