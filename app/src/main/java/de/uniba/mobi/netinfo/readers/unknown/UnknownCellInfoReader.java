package de.uniba.mobi.netinfo.readers.unknown;

import de.uniba.mobi.netinfo.readers.CellInfoReader;

public final class UnknownCellInfoReader extends CellInfoReader {

    public UnknownCellInfoReader() {

    }

    @Override
    public String readSignalStrengthInfo() {
        return null;
    }

    @Override
    public String readIdentityInfo() {
        return null;
    }
}
