package com.wallet.transaction.service.impl;

import com.wallet.transaction.dto.*;
import com.wallet.transaction.repo.TblAccountRepo;
import com.wallet.transaction.service.ProcedureService;
import com.wallet.transaction.util.AESencryption;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

@Service
public class ProcedureServiceImpl implements ProcedureService {

    @PersistenceContext
    EntityManager em;


    private final AESencryption aeSencryption = new AESencryption();
    @Autowired
    private TblAccountRepo tblAccountRepo;
    @Value("${mobile.channelCode}")
    private String mobileChannelCode;
    @Value("${mobile.clientSecret}")
    private String mobileClientSecret;
    @Value("${pos.channelCode}")
    private String posChannelCode;
    @Value("${pos.clientSecret}")
    private String posClientSecret;

    @Override
    public ProcResponse walletToCardAcquirer(Long fromAccountId, String toCardNo, String toBankName, String toAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.WALLET_TO_CARD_ACQUIRER(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, mobileClientSecret);
                    call.setString(2, mobileChannelCode);
                    call.setLong(3, fromAccountId);
                    call.setString(4, toCardNo);
                    call.setString(5, toBankName);
                    call.setString(6, toAccountTitle);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, appUserId);
                    call.setString(9, stan);
                    call.setString(10, rrn);

