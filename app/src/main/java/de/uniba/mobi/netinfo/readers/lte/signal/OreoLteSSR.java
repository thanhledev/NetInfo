package de.uniba.mobi.netinfo.readers.lte.signal;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.CellSignalStrengthLte;

public final class OreoLteSSR extends LteSignalStrengthReader {

    public OreoLteSSR(CellSignalStrengthLte info) {
        super(info);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public String readSignalStrength() {
        String signalInfo = "";

        signalInfo += "AsuLevel:         " + info.getAsuLevel() + " \n";
        signalInfo += "Cqi:              " + info.getCqi() + " \n";
        signalInfo += "Dbm:              " + info.getDbm() + " \n";
        signalInfo += "SignalLevel:      " + info.getLevel() + " \n";
        signalInfo += "Rsrp:             " + info.getRsrp() + " \n";
        signalInfo += "Rsrq:             " + info.getRsrq() + " \n";
        signalInfo += "Rssnr:            " + info.getRssnr() + " \n";
        signalInfo += "TimingAdvance:    " + info.getTimingAdvance() + " \n";

        return signalInfo;
    }
}
