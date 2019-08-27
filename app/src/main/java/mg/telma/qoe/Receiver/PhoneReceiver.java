package mg.telma.qoe.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import mg.telma.qoe.Constant.Constants;
import mg.telma.qoe.service.CallTestService;
import mg.telma.qoe.utils.FileHelper;

public class PhoneReceiver extends BroadcastReceiver {
    public static final String TAG = PhoneReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED) &&
                !action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.e(TAG, "PhoneReceiver: Received unexpected intent: " + action);
            return;
        }

        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        String extraState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.d(TAG, "PhoneReceiver phone number: " + phoneNumber);

        if (!FileHelper.isStorageWritable(context))
            return;

        if (extraState != null) {
            dispatchExtra(context, intent, phoneNumber, extraState);
        } else if (phoneNumber != null) {
            context.startService(new Intent(context, CallTestService.class)
                    .putExtra("commandType", Constants.STATE_INCOMING_NUMBER)
                    .putExtra("phoneNumber", phoneNumber));
        }
    }

    private void dispatchExtra(Context context, Intent intent,
                               String phoneNumber, String extraState) {
        if (extraState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            context.startService(new Intent(context, CallTestService.class)
                    .putExtra("commandType", Constants.STATE_CALL_START));
        } else if (extraState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            context.startService(new Intent(context, CallTestService.class)
                    .putExtra("commandType", Constants.STATE_CALL_END));
        } else if (extraState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            if (phoneNumber == null)
                phoneNumber = intent.getStringExtra(
                        TelephonyManager.EXTRA_INCOMING_NUMBER);

            context.startService(new Intent(context, CallTestService.class)
                    .putExtra("commandType", Constants.STATE_INCOMING_NUMBER)
                    .putExtra("phoneNumber", phoneNumber));
        }
    }
}
