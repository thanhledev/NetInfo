package de.uniba.mobi.netinfo.readers.wcdma.identity;

import android.telephony.CellIdentityWcdma;

public abstract class WcdmaIdentityReader {
    protected CellIdentityWcdma info;

    public WcdmaIdentityReader(CellIdentityWcdma info) {
        this.info = info;
    }

    public abstract String readIdentity();
}
