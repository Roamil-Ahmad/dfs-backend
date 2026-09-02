package com.dfs.backoffice.utils;

import com.dfs.backoffice.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class RequestValidator {
    public static <T> boolean isNullOrEmpty(T input) {
        return input == null || (input instanceof String && ((String) input).isEmpty()) ||
                (input instanceof List && ((List<?>) input).isEmpty()) ||
                (input instanceof Map && ((Map<?, ?>) input).isEmpty()) ||
                (input instanceof BigDecimal && ((BigDecimal) input).compareTo(BigDecimal.ZERO) <= 0);
    }

    public static void userLoginValidator(LoginRequest loginRequest) {
        if (isNullOrEmpty(loginRequest.getUsername())) {
            throw new ValidationException("Username Required");
        }
    }

    public static void createUserJsonValidate(CreateUserRequest createUserRequest) {
        if (isNullOrEmpty(createUserRequest.getDepartment())) {
            throw new ValidationException("Department Required");
        }
    }

    public static void updateUserJsonValidate(CreateUserRequest createUserRequest) {
        if (isNullOrEmpty(createUserRequest.getUserId())) {
            throw new ValidationException("UserId Required");
        }
    }

    public static void changePasswordJsonValidate(ChangePasswordRequest changePasswordRequest) {
        if (isNullOrEmpty(changePasswordRequest.getOldPass())) {
            throw new ValidationException("Old Password Required");
        }
    }

    public static void changeUserPasswordJsonValidate(ChangeUserPasswordRequest changeUserPasswordRequest) {
        if (isNullOrEmpty(changeUserPasswordRequest.getUserName())) {
            throw new ValidationException("Username Required");
        }
    }

    public static void createMenuJsonValidate(CreateMenuRequest createMenuRequest) {
        if (isNullOrEmpty(createMenuRequest.getMenuDescr())) {
            throw new ValidationException("Menu Description Required");
        }
    }

    public static void updateMenuJsonValidate(CreateMenuRequest createMenuRequest) {
        if (isNullOrEmpty(createMenuRequest.getMenuDescr())) {
            throw new ValidationException("Menu Description Required");
        }
    }

    public static void createRoleJsonValidate(CreateRoleRequest createRoleRequest) {
        if (isNullOrEmpty(createRoleRequest.getRoleDescr())) {
            throw new ValidationException("Role Description Required");
        }
    }

    public static void updateRoleJsonValidate(CreateRoleRequest createRoleRequest) {
        if (isNullOrEmpty(createRoleRequest.getRoleDescr())) {
            throw new ValidationException("Role Description Required");
        }
    }

    public static void inactiveRoleJsonValidate(CreateRoleRequest createRoleRequest) {
        if (isNullOrEmpty(createRoleRequest.getIsActive())) {
            throw new ValidationException("isActive Required");
        }
    }

    public static void createRoleRightsJsonValidate(CreateRoleRightsRequest createRoleRightsRequest) {
        if (isNullOrEmpty(createRoleRightsRequest.getRoleId())) {
            throw new ValidationException("RoleId Required");
        }
    }

    public static void updateRoleRightsJsonValidate(CreateRoleRightsRequest createRoleRightsRequest) {
        if (isNullOrEmpty(createRoleRightsRequest.getRoleId())) {
            throw new ValidationException("RoleId Required");
        }
    }

    public static void inactiveRoleRightsJsonValidate(CreateRoleRightsRequest createRoleRightsRequest) {
        if (isNullOrEmpty(createRoleRightsRequest.getRoleId())) {
            throw new ValidationException("RoleId Required");
        }
    }

    public static void createAccountLevelJsonValidate(AccountLevelRequest accountLevelRequest) {
        if (isNullOrEmpty(accountLevelRequest.getAccountLevelDescr())) {
            throw new ValidationException("Account Level Description Required");
        }
    }

    public static void updateAccountLevelJsonValidate(AccountLevelRequest accountLevelRequest) {
        if (isNullOrEmpty(accountLevelRequest.getAccountLevelDescr())) {
            throw new ValidationException("Account Level Description Required");
        }
    }

    public static void createTransactionWiseLimitJsonValidate(TblTransLimitRequest tblTransLimitRequest) {
        if (isNullOrEmpty(tblTransLimitRequest.getLimitProfileName())) {
            throw new ValidationException("Limit Profile Name Required");
        }
    }

    public static void updateTransactionWiseLimitJsonValidate(TblTransLimitRequest tblTransLimitRequest) {
        if (isNullOrEmpty(tblTransLimitRequest.getLimitProfileName())) {
            throw new ValidationException("Limit Profile Name Required");
        }
    }

    public static void searchUserJsonValidate(UserSearch userSearch) {
        if (isNullOrEmpty(userSearch.getFromDate()) && isNullOrEmpty(userSearch.getToDate()) && isNullOrEmpty(userSearch.getEmail()) && isNullOrEmpty(userSearch.getEmployeeName()) && isNullOrEmpty(userSearch.getMobileNo()) && isNullOrEmpty(userSearch.getEmployeeNo()) && isNullOrEmpty(userSearch.getRoleId()) && isNullOrEmpty(userSearch.getStatusId())) {
            throw new ValidationException("Select Any One Option");
        }
    }

    public static void searchRoleJsonValidate(SearchRole searchRole) {
        if (isNullOrEmpty(searchRole.getFromDate()) && isNullOrEmpty(searchRole.getToDate()) && isNullOrEmpty(searchRole.getCreateUser()) && isNullOrEmpty(searchRole.getRoleDescr()) && isNullOrEmpty(searchRole.getStatusId())) {
            throw new ValidationException("Select Any One Option");
        }
    }

    public static void searchCustomerJsonValidate(SearchCustomerRequest searchCustomerRequest) {
        if (isNullOrEmpty(searchCustomerRequest.getFromDate()) && isNullOrEmpty(searchCustomerRequest.getToDate()) && isNullOrEmpty(searchCustomerRequest.getAccountStatusId()) && isNullOrEmpty(searchCustomerRequest.getFullName()) && isNullOrEmpty(searchCustomerRequest.getMobileNo()) && isNullOrEmpty(searchCustomerRequest.getNidNo())) {
            throw new ValidationException("Select Any One Option");
        }
    }

    public static void searchAccountLevelJsonValidate(SearchAccountLevel searchAccountLevel) {
        if (isNullOrEmpty(searchAccountLevel.getFromDate()) && isNullOrEmpty(searchAccountLevel.getToDate()) && isNullOrEmpty(searchAccountLevel.getAccountLevelDescr()) && isNullOrEmpty(searchAccountLevel.getGlAccountId())) {
            throw new ValidationException("Select Any One Option");
        }
    }

    public static void createGlAccountJsonValidate(GlCreationRequest glCreationRequest) {
        if (isNullOrEmpty(glCreationRequest.getGlAccountDescr())) {
            throw new ValidationException("GL Account Descr Required");
        }
    }

    public static void updateGlAccountJsonValidate(GlCreationRequest glCreationRequest) {
        if (isNullOrEmpty(glCreationRequest.getGlAccountDescr())) {
            throw new ValidationException("GL Account Descr Required");
        }
    }

    public static void accountTypeCreationJsonValidate(AccountTypeCreation accountTypeCreation) {
        if (isNullOrEmpty(accountTypeCreation.getAccountType())) {
            throw new ValidationException("Account Type Required");
        }
        if (isNullOrEmpty(accountTypeCreation.getAccountTypeCode())) {
            throw new ValidationException("Account Type Code Required");
        }
        if (isNullOrEmpty(accountTypeCreation.getAccountTypeDescr())) {
            throw new ValidationException("Account Type Description Required");
        }
    }

    public static void accountTypeUpdateJsonValidate(AccountTypeUpdate accountTypeCreation) {
        if (isNullOrEmpty(accountTypeCreation.getAccountType())) {
            throw new ValidationException("Account Type Required");
        }
        if (isNullOrEmpty(accountTypeCreation.getAccountTypeCode())) {
            throw new ValidationException("Account Type Code Required");
        }
        if (isNullOrEmpty(accountTypeCreation.getAccountTypeDescr())) {
            throw new ValidationException("Account Type Description Required");
        }
        if (isNullOrEmpty(accountTypeCreation.getAccountTypeDescr())) {
            throw new ValidationException("Account Type Description Required");
        }
        if (isNullOrEmpty(accountTypeCreation.getAccountTypeId())) {
            throw new ValidationException("Account Type ID Required");
        }
    }

    public static void saveTransDocsJsonValidate(TransDocsRequest transDocsRequest) {
        if (isNullOrEmpty(transDocsRequest.getTransDocsDescr())) {
            throw new ValidationException("Trans Docs Descr Required");
        }
    }

    public static void updateTransDocsJsonValidate(TransDocsRequest transDocsRequest) {
        if (isNullOrEmpty(transDocsRequest.getTransDocsDescr())) {
            throw new ValidationException("Trans Docs Descr Required");
        }
    }

    public static void getAllTransDocsJsonValidate(SearchTransDocs searchTransDocs) {
        if (isNullOrEmpty(searchTransDocs.getFromDate()) && isNullOrEmpty(searchTransDocs.getToDate()) && isNullOrEmpty(searchTransDocs.getIsActive()) && isNullOrEmpty(searchTransDocs.getTransDocsDescr())) {
            throw new ValidationException("Select Any One Option");
        }
    }

    public static void searchTransactionJsonValidate(SearchTransactionRequest searchTransactionRequest) {
        if (isNullOrEmpty(searchTransactionRequest.getCardNo()) && isNullOrEmpty(searchTransactionRequest.getCustomerName()) && isNullOrEmpty(searchTransactionRequest.getDateFrom()) && isNullOrEmpty(searchTransactionRequest.getDateTo())
                && isNullOrEmpty(searchTransactionRequest.getStatus()) && isNullOrEmpty(searchTransactionRequest.getTransDocsId()) && isNullOrEmpty(searchTransactionRequest.getTransRefNum())) {
            throw new ValidationException("Select Any One Option");
        }
    }

    public static void transactionDetailJsonValidate(TransactionDetailRequest transactionDetailRequest) {
        if (isNullOrEmpty(transactionDetailRequest.getMwRequestId())) {
            throw new ValidationException("ID Required");
        }
    }

    public static void createAgentAccountJsonValidate(CreateAgentAccountRequest createAgentAccountRequest) {
        if (isNullOrEmpty(createAgentAccountRequest.getAgentNidNo())) {
            throw new ValidationException("NidNo Required");
        }
    }

    public static void updateAgentAccountJsonValidate(CreateAgentAccountRequest createAgentAccountRequest) {
        if (isNullOrEmpty(createAgentAccountRequest.getAgentid())) {
            throw new ValidationException("Agent Id Required");
        }
    }

    public static void searchAgentJsonValidate(SearchAgent searchAgent) {
        if (isNullOrEmpty(searchAgent.getFromDate()) && isNullOrEmpty(searchAgent.getToDate()) && isNullOrEmpty(searchAgent.getAgentClassId()) && isNullOrEmpty(searchAgent.getCnic()) && isNullOrEmpty(searchAgent.getMobileNo())) {
            throw new ValidationException("Select Any One Option");
        }
    }

    public static void twoFactorAuthJsonValidate(TwoFactorAuthRequest twoFactorAuthRequest) {
        if (isNullOrEmpty(twoFactorAuthRequest.getTwoFaType())) {
            throw new ValidationException("2FA Method Required");
        }
        if (twoFactorAuthRequest.getTwoFaType().equalsIgnoreCase("E") && isNullOrEmpty(twoFactorAuthRequest.getEmail())) {
            throw new ValidationException("Email Required");
        }
        if (twoFactorAuthRequest.getTwoFaType().equalsIgnoreCase("S") && isNullOrEmpty(twoFactorAuthRequest.getMobileNo())) {
            throw new ValidationException("Phone Number Required");
        }
        if (isNullOrEmpty(twoFactorAuthRequest.getTwoFaEnabled())) {
            throw new ValidationException("2FA Required");
        }
    }

    public static void validateGenerateNotificationRequest(GenerateNotificationRequest generateNotificationRequest) {
        if (isNullOrEmpty(generateNotificationRequest.getType())) {
            throw new ValidationException("INVALID TYPE");
        }
        if (generateNotificationRequest.getType().equalsIgnoreCase("E") && isNullOrEmpty(generateNotificationRequest.getEmail())) {
            throw new ValidationException("INVALID EMAIL");
        }
        if (generateNotificationRequest.getType().equalsIgnoreCase("M") && isNullOrEmpty(generateNotificationRequest.getEmail())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }

        if (generateNotificationRequest.getType().equalsIgnoreCase("E") && isNullOrEmpty(generateNotificationRequest.getSubject())) {
            throw new ValidationException("INVALID EMAIL SUBJECT");
        }
        if (isNullOrEmpty(generateNotificationRequest.getSms())) {
            throw new ValidationException("INVALID SMS");
        }
    }

    public static void validateGenerateOtp(GenerateOtpRequest generateOtpRequest) {

        if (isNullOrEmpty(generateOtpRequest.getOtpType())) {
            throw new ValidationException("INVALID OTP TYPE");
        }
        if (generateOtpRequest.getOtpType().equalsIgnoreCase("EOV") && isNullOrEmpty(generateOtpRequest.getEmail())) {
            throw new ValidationException("INVALID EMAIL");
        }
        if (isNullOrEmpty(generateOtpRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
    }

    public static void validateVerifyOtp(VerifyOtpRequest verifyOtpRequest) {
        if (isNullOrEmpty(verifyOtpRequest.getOtpType())) {
            throw new ValidationException("INVALID OTP TYPE");
        }
        if (verifyOtpRequest.getOtpType().equalsIgnoreCase("EOV") && isNullOrEmpty(verifyOtpRequest.getEmail())) {
            throw new ValidationException("INVALID EMAIL");
        }
        if (isNullOrEmpty(verifyOtpRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(verifyOtpRequest.getOtpId())) {
            throw new ValidationException("INVALID Otp ID");
        }
        if (isNullOrEmpty(verifyOtpRequest.getOtpPin())) {
            throw new ValidationException("INVALID Otp Pin");
        }
    }

    public static void searchKycJsonValidate(SearchKycRequest searchKycRequest) {
        if (isNullOrEmpty(searchKycRequest.getAccountTitle()) && isNullOrEmpty(searchKycRequest.getFromDate()) && isNullOrEmpty(searchKycRequest.getMobileNo()) && isNullOrEmpty(searchKycRequest.getStatusId())
                && isNullOrEmpty(searchKycRequest.getToDate())) {
            throw new ValidationException("Select Any One Option");
        }
    }

    public static void updateKycJsonValidate(UpdateKycRequest updateKycRequest) {
        if (isNullOrEmpty(updateKycRequest.getAccountId())) {
            throw new ValidationException("Account Id Required");
        }
        if (isNullOrEmpty(updateKycRequest.getAccountLevelId())) {
            throw new ValidationException("Account Level Id Required");
        }
        if (isNullOrEmpty(updateKycRequest.getStatusId())) {
            throw new ValidationException("Status Required");
        }
    }

    public static void auditLogsJsonValidate(AuditLogRequest auditLogRequest) {
        if (isNullOrEmpty(auditLogRequest.getToDate()) && isNullOrEmpty(auditLogRequest.getFromDate()) && isNullOrEmpty(auditLogRequest.getMobileNo()) && isNullOrEmpty(auditLogRequest.getUserName())) {
            throw new ValidationException("Select Any One Option");
        }
    }

    public static void forgetPasswordJsonValidate(ForgetPasswordRequest forgetPasswordRequest) {
        if (isNullOrEmpty(forgetPasswordRequest.getEmail())) {
            throw new ValidationException("Email Required");
        }
    }

    public static void validateCardInquiry(CardInquiryRequest cardInquiryRequest) {
        if (isNullOrEmpty(cardInquiryRequest.getRelationshipNum())) {
            throw new ValidationException("Relationship Number Required");
        }
    }

    public static void validateChangeCardStatus(ChangeCardStatusRequest changeCardStatusRequest) {
        if (isNullOrEmpty(changeCardStatusRequest.getPan())) {
            throw new ValidationException("PAN Required");
        }
        if (isNullOrEmpty(changeCardStatusRequest.getStatusCode())) {
            throw new ValidationException("Card Status Code Required");
        }
    }

    public static void validateFetchLimits(FetchLimitsRequest fetchLimitsRequest) {
        if (isNullOrEmpty(fetchLimitsRequest.getAccountId())) {
            throw new ValidationException("Account Id Required");
        }
        if (isNullOrEmpty(fetchLimitsRequest.getPan())) {
            throw new ValidationException("PAN Required");
        }
    }

    public static void agentDetailJsonValidate(AgentDetailRequest agentDetailRequest) {
        if (isNullOrEmpty(agentDetailRequest.getAgentId())) {
            throw new ValidationException("Agent ID Required");
        }
    }
}
