package mg.telma.qoe.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import mg.telma.qoe.R;
import mg.telma.qoe.service.WebTestService;

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
        mWebView.setVisibility(View.INVISIBLE);
        mButton.setOnClickListener(view -> {
            Boolean pingTestStarted = false;
            Boolean pingTestFinished = false;
            Boolean downloadTestStarted = false;
            Boolean downloadTestFinished = false;
            Boolean uploadTestStarted = false;
            Boolean uploadTestFinished = false;



            while (true) {
                if (!pingTestStarted) {
                    new WebTestService(mWebView, pages[0], getActivity()).start();
                    pingTestStarted = true;
                }
                if (pingTestFinished && !downloadTestStarted) {
                    new WebTestService(mWebView, pages[1], getActivity()).start();
                    downloadTestStarted = true;
                }
                if (downloadTestFinished && !uploadTestStarted) {
                    new WebTestService(mWebView, pages[2], getActivity()).start();
                    uploadTestStarted = true;
                }

               break;
            }


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
