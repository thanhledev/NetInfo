package de.uniba.mobi.netinfo.readers.lte.identity;

import android.telephony.CellIdentityLte;

public abstract class LteIdentityReader {
    protected CellIdentityLte info;

    public LteIdentityReader(CellIdentityLte info) {
        this.info = info;
    }

    public abstract String readIdentity();
}
