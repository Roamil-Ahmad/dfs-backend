package com.dfs.agentapp.dto.common;

import com.dfs.agentapp.dto.Document;
import com.dfs.agentapp.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerKycRequest {
    private String fullName;
    private String firstName;
    private String LastName;
    private String fatherName;
    private String fatherNameDari;
    private String grandFatherName = Constants.EMPTY;
    private String grandFatherNameDari = Constants.EMPTY;
    private String mobileNumber;
    private String gender;
    private String nidNo;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "NidNo Date of Birth must be in the format yyyy-MM-dd")
    private String dob;
    private String placeOfBirth;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "NidNo Issuance Date must be in the format yyyy-MM-dd")
    private String nidIssuanceDate;
    private String permenantAddress;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "NidNo Issuance Date must be in the format yyyy-MM-dd")
    private String nidExpiryDate;
    private String dfsId;
    private List<Document> documents;
    private String pin;
    private String confirmMpin;
    private String livenessScore;
    private String matchScore;
    private String mrzData;
    private String signature;
    private String country;
    private String nationality;
    private String presentAddress;
    private String email;
    private String customerTypeId;
    private String cityId;
    private String fatherHusbandName;
    private String occupationId;
// new columns
    private String linkRaast;
    private String tandCAccepted;
    private String dualNationality;
    private String dualCountry;
    private String usBorn;
    private String referenceMobNumber;
    private String selfDecleration;
    private String latitude;
    private String longitude;
    private String dualCurrentAddress;
    private String dualCurrentAddressCountry;
    private String dualMailingAddress;
    private String dualMailingAddressCountry;
    private String dualPermanentAddress;
    private String dualPlaceOfBirth;
    private String dualPlaceOfBirthCountry;
    private String usCitizen;
    private String taxpayerIdentificationNumber;
    private String noTinDescription;
    private String noTinReason;
    private String expectedMonthlyVolumeId;
    private String fundProviderCnic;
    private String fundProviderFatherName;
    private String fundProviderName;
    private String fundProviderRelationId;
    private String recoveryQuestionId;
    private String recoveryAnswer;
    private String selectedBirthPlace;
    private String selectedMotherName;
    private String faceLivenessScore;
    private String pmd;
    private String businessName;
    private String businessTypeId;
    private String businessAddress;
    private String accountPurposeId;

}