                    call.registerOutParameter(11, Types.VARCHAR);
                    call.registerOutParameter(12, Types.INTEGER);
                    call.registerOutParameter(13, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(11);
                    P_RESPONSESTATUS[0] = call.getInt(12);
                    P_RESPONSEDESCR[0] = call.getString(13);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse cardToWalletAcquirer(String fromCardNo, String fromBankName, String fromAccountTitle, String toAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.CARD_TO_WALLET_ACQUIRER(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setString(3, fromCardNo);
                    call.setString(4, fromBankName);
                    call.setString(5, fromAccountTitle);
                    call.setString(6, toAccountId);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, appUserId);
                    call.setString(9, stan);
                    call.setString(10, rrn);

                    call.registerOutParameter(11, Types.VARCHAR);
                    call.registerOutParameter(12, Types.INTEGER);
                    call.registerOutParameter(13, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(11);
                    P_RESPONSESTATUS[0] = call.getInt(12);
                    P_RESPONSEDESCR[0] = call.getString(13);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse billPaymentIssuer(String fromCardNo, String utilityCompanyCode, String utilityConsumerNo, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];
            final String[] P_CURRENTBALANCE = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.BILL_PAYMENT_ISSUER(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setString(3, fromCardNo);
                    call.setString(4, utilityCompanyCode);
                    call.setString(5, utilityConsumerNo);
                    call.setBigDecimal(6, transAmount);
                    call.setLong(7, appUserId);
                    call.setString(8, stan);
                    call.setString(9, rrn);

                    call.registerOutParameter(10, Types.VARCHAR);
                    call.registerOutParameter(11, Types.VARCHAR);
                    call.registerOutParameter(12, Types.INTEGER);
                    call.registerOutParameter(13, Types.VARCHAR);

                    call.execute();

                    P_CURRENTBALANCE[0] = call.getString(10);
                    P_RESPONSECODE[0] = call.getString(11);
                    P_RESPONSESTATUS[0] = call.getInt(12);
                    P_RESPONSEDESCR[0] = call.getString(13);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setBalance(P_CURRENTBALANCE[0]);
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse cashinAcquirer(String toCardNo, String toBankName, String toAccountTitle, String fromAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn, String province, String district) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.CASHIN_ACQUIRER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, mobileClientSecret);
                    call.setString(2, mobileChannelCode);
                    call.setString(3, toCardNo);
                    call.setString(4, toBankName);
                    call.setString(5, toAccountTitle);
                    call.setString(6, fromAccountId);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, appUserId);
                    call.setString(9, stan);
                    call.setString(10, rrn);
                    call.setString(11, province);
                    call.setString(12, district);

                    call.registerOutParameter(13, Types.VARCHAR);
                    call.registerOutParameter(14, Types.INTEGER);
                    call.registerOutParameter(15, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(13);
                    P_RESPONSESTATUS[0] = call.getInt(14);
                    P_RESPONSEDESCR[0] = call.getString(15);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse cashoutAcquirer(String fromCardNo, String fromBankName, String fromAccountTitle, String toAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn, String province, String district) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.CASHOUT_ACQUIRER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, mobileClientSecret);
                    call.setString(2, mobileChannelCode);
                    call.setString(3, toAccountId);
                    call.setString(4, fromCardNo);
                    call.setString(5, fromBankName);
                    call.setString(6, fromAccountTitle);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, appUserId);
                    call.setString(9, stan);
                    call.setString(10, rrn);
                    call.setString(11, province);
                    call.setString(12, district);

                    call.registerOutParameter(13, Types.VARCHAR);
                    call.registerOutParameter(14, Types.INTEGER);
                    call.registerOutParameter(15, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(13);
                    P_RESPONSESTATUS[0] = call.getInt(14);
                    P_RESPONSEDESCR[0] = call.getString(15);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AgentCashInResponse agentCashIn(String clientSecret, String channelCode, Long appUserId, String relationshipId, String transmissionDate, String transmissionTime, String stan, String rrn, String fromAccountNumber, String toAccountNumber, String transactionAmount, Float latitude, Float longitude) {
        try {
            Session session = em.unwrap(Session.class);
            Long accountId = tblAccountRepo.findByAccountNo(toAccountNumber.substring(6)).getAccountId();

            final Long[] P_TRANS_HEAD_ID = new Long[1];
            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.AGENT_CASH_IN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setLong(3, appUserId);
                    call.setString(4, relationshipId);
                    call.setString(5, transmissionDate);
                    call.setString(6, transmissionTime);
                    call.setString(7, stan);
                    call.setString(8, rrn);
                    call.setString(9, fromAccountNumber);
                    call.setLong(10, accountId);
                    call.setString(11, transactionAmount);
                    call.setFloat(12, latitude);
                    call.setFloat(13, longitude);

                    call.registerOutParameter(14, Types.NUMERIC);
                    call.registerOutParameter(15, Types.VARCHAR);
                    call.registerOutParameter(16, Types.INTEGER);
                    call.registerOutParameter(17, Types.VARCHAR);

                    call.execute();

                    P_TRANS_HEAD_ID[0] = call.getLong(14);
                    P_RESPONSECODE[0] = call.getString(15);
                    P_RESPONSESTATUS[0] = call.getInt(16);
                    P_RESPONSEDESCR[0] = call.getString(17);
                }
            });

            AgentCashInResponse resp = new AgentCashInResponse();
            resp.setTransHeadId(P_TRANS_HEAD_ID[0]);
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BalanceInquiryResponse balanceInquiry(String clientSecret, String channelCode, String accountNumber, String stan, String rrn) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_CURRENTBALANCE = new String[1];
            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.BALANCE_INQUIRY(?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setString(3, accountNumber);
                    call.setString(4, stan);
                    call.setString(5, rrn);

                    call.registerOutParameter(6, Types.VARCHAR);
                    call.registerOutParameter(7, Types.VARCHAR);
                    call.registerOutParameter(8, Types.INTEGER);
                    call.registerOutParameter(9, Types.VARCHAR);

                    call.execute();

                    P_CURRENTBALANCE[0] = call.getString(6);
                    P_RESPONSECODE[0] = call.getString(7);
                    P_RESPONSESTATUS[0] = call.getInt(8);
                    P_RESPONSEDESCR[0] = call.getString(9);
                }
            });

            BalanceInquiryResponse resp = new BalanceInquiryResponse();
            resp.setCurrentBalance(P_CURRENTBALANCE[0]);
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse cardToCardCredit(String toCardNo, String toBankName, String toAccountTitle, String fromAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.CARD_TO_CARD_CREDIT(?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, toCardNo);
                    call.setString(2, toBankName);
                    call.setString(3, toAccountTitle);
                    call.setString(4, fromAccountId);
                    call.setBigDecimal(5, transAmount);
                    call.setLong(6, appUserId);
                    call.setString(7, stan);
                    call.setString(8, rrn);

                    call.registerOutParameter(9, Types.VARCHAR);
                    call.registerOutParameter(10, Types.INTEGER);
                    call.registerOutParameter(11, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(9);
                    P_RESPONSESTATUS[0] = call.getInt(10);
                    P_RESPONSEDESCR[0] = call.getString(11);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse cardToCardDebit(String fromCardNo, String fromBankName, String fromAccountTitle, String toAccountId, BigDecimal transAmount, Long appUserId, String stan, String rrn) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.CARD_TO_CARD_DEBIT(?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, fromCardNo);
                    call.setString(2, fromBankName);
                    call.setString(3, fromAccountTitle);
                    call.setString(4, toAccountId);
                    call.setBigDecimal(5, transAmount);
                    call.setLong(6, appUserId);
                    call.setString(7, stan);
                    call.setString(8, rrn);

                    call.registerOutParameter(9, Types.VARCHAR);
                    call.registerOutParameter(10, Types.INTEGER);
                    call.registerOutParameter(11, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(9);
                    P_RESPONSESTATUS[0] = call.getInt(10);
                    P_RESPONSEDESCR[0] = call.getString(11);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse cardToWalletIssuer(String toCardNo, String toBankName, String toAccountTitle, String fromAccountNumber, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];
            final String[] P_BALANCE = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.CARD_TO_WALLET_ISSUER(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setString(3, toCardNo);
                    call.setString(4, toBankName);
                    call.setString(5, toAccountTitle);
                    call.setString(6, fromAccountNumber);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, 3);
                    call.setString(9, stan);
                    call.setString(10, rrn);
                    call.registerOutParameter(11, Types.VARCHAR);
                    call.registerOutParameter(12, Types.VARCHAR);
                    call.registerOutParameter(13, Types.INTEGER);
                    call.registerOutParameter(14, Types.VARCHAR);

                    call.execute();
                    P_BALANCE[0] = call.getString(11);
                    P_RESPONSECODE[0] = call.getString(12);
                    P_RESPONSESTATUS[0] = call.getInt(13);
                    P_RESPONSEDESCR[0] = call.getString(14);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);
            resp.setBalance(P_BALANCE[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse cashinIssuer(String fromCardNo, String fromBankName, String fromAccountTitle, String fromAccountNumber, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];
            final String[] P_BALANCE = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.CASHIN_ISSUER(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setString(3, fromCardNo);
                    call.setString(4, fromBankName);
                    call.setString(5, fromAccountTitle);
                    call.setString(6, fromAccountNumber);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, 3);
                    call.setString(9, stan);
                    call.setString(10, rrn);
                    call.registerOutParameter(11, Types.VARCHAR);
                    call.registerOutParameter(12, Types.VARCHAR);
                    call.registerOutParameter(13, Types.INTEGER);
                    call.registerOutParameter(14, Types.VARCHAR);

                    call.execute();
                    P_BALANCE[0] = call.getString(11);
                    P_RESPONSECODE[0] = call.getString(12);
                    P_RESPONSESTATUS[0] = call.getInt(13);
                    P_RESPONSEDESCR[0] = call.getString(14);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);
            resp.setBalance(P_BALANCE[0]);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse cashoutIssuer(String fromCardNo, String toCardNo, String toBankName, String toAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_CURRENTBALANCE = new String[1];
            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.CASHOUT_ISSUER(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setString(3, fromCardNo);
                    call.setString(4, toCardNo);
                    call.setString(5, toBankName);
                    call.setString(6, toAccountTitle);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, appUserId);
                    call.setString(9, stan);
                    call.setString(10, rrn);

                    call.registerOutParameter(11, Types.VARCHAR);
                    call.registerOutParameter(12, Types.VARCHAR);
                    call.registerOutParameter(13, Types.INTEGER);
                    call.registerOutParameter(14, Types.VARCHAR);

                    call.execute();

                    P_CURRENTBALANCE[0] = call.getString(11);
                    P_RESPONSECODE[0] = call.getString(12);
                    P_RESPONSESTATUS[0] = call.getInt(13);
                    P_RESPONSEDESCR[0] = call.getString(14);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setBalance(P_CURRENTBALANCE[0]);
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse purchaseAcquirer(Long toAccountId, String fromCardNo, String fromBankName, String fromAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn, String cardAcceptorNameLocation, String cardAcceptorTerminalId) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.PURCHASE_ACQUIRER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, posClientSecret);
                    call.setString(2, posChannelCode);
                    call.setLong(3, toAccountId);
                    call.setString(4, fromCardNo);
                    call.setString(5, fromBankName);
                    call.setString(6, fromAccountTitle);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, appUserId);
                    call.setString(9, stan);
                    call.setString(10, rrn);
                    call.setString(11, cardAcceptorNameLocation);
                    call.setString(12, cardAcceptorTerminalId);

                    call.registerOutParameter(13, Types.VARCHAR);
                    call.registerOutParameter(14, Types.INTEGER);
                    call.registerOutParameter(15, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(13);
                    P_RESPONSESTATUS[0] = call.getInt(14);
                    P_RESPONSEDESCR[0] = call.getString(15);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse purchaseIssuer(String fromCardNo, String toCardNo, String toBankName, String toAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn, String cardAcceptorNameLocation, String cardAcceptorTerminalId, String clientSecret, String channelCode) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_CURRENTBALANCE = new String[1];
            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.PURCHASE_ISSUER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setString(3, fromCardNo);
                    call.setString(4, toCardNo);
                    call.setString(5, toBankName);
                    call.setString(6, toAccountTitle);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, appUserId);
                    call.setString(9, stan);
                    call.setString(10, rrn);
                    call.setString(11, cardAcceptorNameLocation);
                    call.setString(12, cardAcceptorTerminalId);

                    call.registerOutParameter(13, Types.VARCHAR);
                    call.registerOutParameter(14, Types.VARCHAR);
                    call.registerOutParameter(15, Types.INTEGER);
                    call.registerOutParameter(16, Types.VARCHAR);

                    call.execute();

                    P_CURRENTBALANCE[0] = call.getString(13);
                    P_RESPONSECODE[0] = call.getString(14);
                    P_RESPONSESTATUS[0] = call.getInt(15);
                    P_RESPONSEDESCR[0] = call.getString(16);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setBalance(P_CURRENTBALANCE[0]);
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TitleFetchResponse titleFetch(String clientSecret, String channelCode, String accountNumber, String stan, String rrn) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_ACCOUNTTITLE = new String[1];
            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.TITLE_FETCH(?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setString(3, accountNumber);
                    call.setString(4, stan);
                    call.setString(5, rrn);

                    call.registerOutParameter(6, Types.VARCHAR);
                    call.registerOutParameter(7, Types.VARCHAR);
                    call.registerOutParameter(8, Types.INTEGER);
                    call.registerOutParameter(9, Types.VARCHAR);

                    call.execute();

                    P_ACCOUNTTITLE[0] = call.getString(6);
                    P_RESPONSECODE[0] = call.getString(7);
                    P_RESPONSESTATUS[0] = call.getInt(8);
                    P_RESPONSEDESCR[0] = call.getString(9);
                }
            });

            TitleFetchResponse resp = new TitleFetchResponse();
            resp.setAccountTitle(aeSencryption.decrypt(P_ACCOUNTTITLE[0]));
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse walletToCardIssuer(String toAccountNumber, String fromCardNo, String fromBankName, String fromAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn, String clientSecret, String channelCode) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];
            final String[] P_BALANCE = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.WALLET_TO_CARD_ISSUER(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setString(3, toAccountNumber);
                    call.setString(4, fromCardNo);
                    call.setString(5, fromBankName);
                    call.setString(6, fromAccountTitle);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, 3);
                    call.setString(9, stan);
                    call.setString(10, rrn);
                    call.registerOutParameter(11, Types.VARCHAR);
                    call.registerOutParameter(12, Types.VARCHAR);
                    call.registerOutParameter(13, Types.INTEGER);
                    call.registerOutParameter(14, Types.VARCHAR);

                    call.execute();
                    P_BALANCE[0] = call.getString(11);
                    P_RESPONSECODE[0] = call.getString(12);
                    P_RESPONSESTATUS[0] = call.getInt(13);
                    P_RESPONSEDESCR[0] = call.getString(14);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);
            resp.setBalance(P_BALANCE[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse walletToGl(Long fromAccountId, Long toGlAccountId, BigDecimal transAmount, Long appUserId) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.WALLET_TO_GL(?,?,?,?,?,?,?)}");

                    call.setLong(1, fromAccountId);
                    call.setLong(2, toGlAccountId);
                    call.setBigDecimal(3, transAmount);
                    call.setLong(4, appUserId);

                    call.registerOutParameter(5, Types.VARCHAR);
                    call.registerOutParameter(6, Types.INTEGER);
                    call.registerOutParameter(7, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(5);
                    P_RESPONSESTATUS[0] = call.getInt(6);
                    P_RESPONSEDESCR[0] = call.getString(7);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public WalletToWalletResponse walletToWallet(String clientSecret, String channelCode, Long appUserId, String relationshipId, String transmissionDate, String transmissionTime, String stan, String rrn, String dateLocalTran, String timeLocalTran, String acqInstCode, String merchantType, String posEntryMode, String cardAcceptorNameLocation, String cardAcceptorTerminalId, String fromAccountNumber, String fromAccountType, String fromAccountCurrency, String toAccountNumber, String toAccountType, String toAccountCurrency, String transactionAmount, String transactionCurrency, String transactionFee, String udf1, String udf2, String udf3, String udf4, String udf5) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_AUTHIDRESPONSE = new String[1];
            final String[] P_RESPONSECODE = new String[1];
            final String[] P_AVAILABLEBALANCE = new String[1];
            final String[] P_ACTUALBALANCE = new String[1];
            final String[] P_RESPONSEDESCR = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_CHECKPOINT = new String[1];
            final Long[] P_TRANS_HEAD_ID = new Long[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.WALLET_TO_WALLET(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setLong(3, appUserId);
                    call.setString(4, relationshipId);
                    call.setString(5, transmissionDate);
                    call.setString(6, transmissionTime);
                    call.setString(7, stan);
                    call.setString(8, rrn);
                    call.setString(9, dateLocalTran);
                    call.setString(10, timeLocalTran);
                    call.setString(11, acqInstCode);
                    call.setString(12, merchantType);
                    call.setString(13, posEntryMode);
                    call.setString(14, cardAcceptorNameLocation);
                    call.setString(15, cardAcceptorTerminalId);
                    call.setString(16, fromAccountNumber);
                    call.setString(17, fromAccountType);
                    call.setString(18, fromAccountCurrency);
                    call.setString(19, toAccountNumber);
                    call.setString(20, toAccountType);
                    call.setString(21, toAccountCurrency);
                    call.setString(22, transactionAmount);
                    call.setString(23, transactionCurrency);
                    call.setString(24, transactionFee);
                    call.setString(25, udf1);
                    call.setString(26, udf2);
                    call.setString(27, udf3);
                    call.setString(28, udf4);
                    call.setString(29, udf5);

                    call.registerOutParameter(30, Types.VARCHAR);
                    call.registerOutParameter(31, Types.VARCHAR);
                    call.registerOutParameter(32, Types.VARCHAR);
                    call.registerOutParameter(33, Types.VARCHAR);
                    call.registerOutParameter(34, Types.VARCHAR);
                    call.registerOutParameter(35, Types.INTEGER);
                    call.registerOutParameter(36, Types.VARCHAR);
                    call.registerOutParameter(37, Types.NUMERIC);

                    call.execute();

                    P_AUTHIDRESPONSE[0] = call.getString(30);
                    P_RESPONSECODE[0] = call.getString(31);
                    P_AVAILABLEBALANCE[0] = call.getString(32);
                    P_ACTUALBALANCE[0] = call.getString(33);
                    P_RESPONSEDESCR[0] = call.getString(34);
                    P_RESPONSESTATUS[0] = call.getInt(35);
                    P_CHECKPOINT[0] = call.getString(36);
                    P_TRANS_HEAD_ID[0] = call.getLong(37);
                }
            });

            WalletToWalletResponse resp = new WalletToWalletResponse();
            resp.setAuthIdResponse(P_AUTHIDRESPONSE[0]);
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setAvailableBalance(P_AVAILABLEBALANCE[0]);
            resp.setActualBalance(P_ACTUALBALANCE[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setCheckPoint(P_CHECKPOINT[0]);
            resp.setTransHeadId(P_TRANS_HEAD_ID[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse reversal(ReversalRequest reversalRequest) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.REVERSAL(?,?,?,?,?,?,?,?)}");

                    call.setString(1, reversalRequest.getClientSecret());
                    call.setString(2, reversalRequest.getChannelCode());
                    call.setLong(3, 3);
                    call.setString(4, reversalRequest.getStan());
                    call.setString(5, reversalRequest.getRrn());

                    call.registerOutParameter(6, Types.VARCHAR);
                    call.registerOutParameter(7, Types.INTEGER);
                    call.registerOutParameter(8, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(6);
                    P_RESPONSESTATUS[0] = call.getInt(7);
                    P_RESPONSEDESCR[0] = call.getString(8);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse cardToCard(CardToCardProcRequest cardToCardProcRequest) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.CARD_TO_CARD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, cardToCardProcRequest.getClientSecret());
                    call.setString(2, cardToCardProcRequest.getChannelCode());
                    call.setString(3, cardToCardProcRequest.getFromCardNo());
                    call.setString(4, cardToCardProcRequest.getFromBankName());
                    call.setString(5, cardToCardProcRequest.getFromAccountTitle());
                    call.setString(6, cardToCardProcRequest.getToCardNo());
                    call.setString(7, cardToCardProcRequest.getToBankName());
                    call.setString(8, cardToCardProcRequest.getToAccountTitle());
                    call.setBigDecimal(9, cardToCardProcRequest.getTransAmount());
                    call.setLong(10, cardToCardProcRequest.getAppUserId());
                    call.setString(11, cardToCardProcRequest.getStan());
                    call.setString(12, cardToCardProcRequest.getRrn());

                    call.registerOutParameter(13, Types.VARCHAR);
                    call.registerOutParameter(14, Types.INTEGER);
                    call.registerOutParameter(15, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(13);
                    P_RESPONSESTATUS[0] = call.getInt(14);
                    P_RESPONSEDESCR[0] = call.getString(15);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse billPaymentAcquirer(BillPaymentAcquirerRequest request) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.BILL_PAYMENT_ACQUIRER(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, request.getClientSecret());
                    call.setString(2, request.getChannelCode());
                    call.setString(3, request.getFromCardNo());
                    call.setString(4, request.getFromCardTitle());
                    call.setString(5, request.getUtilityCompanyCode());
                    call.setString(6, request.getUtilityConsumerNo());
                    call.setBigDecimal(7, request.getTransAmount());
                    call.setLong(8, request.getAppUserId());
                    call.setString(9, request.getStan());
                    call.setString(10, request.getRrn());

                    call.registerOutParameter(11, Types.VARCHAR);
                    call.registerOutParameter(12, Types.INTEGER);
                    call.registerOutParameter(13, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(11);
                    P_RESPONSESTATUS[0] = call.getInt(12);
                    P_RESPONSEDESCR[0] = call.getString(13);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse walletToWalletAcquirer(String accountNo, String pan, String amount, BigDecimal appUserId, String toBankName, String stan, String rrn) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.WALLET_TO_WALLET_ACQUIRER(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, mobileClientSecret);
                    call.setString(2, mobileChannelCode);
                    call.setLong(3, Long.parseLong(accountNo));
                    call.setString(4, pan);
                    call.setString(5, toBankName);
                    call.setString(6, "");
                    call.setBigDecimal(7, new BigDecimal(amount));
                    call.setBigDecimal(8, appUserId);
                    call.setString(9, stan);
                    call.setString(10, rrn);

                    call.registerOutParameter(11, Types.VARCHAR);
                    call.registerOutParameter(12, Types.INTEGER);
                    call.registerOutParameter(13, Types.VARCHAR);

                    call.execute();

                    P_RESPONSECODE[0] = call.getString(11);
                    P_RESPONSESTATUS[0] = call.getInt(12);
                    P_RESPONSEDESCR[0] = call.getString(13);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse walletToWalletIssuer(String toAccountNo, String fromAccountNo, String amount, BigDecimal appUserId, String stan, String rrn, String clientSecret, String channelCode) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];
            final String[] P_BALANCE = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.WALLET_TO_WALLET_ISSUER(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setString(3, toAccountNo);
                    call.setString(4, fromAccountNo);
                    call.setString(5, "");
                    call.setString(6, "");
                    call.setBigDecimal(7, new BigDecimal(amount));
                    call.setLong(8, 3);
                    call.setString(9, stan);
                    call.setString(10, rrn);
                    call.registerOutParameter(11, Types.VARCHAR);
                    call.registerOutParameter(12, Types.VARCHAR);
                    call.registerOutParameter(13, Types.INTEGER);
                    call.registerOutParameter(14, Types.VARCHAR);

                    call.execute();
                    P_BALANCE[0] = call.getString(11);
                    P_RESPONSECODE[0] = call.getString(12);
                    P_RESPONSESTATUS[0] = call.getInt(13);
                    P_RESPONSEDESCR[0] = call.getString(14);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);
            resp.setBalance(P_BALANCE[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProcResponse purchase(Long toAccountId, String fromCardNo, String fromBankName, String fromAccountTitle, BigDecimal transAmount, Long appUserId, String stan, String rrn, String cardAcceptorNameLocation, String cardAcceptorTerminalId, String clientSecret, String channelCode) {
        try {
            Session session = em.unwrap(Session.class);

            final String[] P_RESPONSECODE = new String[1];
            final Integer[] P_RESPONSESTATUS = new Integer[1];
            final String[] P_RESPONSEDESCR = new String[1];

            session.doWork(new Work() {
                public void execute(Connection connection) throws SQLException {
                    CallableStatement call = connection.prepareCall("{call PKG_PAYMENTS1.PURCHASE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                    call.setString(1, clientSecret);
                    call.setString(2, channelCode);
                    call.setLong(3, toAccountId);
                    call.setString(4, fromCardNo);
                    call.setString(5, fromBankName);
                    call.setString(6, fromAccountTitle);
                    call.setBigDecimal(7, transAmount);
                    call.setLong(8, appUserId);
                    call.setString(9, stan);
                    call.setString(10, rrn);
                    call.setString(11, cardAcceptorNameLocation);
                    call.setString(12, cardAcceptorTerminalId);
                    call.registerOutParameter(13, Types.VARCHAR);
                    call.registerOutParameter(14, Types.INTEGER);
                    call.registerOutParameter(15, Types.VARCHAR);

                    call.execute();
                    P_RESPONSECODE[0] = call.getString(13);
                    P_RESPONSESTATUS[0] = call.getInt(14);
                    P_RESPONSEDESCR[0] = call.getString(15);
                }
            });

            ProcResponse resp = new ProcResponse();
            resp.setResponseCode(P_RESPONSECODE[0]);
            resp.setResponseStatus(P_RESPONSESTATUS[0]);
            resp.setResponseDescription(P_RESPONSEDESCR[0]);

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}