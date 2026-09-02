package com.dfs.app.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotCardPinRequest {
    private String pan;
    private String accountNumber;
    private String oldPin;
    private String newPink;
}