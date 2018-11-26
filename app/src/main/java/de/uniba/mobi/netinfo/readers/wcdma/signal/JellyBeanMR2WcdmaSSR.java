package de.uniba.mobi.netinfo.readers.wcdma.signal;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellSignalStrengthWcdma;

public final class JellyBeanMR2WcdmaSSR extends WcdmaSignalStrengthReader {

    public JellyBeanMR2WcdmaSSR(CellSignalStrengthWcdma info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public String readSignalStrength() {
        String signalInfo = "";

        signalInfo += "AsuLevel:         " + info.getAsuLevel() + " \n";
        signalInfo += "Dbm:              " + info.getDbm() + " \n";
        signalInfo += "SignalLevel:      " + info.getLevel() + " \n";

        return signalInfo;
    }
}
