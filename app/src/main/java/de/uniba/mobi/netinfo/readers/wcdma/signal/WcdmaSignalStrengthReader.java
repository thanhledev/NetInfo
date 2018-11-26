package de.uniba.mobi.netinfo.readers.wcdma.signal;

import android.telephony.CellSignalStrengthWcdma;

public abstract class WcdmaSignalStrengthReader {
    protected CellSignalStrengthWcdma info;

    public WcdmaSignalStrengthReader(CellSignalStrengthWcdma info) {
        this.info = info;
    }

    public abstract String readSignalStrength();
}
