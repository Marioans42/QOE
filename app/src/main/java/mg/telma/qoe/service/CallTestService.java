package mg.telma.qoe.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import mg.telma.qoe.Constant.Constants;
import mg.telma.qoe.MainActivity;
import mg.telma.qoe.utils.FileHelper;

public class CallTestService extends Service {

    private MediaRecorder recorder;
    private String phoneNumber;
    public static final String TAG = CallTestService.class.getSimpleName();

    private DocumentFile file;
    private boolean onCall = false;
    private boolean recording = false;
    private boolean onForeground = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "RecordService onStartCommand");
        if (intent == null)
            return START_NOT_STICKY;

        int commandType = intent.getIntExtra("commandType", 0);
        if (commandType == 0)
            return START_NOT_STICKY;


        switch (commandType) {
            case Constants.RECORDING_ENABLED:
                Log.d(TAG, "RecordService RECORDING_ENABLED");
                if ( phoneNumber != null && onCall && !recording) {
                    Log.d(TAG, "RecordService STATE_START_RECORDING");
                    startService();
                    startRecording();
                }
                break;
            case Constants.RECORDING_DISABLED:
                Log.d(TAG, "RecordService RECORDING_DISABLED");
                if (onCall && phoneNumber != null && recording) {
                    Log.d(TAG, "RecordService STATE_STOP_RECORDING");
                    stopAndReleaseRecorder();
                    recording = false;
                }
                break;
            case Constants.STATE_INCOMING_NUMBER:
                Log.d(TAG, "RecordService STATE_INCOMING_NUMBER");
                startService();
                if (phoneNumber == null)
                    phoneNumber = intent.getStringExtra("phoneNumber");
                break;
            case Constants.STATE_CALL_START:
                Log.d(TAG, "RecordService STATE_CALL_START");
                onCall = true;

                if (phoneNumber != null && !recording) {
                    startService();
                    startRecording();
                }
                break;
            case Constants.STATE_CALL_END:
                Log.d(TAG, "RecordService STATE_CALL_END");
                onCall = false;
                phoneNumber = null;
                stopAndReleaseRecorder();
                recording = false;
                stopService();
                break;
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "RecordService onDestroy");
        stopAndReleaseRecorder();
        stopService();
        super.onDestroy();
    }


    private void stopService() {
        Log.d(TAG, "RecordService stopService");
        stopForeground(true);
        onForeground = false;
        this.stopSelf();
    }

    private void stopAndReleaseRecorder() {
        if (recorder == null)
            return;
        Log.d(TAG, "RecordService stopAndReleaseRecorder");
        boolean recorderStopped = false;
        boolean exception = false;

        try {
            recorder.stop();
            recorderStopped = true;
        } catch (IllegalStateException e) {
            Log.e(TAG, "Failed to stop recorder.  Perhaps it wasn't started?", e);
            exception = true;
        }
        recorder.reset();
        recorder.release();
        recorder = null;
        if (exception) {
        }
        if (recorderStopped) {
            Toast.makeText(this, "Ended",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void startRecording() {
        Log.d(TAG, "RecordService startRecording");
        recorder = new MediaRecorder();

        try {
            System.out.println("Chemin " +Environment.getDataDirectory());
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            file = FileHelper.getFile(this, phoneNumber);
            ParcelFileDescriptor fd = getContentResolver()
                    .openFileDescriptor(file.getUri(), "rw");
            if (fd == null)
                throw new Exception("Failed open recording file.");
            recorder.setOutputFile(Environment.getDataDirectory()
                    + "/myaudio.3gp");

            recorder.setOnErrorListener((mr, what, extra) -> {
                Log.e(TAG, "OnErrorListener " + what + "," + extra);
            });

            recorder.setOnInfoListener((mr, what, extra) -> {
                Log.e(TAG, "OnInfoListener " + what + "," + extra);
            });

            recorder.prepare();

            // Sometimes the recorder takes a while to start up
            Thread.sleep(2000);

            recorder.start();
            recording = true;

            Log.d(TAG, "RecordService: Recorder started!");
            Toast toast = Toast.makeText(this,
                    "RecordService: Recorder started!",
                    Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            Log.e(TAG, "Failed to set up recorder.", e);
            Toast toast = Toast.makeText(this,
                   "Impossible",
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void startService() {
        if (onForeground)
            return;

        Log.d(TAG, "RecordService startService");
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        onForeground = true;
    }
}
