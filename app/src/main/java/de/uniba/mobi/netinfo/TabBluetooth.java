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
import android.widget.Toast;

public class TabBluetooth extends Fragment {
    private boolean isVisible;
    private boolean isStarted;
    private boolean isFirstShow;
    private Context baseContext;
    private BroadcastReceiver internalReceiver;

    // widgets
    private TextView bluetoothDeviceStateInfo;
    private TextView bluetoothDeviceNameInfo;
    private TextView bluetoothDeviceAddressInfo;
    private TextView bluetoothLE2MPHYInfo;
    private TextView bluetoothLECodedPHYInfo;
    private TextView bluetoothLEExtendedAdvertisingInfo;
    private TextView bluetoothMultiAdvertisementInfo;
    private TextView bluetoothOffloadedFiltersInfo;
    private TextView bluetoothOffloadedScanBatchingInfo;
    private TextView bluetoothPairedDevicesInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_bluetooth, container, false);

        // set isFirstShow
        isFirstShow = true;

        // map widgets
        bluetoothDeviceStateInfo = rootView.findViewById(R.id.bluetoothDeviceStateInfoVal);
        bluetoothDeviceNameInfo = rootView.findViewById(R.id.bluetoothDeviceNameInfoVal);
        bluetoothDeviceAddressInfo = rootView.findViewById(R.id.bluetoothDeviceAddressInfoVal);
        bluetoothLE2MPHYInfo = rootView.findViewById(R.id.bluetoothLE2MPHYInfoVal);
        bluetoothLECodedPHYInfo = rootView.findViewById(R.id.bluetoothLECodedPHYInfoVal);
        bluetoothLEExtendedAdvertisingInfo = rootView.findViewById(R.id.bluetoothLEExtendedAdvertisingInfoVal);
        bluetoothMultiAdvertisementInfo = rootView.findViewById(R.id.bluetoothMultiAdvertisementInfoVal);
        bluetoothOffloadedFiltersInfo = rootView.findViewById(R.id.bluetoothOffloadedFiltersInfoVal);
        bluetoothOffloadedScanBatchingInfo = rootView.findViewById(R.id.bluetoothOffloadedScanBatchingInfoVal);
        bluetoothPairedDevicesInfo = rootView.findViewById(R.id.bluetoothPairedDevicesInfoVal);
        bluetoothPairedDevicesInfo.setMovementMethod(new ScrollingMovementMethod());
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
            displayBluetoothInformation();
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
            displayBluetoothInformation();
        }
    }

    private void displayBluetoothInformation() {
        // set isFirstShow
        if(isFirstShow) isFirstShow = false;

        // get device state
        bluetoothDeviceStateInfo.setText(NetInfo.bluetoothDeviceState);

        // get device name
        bluetoothDeviceNameInfo.setText(NetInfo.bluetoothDeviceName);

        // get device address
        bluetoothDeviceAddressInfo.setText(NetInfo.bluetoothDeviceAddress);

        // get supported features
        bluetoothLE2MPHYInfo.setText(NetInfo.bluetoothLE2MPHY ? "Supported" : "Unavailable");
        bluetoothLECodedPHYInfo.setText(NetInfo.bluetoothLECodedPHY ? "Supported" : "Unavailable");
        bluetoothLEExtendedAdvertisingInfo.setText(NetInfo.bluetoothLEExtendedAdvertising ? "Supported" : "Unavailable");
        bluetoothMultiAdvertisementInfo.setText(NetInfo.bluetoothMultiAdvertisement ? "Supported" : "Unavailable");
        bluetoothOffloadedFiltersInfo.setText(NetInfo.bluetoothOffloadedFilters ? "Supported" : "Unavailable");
        bluetoothOffloadedScanBatchingInfo.setText(NetInfo.bluetoothOffloadedScanBatching ? "Supported" : "Unavailable");

        // get paired devices
        bluetoothPairedDevicesInfo.setText(NetInfo.bluetoothPairedDevicesInfo);

        Toast.makeText(getActivity(), "Show bluetooth information", Toast.LENGTH_SHORT).show();
    }

    private void setupFragment() {

    }
}
