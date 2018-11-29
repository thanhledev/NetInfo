package de.uniba.mobi.netinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabWifi extends Fragment {
    private boolean isVisible;
    private boolean isStarted;
    private boolean isFirstShow;
    private Context baseContext;
    private BroadcastReceiver internalReceiver;

    // get widgets
    private TextView wifiConnectionState;
    private TextView wifiIpAddress;
    private TextView wifiMacAddress;
    private TextView wifiLinkSpeed;
    private TextView wifiFrequency;
    private TextView wifiSSID;
    private TextView wifiBSSID;
    private TextView wifiRSSI;
    private TextView wifiDHCPInfo;
    private TextView wifiConfiguredNetworksInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_wifi, container, false);

        // set isFirstShow
        isFirstShow = true;

        // map widgets
        wifiConnectionState = rootView.findViewById(R.id.wifiConnectionStateVal);
        wifiIpAddress = rootView.findViewById(R.id.wifiIpAddressVal);
        wifiMacAddress = rootView.findViewById(R.id.wifiMacAddressVal);
        wifiLinkSpeed = rootView.findViewById(R.id.wifiLinkSpeedVal);
        wifiFrequency = rootView.findViewById(R.id.wifiFrequencyVal);
        wifiSSID = rootView.findViewById(R.id.wifiSSIDVal);
        wifiBSSID = rootView.findViewById(R.id.wifiBSSIDVal);
        wifiRSSI = rootView.findViewById(R.id.wifiRSSIVal);
        wifiDHCPInfo = rootView.findViewById(R.id.wifiDHCPInfoVal);
        wifiDHCPInfo.setMovementMethod(new ScrollingMovementMethod());
        wifiConfiguredNetworksInfo = rootView.findViewById(R.id.wifiConfiguredNetworksInfoVal);
        wifiConfiguredNetworksInfo.setMovementMethod(new ScrollingMovementMethod());

        // setup fragment broadcast receiver
        setupFragment();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if(isVisible && isFirstShow) {
            displayWifiInformation();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if(isVisibleToUser && isStarted && isFirstShow) {
            displayWifiInformation();
        }
    }

    private void displayWifiInformation() {
        // set isFirstShow
        if(isFirstShow) isFirstShow = false;

        // get wifi state
        wifiConnectionState.setText(NetInfo.currentWifiState);

        // get ip address
        wifiIpAddress.setText(NetInfo.internalIpAddress);

        // get mac address
        wifiMacAddress.setText(NetInfo.macAddress);

        // get link speed
        wifiLinkSpeed.setText(NetInfo.linkSpeed != Integer.MAX_VALUE ?
            String.format("%d Mbps", NetInfo.linkSpeed) : "Unavailable");

        // get frequency
        wifiFrequency.setText(NetInfo.frequency != Integer.MAX_VALUE ?
                String.format("%d MHz", NetInfo.frequency) : "Unavailable");

        // get SSID
        wifiSSID.setText(NetInfo.serviceSetIdentifier);

        // get BSSID
        wifiBSSID.setText(NetInfo.basicServiceSetIdentifier);

        // get RSSI
        wifiRSSI.setText(String.format("%d dBm", NetInfo.receivedSignalStrengthIndicator));

        // get DHCP
        wifiDHCPInfo.setText(NetInfo.dhcpInfo);

        // get configured networks
        wifiConfiguredNetworksInfo.setText(NetInfo.configuredNetworksInfo);
    }

    private void setupFragment() {

    }


}
