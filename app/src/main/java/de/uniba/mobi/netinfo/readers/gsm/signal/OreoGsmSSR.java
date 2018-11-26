package de.uniba.mobi.netinfo.readers.gsm.signal;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellSignalStrengthGsm;

public final class OreoGsmSSR extends GsmSignalStrengthReader {

    public OreoGsmSSR(CellSignalStrengthGsm info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public String readSignalStrength() {
        String signalInfo = "";

        signalInfo += "Asu Level:      " + info.getAsuLevel() + " \n";
        signalInfo += "Dbm Strength:   " + info.getDbm() + " \n";
        signalInfo += "Signal Level:   " + info.getLevel() + " \n";
        signalInfo += "Timing Advance: " + info.getTimingAdvance() + " \n";

        return signalInfo;
    }
}
