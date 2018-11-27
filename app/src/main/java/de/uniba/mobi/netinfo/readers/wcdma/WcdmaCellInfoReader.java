package de.uniba.mobi.netinfo.readers.wcdma;

import android.os.Build;
import android.telephony.CellInfoWcdma;

import de.uniba.mobi.netinfo.readers.CellInfoReader;
import de.uniba.mobi.netinfo.readers.wcdma.identity.JellyBeanMR2WcdmaIDR;
import de.uniba.mobi.netinfo.readers.wcdma.identity.NougatWcdmaIDR;
import de.uniba.mobi.netinfo.readers.wcdma.identity.PieWcdmaIDR;
import de.uniba.mobi.netinfo.readers.wcdma.identity.WcdmaIdentityReader;
import de.uniba.mobi.netinfo.readers.wcdma.signal.JellyBeanMR2WcdmaSSR;
import de.uniba.mobi.netinfo.readers.wcdma.signal.WcdmaSignalStrengthReader;

public final class WcdmaCellInfoReader extends CellInfoReader {

    // properties
    private CellInfoWcdma info;

    // readers
    private WcdmaSignalStrengthReader signalStrengthReader;
    private WcdmaIdentityReader identityReader;

    // constructor
    public WcdmaCellInfoReader(CellInfoWcdma wcdma, int apiLevel) {
        this.info = wcdma;
        this.apiLevel = apiLevel;

        // get correct readers
        signalStrengthReader = new JellyBeanMR2WcdmaSSR(info.getCellSignalStrength());

        if(this.apiLevel >= Build.VERSION_CODES.P) {
            identityReader = new PieWcdmaIDR(info.getCellIdentity());
        } else if(this.apiLevel >= Build.VERSION_CODES.N) {
            identityReader = new NougatWcdmaIDR(info.getCellIdentity());
        } else {
            identityReader = new JellyBeanMR2WcdmaIDR(info.getCellIdentity());
        }
    }

    @Override
    public String readSignalStrengthInfo() {
        return signalStrengthReader.readSignalStrength();
    }

    @Override
    public String readIdentityInfo() {
        return identityReader.readIdentity();
    }
}
