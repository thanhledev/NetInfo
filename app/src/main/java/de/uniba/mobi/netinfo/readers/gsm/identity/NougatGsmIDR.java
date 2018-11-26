package de.uniba.mobi.netinfo.readers.gsm.identity;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityGsm;

public final class NougatGsmIDR extends GsmIdentityReader {

    public NougatGsmIDR(CellIdentityGsm info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public String readIdentity() {
        String iInfo = "";

        iInfo += "Arfcn:   " + info.getArfcn() + " \n";
        iInfo += "Bsic:    " + info.getBsic() + " \n";
        iInfo += "Cid:     " + info.getCid() + " \n";
        iInfo += "Lac:     " + info.getLac() + " \n";
        iInfo += "Mcc:     " + info.getMcc() + " \n";
        iInfo += "Mnc:     " + info.getMnc() + " \n";

        return iInfo;
    }
}
