package mg.telma.qoe.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.axet.androidlibrary.widgets.OptimizationPreferenceCompat;

import mg.telma.qoe.app.CallApplication;
import mg.telma.qoe.service.CallTestService;

public class OnUpgradeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        OptimizationPreferenceCompat.setBootInstallTime(context, CallApplication.PREFERENCE_INSTALL, System.currentTimeMillis());
        CallTestService.startIfEnabled(context);
    }
}
