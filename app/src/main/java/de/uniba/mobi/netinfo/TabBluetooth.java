package de.uniba.mobi.netinfo;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_bluetooth, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if(isVisible) {
            notifyUser();
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
            notifyUser();
        }
    }

    private void notifyUser() {
        Toast.makeText(getActivity(), "Show bluetooth information", Toast.LENGTH_SHORT).show();
    }
}
