package mg.telma.qoe.Listener;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

public class PhoneListener extends PhoneStateListener {
    private int signalStrengthValue;

    public int getSignalStrengthValue() {
        return signalStrengthValue;
    }

    public void setSignalStrengthValue(int signalStrengthValue) {
        this.signalStrengthValue = signalStrengthValue;
    }

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        if (signalStrength.isGsm()) {
            if (signalStrength.getGsmSignalStrength() != 99)
                this.setSignalStrengthValue(signalStrength.getGsmSignalStrength() * 2 - 113);
            else
                this.setSignalStrengthValue(signalStrength.getCdmaDbm());
        } else {
            this.setSignalStrengthValue(signalStrength.getCdmaDbm());
        }

    }
}
