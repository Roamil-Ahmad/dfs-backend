package com.dfs.agentapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for the nadra service's POST /nadra/v1/cnicVerification.
 *
 * That endpoint consumes a plain body, not the standard {@code Request} envelope, so this DTO is
 * posted as-is. {@code cnicIssuanceDate} is the raw "yyyy-MM-dd" string taken from the caller's
 * payload; nadra parses it with the same pattern, so no timezone conversion happens in between.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NadraVerificationRequest {
    private String cnic;
    private String mobileNumber;
    private String cnicIssuanceDate;
}
