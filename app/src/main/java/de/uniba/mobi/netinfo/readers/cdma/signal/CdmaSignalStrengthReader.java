package de.uniba.mobi.netinfo.readers.cdma.signal;

import android.telephony.CellSignalStrengthCdma;

public abstract class CdmaSignalStrengthReader {

    protected CellSignalStrengthCdma info;

    public CdmaSignalStrengthReader(CellSignalStrengthCdma info) {
        this.info = info;
    }

    public abstract String readSignalStrength();
}
