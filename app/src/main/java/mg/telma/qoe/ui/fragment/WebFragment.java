package mg.telma.qoe.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import mg.telma.qoe.R;

public class WebFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    final DecimalFormat dec = new DecimalFormat("#.##");
    String[] pages = {"Google", "Youtube", "FaceBook", "Wikipedia"};

    private Button mButton;
    private TextView mTextView;
    private TextView mTextViewTime;
    private TextView mTextViewUrl;
    private WebView mWebView;
    private String mURL;

    double startTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewWeb = inflater.inflate(R.layout.fragment_web, container, false);
        Spinner spin = viewWeb.findViewById(R.id.page);
        mButton = viewWeb.findViewById(R.id.startWeb);
        mWebView = viewWeb.findViewById(R.id.web_view);
        mTextView = viewWeb.findViewById(R.id.tv);
        mTextViewUrl = viewWeb.findViewById(R.id.url);
        mTextViewTime = viewWeb.findViewById(R.id.time);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, pages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        mWebView.setVisibility(View.INVISIBLE);
        mButton.setOnClickListener(view -> {
            // Set a WebViewClient for WebView
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    double startTime = System.currentTimeMillis();
                    WebFragment.this.startTime = startTime;
                    mTextViewUrl.setText(mURL);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    // Page loading finished
                    double duration = (System.currentTimeMillis() - WebFragment.this.startTime) / 1000;
                    System.out.println("startTime :" + dec.format(duration));
                    mTextViewTime.setText(duration + "s");

                    //Toast.makeText(mContext,"Page Loaded. Time Loaded " + dec.format(duration), Toast.LENGTH_SHORT).show();
                }
            });

            // Set a WebChromeClient for WebView
            // Another way to determine when page loading finish
            mWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int newProgress) {
                    mTextView.setText(newProgress + "%");
                    mWebView.setVisibility(View.VISIBLE);
                    if (newProgress == 100) {
                        // Page loading finish
                        mTextView.setText(newProgress + "%");
                    }
                }
            });

            mWebView.setOnTouchListener((v, event) -> true);
            // Enable JavaScript
            mWebView.getSettings().setJavaScriptEnabled(true);

            // Load the url in the WebView
            mWebView.loadUrl(mURL);
        });

        return viewWeb;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //Toast.makeText(getActivity().getApplicationContext(), "Selected User: " + pages[position], Toast.LENGTH_SHORT).show();
        mURL = "http://www." + pages[position] + ".com";
        //Toast.makeText(getActivity().getApplicationContext(), "url: " + mURL, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}