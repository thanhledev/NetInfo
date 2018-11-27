package de.uniba.mobi.netinfo.readers.gsm.identity;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityGsm;

public final class PieGsmIDR extends GsmIdentityReader {

    public PieGsmIDR(CellIdentityGsm info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    public String readIdentity() {
        String iInfo = "";

        iInfo += "Arfcn:   " + info.getArfcn() + " \n";
        iInfo += "Bsic:    " + info.getBsic() + " \n";
        iInfo += "Cid:     " + info.getCid() + " \n";
        iInfo += "Lac:     " + info.getLac() + " \n";
        iInfo += "Mcc:     " + info.getMccString() + " \n";
        iInfo += "Mnc:     " + info.getMncString() + " \n";
        iInfo += "MNO:     " + info.getMobileNetworkOperator() + " \n";

        return iInfo;
    }
}
