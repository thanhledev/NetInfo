package de.uniba.mobi.netinfo.readers.gsm;

import android.os.Build;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;

import de.uniba.mobi.netinfo.readers.CellInfoReader;
import de.uniba.mobi.netinfo.readers.gsm.identity.GsmIdentityReader;
import de.uniba.mobi.netinfo.readers.gsm.identity.JellyBeanMR1GsmIDR;
import de.uniba.mobi.netinfo.readers.gsm.identity.NougatGsmIDR;
import de.uniba.mobi.netinfo.readers.gsm.identity.PieGsmIDR;
import de.uniba.mobi.netinfo.readers.gsm.signal.GsmSignalStrengthReader;
import de.uniba.mobi.netinfo.readers.gsm.signal.JellyBeanMR1GsmSSR;
import de.uniba.mobi.netinfo.readers.gsm.signal.OreoGsmSSR;

public final class GsmCellInfoReader extends CellInfoReader {

    // properties
    private CellInfoGsm info;

    // readers
    private GsmSignalStrengthReader signalReader;
    private GsmIdentityReader identityReader;

    // constructor
    public GsmCellInfoReader(CellInfoGsm gsm, int apiLevel) {
        this.info = gsm;
        this.apiLevel = apiLevel;

        // get correct readers
        if(this.apiLevel >= Build.VERSION_CODES.O) {
            signalReader = new OreoGsmSSR(info.getCellSignalStrength());
        } else {
            signalReader = new JellyBeanMR1GsmSSR(info.getCellSignalStrength());
        }

        if(this.apiLevel >= Build.VERSION_CODES.P) {
            identityReader = new PieGsmIDR(info.getCellIdentity());
        } else if(this.apiLevel >= Build.VERSION_CODES.N) {
            identityReader = new NougatGsmIDR(info.getCellIdentity());
        } else {
            identityReader = new JellyBeanMR1GsmIDR(info.getCellIdentity());
        }
    }

    // override methods
    @Override
    public String readSignalStrengthInfo() {
        return signalReader.readSignalStrength();
    }

    @Override
    public String readIdentityInfo() {
        return identityReader.readIdentity();
    }
}
