package de.uniba.mobi.netinfo.readers.wcdma.identity;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityWcdma;

public final class NougatWcdmaIDR extends WcdmaIdentityReader {

    public NougatWcdmaIDR(CellIdentityWcdma info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public String readIdentity() {
        String identityInfo = "";

        identityInfo += "Cid:         " + info.getCid() + " \n";
        identityInfo += "Lac:         " + info.getLac() + " \n";
        identityInfo += "Mcc:         " + info.getMcc() + " \n";
        identityInfo += "Mnc:         " + info.getMnc() + " \n";
        identityInfo += "Psc:         " + info.getPsc() + " \n";
        identityInfo += "Uarfcn:      " + info.getUarfcn() + " \n";

        return identityInfo;
    }
}
