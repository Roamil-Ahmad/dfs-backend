package com.dfs.app.util;

import java.nio.charset.StandardCharsets;

public class CRCGenerator {

    public static String generateCRC(Integer length, String value) {

            byte[] data = value.getBytes(StandardCharsets.US_ASCII);
            int result = crc16(data, 0, data.length);
            return Integer.toHexString(result).toUpperCase();

    }

    private static int crc16(byte[] data, int offset, int length) {
        if (data == null || offset < 0 || offset > data.length - 1 || offset + length > data.length) {
            return 0;
        }
        int crc = 0xFFFF;
        for (int i = 0; i < length; ++i) {
            crc ^= (data[offset + i] & 0xFF) << 8;
            for (int j = 0; j < 8; ++j) {
                crc = (crc & 0x8000) > 0 ? (crc << 1) ^ 0x1021 : crc << 1;
            }
        }
        return crc & 0xFFFF;
    }

}
