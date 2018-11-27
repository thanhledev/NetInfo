package de.uniba.mobi.netinfo.readers.wcdma.identity;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityWcdma;

public final class PieWcdmaIDR extends WcdmaIdentityReader {

    public PieWcdmaIDR(CellIdentityWcdma info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    public String readIdentity() {
        String identityInfo = "";

        identityInfo += "Cid:         " + info.getCid() + " \n";
        identityInfo += "Lac:         " + info.getLac() + " \n";
        identityInfo += "Mcc:         " + info.getMccString() + " \n";
        identityInfo += "Mnc:         " + info.getMncString() + " \n";
        identityInfo += "MN Operator: " + info.getMobileNetworkOperator() + " \n";
        identityInfo += "Psc:         " + info.getPsc() + " \n";
        identityInfo += "Uarfcn:      " + info.getUarfcn() + " \n";

        return identityInfo;
    }
}
