package mg.telma.qoe.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import mg.telma.qoe.init.InitApplication;
import mg.telma.qoe.service.CellularService;
import mg.telma.qoe.service.PingTestService;
import mg.telma.qoe.service.UploadTestService;

public class BaseActivity extends AppCompatActivity {
    @Inject
    CellularService cellularService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((InitApplication) getApplication()).getApplicationComponent().inject(this);
    }
}
