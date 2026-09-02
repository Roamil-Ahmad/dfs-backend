package com.dfs.switchgateway.soketClient.codecs;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * <p>
 * <code>PhoenixCodecFactory</code> holds custom implemented
 * <code>ProtocolEncoder</code> and ProtocolDecoder.
 * </p>
 * <p>
 * The encoder is used to encode request into underlying protocol specifications
 * and decode is used to decode the underlying response into application's
 * specific objects.
 */
public class TransactionCodecFactory implements ProtocolCodecFactory {

    private ProtocolDecoder decoder;
    private ProtocolEncoder encoder;

    public TransactionCodecFactory() {
        this.decoder = new TransactionsResponseDecoder();
        this.encoder = new TransactionRequestEncoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return encoder;
    }

}
