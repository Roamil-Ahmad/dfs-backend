package com.dfs.agentapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * One partner on a corporate agent account.
 *
 * <p>Each partner gets their own login, so each one becomes a TBL_APP_USER row of its own against
 * the same agent. The email is the login identifier and is stored in USERNAME - TBL_APP_USER has
 * no email column of its own.</p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Partner {
    private String email;
    private String password;

}
