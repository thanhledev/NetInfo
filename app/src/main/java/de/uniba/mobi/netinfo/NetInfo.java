package de.uniba.mobi.netinfo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.telecom.Connection;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import de.uniba.mobi.netinfo.readers.CellInfoReader;
import de.uniba.mobi.netinfo.readers.cdma.CdmaCellInfoReader;
import de.uniba.mobi.netinfo.readers.gsm.GsmCellInfoReader;
import de.uniba.mobi.netinfo.readers.lte.LteCellInfoReader;
import de.uniba.mobi.netinfo.readers.unknown.UnknownCellInfoReader;
import de.uniba.mobi.netinfo.readers.wcdma.WcdmaCellInfoReader;

public class NetInfo {

    private Handler mHanlder = new Handler();

    // private & public static variables for mobile information
    private static TelephonyManager telephonyManager;
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
    private static int dataState;
    public static String mobileDataState;
    private static int phoneType;
    public static String mobilePhoneType;
    private static int networkType;
    public static String mobileNetworkType;
    public static boolean isRoaming;
    public static int SIMState;
    public static String SIMOperatorName;
    public static String neighborCellsInfo;

    // mobile cell location
    public static String mobileCountryCode;
    public static String mobileNetworkCode;
    public static int locationAreaCode;
    public static int cellIdentifier;

    // private & public variables for wifi information
    private static WifiManager wifiManager;
    private static ConnectivityManager connectivityManager;

    public static int wifiState;

    // internal connection information
    public static String internalIpAddress;
    public static String macAddress;
    public static int linkSpeed;
    public static String serviceSetIdentifier;
    public static String basicServiceSetIdentifier;
    public static int frequency;
    public static int receivedSignalStrengthIndicator;

    // dhcp info
    public static String dnsServer1;
    public static String dnsServer2;
    public static String gateway;
    public static int leaseDuration;
    public static String netmask;
    public static String dhcpServerAddress;

    // configured network info
    public static String configuredNetworksInfo;

    // private static instance variable
    private static NetInfo instance;
    private static final String APP_TAG = "NetInfo";

    // private variables
    private Activity parentActivity;

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

    public void setActivity(Activity activity) {
        this.parentActivity = activity;
    }

    /**
     * Network methods
     */

    @SuppressWarnings("MissingPermission")
    public void getNetworkInformation() {

        telephonyManager = (TelephonyManager) this.parentActivity.getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        // map values
        dataState = telephonyManager.getDataState();
        mobileDataState = getDeviceDataState();
        phoneType = telephonyManager.getPhoneType();
        mobilePhoneType = getDevicePhoneType();
        networkType = telephonyManager.getNetworkType();
        mobileNetworkType = getDeviceNetworkType();

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

        // load cellInfo
        getCellInfo();

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

        // load wifi info
        getWifiInformation();
        // load bluetooth info

        // load device info
    }

