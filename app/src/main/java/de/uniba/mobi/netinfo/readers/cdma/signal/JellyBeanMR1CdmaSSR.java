package de.uniba.mobi.netinfo.readers.cdma.signal;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellSignalStrengthCdma;

public final class JellyBeanMR1CdmaSSR extends CdmaSignalStrengthReader {

    public JellyBeanMR1CdmaSSR(CellSignalStrengthCdma info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public String readSignalStrength() {
        String signalInfo = "";

        signalInfo += "AsuLevel:       " + info.getAsuLevel() + " \n";
        signalInfo += "CdmaDbm:        " + info.getCdmaDbm() + " \n";
        signalInfo += "CdmaEcio:       " + info.getCdmaEcio() + " \n";
        signalInfo += "CdmaLevel:      " + info.getCdmaLevel() + " \n";
        signalInfo += "Dbm:            " + info.getDbm() + " \n";
        signalInfo += "EvdoDbm:        " + info.getEvdoDbm() + " \n";
        signalInfo += "EvdoEcio:       " + info.getEvdoEcio() + " \n";
        signalInfo += "EvdoLevel:      " + info.getEvdoLevel() + " \n";
        signalInfo += "EvdoSnr:        " + info.getEvdoSnr() + " \n";
        signalInfo += "SignalLevel:    " + info.getLevel() + " \n";

        return signalInfo;
    }
}
