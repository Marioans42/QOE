package mg.telma.qoe.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class WebTestService extends Thread {

    private Button mButton;
    private TextView mTextView;
    private TextView mTextViewTime;
    private TextView mTextViewUrl;
    private WebView mWebView;
    private String mURL;
    String[] pages = { "Google", "Youtube", "FaceBook", "Wikipedia"};

    double startTime ;
    final DecimalFormat dec = new DecimalFormat("#.##");
    Activity activity;

    public WebTestService(WebView mWebView, Activity activity) {
        this.mWebView = mWebView;
        this.activity = activity;
    }

    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< pages.length; i++) {
                    mWebView.loadUrl("http://www."+pages[i]+".com");
                    mWebView.setWebViewClient(new WebViewClient(){

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon){
                            startTime = System.currentTimeMillis();
                        }
                        @Override
                        public void onPageFinished(WebView view, String url){
                            // Page loading finished
                            double duration = (System.currentTimeMillis() - startTime) /1000 ;
                            System.out.println("startTime :" + dec.format(duration));
                            try {
                                sleep(200);
                                System.out.println("sleeping :" );
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            //mTextViewTime.setText(duration + "s");

                            //Toast.makeText(mContext,"Page Loaded. Time Loaded " + dec.format(duration), Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Set a WebChromeClient for WebView
                    // Another way to determine when page loading finish
                  /*  mWebView.setWebChromeClient(new WebChromeClient(){
                        public void onProgressChanged(WebView view, int newProgress){
                            //mTextView.setText(newProgress + "%");
                            mWebView.setVisibility(View.VISIBLE);
                            if(newProgress == 100){
                                // Page loading finish
                                //mTextView.setText(newProgress + "%");
                            }
                        }
                    });*/
                }



                mWebView.setOnTouchListener((v, event) -> true);
                // Enable JavaScript
                mWebView.getSettings().setJavaScriptEnabled(true);

                mWebView.clearCache(true);

                // Load the url in the WebView

            }
        });
    }
}
