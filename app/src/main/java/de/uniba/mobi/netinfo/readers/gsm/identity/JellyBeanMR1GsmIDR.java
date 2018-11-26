package de.uniba.mobi.netinfo.readers.gsm.identity;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityGsm;

public final class JellyBeanMR1GsmIDR extends GsmIdentityReader {

    public JellyBeanMR1GsmIDR(CellIdentityGsm info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public String readIdentity() {
        String iInfo = "";

        iInfo += "Cid:     " + info.getCid() + " \n";
        iInfo += "Lac:     " + info.getLac() + " \n";
        iInfo += "Mcc:     " + info.getMcc() + " \n";
        iInfo += "Mnc:     " + info.getMnc() + " \n";
        iInfo += "Psc:     " + info.getPsc() + " \n";

        return iInfo;
    }
}
