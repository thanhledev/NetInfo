package de.uniba.mobi.netinfo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.uniba.mobi.netinfo.readers.CellInfoReader;
import de.uniba.mobi.netinfo.readers.cdma.CdmaCellInfoReader;
import de.uniba.mobi.netinfo.readers.gsm.GsmCellInfoReader;
import de.uniba.mobi.netinfo.readers.lte.LteCellInfoReader;
import de.uniba.mobi.netinfo.readers.unknown.UnknownCellInfoReader;
import de.uniba.mobi.netinfo.readers.wcdma.WcdmaCellInfoReader;

public class NetInfo {

    private Handler mHanlder = new Handler();

    // system managers
    private static WifiManager wifiManager;
    private static ConnectivityManager connectivityManager;
    private static TelephonyManager telephonyManager;
    private static BluetoothManager bluetoothManager;
    private static BluetoothAdapter bluetoothAdapter;

    // private & public static variables for mobile information
    public static String IMEINumber = "";
    public static String subscriberId = "";
    public static String SIMSerialNumber = "";
    public static String lineNumber = "";
    public static String networkCountryISO = "";
    public static String networkOperator = "";
    public static String networkOperatorName = "";
    public static String SIMCountryISO = "";
    public static String softwareVersion = "";
    public static String voiceMailNumber = "";
    private static int dataState;
    public static String mobileDataState = "";
    private static int phoneType;
    public static String mobilePhoneType = "";
    private static int networkType;
    public static String mobileNetworkType = "";
    public static boolean isRoaming;
    public static int SIMState;
    public static String SIMOperatorName = "";
    public static String neighborCellsInfo = "";

    // mobile cell location
    public static String mobileCountryCode = "";
    public static String mobileNetworkCode = "";
    public static int locationAreaCode;
    public static int cellIdentifier;

    // private & public variables for wifi information
    private static int wifiState;
    public static String currentWifiState = "";

    // internal connection information
    public static String internalIpAddress = "";
    public static String macAddress = "";
    public static int linkSpeed;
    public static String serviceSetIdentifier = "";
    public static String basicServiceSetIdentifier = "";
    public static int frequency;
    public static int receivedSignalStrengthIndicator;

    // dhcp info
    public static String dhcpInfo = "";
    private static String dnsServer1 = "";
    private static String dnsServer2 = "";
    private static String gateway = "";
    private static int leaseDuration;
    private static String netmask = "";
    private static String dhcpServer = "";

    // configured network info
    public static String configuredNetworksInfo = "";

    // private & public variables for network information
    public static String activeNetworkInfo = "";
    public static String allNetworkInfo = "";

    // private & public variables for bluetooth information
    public static String bluetoothDeviceState = "";
    public static String bluetoothDeviceName = "";
    public static String bluetoothDeviceAddress = "";
    public static boolean bluetoothLE2MPHY;
    public static boolean bluetoothLECodedPHY;
    public static boolean bluetoothLEExtendedAdvertising;
    public static boolean bluetoothMultiAdvertisement;
    public static boolean bluetoothOffloadedFilters;
    public static boolean bluetoothOffloadedScanBatching;
    public static String bluetoothPairedDevicesInfo = "";

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
     * Mobile methods
     */

