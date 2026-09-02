package com.dfs.app.service;

import com.dfs.app.dto.*;
import com.dfs.app.dto.common.Request;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;

public interface InviteFriendService {
    HashMap<String, Object> inviteFriendRequest(InviteFriendRequest inviteFriendRequest, Request apiRequest, String header);

    HashMap<String, Object> getInviteStats(long parseLong);
}
