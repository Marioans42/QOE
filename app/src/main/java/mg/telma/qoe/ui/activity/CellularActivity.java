package mg.telma.qoe.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            checkPerms();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPerms() {
        String[] perms = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.PROCESS_OUTGOING_CALLS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        List<String> requestingPerms = new ArrayList<>();
        for (String perm : perms) {
            if (checkSelfPermission(perm) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestingPerms.add(perm);
            }
        }
        if (requestingPerms.size() > 0) {
            requestPermissions(requestingPerms.toArray(new String[requestingPerms.size()]), 0);
        }
    }
}
