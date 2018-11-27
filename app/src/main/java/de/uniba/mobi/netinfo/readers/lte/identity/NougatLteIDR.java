package de.uniba.mobi.netinfo.readers.lte.identity;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityLte;

public final class NougatLteIDR extends LteIdentityReader {

    public NougatLteIDR(CellIdentityLte info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public String readIdentity() {
        String identityInfo = "";

        identityInfo += "Ci:          " + info.getCi() + " \n";
        identityInfo += "Earfcn:      " + info.getEarfcn() + " \n";
        identityInfo += "Mcc:         " + info.getMcc() + " \n";
        identityInfo += "Mnc:         " + info.getMnc() + " \n";
        identityInfo += "Pci:         " + info.getPci() + " \n";
        identityInfo += "Tac:         " + info.getTac() + " \n";

        return identityInfo;
    }
}
