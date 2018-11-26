package de.uniba.mobi.netinfo.readers.cdma.identity;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityCdma;

public final class JellyBeanMR1CdmaIDR extends CdmaIdentityReader {

    public JellyBeanMR1CdmaIDR(CellIdentityCdma info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public String readIdentity() {
        String identityInfo = "";

        identityInfo += "BaseStationId: " + info.getBasestationId() + " \n";
        identityInfo += "Latitude:      " + info.getLatitude() + " \n";
        identityInfo += "Longitude:     " + info.getLongitude() + " \n";
        identityInfo += "NetworkId:     " + info.getNetworkId() + " \n";
        identityInfo += "SystemId:      " + info.getSystemId() + " \n";

        return identityInfo;
    }
}
