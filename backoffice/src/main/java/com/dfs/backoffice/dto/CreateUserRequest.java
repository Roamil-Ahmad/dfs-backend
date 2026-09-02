package com.dfs.backoffice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreateUserRequest {

    private long userId;
    private String userName;
    private BigDecimal nidNo;
    private String department;
    private String designation;
    private Date dob;
    private String email;
    private String employeeName;
    private String employeeNo;
    private String isActive;
    private String landline;
    private String mobileNo;
    private String grandFatherName;
    private String pwdUpdateFlag;
    private BigDecimal statusId;
    private BigDecimal roleId;

}
