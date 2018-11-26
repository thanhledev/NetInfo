package de.uniba.mobi.netinfo.readers.lte.signal;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellSignalStrengthLte;

public final class JellyBeanMR1LteSSR extends LteSignalStrengthReader {

    public JellyBeanMR1LteSSR(CellSignalStrengthLte info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public String readSignalStrength() {
        String signalInfo = "";

        signalInfo += "AsuLevel:         " + info.getAsuLevel() + " \n";
        signalInfo += "Dbm:              " + info.getDbm() + " \n";
        signalInfo += "SignalLevel:      " + info.getLevel() + " \n";
        signalInfo += "TimingAdvance:    " + info.getTimingAdvance() + " \n";

        return signalInfo;
    }
}
