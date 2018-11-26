package de.uniba.mobi.netinfo.readers.lte.signal;

import android.telephony.CellSignalStrengthLte;

public abstract class LteSignalStrengthReader {
    protected CellSignalStrengthLte info;

    public LteSignalStrengthReader(CellSignalStrengthLte info) {
        this.info = info;
    }

    public abstract String readSignalStrength();
}
