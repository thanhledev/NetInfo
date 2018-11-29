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

public class TabNetwork extends Fragment {

    // private properties
    private boolean isVisible;
    private boolean isStarted;
    private boolean isFirstShow;
    private Context baseContext;
    private BroadcastReceiver internalReceiver;

    // widgets
    private TextView networkActiveNetworkInfo;
    private TextView networkAllNetworksInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_network, container, false);

        // set isFirstShow
        isFirstShow = true;

        // maps widgets
        networkActiveNetworkInfo = rootView.findViewById(R.id.networkActiveNetworkInfoVal);
        networkAllNetworksInfo = rootView.findViewById(R.id.networkAllNetworksInfoVal);

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
            displayNetworkInformation();
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
            displayNetworkInformation();
        }
    }

    private void displayNetworkInformation() {
        // set isFirstShow
        if(isFirstShow) isFirstShow = false;

        networkActiveNetworkInfo.setText(NetInfo.activeNetworkInfo);
        networkAllNetworksInfo.setText(NetInfo.allNetworkInfo);
    }

    private void setupFragment() {

    }
}
