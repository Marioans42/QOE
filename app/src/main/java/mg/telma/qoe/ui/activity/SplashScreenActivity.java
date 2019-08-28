package mg.telma.qoe.ui.activity;

import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import mg.telma.qoe.R;

public class SplashScreenActivity extends AppCompatActivity {

    private Handler splashHandler;
    private Runnable splashRunnable;
    private Intent mMainPagerIntent;
    private ProgressBar mSpinner;
    private static final long SPLASHTIME = 2000;
    private static final int RESULT_CALL = 1;


    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_splash);
            splashRunnable = () -> {
                SplashScreenActivity splashScreenActivity = SplashScreenActivity.this;
                splashScreenActivity.mMainPagerIntent = new Intent(splashScreenActivity, CallTestFragment.class);
                splashScreenActivity.startActivityForResult(mMainPagerIntent, 1);
                SplashScreenActivity.this.finish();

            };


        this.mSpinner = findViewById(R.id.progressBar);
        this.mSpinner.setIndeterminate(true);
        this.mSpinner.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), Mode.SRC_ATOP);

        splashHandler = new Handler();
        splashHandler.postDelayed(splashRunnable, SPLASHTIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashHandler.removeCallbacks(splashRunnable);
    }
}
