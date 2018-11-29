package de.uniba.mobi.netinfo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private static final int PERMISSION_FOR_NET_INFO = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askPermissions(true);
        } else {
            loadNetInfo();
        }
    }

    private boolean checkPermission(List<String> permissionsList, String permission) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                return false;
            }
        }
        return true;
    }

    private void askPermissions(boolean isForOpen) {
        List permissionsRequired = new ArrayList();

        final List<String> permissionsList = new ArrayList<>();

        if(!checkPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
            permissionsRequired.add("Read Phone State");
        }
        if(!checkPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                !checkPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionsRequired.add("Location");
        }

        if(permissionsList.size() > 0) {
            if(isForOpen) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(this, permissionsList.toArray(
                            new String[permissionsList.size()]), PERMISSION_FOR_NET_INFO);
                }
            }
        } else {
            loadNetInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_FOR_NET_INFO:
                Map<String, Integer> perms = new HashMap<>();
                // initialize
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);;

                // fill with results
                for(int i = 0; i< permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }

                // check for all permissions
                if(perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    loadNetInfo();
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loadNetInfo() {

        NetInfo.getInstance().setActivity(this);
        NetInfo.getInstance().getMobileInformation();
        NetInfo.getInstance().getWifiInformation();
        NetInfo.getInstance().getNetworkInformation();
        NetInfo.getInstance().getBluetoothInformation();

        runOnUiThread(() -> {
            Intent newIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(newIntent);

            finish();
        });
    }
}
