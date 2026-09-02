package com.dfs.thirdparties.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequestDto {


    private String mobileNumber;
    private String message;
    private String messageTemplateId;
}
