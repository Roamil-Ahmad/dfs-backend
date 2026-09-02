package com.dfs.switchgateway.soketClient.codecs;

import com.dfs.switchgateway.utils.CommonUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.jpos.iso.ISOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionsResponseDecoder extends CumulativeProtocolDecoder {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    private static final String DECODER_STATE_KEY = TransactionsResponseDecoder.class.getName() + ".STATE";

    private static class DecoderState {
        int packetLength = -1;
    }

    @Override
    protected boolean doDecode( IoSession session, IoBuffer io, ProtocolDecoderOutput out ) throws Exception {

        io.setAutoExpand(true);
        io.setAutoShrink(true);
        DecoderState decoderState = (DecoderState) session.getAttribute(DECODER_STATE_KEY);
        if (decoderState == null) {
            decoderState = new DecoderState();
            session.setAttribute(DECODER_STATE_KEY, decoderState);
        }

        if (!io.hasRemaining()) {
            return false;
        }

        String hexdump = io.getHexDump();
        // logger.info("PDU Hexdump: " + hexdump);

        if (decoderState.packetLength == -1) {
            // try to read packet
            if (io.remaining() >= 4) {
                //for  1 link
                byte[] packetLengthBytes = new byte[2];
                // for mock testing
//                byte[] packetLengthBytes = new byte[4];
                io.get(packetLengthBytes);
                String hexString = CommonUtils.bytesToHex(packetLengthBytes);
                decoderState.packetLength = (Integer.parseInt(hexString, 16));
            } else {
                return false;
            }
        }

        if ((io.remaining() < decoderState.packetLength)) {
            logger.info("###################Packet not received completely##################");
            logger.info("Actual Packet Remaining Length ->" + (decoderState.packetLength - 2));
            logger.info("Buffer Remaining bytes->" + io.remaining());
            return false;
        }

        byte[] packetByte = null;

        packetByte = new byte[decoderState.packetLength];
        io.get(packetByte);
        String packet = new String(packetByte);
        logger.info("PDU: " + packet);
        decoderState.packetLength = -1;
        out.write(packetByte);

        String s1 = ISOUtil.hexdump(packetByte);
        String s2 = ISOUtil.hexString(packetByte);

        logger.info("Decoding ISO 8583:\n" + s1);
        logger.info("Hexdump ISO 8583:\n" + s2);

        // logger.info("PDU Hexdump: " + Hex.encodeHexString(packetByte));
        return true;
    }

}
