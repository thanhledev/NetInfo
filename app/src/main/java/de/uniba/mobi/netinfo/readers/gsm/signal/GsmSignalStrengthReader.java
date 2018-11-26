package de.uniba.mobi.netinfo.readers.gsm.signal;

import android.telephony.CellSignalStrengthGsm;

public abstract class GsmSignalStrengthReader {

    protected CellSignalStrengthGsm info;

    public GsmSignalStrengthReader(CellSignalStrengthGsm info) {
        this.info = info;
    }

    public abstract String readSignalStrength();
}
