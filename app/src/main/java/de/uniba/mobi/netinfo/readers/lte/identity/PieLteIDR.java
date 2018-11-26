package de.uniba.mobi.netinfo.readers.lte.identity;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellIdentityLte;

public final class PieLteIDR extends LteIdentityReader {

    public PieLteIDR(CellIdentityLte info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    public String readIdentity() {
        String identityInfo = "";

        identityInfo += "Bandwidth:   " + info.getBandwidth() + " \n";
        identityInfo += "Ci:          " + info.getCi() + " \n";
        identityInfo += "Earfcn:      " + info.getEarfcn() + " \n";
        identityInfo += "Mcc:         " + info.getMccString() + " \n";
        identityInfo += "Mnc:         " + info.getMncString() + " \n";
        identityInfo += "MN Operator: " + info.getMobileNetworkOperator() + " \n";
        identityInfo += "Pci:         " + info.getPci() + " \n";
        identityInfo += "Tac:         " + info.getTac() + " \n";

        return identityInfo;
    }
}
