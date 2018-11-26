package de.uniba.mobi.netinfo;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

import de.uniba.mobi.netinfo.readers.CellInfoReader;
import de.uniba.mobi.netinfo.readers.cdma.CdmaCellInfoReader;
import de.uniba.mobi.netinfo.readers.gsm.GsmCellInfoReader;
import de.uniba.mobi.netinfo.readers.lte.LteCellInfoReader;
import de.uniba.mobi.netinfo.readers.unknown.UnknownCellInfoReader;
import de.uniba.mobi.netinfo.readers.wcdma.WcdmaCellInfoReader;

public class NetInfo {

    private Handler mHanlder = new Handler();

    // public static variables
    public static String IMEINumber;
    public static String subscriberId;
    public static String SIMSerialNumber;
    public static String lineNumber;
    public static String networkCountryISO;
    public static String networkOperator;
    public static String networkOperatorName;
    public static String SIMCountryISO;
    public static String softwareVersion;
    public static String voiceMailNumber;
    public static int dataState;
    public static int phoneType;
    public static int networkType;
    public static boolean isRoaming;
    public static int SIMState;
    public static String SIMOperatorName;
    public static String neighborCellsInfo;

    // private static instance variable
    private static NetInfo instance;
    private static final String APP_TAG = "NetInfo";

    // private variables
    private Activity parentActivity;
    private static TelephonyManager telephonyManager;

    private NetInfo() {

    }

    public static NetInfo getInstance() {
        if(instance == null) {
            synchronized (NetInfo.class) {
                if(instance == null) {
                    instance = new NetInfo();
                }
            }
        }

        return instance;
    }

    @SuppressWarnings("MissingPermission")
    public void getInformation() {

        telephonyManager = (TelephonyManager) this.parentActivity.getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        // map values
        dataState = telephonyManager.getDataState();
        phoneType = telephonyManager.getPhoneType();
        networkType = telephonyManager.getNetworkType();

        IMEINumber = telephonyManager.getDeviceId();
        subscriberId = telephonyManager.getSubscriberId();
        SIMSerialNumber = telephonyManager.getSimSerialNumber();
        lineNumber = telephonyManager.getLine1Number();
        networkCountryISO = telephonyManager.getNetworkCountryIso();
        networkOperator = telephonyManager.getNetworkOperator();
        networkOperatorName = telephonyManager.getNetworkOperatorName();
        SIMCountryISO = telephonyManager.getSimCountryIso();
        softwareVersion = telephonyManager.getDeviceSoftwareVersion();
        voiceMailNumber = telephonyManager.getVoiceMailNumber();

        isRoaming = telephonyManager.isNetworkRoaming();
        SIMState = telephonyManager.getSimState();

        switch (SIMState) {
            case TelephonyManager.SIM_STATE_UNKNOWN:
                SIMOperatorName = "Unknown";
                break;
            case TelephonyManager.SIM_STATE_ABSENT:
                SIMOperatorName = "Absent";
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                SIMOperatorName = "PIN required";
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                SIMOperatorName = "PUK required";
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                SIMOperatorName = "Network locked";
                break;
            case TelephonyManager.SIM_STATE_NOT_READY:
                SIMOperatorName = "SIM not ready";
                break;
            case TelephonyManager.SIM_STATE_READY:
                SIMOperatorName = telephonyManager.getSimOperatorName();
                break;
            case TelephonyManager.SIM_STATE_PERM_DISABLED:
                SIMOperatorName = "PERM disabled";
                break;
            case TelephonyManager.SIM_STATE_CARD_IO_ERROR:
                SIMOperatorName = "SIM IO error";
                break;
            case TelephonyManager.SIM_STATE_CARD_RESTRICTED:
                SIMOperatorName = "SIM restricted";
                break;
        }

        // load cell info
        neighborCellsInfo = "List of all neighbor cells\n";
        try {
            List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();

            neighborCellsInfo += "Neighbor cells: " + cellInfoList.size() + "\n";
            int count = 1;

            for (final CellInfo info : cellInfoList) {
                neighborCellsInfo += "Processing Cell #" + count + "\n";
                neighborCellsInfo += "------------------------------\n";
                CellInfoReader reader;
                if(info instanceof CellInfoGsm) {
                    reader = new GsmCellInfoReader((CellInfoGsm)info, Build.VERSION.SDK_INT);
                } else if (info instanceof  CellInfoCdma) {
                    reader = new CdmaCellInfoReader((CellInfoCdma)info, Build.VERSION.SDK_INT);
                } else if (info instanceof  CellInfoLte) {
                    reader = new LteCellInfoReader((CellInfoLte)info, Build.VERSION.SDK_INT);
                } else if (info instanceof CellInfoWcdma && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    reader = new WcdmaCellInfoReader((CellInfoWcdma)info, Build.VERSION.SDK_INT);
                } else {
                    reader = new UnknownCellInfoReader();
                }

                neighborCellsInfo += reader.readIdentityInfo();
                neighborCellsInfo += reader.readSignalStrengthInfo();
                neighborCellsInfo += "------------------------------\n";
                count++;
            }
        } catch (NullPointerException npe) {
            Log.e(APP_TAG, npe.getMessage());
        }
    }

    public void setActivity(Activity activity) {
        this.parentActivity = activity;
    }
}
