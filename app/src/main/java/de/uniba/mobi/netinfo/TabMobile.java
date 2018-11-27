package de.uniba.mobi.netinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class TabMobile extends Fragment {

    // private properties
    private boolean isVisible;
    private boolean isStarted;
    private boolean isFirstShow;
    private Context baseContext;
    private BroadcastReceiver internalReceiver;
    // get widgets
    private TextView mobileDataState;
    private TextView mobilePhoneType;
    private TextView mobileNetworkType;
    private TextView mobileNeighboringCellInfo;
    private TextView mobileCellLocationInfo;
    private TextView mobileMCC;
    private TextView mobileMNC;
    private TextView mobileNetworkOperatorName;
    private TextView mobileSIMOperatorName;
    private TextView mobilePhoneNumber;

    // broadcast

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_mobile, container, false);

        // map widgets
        mobileDataState = rootView.findViewById(R.id.mobileDataStateVal);
        mobilePhoneType = rootView.findViewById(R.id.mobilePhoneTypeVal);
        mobileNeighboringCellInfo = rootView.findViewById(R.id.mobileNeighboringCellInfoVal);
        mobileNeighboringCellInfo.setMovementMethod(new ScrollingMovementMethod());
        mobileCellLocationInfo = rootView.findViewById(R.id.mobileCellLocationInfoVal);
        mobileCellLocationInfo.setMovementMethod(new ScrollingMovementMethod());
        mobileNetworkType = rootView.findViewById(R.id.mobileNetworkTypeVal);
        mobileMCC = rootView.findViewById(R.id.mobileMCCVal);
        mobileMNC = rootView.findViewById(R.id.mobileMNCVal);
        mobileNetworkOperatorName = rootView.findViewById(R.id.mobileNetworkOperatorNameVal);
        mobileSIMOperatorName = rootView.findViewById(R.id.mobileSIMOperatorNameVal);
        mobilePhoneNumber = rootView.findViewById(R.id.mobilePhoneNumber);

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
        if(isVisible && !isFirstShow) {
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
        if(isVisibleToUser && isStarted && !isFirstShow) {
            displayMobileInformation();
        }
    }

    private void displayMobileInformation() {
        // set first load
        isFirstShow = true;

        // start OpenCellIdService
        ((MainActivity)getActivity()).startService();

        // data state
        mobileDataState.setText(NetInfo.getInstance().getDataState());

        // phone type
        mobilePhoneType.setText(NetInfo.getInstance().getPhoneType());

        // network type
        mobileNetworkType.setText(NetInfo.getInstance().getNetworkClass());

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

    private void setupFragment() {
        internalReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            mobileCellLocationInfo.append(intent.getStringExtra("Updated") + "\n" +
                intent.getStringExtra("OpenCellResp") + "\n");
            }
        };

        baseContext.registerReceiver(internalReceiver, new IntentFilter(MainActivity.mMobileInternalOpenCellIdAction));
    }
}
