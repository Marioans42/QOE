package mg.telma.qoe.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import mg.telma.qoe.R;

public class WebFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    final DecimalFormat dec = new DecimalFormat("#.##");
    String[] pages = { "Google", "Youtube", "FaceBook", "Wikipedia"};

    private Button mButton;
    private TextView mTextView;
    private TextView mTextViewTime;
    private TextView mTextViewUrl;
    private WebView mWebView;
    private String mURL;
    private boolean isTestYT;
    private boolean isTestFb;
    private boolean isTestGoogle;
    private boolean isTestWiki;
    double startTime ;

    CountDownTimer mTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewWeb =  inflater.inflate(R.layout.fragment_web, container, false);
        Spinner spin =  viewWeb.findViewById(R.id.page);
        mButton = viewWeb.findViewById(R.id.startWeb);
        mWebView =  viewWeb.findViewById(R.id.web_view);
        mTextView =  viewWeb.findViewById(R.id.tv);
        mTextViewTime = viewWeb.findViewById(R.id.time);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        //mWebView.setVisibility(View.INVISIBLE);
        mButton.setOnClickListener(view -> {

            mTimer = new CountDownTimer(5000, 1000) {

                String[] myArray = {"https://Google.com", "https://youtube.com","https://facebook.com"};


                int currentIndex = 0;

                public void onTick(long millisUntilFinished) {

                    //number.setText("" + millisUntilFinished / 1000);
                }

                //code comment start
                // i think this part could be written better
                // but it works!!!
                public void onFinish() {
                    if (currentIndex < myArray.length) {

                        mWebView.loadUrl(myArray[currentIndex]);
                        currentIndex++;
                    } else {
                        currentIndex = 0;
                        if (currentIndex < myArray.length)

                            mWebView.loadUrl(myArray[currentIndex]);
                        currentIndex++;
                        //mTimer.start();
                    }

                    //mTimer.start();

                }
                //code comment end
            };

            mTimer.start();
           // mWebView = (WebView) findViewById(R.id.webView);
            mWebView.getSettings().setJavaScriptEnabled(true);
            //URL of first image
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(mWebView, url);

                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                }
            });


        });

        return viewWeb;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
       // Toast.makeText(getActivity().getApplicationContext(), "Selected User: "+ pages[position] ,Toast.LENGTH_SHORT).show();

        //Toast.makeText(getActivity().getApplicationContext(), "url: "+ mURL ,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }


}
