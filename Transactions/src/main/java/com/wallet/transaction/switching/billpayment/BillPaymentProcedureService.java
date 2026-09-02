package com.wallet.transaction.switching.billpayment;

import com.wallet.transaction.switching.common.ProcedureResult;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Calls the bill payment stored procedure.
 *
 * The debit is entirely the database's: this class prepares parameters, executes the call and reads
 * the OUT parameters back. It never posts, debits, credits or compensates in Java, exactly as
 * {@code IbftProcedureService} does for IBFT.
 *
 * <p>The procedure is {@code PKG_MW.BILL_PAYMENT}: 28 IN parameters, of which 24 is IN OUT,
 * followed by 8 OUT. Its first thirteen positions match PKG_MW's other procedures - so it
 * authenticates against TBL_MW_CHANNEL through VALIDATE_TOKEN and resolves the payer through
 * VALIDATE_PAN_CNIC - but from position 14 the layout is its own.</p>
 */
@Service
public class BillPaymentProcedureService {

    private static final Logger log = LoggerFactory.getLogger(BillPaymentProcedureService.class);

    private static final String BILL_PAYMENT_CALL =
            "{call PKG_MW.BILL_PAYMENT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

    @PersistenceContext
    private EntityManager em;

    /** PKG_MW.BILL_PAYMENT - debits the DFS customer for the bill being settled. */
    public ProcedureResult billPayment(BillPaymentProcedureRequest request) {
        ProcedureResult result = new ProcedureResult();
        try {
            Session session = em.unwrap(Session.class);
            session.doWork(connection -> {
                // try-with-resources: the statement is closed even when the call fails, so a
                // repeated failure cannot exhaust the database's open cursors.
                try (CallableStatement statement = connection.prepareCall(BILL_PAYMENT_CALL)) {
                    statement.setString(1, request.getClientSecret());
                    statement.setString(2, request.getChannelCode());
                    setLong(statement, 3, request.getUserId());
                    statement.setString(4, request.getRelationshipId());
                    statement.setString(5, request.getTransmissionDate());
                    statement.setString(6, request.getTransmissionTime());
                    statement.setString(7, request.getStan());
                    statement.setString(8, request.getRrn());
                    statement.setString(9, request.getDateLocalTran());
                    statement.setString(10, request.getTimeLocalTran());
                    statement.setString(11, request.getAcquiringInstitutionCode());
                    statement.setString(12, request.getMerchantType());
                    statement.setString(13, request.getPosEntryMode());
                    statement.setString(14, request.getCardAcceptorNameLocation());
                    statement.setString(15, request.getCardAcceptorTerminalId());
                    statement.setString(16, request.getFromAccountNumber());
                    statement.setString(17, request.getFromAccountType());
                    statement.setString(18, request.getFromAccountCurrency());
                    statement.setString(19, request.getTransactionAmount());
                    statement.setString(20, request.getTransactionCurrency());
                    statement.setString(21, request.getUtilityCompanyId());
                    statement.setString(22, request.getUtilityConsumerNumber());
                    statement.setString(23, request.getTransactionFee());

                    // 24 is IN OUT: it is both sent and read back.
                    statement.setString(24, request.getUdf1());
                    statement.registerOutParameter(24, Types.VARCHAR);

                    statement.setString(25, request.getUdf2());
                    statement.setString(26, request.getUdf3());
                    statement.setString(27, request.getUdf4());
                    statement.setString(28, request.getUdf5());

                    statement.registerOutParameter(29, Types.VARCHAR);   // P_AUTHIDRESPONSE
                    statement.registerOutParameter(30, Types.VARCHAR);   // P_ERRORRESPONSE
                    statement.registerOutParameter(31, Types.VARCHAR);   // P_AVAILABLEBALANCE
                    statement.registerOutParameter(32, Types.VARCHAR);   // P_ACTUALBALANCE
                    statement.registerOutParameter(33, Types.VARCHAR);   // P_RESPONSEDESCR
                    statement.registerOutParameter(34, Types.INTEGER);   // P_RESPONSESTATUS
                    statement.registerOutParameter(35, Types.VARCHAR);   // P_CHECKPOINT
                    statement.registerOutParameter(36, Types.NUMERIC);   // P_TRANS_HEAD_ID

                    statement.execute();

                    result.setUdf1(statement.getString(24));
                    result.setAuthIdResponse(statement.getString(29));
                    result.setErrorResponse(statement.getString(30));
                    result.setAvailableBalance(statement.getString(31));
                    result.setActualBalance(statement.getString(32));
                    result.setResponseDescription(statement.getString(33));

                    // getInt answers 0 for SQL NULL, which would read as "aborted" rather than
                    // "the procedure never said". wasNull tells the two apart.
                    int status34 = statement.getInt(34);
                    result.setResponseStatus(statement.wasNull() ? null : status34);

                    result.setCheckpoint(statement.getString(35));
                    long transHeadId = statement.getLong(36);
                    result.setTransHeadId(statement.wasNull() ? null : transHeadId);
                }
            });

            log.info("PKG_MW.BILL_PAYMENT completed | STAN:{} | RRN:{} | status:{} | code:{} | checkpoint:{}",
                    request.getStan(), request.getRrn(), result.getResponseStatus(),
                    result.getErrorResponse(), result.getCheckpoint());
            return result;

        } catch (Exception e) {
            // Report the real database message rather than masking it, so the failure is
            // diagnosable and no success is fabricated.
            log.error("PKG_MW.BILL_PAYMENT failed | STAN:{} | RRN:{} | {}",
                    request.getStan(), request.getRrn(), rootMessage(e), e);
            result.setResponseStatus(null);
            result.setErrorResponse(null);
            result.setResponseDescription(rootMessage(e));
            return result;
        }
    }

    private void setLong(CallableStatement statement, int index, Long value) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.NUMERIC);
        } else {
            statement.setLong(index, value);
        }
    }

    private String rootMessage(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }
}
