package com.ferdousanam.classes;

import java.io.Serializable;

public class DataPacket implements Serializable {

    private final byte[] rawBytes;

    public DataPacket(byte[] rawBytes) {
        this.rawBytes = rawBytes;
    }

    public byte[] getRawBytes() {
        return rawBytes;
    }
}
