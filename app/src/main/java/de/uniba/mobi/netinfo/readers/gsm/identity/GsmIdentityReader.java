package de.uniba.mobi.netinfo.readers.gsm.identity;

import android.telephony.CellIdentityGsm;

public abstract class GsmIdentityReader {

    protected CellIdentityGsm info;

    public GsmIdentityReader(CellIdentityGsm info) {
        this.info = info;
    }

    public abstract String readIdentity();
}
