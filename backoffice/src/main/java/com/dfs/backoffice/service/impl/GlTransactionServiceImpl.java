package com.dfs.backoffice.service.impl;

import com.dfs.backoffice.dto.GlTransactionRequest;
import com.dfs.backoffice.dto.GlTransactionResponse;
import com.dfs.backoffice.dto.TitleFetchRequest;
import com.dfs.backoffice.model.TblAccount;
import com.dfs.backoffice.repo.TblAccountRepo;
import com.dfs.backoffice.service.GlTransactionService;
import com.dfs.backoffice.utils.JwtConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;

@Service
public class GlTransactionServiceImpl implements GlTransactionService {
    @PersistenceContext
    EntityManager em;
    @Autowired
    private TblAccountRepo tblAccountRepo;

    @Override
    public GlTransactionResponse glToGlTransfer(GlTransactionRequest glTransactionRequest, HttpServletRequest request) {
        GlTransactionResponse glToGlTransactionResponse = new GlTransactionResponse();
        try {
            Session session = em.unwrap(Session.class);

            final int[] responseStatus = new int[1];
            final String[] responseDescr = new String[1];

            session.doWork(connection -> {
                // PKG_FUNDS_TRANSFER.FT_GL_TO_GL(P_F_GL_ACCOUNT_ID, P_T_GL_ACCOUNT_ID,
                //                                P_TRANS_AMOUNT, P_USER_ID,
                //                                P_STATUS OUT, P_STATUS_DESCR OUT)
                // Argument order is from-GL then to-GL, the same as the old package.
                try (CallableStatement call = connection.prepareCall("{call PKG_FUNDS_TRANSFER.FT_GL_TO_GL(?,?,?,?,?,?) }")) {
                    call.setLong(1, glTransactionRequest.getFromAccountId());
                    call.setLong(2, glTransactionRequest.getToAccountId());
                    call.setLong(3, glTransactionRequest.getTransAmount());
                    call.setBigDecimal(4, new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
                    call.registerOutParameter(5, Types.INTEGER);
                    call.registerOutParameter(6, Types.VARCHAR);
                    call.execute();

                    responseStatus[0] = call.getInt(5);
                    responseDescr[0] = call.getString(6);
                }
            });

            glToGlTransactionResponse.setResponseStatus(responseStatus[0]);
            // PKG_FUNDS_TRANSFER returns only a status and a description; there is no separate
            // response code, and the controller never read this field.
            glToGlTransactionResponse.setResponseDescr(responseDescr[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return glToGlTransactionResponse;
    }

    @Override
    public GlTransactionResponse walletToGlTransfer(GlTransactionRequest glTransactionRequest, HttpServletRequest request) {
        GlTransactionResponse glToGlTransactionResponse = new GlTransactionResponse();
        try {
            Session session = em.unwrap(Session.class);

            final int[] responseStatus = new int[1];
            final String[] responseDescr = new String[1];

            session.doWork(connection -> {
                // PKG_FUNDS_TRANSFER.FT_WALLET_TO_GL(P_GL_ACCOUNT_ID, P_ACCOUNT_ID,
                //                                    P_TRANS_AMOUNT, P_USER_ID,
                //                                    P_STATUS OUT, P_STATUS_DESCR OUT)
                //
                // NOTE the argument order. The old PKG_PAYMENTS.WALLET_TO_GL took
                // (P_F_ACCOUNT_ID, P_T_GL_ACCOUNT_ID), i.e. from then to. This one takes the GL
                // account FIRST and the wallet second, so on this transfer - where the money
                // leaves the wallet and lands in the GL - "to" goes in position 1 and "from" in
                // position 2. Passing them in the old order would debit the wrong side.
                try (CallableStatement call = connection.prepareCall("{call PKG_FUNDS_TRANSFER.FT_WALLET_TO_GL(?,?,?,?,?,?) }")) {
                    call.setLong(1, glTransactionRequest.getToAccountId());
                    call.setLong(2, glTransactionRequest.getFromAccountId());
                    call.setLong(3, glTransactionRequest.getTransAmount());
                    call.setBigDecimal(4, new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
                    call.registerOutParameter(5, Types.INTEGER);
                    call.registerOutParameter(6, Types.VARCHAR);
                    call.execute();

                    responseStatus[0] = call.getInt(5);
                    responseDescr[0] = call.getString(6);
                }
            });

            glToGlTransactionResponse.setResponseStatus(responseStatus[0]);
            // PKG_FUNDS_TRANSFER returns only a status and a description; there is no separate
            // response code, and the controller never read this field.
            glToGlTransactionResponse.setResponseDescr(responseDescr[0]);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return glToGlTransactionResponse;
    }

    @Override
    public GlTransactionResponse glToWalletTransfer(GlTransactionRequest glTransactionRequest, HttpServletRequest request) {
        GlTransactionResponse glToGlTransactionResponse = new GlTransactionResponse();
        try {
            Session session = em.unwrap(Session.class);

            final int[] responseStatus = new int[1];
            final String[] responseDescr = new String[1];

            session.doWork(connection -> {
                // PKG_FUNDS_TRANSFER.FT_GL_TO_WALLET(P_GL_ACCOUNT_ID, P_ACCOUNT_ID,
                //                                    P_TRANS_AMOUNT, P_USER_ID,
                //                                    P_STATUS OUT, P_STATUS_DESCR OUT)
                // Here the GL is the "from" side, so from/to order is unchanged.
                try (CallableStatement call = connection.prepareCall("{call PKG_FUNDS_TRANSFER.FT_GL_TO_WALLET(?,?,?,?,?,?) }")) {
                    call.setLong(1, glTransactionRequest.getFromAccountId());
                    call.setLong(2, glTransactionRequest.getToAccountId());
                    call.setLong(3, glTransactionRequest.getTransAmount());
                    call.setBigDecimal(4, new BigDecimal((String) request.getAttribute(JwtConstants.APP_USER_ID)));
                    call.registerOutParameter(5, Types.INTEGER);
                    call.registerOutParameter(6, Types.VARCHAR);
                    call.execute();

                    responseStatus[0] = call.getInt(5);
                    responseDescr[0] = call.getString(6);
                }
            });

            glToGlTransactionResponse.setResponseStatus(responseStatus[0]);
            // PKG_FUNDS_TRANSFER returns only a status and a description; there is no separate
            // response code, and the controller never read this field.
            glToGlTransactionResponse.setResponseDescr(responseDescr[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return glToGlTransactionResponse;
    }

    @Override
    public TblAccount getAccountAgainstAccountNo(TitleFetchRequest titleFetchRequest) {
        return tblAccountRepo.getAccountAgainstAccountNo(titleFetchRequest.getAccountNo());
    }
}
