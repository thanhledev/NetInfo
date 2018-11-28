package de.uniba.mobi.netinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class TabBluetooth extends Fragment {
    private boolean isVisible;
    private boolean isStarted;
    private boolean isFirstShow;
    private Context baseContext;
    private BroadcastReceiver internalReceiver;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_bluetooth, container, false);

        // set isFirstShow
        isFirstShow = true;

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
        isFirstShow = false;

        Toast.makeText(getActivity(), "Show bluetooth information", Toast.LENGTH_SHORT).show();
    }

    private void setupFragment() {

    }
}
