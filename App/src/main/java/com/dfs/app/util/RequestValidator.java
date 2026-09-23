package com.dfs.app.util;


import com.dfs.app.dto.*;
import com.dfs.app.dto.common.CustomerKycRequest;
import com.dfs.app.dto.common.Request;
import com.dfs.app.dto.common.VerifyOtpRequest;
import com.dfs.app.dto.common.VerifyOtpResponse;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class RequestValidator {
    public static <T> boolean isNullOrEmpty(T input) {
        return input == null || (input instanceof String && ((String) input).isEmpty()) ||
                (input instanceof List && ((List<?>) input).isEmpty()) ||
                (input instanceof Map && ((Map<?, ?>) input).isEmpty()) ||
                (input instanceof BigDecimal && ((BigDecimal) input).compareTo(BigDecimal.ZERO) <= 0);
    }

    public static void mobileRegistrationRequestValidation(MobileRegistrationRequest mobileRegistrationRequest) {
        if (isNullOrEmpty(mobileRegistrationRequest.getAppVersion())) {
            throw new ValidationException("INVALID APP VERSION");
        }
        if (isNullOrEmpty(mobileRegistrationRequest.getMobileNo())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(mobileRegistrationRequest.getDeviceModel())) {
            throw new ValidationException("INVALID DEVICE MODEL");
        }
        if (isNullOrEmpty(mobileRegistrationRequest.getImeiNo())) {
            throw new ValidationException("INVALID IMEI NUMBER");
        }
        if (isNullOrEmpty(mobileRegistrationRequest.getIpAddressP())) {
            throw new ValidationException("INVALID PRIMARY IP ADDRESS");
        }
        if (isNullOrEmpty(mobileRegistrationRequest.getIpAddressA())) {
            throw new ValidationException("INVALID ALTERNATE IP ADDRESS");
        }
        if (isNullOrEmpty(mobileRegistrationRequest.getNidNo())) {
            throw new ValidationException("INVALID NID NUMBER");
        }
        // The bean-validation annotations on the DTO are not enforced anywhere in this service
        // (nothing is annotated @Valid), so the format is checked here.
        if (isNullOrEmpty(mobileRegistrationRequest.getNidIssuanceDate())
                || !mobileRegistrationRequest.getNidIssuanceDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new ValidationException("NidNo Issuance Date must be in the format yyyy-MM-dd");
        }
    }

    public static void validateVerifyOtp(VerifyOtpRequest verifyOtpRequest) {
        if (isNullOrEmpty(verifyOtpRequest.getOtpType())) {
            throw new ValidationException("INVALID OTP TYPE");
        }
        if (verifyOtpRequest.getOtpType().charAt(0) == 'E' && isNullOrEmpty(verifyOtpRequest.getEmail())) {
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

    public static void validateNidRequest(CustomerKycRequest nidRequest) {
        if (isNullOrEmpty(nidRequest.getFullName())) {
            throw new ValidationException("INVALID FULL NAME");
        }
        if (isNullOrEmpty(nidRequest.getFatherName())) {
            throw new ValidationException("INVALID FATHER NAME");
        }
        if (isNullOrEmpty(nidRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(nidRequest.getGender())) {
            throw new ValidationException("INVALID GENDER");
        }
        if (isNullOrEmpty(nidRequest.getNidNumber())) {
            throw new ValidationException("INVALID NID NUMBER");
        }
        if (isNullOrEmpty(nidRequest.getDob())) {
            throw new ValidationException("INVALID DATE OF BIRTH");
        }
        if (isNullOrEmpty(nidRequest.getNidIssuanceDate())) {
            throw new ValidationException("INVALID NID ISSUANCE DATE");
        }
        if (isNullOrEmpty(nidRequest.getPin())) {
            throw new ValidationException("INVALID PIN");
        }
        if (isNullOrEmpty(nidRequest.getConfirmMpin())) {
            throw new ValidationException("INVALID CONFIRM MPIN");
        }

        // Add further validation logic if needed
    }


    public static void validateLoginRequest(LoginRequest loginRequest, Request request) {
        if (isNullOrEmpty(loginRequest.getUsername())) {
            throw new ValidationException("INVALID USERNAME");
        }
        if (isNullOrEmpty(loginRequest.getPassword())) {
            throw new ValidationException("INVALID PASSWORD");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
    }

    /**
     * Bulk account upload: one account, carrying a mobile number and an identity number.
     *
     * <p>The segment is not checked here - it lives on the envelope and the column accepts null.</p>
     */
    public static void validateBulkAccountRequest(BulkAccountRequest bulkAccountRequest) {
        if (bulkAccountRequest == null) {
            throw new ValidationException("INVALID ACCOUNT");
        }
        if (isNullOrEmpty(bulkAccountRequest.getMobileNo()) || bulkAccountRequest.getMobileNo().trim().isEmpty()) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(bulkAccountRequest.getNidNo()) || bulkAccountRequest.getNidNo().trim().isEmpty()) {
            throw new ValidationException("INVALID NID NUMBER");
        }
    }

    public static void validateMpinVerificationRequest(MpinVerificationRequest mpinVerificationRequest, Request request) {
        if (isNullOrEmpty(mpinVerificationRequest.getMobileNumber())) {
            throw new ValidationException("INVALID USERNAME");
        }
        if (isNullOrEmpty(mpinVerificationRequest.getMpin())) {
            throw new ValidationException("INVALID MPIN");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
    }

    public static void validateGetBalanceRequest(GetBalanceRequest getBalanceRequest, Request request) {
        if (isNullOrEmpty(getBalanceRequest.getMobileNumber())) {
            throw new ValidationException("INVALID USERNAME");
        }
        if (isNullOrEmpty(getBalanceRequest.getAccountLevelCode())) {
            throw new ValidationException("INVALID ACCOUNT LEVEL CODE");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
    }

    public static void validateUploadDocumentRequest(UploadDocumentRequest uploadDocumentRequest, Request request) {
        if (isNullOrEmpty(uploadDocumentRequest.getMobileNumber())) {
            throw new ValidationException("INVALID Mobile Number");
        }
        if (isNullOrEmpty(uploadDocumentRequest.getAccountLevelCode())) {
            throw new ValidationException("INVALID Account Level Code");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("Invalid Imei No");
        }
        if (isNullOrEmpty(uploadDocumentRequest.getDocuments())) {
            throw new ValidationException("No File to Upload");
        }
    }

    public static void validateNidBvsRequest(NidBvsRequest nidBvsRequest, Request request) {
        if (isNullOrEmpty(nidBvsRequest.getMobileNumber())) {
            throw new ValidationException("INVALID Mobile Number");
        }
        if (isNullOrEmpty(nidBvsRequest.getNidNumber())) {
            throw new ValidationException("Invalid NidNo Number");
        }
        if (isNullOrEmpty(nidBvsRequest.getBvsRequest())) {
            throw new ValidationException("Invalid Bvs Request");
        }

        Set<Integer> fingerIndices = new HashSet<>();
        for (BvsRequest bvsRequest : nidBvsRequest.getBvsRequest()) {
            int fingerIndex = bvsRequest.getFingerIndex();

            // Validate finger index range
            if (fingerIndex < 1 || fingerIndex > 8) {
                throw new ValidationException("Invalid Finger Index: " + fingerIndex + ". Must be between 1 and 8.");
            }

            // Check for duplicate indices
            if (!fingerIndices.add(fingerIndex)) {
                throw new ValidationException("Duplicate Finger Index: " + fingerIndex);
            }

            // Validate template and template type
            if (isNullOrEmpty(bvsRequest.getIsoTemplate())) {
                throw new ValidationException("Invalid Iso Finger Template for index: " + fingerIndex);
            }
            if (isNullOrEmpty(bvsRequest.getBase64Template())) {
                throw new ValidationException("Invalid Base64 Finger Template for index: " + fingerIndex);
            }
            if (isNullOrEmpty(bvsRequest.getTemplateType())) {
                throw new ValidationException("Invalid Template Type for index: " + fingerIndex);
            }
        }

        // Check if all indices from 1 to 8 are present
        for (int i = 1; i <= 8; i++) {
            if (!fingerIndices.contains(i)) {
                throw new ValidationException("Missing Finger Index: " + i);
            }
        }
    }

    public static void validateChangeMpinRequestRequest(ChangeMpinRequest changeMpinRequest, Request request) {
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(changeMpinRequest.getMobileNumber())) {
            throw new ValidationException("INVALID USERNAME");
        }
        if (isNullOrEmpty(changeMpinRequest.getCurrentMpin())) {
            throw new ValidationException("INVALID Current Mpin");
        }
        if (isNullOrEmpty(changeMpinRequest.getNewMpin())) {
            throw new ValidationException("INVALID New Mpin");
        }
        if (isNullOrEmpty(changeMpinRequest.getConfirmMpin())) {
            throw new ValidationException("INVALID Confirm Mpin");
        }
        if (!changeMpinRequest.getConfirmMpin().equals(changeMpinRequest.getNewMpin())) {
            throw new ValidationException("New And Confirm Pin Not Matched");
        }

    }


    public static void validateMiniStatmentRequest(MiniStatementRequest MiniStatementRequest, Request request) {
        if (isNullOrEmpty(MiniStatementRequest.getMobileNumber())) {
            throw new ValidationException("Account No Required");
        }
        if (isNullOrEmpty(MiniStatementRequest.getAccountLevelCode())) {
            throw new ValidationException("Account Level Code Required");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }

    }

    public static void validateLogoutRequest(LogoutRequest logoutRequest, Request request) {
        if (isNullOrEmpty(logoutRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
    }

    public static void validateAddFavPayRequest(AddFavPayRequest favPayRequest, Request request) {
        if (isNullOrEmpty(favPayRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(favPayRequest.getBeneficaryType())) {
            throw new ValidationException("INVALID BENEFICARY  TYPE");
        }
        if (favPayRequest.getBeneficaryType().equals("PRT") || favPayRequest.getBeneficaryType().equals("PTU") ||
                favPayRequest.getBeneficaryType().equals("IBFT")) {
            if (isNullOrEmpty(favPayRequest.getBeneficaryAccountNo())) {
                throw new ValidationException("Invalid Account No");
            }
            if (isNullOrEmpty(favPayRequest.getBeneficaryBankImd())) {
                throw new ValidationException("Invalid Beneficary Bank Imd");
            }
        }
        if (isNullOrEmpty(favPayRequest.getBeneficaryAccountType())) {
            throw new ValidationException("Invalid Beneficary Account Type");
        }
        if (favPayRequest.getBeneficaryAccountType().equalsIgnoreCase("W")) {
            if (isNullOrEmpty(favPayRequest.getBeneficaryMobileNumber())) {
                throw new ValidationException("Invalid Beneficary Mobile Number");
            }
        }
        if (isNullOrEmpty(favPayRequest.getBeneficaryNickName())) {
            throw new ValidationException("Invalid Beneficary Nick Name");
        }
        if (isNullOrEmpty(favPayRequest.getBeneficaryAccountTitle())) {
            throw new ValidationException("Invalid Beneficary Account Title");
        }
    }

    public static void validateUpdateFavPayRequest(UpdateFavPayRequest updateFavPayRequest, Request request) {
        if (isNullOrEmpty(updateFavPayRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(updateFavPayRequest.getBeneficaryId())) {
            throw new ValidationException("INVALID Beneficary Id");
        }

        if (isNullOrEmpty(updateFavPayRequest.getBeneficaryType())) {
            throw new ValidationException("INVALID BENEFICARY  TYPE");
        }
        if (updateFavPayRequest.getBeneficaryType().equals("PRT") || updateFavPayRequest.getBeneficaryType().equals("PTU") ||
                updateFavPayRequest.getBeneficaryType().equals("IBFT")) {
            if (isNullOrEmpty(updateFavPayRequest.getBeneficaryAccountNo())) {
                throw new ValidationException("Invalid Account No");
            }
            if (isNullOrEmpty(updateFavPayRequest.getBeneficaryBankImd())) {
                throw new ValidationException("Invalid Beneficary Bank Imd");
            }
        }
        if (isNullOrEmpty(updateFavPayRequest.getBeneficaryAccountType())) {
            throw new ValidationException("Invalid Beneficary Account Type");
        }
        if (updateFavPayRequest.getBeneficaryAccountType().equalsIgnoreCase("W")) {
            if (isNullOrEmpty(updateFavPayRequest.getBeneficaryMobileNumber())) {
                throw new ValidationException("Invalid Beneficary Mobile Number");
            }
        }
        if (isNullOrEmpty(updateFavPayRequest.getBeneficaryNickName())) {
            throw new ValidationException("Invalid Beneficary Nick Name");
        }
        if (isNullOrEmpty(updateFavPayRequest.getBeneficaryAccountTitle())) {
            throw new ValidationException("Invalid Beneficary Account Title");
        }
    }

    public static void validateDecodeQrRequest(DecodeQrRequest decodeQrRequest, Request request) {
        if (isNullOrEmpty(decodeQrRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(decodeQrRequest.getQr())) {
            throw new ValidationException("Invalid Qr");
        }
    }

    public static void validateQrRequest(QrRequest qrRequest, Request request) {
        if (isNullOrEmpty(qrRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (!isNullOrEmpty(qrRequest.getAmount()) && !qrRequest.getAmount().matches("\\d+")) {
            throw new ValidationException("INVALID AMOUNT");
        }

        if (isNullOrEmpty(qrRequest.getAccountLevelCode())) {
            throw new ValidationException("Invalid Account Level Code");
        }
        if (isNullOrEmpty(qrRequest.getType()) || !qrRequest.getType().equalsIgnoreCase("P")) {
            throw new ValidationException("Invalid Qr Type");
        }
    }

    public static void validateGetAppScreenDataRequest(String name, String languageId) {
        if (isNullOrEmpty(name)) {
            throw new ValidationException("Invalid App Screen Name");
        }
        if (isNullOrEmpty(languageId) || !languageId.matches("\\d+")) {
            throw new ValidationException("Invalid Language Id");
        }
    }

    public static void validateUpdateAccountLevelRequest(UpdateAccountLevelRequest updateAccountLevelRequest, Request request) {
        if (isNullOrEmpty(updateAccountLevelRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(updateAccountLevelRequest.getStep())) {
            throw new ValidationException("Invalid Step No");
        }
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(updateAccountLevelRequest.getAccountLevelCode())) {
            throw new ValidationException("INVALID ACCOUNT LEVEL");
        }
        if (!isNullOrEmpty(updateAccountLevelRequest.getStep()) && updateAccountLevelRequest.getStep().equals("3")) {
            if (isNullOrEmpty(updateAccountLevelRequest.getVerifyOtpRequest())) {
                throw new ValidationException("INVALID OTP ID");
            }
            if (isNullOrEmpty(updateAccountLevelRequest.getVerifyOtpRequest().getOtpId())) {
                throw new ValidationException("INVALID OTP ID");
            }
            if (isNullOrEmpty(updateAccountLevelRequest.getOccupationCode())) {
                throw new ValidationException("INVALID OCCUPATION CODE");
            }
            if (isNullOrEmpty(updateAccountLevelRequest.getSourceOfIncomeCode())) {
                throw new ValidationException("INVALID SOURCE OF INCOME CODE");
            }
            if (isNullOrEmpty(updateAccountLevelRequest.getPurposeOfAccountCode())) {
                throw new ValidationException("INVALID PURPOSE OF ACCOUNT CODE");
            }
            if (!isNullOrEmpty(updateAccountLevelRequest.getEmail()) && !isValidEmail(updateAccountLevelRequest.getEmail())) {
                throw new ValidationException("INVALID EMAIL FORMAT");
            }
        } else if (!isNullOrEmpty(updateAccountLevelRequest.getStep()) && updateAccountLevelRequest.getStep().equals("2")) {
            validateVerifyOtp(updateAccountLevelRequest.getVerifyOtpRequest());
        }


    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    public static void validateUpdateDeviceRequest(UpdateDeviceRequest updateDeviceRequest) {
        if (isNullOrEmpty(updateDeviceRequest.getUsername())) {
            throw new ValidationException("INVALID USERNAME");
        }

        if (isNullOrEmpty(updateDeviceRequest.getAccountTypeCode())) {
            throw new ValidationException("INVALID ACC TYPE CODE");
        }
        if (isNullOrEmpty(updateDeviceRequest.getAccountRegTypeCode())) {
            throw new ValidationException("INVALID REG TYPE CODE");
        }
    }

    public static void validateVerifyDeviceRequest(VerifyDeviceRequest verifyDeviceRequest) {
        if (isNullOrEmpty(verifyDeviceRequest.getAppVersion())) {
            throw new ValidationException("INVALID APP VERSION");
        }
        if (isNullOrEmpty(verifyDeviceRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(verifyDeviceRequest.getDeviceModel())) {
            throw new ValidationException("INVALID DEVICE MODEL");
        }
        if (isNullOrEmpty(verifyDeviceRequest.getImeiNo())) {
            throw new ValidationException("INVALID IMEI NUMBER");
        }
        if (isNullOrEmpty(verifyDeviceRequest.getIpAddressP())) {
            throw new ValidationException("INVALID PRIMARY IP ADDRESS");
        }
        if (isNullOrEmpty(verifyDeviceRequest.getIpAddressA())) {
            throw new ValidationException("INVALID ALTERNATE IP ADDRESS");
        }
    }

    public static void validateResetPasswordRequestRequest(ResetPasswordRequest resetPasswordRequest) {


        if (isNullOrEmpty(resetPasswordRequest.getMobileNumber())) {
            throw new ValidationException("INVALID MOBILE NUMBER");
        }
        if (isNullOrEmpty(resetPasswordRequest.getStep())) {
            throw new ValidationException("INVALID STEP");
        }
        if (!isNullOrEmpty(resetPasswordRequest.getStep()) && resetPasswordRequest.getStep().equals("2")) {
            validateVerifyOtp(resetPasswordRequest.getVerifyOtpRequest());
        }
        if (!isNullOrEmpty(resetPasswordRequest.getStep()) && resetPasswordRequest.getStep().equals("3")) {
            validateVerifyOtpResponse(resetPasswordRequest.getVerifyOtpResponse());
        }

    }

    private static void validateVerifyOtpResponse(VerifyOtpResponse verifyOtpResponse) {
        if (isNullOrEmpty(verifyOtpResponse.getOtpId())) {
            throw new ValidationException("INVALID Otp ID");
        }
        if (isNullOrEmpty(verifyOtpResponse.getStatus())) {
            throw new ValidationException("INVALID STATUS");
        }
    }

    public static void validateSessionRequest(ValidateSessionRequest validateSessionRequest, Request request) {
        if (isNullOrEmpty(request.getImieNo())) {
            throw new ValidationException("INVALID IMEI NO");
        }
        if (isNullOrEmpty(validateSessionRequest.getAutoLoginToken())) {
            throw new ValidationException("INVALID SESSION TOKEN");
        }
        if (isNullOrEmpty(validateSessionRequest.getAccountNo())) {
            throw new ValidationException("INVALID ACCOUNT NO");
        }
    }

    public static void validateEmailAccountStatementRequest(AccountStatementRequest accountStatementRequest, Request request) {
        if (isNullOrEmpty(accountStatementRequest.getFromDate())) {
            throw new ValidationException("INVALID FROM DATE");
        }
        if (isNullOrEmpty(accountStatementRequest.getToDate())) {
            throw new ValidationException("INVALID TO DATE");
        }
        if (isNullOrEmpty(accountStatementRequest.getEmail())) {
            throw new ValidationException("INVALID EMAIL");
        }
    }

    public static void validateUpdateEmailRequest(UpdateEmailRequest updateEmailRequest, Request request) {
        if (isNullOrEmpty(updateEmailRequest.getEmail())) {
            throw new ValidationException("Invalid Email");
        }
        if (isNullOrEmpty(updateEmailRequest.getMobileNumber())) {
            throw new ValidationException("Invalid Mobile Number");
        }
    }

    public static void validateComplaintRequest(ComplaintRequest complaintRequest) {
        if (isNullOrEmpty(complaintRequest.getIssueTypeId())) {
            throw new ValidationException("Invalid Issue Type Id");
        }
        if (isNullOrEmpty(complaintRequest.getMobileNumber())) {
            throw new ValidationException("Invalid Mobile Number");
        }
        if (isNullOrEmpty(complaintRequest.getIssueDecr())) {
            throw new ValidationException("Invalid Issue Descr");
        }
    }

    public static void validategetContactListRequest(ContactlistDTO contactlistDTO) {
        if (isNullOrEmpty(contactlistDTO.getContactList())) {
            throw new ValidationException("Contact List Empty");
        }
    }
}
