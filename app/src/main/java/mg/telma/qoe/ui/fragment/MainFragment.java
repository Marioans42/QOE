package mg.telma.qoe.ui.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mg.telma.qoe.R;
import mg.telma.qoe.service.LocationService;
import mg.telma.qoe.utils.Reseau;


public class MainFragment extends Fragment {

    @BindView(R.id.mcc)
    TextView mcc;

    @BindView(R.id.mnc)
    TextView mnc;

    @BindView(R.id.cell_id)
    TextView cellId;

    @BindView(R.id.lac)
    TextView lac;

    @BindView(R.id.niv_signal)
    TextView signalLvl;

    @BindView(R.id.longitude)
    TextView longitude;

    @BindView(R.id.latitude)
    TextView latitude;

    @BindView(R.id.qualite_signal)
    TextView signalQuality;

    @BindView(R.id.msisdn)
    TextView msisdn;

    @BindView(R.id.imsi)
    TextView imsi;

    @BindView(R.id.imei)
    TextView imei;

    private Unbinder unbinder;
    private static final int REQUEST_LOCATION = 0;
    private static final int REQUEST_PHONE_STATE = 1;
    int signalStrengthValue ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        WifiManager wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Switch dataSwitch = view.findViewById(R.id.switch_data);
        dataSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                wifi.setWifiEnabled(true);
            } else {
                wifi.setWifiEnabled(false);
            }

        });


        if (dataSwitch.isChecked()) {
            wifi.setWifiEnabled(true);
        } else {
            wifi.setWifiEnabled(false);
        }
        getInfoCellular();
        getLocation();
        return view;
    }

    public void getInfoCellular() {

        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
          /*  ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);*/
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_PHONE_STATE);

            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager.getCellLocation();
        mcc.setText(telephonyManager.getNetworkOperator().substring(0, 3));
        mnc.setText(telephonyManager.getNetworkOperator().substring(3));
        cellId.setText(String.valueOf(cellLocation.getCid()));
        lac.setText(String.valueOf(cellLocation.getLac()));
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                if (signalStrength.isGsm()) {
                    if (signalStrength.getGsmSignalStrength() != 99)
                        signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
                    else
                        signalStrengthValue = signalStrength.getGsmSignalStrength();
                } else {
                    signalStrengthValue = signalStrength.getCdmaDbm();
                }
                signalLvl.setText(String.valueOf(signalStrengthValue));
            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        signalQuality.setText(Reseau.getNetworkClass(telephonyManager));


       
            imei.setText(telephonyManager.getDeviceId());


        imsi.setText(telephonyManager.getSubscriberId());

    }

    public void getLocation() {
        LocationService locationService = new LocationService(getContext());
        locationService.getLocation(getActivity());
        longitude.setText(String.valueOf(locationService.getLongitude()));
        latitude.setText(String.valueOf(locationService.getLatitude()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permission denied",
                            Toast.LENGTH_SHORT).show();

                }
            case REQUEST_PHONE_STATE:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permission denied",
                            Toast.LENGTH_SHORT).show();

                }
        }


    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