    public void getCellInfo() {
        if(phoneType == TelephonyManager.PHONE_TYPE_GSM) {
            // get cell information of a gsm phone
            getGSMCellInfo();
        } else if(phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
            // get cell information of a cdma phone
            getCDMACellInfo();
        } else {
            // unknown
            mobileCountryCode = "Unknown";
            mobileNetworkCode = "Unknown";
            locationAreaCode = Integer.MAX_VALUE;
            cellIdentifier = Integer.MAX_VALUE;
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getGSMCellInfo() {
        try {
            GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager.getCellLocation();

            if(gsmCellLocation == null) {
                mobileCountryCode = "Unknown";
                mobileNetworkCode = "Unknown";
                locationAreaCode = Integer.MAX_VALUE;
                cellIdentifier = Integer.MAX_VALUE;
            } else {
                locationAreaCode = gsmCellLocation.getLac();
                cellIdentifier = gsmCellLocation.getCid();

                // get mcc and mnc codes
                mobileCountryCode = telephonyManager.getNetworkOperator().substring(0,3);
                mobileNetworkCode = telephonyManager.getNetworkOperator().substring(3);
            }

        } catch (Exception e) {
            Log.e(APP_TAG, e.getMessage());
            mobileCountryCode = "Unknown";
            mobileNetworkCode = "Unknown";
            locationAreaCode = Integer.MAX_VALUE;
            cellIdentifier = Integer.MAX_VALUE;
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getCDMACellInfo() {
        try {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager.getCellLocation();

            if(cdmaCellLocation == null && SIMState != TelephonyManager.SIM_STATE_READY) {
                mobileCountryCode = "Unknown";
                mobileNetworkCode = "Unknown";
                locationAreaCode = Integer.MAX_VALUE;
                cellIdentifier = Integer.MAX_VALUE;
            } else {
                locationAreaCode = cdmaCellLocation.getNetworkId();
                cellIdentifier = cdmaCellLocation.getBaseStationId();
                cellIdentifier /= 16;

                // get mcc and mnc via sim operator
                mobileCountryCode = telephonyManager.getSimOperator().substring(0,3);
                mobileNetworkCode = telephonyManager.getSimOperator().substring(3);
            }

        } catch (Exception e) {
            Log.e(APP_TAG, e.getMessage());
            mobileCountryCode = "Unknown";
            mobileNetworkCode = "Unknown";
            locationAreaCode = Integer.MAX_VALUE;
            cellIdentifier = Integer.MAX_VALUE;
        }
    }

    public boolean isLocationEnabled() {
        if(mobileCountryCode == "Unknown" || mobileNetworkCode == "Unknown" ||
                locationAreaCode == Integer.MAX_VALUE || cellIdentifier == Integer.MAX_VALUE) {
            return false;
        }
        return true;
    }

    private String getDeviceDataState() {
        switch (dataState) {
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

    private String getDevicePhoneType() {
        switch (phoneType) {
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

    private String getDeviceNetworkType() {
        switch (networkType) {
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

    /**
     * Wifi methods
     */

    public void getWifiInformation() {

        // init managers
        connectivityManager = (ConnectivityManager) this.parentActivity.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        wifiManager = (WifiManager) this.parentActivity.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        // get wifi state
        wifiState = wifiManager.getWifiState();

        if(wifiState == WifiManager.WIFI_STATE_ENABLED) {
            // based on current device api
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                nwGetConnectedWifiInfo();
            } else {
                owGetConnectedWifiInfo();
            }
        } else {
            setWifiInformationUnknown();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void owGetConnectedWifiInfo() {
        // check wifi connected
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(!mWifi.isConnected()) {
            setWifiInformationUnknown();
        } else {
            WifiInfo connectedInfo = wifiManager.getConnectionInfo();

            // connection information
            internalIpAddress = convertToIpAddress(connectedInfo.getIpAddress());
            macAddress = connectedInfo.getMacAddress();
            linkSpeed = connectedInfo.getLinkSpeed();
            serviceSetIdentifier = connectedInfo.getSSID();
            basicServiceSetIdentifier = connectedInfo.getBSSID();
            receivedSignalStrengthIndicator = connectedInfo.getRssi();
            frequency = Integer.MAX_VALUE;

            // dhcp info
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

            dnsServer1 = convertToIpAddress(dhcpInfo.dns1);
            dnsServer2 = convertToIpAddress(dhcpInfo.dns2);
            gateway = convertToIpAddress(dhcpInfo.gateway);
            leaseDuration = dhcpInfo.leaseDuration;
            netmask = convertToIpAddress(dhcpInfo.netmask);
            dhcpServerAddress = convertToIpAddress(dhcpInfo.serverAddress);

            // configured networks
            configuredNetworksInfo = getConfiguredNetworksInfo(Build.VERSION.SDK_INT);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void nwGetConnectedWifiInfo() {
        // check wifi connected
        Network[] networks = connectivityManager.getAllNetworks();

        if(networks == null || networks.length == 0) {
            setWifiInformationUnknown();
        } else {
            for (int i = 0; i < networks.length; i++) {
                Network net = networks[i];
                NetworkInfo netInfo = connectivityManager.getNetworkInfo(net);

                if(netInfo.getType() == ConnectivityManager.TYPE_WIFI && netInfo.isConnected()) {
                    WifiInfo connectedInfo = wifiManager.getConnectionInfo();

                    // connection information
                    internalIpAddress = convertToIpAddress(connectedInfo.getIpAddress());
                    macAddress = getDeviceMacAddr();
                    linkSpeed = connectedInfo.getLinkSpeed();
                    serviceSetIdentifier = connectedInfo.getSSID();
                    basicServiceSetIdentifier = connectedInfo.getBSSID();
                    receivedSignalStrengthIndicator = connectedInfo.getRssi();
                    frequency = connectedInfo.getFrequency();

                    // dhcp information
                    DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

                    dnsServer1 = convertToIpAddress(dhcpInfo.dns1);
                    dnsServer2 = convertToIpAddress(dhcpInfo.dns2);
                    gateway = convertToIpAddress(dhcpInfo.gateway);
                    leaseDuration = dhcpInfo.leaseDuration;
                    netmask = convertToIpAddress(dhcpInfo.netmask);
                    dhcpServerAddress = convertToIpAddress(dhcpInfo.serverAddress);

                    // configured networks
                    configuredNetworksInfo = getConfiguredNetworksInfo(Build.VERSION.SDK_INT);
                }
            }
        }
    }

    private void setWifiInformationUnknown() {
        internalIpAddress = macAddress = serviceSetIdentifier = basicServiceSetIdentifier =
                 dnsServer1 = dnsServer2 = gateway = netmask = dhcpServerAddress = "Unknown";
        linkSpeed = frequency = receivedSignalStrengthIndicator = leaseDuration = Integer.MAX_VALUE;
    }

    private String convertToIpAddress(int address) {
        return String.format("%d.%d.%d.%d",
                (address & 0xff),
                (address >> 8 & 0xff),
                (address >> 16 & 0xff),
                (address >> 24 & 0xff));
    }

    private String getDeviceMacAddr() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface nInf : interfaces) {
                if(!nInf.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nInf.getHardwareAddress();
                if(macBytes == null) {
                    return "Unavailable";
                }

                StringBuilder sb = new StringBuilder();
                for (byte b: macBytes) {
                    sb.append(String.format("%02X:", b));
                }

                if(sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }

                return sb.toString();
            }
        } catch (Exception e) { }
        return "02:00:00:00:00:00";
    }

    private String getConfiguredNetworksInfo(int apiLevel) {
        String info = "";

        List<WifiConfiguration> wifiConfigs = wifiManager.getConfiguredNetworks();

        if(wifiConfigs.size() > 0) {
            info += "Found " + wifiConfigs.size() + " configured network(s).\n";

            int count = 1;

            for(WifiConfiguration config: wifiConfigs) {
                info += "Detail information of network#" + count + ":\n";
                info += "BSSID:       " + config.BSSID + " \n";
                if(apiLevel >= Build.VERSION_CODES.LOLLIPOP) {
                    info += "FQDN:        " + config.FQDN + " \n";
                }
                info += "SSID:        " + config.SSID + " \n";
                info += "NetworkId:   " + config.networkId + " \n";
                switch (config.status) {
                    case WifiConfiguration.Status.CURRENT:
                        info += "Status:   Connected \n";
                        break;
                    case WifiConfiguration.Status.ENABLED:
                        info += "Status:   Enabled \n";
                        break;
                    default:
                        info += "Status:   Disabled \n";
                        break;
                }
                info += "--------------------------------\n";
            }

        } else {
            info += "Not found any configured networks\n";
        }

        return info;
    }
}
