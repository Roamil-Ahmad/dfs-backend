package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QrRequest {
    private String type;//P type means p2p
    private String amount;// static and dynamci depend on amount
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Due Date must be in the format yyyy-MM-dd")
    @FutureOrPresent(message = "Due Date must not be in the past")
    private String dueDate;// if i am not given then automatically expire after 7 days 2359ddMMyyyyHHmm
    private String amountAfterDueDate;
    private String mobileNumber;
    private String accountLevelCode;

}
