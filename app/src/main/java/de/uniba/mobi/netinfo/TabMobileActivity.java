package de.uniba.mobi.netinfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class TabMobileActivity extends Fragment {

    private boolean isVisible;
    private boolean isStarted;

    // get widgets
    private TextView mobileDataState;
    private TextView mobilePhoneType;
    private TextView mobileNetworkType;
    private EditText mobileNeighboringCellInfo;
    private TextView mobileMCC;
    private TextView mobileMNC;
    private TextView mobileNetworkOperatorName;
    private TextView mobileSIMOperatorName;
    private TextView mobilePhoneNumber;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_mobile, container, false);

        // map widgets
        mobileDataState = rootView.findViewById(R.id.mobileDataStateVal);
        mobilePhoneType = rootView.findViewById(R.id.mobilePhoneTypeVal);
        mobileNeighboringCellInfo = rootView.findViewById(R.id.mobileNeighboringCellInfoVal);
        mobileNetworkType = rootView.findViewById(R.id.mobileNetworkTypeVal);
        mobileMCC = rootView.findViewById(R.id.mobileMCCVal);
        mobileMNC = rootView.findViewById(R.id.mobileMNCVal);
        mobileNetworkOperatorName = rootView.findViewById(R.id.mobileNetworkOperatorNameVal);
        mobileSIMOperatorName = rootView.findViewById(R.id.mobileSIMOperatorNameVal);
        mobilePhoneNumber = rootView.findViewById(R.id.mobilePhoneNumber);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if(isVisible) {
            displayMobileInformation();
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
        if(isVisibleToUser && isStarted) {
            displayMobileInformation();
        }
    }

    private void displayMobileInformation() {

        // data state
        mobileDataState.setText(getDataState());

        // phone type
        mobilePhoneType.setText(getPhoneType());

        // network type
        mobileNetworkType.setText(getNetworkClass());

        // neighboring cell info
        mobileNeighboringCellInfo.setText(NetInfo.getInstance().neighborCellsInfo);

        // mobile country code
        mobileMCC.setText(NetInfo.getInstance().networkCountryISO);

        // mobile network code
        mobileMNC.setText(NetInfo.getInstance().networkOperator);

        // network operator name
        mobileNetworkOperatorName.setText(NetInfo.getInstance().networkOperatorName);

        // sim operator name
        mobileSIMOperatorName.setText(NetInfo.getInstance().SIMOperatorName);

        // phone number
        mobilePhoneNumber.setText(NetInfo.getInstance().lineNumber);
    }

    private String getDataState() {
        switch (NetInfo.getInstance().dataState) {
            case (TelephonyManager.DATA_DISCONNECTED):
                return "Disconnected";
            case (TelephonyManager.DATA_CONNECTED):
                return "Connected";
            case (TelephonyManager.DATA_CONNECTING):
                return "Connecting";
            case (TelephonyManager.DATA_SUSPENDED):
                return "Suspended";
            default:
                return null;
        }
    }

    private String getPhoneType() {
        switch (NetInfo.getInstance().phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                return "CDMA";
            case (TelephonyManager.PHONE_TYPE_GSM):
                return "GSM";
            case (TelephonyManager.PHONE_TYPE_NONE):
                return "Not available";
            default:
                return "Unknown";
        }
    }

    private String getNetworkClass() {
        switch (NetInfo.getInstance().networkType) {
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "Unknown network";
            case TelephonyManager.NETWORK_TYPE_GSM:
                return " GSM";
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return " 2G";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return " GPRS (2.5G)";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return " EDGE (2.75G)";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return " 3G";
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return " H (3G+)";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return " H+ (3G++)";
            case TelephonyManager.NETWORK_TYPE_LTE:
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                return " 4G";
            default:
                return " 4G+";
        }
    }
}
