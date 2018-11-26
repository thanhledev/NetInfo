package de.uniba.mobi.netinfo.readers.wcdma.identity;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityWcdma;

public final class JellyBeanMR2WcdmaIDR extends WcdmaIdentityReader {

    public JellyBeanMR2WcdmaIDR(CellIdentityWcdma info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public String readIdentity() {
        String identityInfo = "";

        identityInfo += "Cid:         " + info.getCid() + " \n";
        identityInfo += "Lac:         " + info.getLac() + " \n";
        identityInfo += "Mcc:         " + info.getMcc() + " \n";
        identityInfo += "Mnc:         " + info.getMnc() + " \n";
        identityInfo += "Psc:         " + info.getPsc() + " \n";

        return identityInfo;
    }
}
