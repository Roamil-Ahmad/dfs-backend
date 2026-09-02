package com.dfs.agentapp.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileRegistrationRequest {
    private String appVersion;
    private String mobileNo;
    private String deviceModel;
    private String imeiNo;
    private String ipAddressP;
    private String ipAddressA;
    private String nidNo;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "NidNo Issuance Date must be in the format yyyy-MM-dd")
    private String nidIssuanceDate;

}
