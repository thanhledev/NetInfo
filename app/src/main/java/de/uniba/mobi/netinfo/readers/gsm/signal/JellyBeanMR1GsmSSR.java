package de.uniba.mobi.netinfo.readers.gsm.signal;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellSignalStrengthGsm;

public final class JellyBeanMR1GsmSSR extends GsmSignalStrengthReader {

    public JellyBeanMR1GsmSSR(CellSignalStrengthGsm info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public String readSignalStrength() {
        String signalInfo = "";

        signalInfo += "Asu Level:      " + info.getAsuLevel() + " \n";
        signalInfo += "Dbm Strength:   " + info.getDbm() + " \n";
        signalInfo += "Signal Level:   " + info.getLevel() + " \n";

        return signalInfo;
    }
}
