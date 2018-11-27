package de.uniba.mobi.netinfo.readers.cdma.identity;

import android.telephony.CellIdentityCdma;

public abstract class CdmaIdentityReader {
    protected CellIdentityCdma info;

    public CdmaIdentityReader(CellIdentityCdma info) {
        this.info = info;
    }

    public abstract String readIdentity();
}
