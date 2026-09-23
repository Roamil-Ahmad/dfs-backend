package com.dfs.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * What the employee-onboard partner is told once a parked request has been acted on.
 *
 * <p>{@code parkRef} is how the partner recognises the request it parked, so it is built to their
 * shape: BA-{mobile number}.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeOnboardConfirmRequest {
    private String parkRef;
    private String status;
    private String dfsAccountNo;
    private String dfsCustomerId;
    private String message;
}
