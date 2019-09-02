package mg.telma.qoe.utils;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.service.quicksettings.Tile;

import mg.telma.qoe.R;
import mg.telma.qoe.app.CallApplication;
import mg.telma.qoe.service.CallTestService;
import mg.telma.qoe.ui.activity.CallTestActivity;

@TargetApi(24)
public class TileService extends android.service.quicksettings.TileService {
    SharedPreferences.OnSharedPreferenceChangeListener receiver = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(CallApplication.PREFERENCE_CALL)) {
                updateTile();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        shared.registerOnSharedPreferenceChangeListener(receiver);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTile();
    }

    void updateTile() {
        Tile tile = getQsTile();
        if (CallTestService.isEnabled(this)) {
            tile.setLabel(getString(R.string.recording_enabled));
            tile.setState(Tile.STATE_ACTIVE);
        } else {
            tile.setLabel(getString(R.string.recording_disabled));
            tile.setState(Tile.STATE_INACTIVE);
        }
        tile.updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        shared.unregisterOnSharedPreferenceChangeListener(receiver);
    }

    @Override
    public void onClick() {
        super.onClick();
        boolean b = !CallTestService.isEnabled(this);
        if(b && !Storage.permitted(this, CallTestActivity.PERMISSIONS)){
            CallTestActivity.startActivity(this, true);
            return;
        }
        CallTestService.setEnabled(this, b);
    }
}
