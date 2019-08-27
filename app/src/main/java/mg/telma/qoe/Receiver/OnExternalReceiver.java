package mg.telma.qoe.Receiver;

import android.content.Context;
import android.content.Intent;

import mg.telma.qoe.service.CallTestService;

public class OnExternalReceiver extends com.github.axet.androidlibrary.services.OnExternalReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isExternal(context))
            return;
        CallTestService.startIfEnabled(context);
    }
}