    @SuppressWarnings("MissingPermission")
    public void getMobileInformation() {

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
            try {
                mobileCountryCode = telephonyManager.getNetworkOperator().substring(0, 3);
                mobileNetworkCode = telephonyManager.getNetworkOperator().substring(3);
            } catch (Exception e) {
                mobileCountryCode = "Unknown";
                mobileNetworkCode = "Unknown";
            }
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
        currentWifiState = getDeviceWifiState();

        if(wifiState == WifiManager.WIFI_STATE_ENABLED) {
            // based on current device api
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                nwGetConnectedWifiInfo();
            } else {
                owGetConnectedWifiInfo();
            }

            // combine and reform dhcp info
            dhcpInfo = buildDHCPInfo();

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
            dhcpServer = convertToIpAddress(dhcpInfo.serverAddress);

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
                    DhcpInfo dhcpInformation = wifiManager.getDhcpInfo();

                    dnsServer1 = convertToIpAddress(dhcpInformation.dns1);
                    dnsServer2 = convertToIpAddress(dhcpInformation.dns2);
                    gateway = convertToIpAddress(dhcpInformation.gateway);
                    leaseDuration = dhcpInformation.leaseDuration;
                    netmask = convertToIpAddress(dhcpInformation.netmask);
                    dhcpServer = convertToIpAddress(dhcpInformation.serverAddress);

                    // configured networks
                    configuredNetworksInfo = getConfiguredNetworksInfo(Build.VERSION.SDK_INT);
                }
            }
        }
    }

    private void setWifiInformationUnknown() {
        internalIpAddress = macAddress = serviceSetIdentifier = basicServiceSetIdentifier =
                 dnsServer1 = dnsServer2 = gateway = netmask = dhcpServer = "Unknown";
        linkSpeed = frequency = receivedSignalStrengthIndicator = leaseDuration = Integer.MAX_VALUE;
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

    private String getDeviceWifiState() {
        switch (wifiState) {
            case WifiManager.WIFI_STATE_ENABLED:
                return "Enabled";
            case WifiManager.WIFI_STATE_ENABLING:
                return "Enabling";
            case WifiManager.WIFI_STATE_DISABLED:
                return "Disabled";
            case WifiManager.WIFI_STATE_DISABLING:
                return "Disabling";
            default:
                return "Unknown";
        }
    }

    private String buildDHCPInfo() {
        String info = "";

        info += "DHCP information:\n";

        info += "Dns1:            " + NetInfo.dnsServer1 + " \n";
        info += "Dns2:            " + NetInfo.dnsServer2 + " \n";
        info += "Gateway:         " + NetInfo.gateway + " \n";
        info += "Lease Duration:  " + NetInfo.leaseDuration + " (s) \n";
        info += "Netmask:         " + NetInfo.netmask + " \n";
        info += "Server:          " + NetInfo.dhcpServer + " \n";

        info += "-------------------";

        return info;
    }

    /**
     * Network methods
     */
    public void getNetworkInformation() {
        // get active network info
        getActiveNetworkInfo();

        // get all networks info
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // new way
            nwGetAllNetworksInfo();
        } else {
            // old way
            owGetAllNetworksInfo();
        }
    }

    private void getActiveNetworkInfo() {
        // get active network info
        NetworkInfo active = connectivityManager.getActiveNetworkInfo();
        if(active != null) {
            // check out for type
            switch (active.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    activeNetworkInfo += "Type: WIFI; Status: " + (active.isConnected() ? "Connected" :
                        "Disconnected");
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    activeNetworkInfo += "Type: MOBILE; Status: " + (active.isConnected() ? "Connected" :
                            "Disconnected");
                    break;
                case ConnectivityManager.TYPE_BLUETOOTH:
                    activeNetworkInfo += "Type: BLUETOOTH; Status: " + (active.isConnected() ? "Connected" :
                            "Disconnected");
                    break;
                case ConnectivityManager.TYPE_WIMAX:
                    activeNetworkInfo += "Type: WIMAX; Status: " + (active.isConnected() ? "Connected" :
                            "Disconnected");
                    break;
                case ConnectivityManager.TYPE_VPN:
                    activeNetworkInfo += "Type: VPN; Status: " + (active.isConnected() ? "Connected" :
                            "Disconnected");
                    break;
                default:
                    activeNetworkInfo += "Type: Unknown; Status: Unavailable";
                    break;
            }
        } else {

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void nwGetAllNetworksInfo() {
        Network[] allNetworks = connectivityManager.getAllNetworks();

        if(allNetworks != null && allNetworks.length > 0) {
            allNetworkInfo += "Found " + allNetworks.length + " networks\n";
            int count = 1;

            for(Network net: allNetworks) {

                NetworkInfo netInfo = connectivityManager.getNetworkInfo(net);

                if(netInfo != null) {
                    allNetworkInfo += String.format("--------[Network#%d]---------\n", count);
                    // check out for type
                    switch (netInfo.getType()) {
                        case ConnectivityManager.TYPE_WIFI:
                            allNetworkInfo += "Type: WIFI; Status: " + (netInfo.isConnected() ? "Connected" :
                                    "Disconnected");
                            break;
                        case ConnectivityManager.TYPE_MOBILE:
                            allNetworkInfo += "Type: MOBILE; Status: " + (netInfo.isConnected() ? "Connected" :
                                    "Disconnected");
                            break;
                        case ConnectivityManager.TYPE_BLUETOOTH:
                            allNetworkInfo += "Type: BLUETOOTH; Status: " + (netInfo.isConnected() ? "Connected" :
                                    "Disconnected");
                            break;
                        case ConnectivityManager.TYPE_WIMAX:
                            allNetworkInfo += "Type: WIMAX; Status: " + (netInfo.isConnected() ? "Connected" :
                                    "Disconnected");
                            break;
                        case ConnectivityManager.TYPE_VPN:
                            allNetworkInfo += "Type: VPN; Status: " + (netInfo.isConnected() ? "Connected" :
                                    "Disconnected");
                            break;
                        default:
                            allNetworkInfo += "Type: Unknown; Status: Unavailable";
                            break;
                    }
                }
                count++;
            }
            allNetworkInfo += "----------------------------\n";

        } else {
            allNetworkInfo += "Found 0 available network\n";
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void owGetAllNetworksInfo() {
        NetworkInfo[] allNetworkInfos = connectivityManager.getAllNetworkInfo();

        if(allNetworkInfos != null && allNetworkInfos.length > 0) {

            allNetworkInfo += "Found " + allNetworkInfos.length + " networks\n";
            int count = 1;

            for(NetworkInfo info : allNetworkInfos) {
                allNetworkInfo += String.format("--------[Network#%d]---------\n", count);
                // check out for type
                switch (info.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                        allNetworkInfo += "Type: WIFI; Status: " + (info.isConnected() ? "Connected" :
                                "Disconnected");
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        allNetworkInfo += "Type: MOBILE; Status: " + (info.isConnected() ? "Connected" :
                                "Disconnected");
                        break;
                    case ConnectivityManager.TYPE_BLUETOOTH:
                        allNetworkInfo += "Type: BLUETOOTH; Status: " + (info.isConnected() ? "Connected" :
                                "Disconnected");
                        break;
                    case ConnectivityManager.TYPE_WIMAX:
                        allNetworkInfo += "Type: WIMAX; Status: " + (info.isConnected() ? "Connected" :
                                "Disconnected");
                        break;
                    case ConnectivityManager.TYPE_VPN:
                        allNetworkInfo += "Type: VPN; Status: " + (info.isConnected() ? "Connected" :
                                "Disconnected");
                        break;
                    default:
                        allNetworkInfo += "Type: Unknown; Status: Unavailable";
                        break;
                }
                count++;
            }
            allNetworkInfo += "----------------------------\n";
        } else {
            allNetworkInfo += "Found 0 available network\n";
        }
    }

    /**
     * Bluetooth methods
     */
    public void getBluetoothInformation() {
        bluetoothManager = (BluetoothManager) this.parentActivity.getApplicationContext()
                .getSystemService(Context.BLUETOOTH_SERVICE);

        bluetoothAdapter = bluetoothManager.getAdapter();

        bluetoothDeviceState += String.format("State: %s\n Discover: %s\n Scan: %s",getBluetoothDeviceState(),
                getBluetoothDiscoveringState(),getBluetoothScanMode());

        bluetoothDeviceName = bluetoothAdapter.getName();
        bluetoothDeviceAddress = getBluetoothMacAddress();
        getBluetoothFeaturesSupported();
        bluetoothPairedDevicesInfo = getPairedBluetoothDevices();
    }

    private String getBluetoothDeviceState() {
        switch (bluetoothAdapter.getState()) {
            case BluetoothAdapter.STATE_ON:
                return "On";
            case BluetoothAdapter.STATE_TURNING_ON:
                return "Turing on";
            case BluetoothAdapter.STATE_TURNING_OFF:
                return "Turning off";
            default:
                return "Off";
        }
    }

    private String getBluetoothDiscoveringState() {
        return bluetoothAdapter.isDiscovering() ? "Discovering" : "Not discovering";
    }

    private String getBluetoothScanMode() {
        switch (bluetoothAdapter.getScanMode()) {
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                return "Connectable";
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                return "Connectable & Discoverable";
            default:
                return "None";
        }
    }

    private String getBluetoothMacAddress() {
        String address = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            try {
                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    address = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (NoSuchFieldException e) {

            } catch (NoSuchMethodException e) {

            } catch (IllegalAccessException e) {

            } catch (InvocationTargetException e) {

            }
        } else {
            address = bluetoothAdapter.getAddress();
        }

        return address;
    }

    private void getBluetoothFeaturesSupported() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nwBluetoothFeaturesSupported();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            owBluetoothFeaturesSupported();
        } else {
            unBluetoothFeaturesSupported();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void nwBluetoothFeaturesSupported() {
        bluetoothLE2MPHY = bluetoothAdapter.isLe2MPhySupported();
        bluetoothLECodedPHY = bluetoothAdapter.isLeCodedPhySupported();
        bluetoothLEExtendedAdvertising = bluetoothAdapter.isLeExtendedAdvertisingSupported();
        bluetoothMultiAdvertisement = bluetoothAdapter.isMultipleAdvertisementSupported();
        bluetoothOffloadedFilters = bluetoothAdapter.isOffloadedFilteringSupported();
        bluetoothOffloadedScanBatching = bluetoothAdapter.isOffloadedScanBatchingSupported();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void owBluetoothFeaturesSupported() {
        bluetoothLE2MPHY = bluetoothLECodedPHY = bluetoothLEExtendedAdvertising = false;
        bluetoothMultiAdvertisement = bluetoothAdapter.isMultipleAdvertisementSupported();
        bluetoothOffloadedFilters = bluetoothAdapter.isOffloadedFilteringSupported();
        bluetoothOffloadedScanBatching = bluetoothAdapter.isOffloadedScanBatchingSupported();
    }

    private void unBluetoothFeaturesSupported() {
        bluetoothLE2MPHY = bluetoothLECodedPHY = bluetoothLEExtendedAdvertising
                = bluetoothMultiAdvertisement = bluetoothOffloadedFilters
                = bluetoothOffloadedScanBatching  = false;
    }

    private String getPairedBluetoothDevices() {
        String info = "Checking for paired bluetooth devices\n";

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if(pairedDevices.isEmpty()) {
            info += "Found 0 paired devices \n";
        } else {
            info += String.format("Found %d paired device(s)\n", pairedDevices.size());
            info += "---------------------------\n";
            int count = 1;

            for(BluetoothDevice device : pairedDevices) {
                info += String.format("Extracting info of device#%d:\n", count);

                info += String.format("Name: %s;\n", device.getName());
                info += String.format("Address: %s;\n", device.getAddress());
                info += String.format("Type: %s;\n", getBluetoothDeviceType(device));

                info += "---------------------------";
            }
        }

        return info;
    }

    private String getBluetoothDeviceType(BluetoothDevice device) {
        switch (device.getType()) {
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                return "Classic";
            case BluetoothDevice.DEVICE_TYPE_LE:
                return "LE";
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                return "Dual";
            default:
                return "Unknown";
        }
    }

    /**
     * Device methods
     */
}
