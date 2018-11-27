package de.uniba.mobi.netinfo.readers.cdma;

import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellInfoCdma;
import android.telephony.CellSignalStrengthCdma;

import de.uniba.mobi.netinfo.readers.CellInfoReader;
import de.uniba.mobi.netinfo.readers.cdma.identity.CdmaIdentityReader;
import de.uniba.mobi.netinfo.readers.cdma.identity.JellyBeanMR1CdmaIDR;
import de.uniba.mobi.netinfo.readers.cdma.signal.CdmaSignalStrengthReader;
import de.uniba.mobi.netinfo.readers.cdma.signal.JellyBeanMR1CdmaSSR;

public final class CdmaCellInfoReader extends CellInfoReader {

    // properties
    private CellInfoCdma info;

    // readers
    private CdmaSignalStrengthReader signalStrengthReader;
    private CdmaIdentityReader identityReader;

    // constructor
    public CdmaCellInfoReader(CellInfoCdma cdma, int apiLevel) {
        this.info = cdma;
        this.apiLevel = apiLevel;

        // get correct readers
        signalStrengthReader = new JellyBeanMR1CdmaSSR(info.getCellSignalStrength());
        identityReader = new JellyBeanMR1CdmaIDR(info.getCellIdentity());
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
