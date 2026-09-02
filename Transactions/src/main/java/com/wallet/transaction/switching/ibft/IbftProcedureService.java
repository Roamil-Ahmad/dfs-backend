package com.wallet.transaction.switching.ibft;

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
 * Calls the IBFT stored procedures.
 *
 * The accounting is entirely the database's: this class prepares parameters, executes the call and
 * reads the OUT parameters back. It never posts, debits, credits or compensates in Java.
 *
 * <p><b>Known state of the database objects.</b> {@code PKG_MW} currently has a VALID specification
 * and an INVALID body, so these calls fail today with ORA-04063 / ORA-06508. The reported errors
 * are:</p>
 * <pre>
 *   ORA-00904 "CREATEUSERTYPE": invalid identifier          (body lines 243, 262, 286, 304)
 *   ORA-00904 "D"."STATUS": invalid identifier              (line 365)
 *   ORA-00904 "C"."CHARGES_APPLICABLE": invalid identifier  (line 385)
 *   PLS-00341 cursor C_CHARGES_APPLICABLE malformed
 *   PLS-00341 cursor C_TRANS_CHARGES malformed
 *   PLS-00320 x 2
 * </pre>
 * <p>Nothing here works around that. The failure is captured and surfaced through the normal error
 * path, and once the database team recompiles the body these same calls consume the real response
 * with no change to this class or to anything above it.</p>
 */
@Service
public class IbftProcedureService {

    private static final Logger log = LoggerFactory.getLogger(IbftProcedureService.class);

    private static final String OUTGOING_CALL =
            "{call PKG_MW.OUTGOING_IBFT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    private static final String INCOMING_CALL =
            "{call PKG_MW.INCOMING_IBFT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

    @PersistenceContext
    private EntityManager em;

    /**
     * PKG_MW.OUTGOING_IBFT - debits the DFS customer for a transfer leaving to another bank.
     * Parameter 18 is the card acceptor name/location and 19 the terminal id.
     */
    public ProcedureResult outgoingIbft(IbftProcedureRequest request) {
        return execute(OUTGOING_CALL, request, false, "PKG_MW.OUTGOING_IBFT");
    }

    /**
     * PKG_MW.INCOMING_IBFT - credits the DFS customer for a transfer arriving from another bank.
     * Parameter 18 is the terminal id and 19 the card acceptor name/location, i.e. the reverse of
     * the outgoing procedure, and P_IDENTIFIER is declared CHAR here.
     */
    public ProcedureResult incomingIbft(IbftProcedureRequest request) {
        return execute(INCOMING_CALL, request, true, "PKG_MW.INCOMING_IBFT");
    }

    private ProcedureResult execute(String call, IbftProcedureRequest request, boolean terminalIdFirst,
                                    String procedureName) {
        ProcedureResult result = new ProcedureResult();
        try {
            Session session = em.unwrap(Session.class);
            session.doWork(connection -> {
                try (CallableStatement statement = connection.prepareCall(call)) {
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
                    statement.setString(14, request.getFromAccountNumber());
                    statement.setString(15, request.getFromAccountType());
                    statement.setString(16, request.getFromAccountCurrency());
                    statement.setString(17, request.getToAccountNumber());

                    // The two procedures order these two parameters differently.
                    if (terminalIdFirst) {
                        statement.setString(18, request.getCardAcceptorTerminalId());
                        statement.setString(19, request.getCardAcceptorNameLocation());
                    } else {
                        statement.setString(18, request.getCardAcceptorNameLocation());
                        statement.setString(19, request.getCardAcceptorTerminalId());
                    }

                    statement.setString(20, request.getTransactionAmount());
                    statement.setString(21, request.getTransactionCurrency());
                    statement.setString(22, request.getTransactionPurpose());
                    statement.setString(23, request.getSourceImd());
                    statement.setString(24, request.getDestinationImd());
                    statement.setString(25, request.getIdentifier());

                    // 26 and 28 are IN OUT.
                    statement.setString(26, request.getRecordData());
                    statement.registerOutParameter(26, Types.VARCHAR);
                    statement.setString(27, request.getTransactionFee());
                    statement.setString(28, request.getUdf1());
                    statement.registerOutParameter(28, Types.VARCHAR);

                    statement.setString(29, request.getUdf2());
                    statement.setString(30, request.getUdf3());
                    statement.setString(31, request.getUdf4());
                    statement.setString(32, request.getUdf5());

                    statement.registerOutParameter(33, Types.VARCHAR);   // P_AUTHIDRESPONSE
                    statement.registerOutParameter(34, Types.VARCHAR);   // P_ERRORRESPONSE
                    statement.registerOutParameter(35, Types.VARCHAR);   // P_RESPONSEDESCR
                    statement.registerOutParameter(36, Types.INTEGER);   // P_RESPONSESTATUS
                    statement.registerOutParameter(37, Types.VARCHAR);   // P_CHECKPOINT
                    statement.registerOutParameter(38, Types.NUMERIC);   // P_TRANS_HEAD_ID

                    statement.execute();

                    result.setRecordData(statement.getString(26));
                    result.setUdf1(statement.getString(28));
                    result.setAuthIdResponse(statement.getString(33));
                    result.setErrorResponse(statement.getString(34));
                    result.setResponseDescription(statement.getString(35));
                    int status36 = statement.getInt(36);
                    result.setResponseStatus(statement.wasNull() ? null : status36);
                    result.setCheckpoint(statement.getString(37));
                    long transHeadId = statement.getLong(38);
                    result.setTransHeadId(statement.wasNull() ? null : transHeadId);
                }
            });

            log.info("{} completed | STAN:{} | RRN:{} | status:{} | error:{} | checkpoint:{} | transHeadId:{}",
                    procedureName, request.getStan(), request.getRrn(), result.getResponseStatus(),
                    result.getErrorResponse(), result.getCheckpoint(), result.getTransHeadId());
            return result;

        } catch (Exception e) {
            // The package body is known to be invalid today. Report the real database message
            // rather than masking it, so the failure is diagnosable and nothing is faked.
            log.error("{} failed | STAN:{} | RRN:{} | {}",
                    procedureName, request.getStan(), request.getRrn(), rootMessage(e), e);
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
