package com.dfs.switchgateway.soketClient.codecs;

import com.dfs.switchgateway.dto.BasePDU;
import com.dfs.switchgateway.utils.CommonUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.jpos.iso.ISOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionRequestEncoder implements ProtocolEncoder {
    private static Logger logger = LoggerFactory.getLogger(TransactionRequestEncoder.class.getSimpleName());

    /**
     * The encode automatically called when message is sent from session.write
     * from handler. It receives the written object, extract & build bytes.
     * After getting bytes, allocate the buffer and sent them to the socket
     * output stream.
     *
     * @throws Exception
     */
    @Override
    public void encode( IoSession session, Object message, ProtocolEncoderOutput out ) throws Exception {
        logger.info("Encoding PDU to Phoenix format.");
        BasePDU pdu = (BasePDU) message;

        String hexArray = Integer.toHexString(pdu.getRawPdu().length);
        hexArray = StringUtils.leftPad(hexArray, 4, '0');

        byte[] metaLengthBytes = CommonUtils.hexStringToByteArray(hexArray);
        IoBuffer buffer = IoBuffer.allocate(metaLengthBytes.length + pdu.getRawPdu().length, false);
        buffer.setAutoExpand(true);
        buffer.setAutoShrink(true);
        if (metaLengthBytes.length != 0) {

            buffer.put(metaLengthBytes);

            buffer.put(pdu.getRawPdu());
//            logger.info("Encoded Request Bytes Length: " + pdu.getRawPdu().length);
//            logger.info("REQUEST STRING ASCII: " + new String(pdu.getRawPdu()));
            logger.info("REQUEST STRING HEX: " + Hex.encodeHexString(metaLengthBytes) + Hex.encodeHexString(pdu.getRawPdu()));

            String s1 = ISOUtil.hexdump(pdu.getRawPdu());
            String s2 = ISOUtil.hexString(pdu.getRawPdu());

//            logger.info("Encoding ISO 8583:\n" + s1);
//            logger.info("Hexdump ISO 8583:\n" + s2);

            buffer.flip();
        } else {
            buffer = null;
        }
        if (buffer != null) {
            out.write(buffer);
        }
    }

    @Override
    public void dispose( IoSession session ) throws Exception {
        logger.info("dispose: closing session...");
        session.close(true);
    }

}
