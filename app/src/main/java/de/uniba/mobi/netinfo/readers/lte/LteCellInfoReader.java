package de.uniba.mobi.netinfo.readers.lte;

import android.os.Build;
import android.telephony.CellInfoLte;

import de.uniba.mobi.netinfo.readers.CellInfoReader;
import de.uniba.mobi.netinfo.readers.lte.identity.JellyBeanMR1LteIDR;
import de.uniba.mobi.netinfo.readers.lte.identity.LteIdentityReader;
import de.uniba.mobi.netinfo.readers.lte.identity.NougatLteIDR;
import de.uniba.mobi.netinfo.readers.lte.identity.PieLteIDR;
import de.uniba.mobi.netinfo.readers.lte.signal.JellyBeanMR1LteSSR;
import de.uniba.mobi.netinfo.readers.lte.signal.LteSignalStrengthReader;
import de.uniba.mobi.netinfo.readers.lte.signal.OreoLteSSR;

public final class LteCellInfoReader extends CellInfoReader {

    // properties
    private CellInfoLte info;

    // readers
    LteSignalStrengthReader signalStrengthReader;
    LteIdentityReader identityReader;

    // constructors
    public LteCellInfoReader(CellInfoLte lte, int apiLevel) {
        this.info = lte;
        this.apiLevel = apiLevel;

        // get correct readers
        if(this.apiLevel >= Build.VERSION_CODES.P) {
            identityReader = new PieLteIDR(info.getCellIdentity());
        } else if(this.apiLevel >= Build.VERSION_CODES.N) {
            identityReader = new NougatLteIDR(info.getCellIdentity());
        } else {
            identityReader = new JellyBeanMR1LteIDR(info.getCellIdentity());
        }

        if(this.apiLevel >= Build.VERSION_CODES.O) {
            signalStrengthReader = new OreoLteSSR(info.getCellSignalStrength());
        } else {
            signalStrengthReader = new JellyBeanMR1LteSSR(info.getCellSignalStrength());
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
