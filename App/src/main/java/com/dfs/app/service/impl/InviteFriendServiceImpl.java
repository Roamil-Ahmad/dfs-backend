package com.dfs.app.service.impl;

import com.dfs.app.controller.HelperClass;
import com.dfs.app.dto.*;
import com.dfs.app.dto.common.*;
import com.dfs.app.model.*;
import com.dfs.app.repo.*;
import com.dfs.app.service.CommonService;
import com.dfs.app.service.InviteFriendService;
import com.dfs.app.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class InviteFriendServiceImpl extends HelperClass implements InviteFriendService {

    @Autowired
    private TblFriendInviteRepo friendInviteRepo;

    @Autowired
    private TblAccountRepo accountRepo;

    @Autowired
    private CommonService commonService;


    @Override
    public HashMap<String, Object> inviteFriendRequest(InviteFriendRequest inviteFriendRequest, Request apiRequest, String header) {
        for (String mobileNo : inviteFriendRequest.getInvitorMobileNo()) {
            int countPendingInvitesByMobile = friendInviteRepo.countPendingInvitesByMobile(mobileNo);
            if (countPendingInvitesByMobile > 0) {
                throw new CustomDataNotFoundException(GenericResponseCode.FRIEND_INVITE_ALREADY_SEND.getResponseCode());
            }
        }

        for (String mobileNo : inviteFriendRequest.getInvitorMobileNo()) {
            int countByAccountNo = friendInviteRepo.countByAccountNo(mobileNo);
            if (countByAccountNo > 0) {
                throw new CustomDataNotFoundException(GenericResponseCode.ACCOUNT_ALREADY_EXISTS_AGAINST_THIS_NUMBER.getResponseCode());
            }
        }

        List<TblFriendInvite> addInviteFriend = addInviteFriend(inviteFriendRequest);
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), addInviteFriend);
    }

    @Override
    public HashMap<String, Object> getInviteStats(long id) {
        Object row = friendInviteRepo.getInviteStatsByAppUserId((int) id);
        InviteStatsResponse response = new InviteStatsResponse();
        if (row != null && row instanceof Object[]) {
            Object[] cols = (Object[]) row;
            if (cols[1] != null) response.setTotalInvites(((Number) cols[1]).intValue());
            if (cols[2] != null) response.setPendingInvites(((Number) cols[2]).intValue());
            if (cols[3] != null) response.setCompleteInvites(((Number) cols[3]).intValue());
        }
        return commonService.getResponse(GenericResponseCode.SUCCESS.getResponseCode(), response);
    }

    private List<TblFriendInvite> addInviteFriend(InviteFriendRequest inviteFriendRequest) {
        List<TblFriendInvite> tblFriendInvites = new ArrayList<TblFriendInvite>();
        for (String mobileNo : inviteFriendRequest.getInvitorMobileNo()) {
            TblFriendInvite friendInvite = new TblFriendInvite();
            friendInvite.setMobileNo(mobileNo);
            friendInvite.setStatus("P");
            TblAccount tblAccount = accountRepo.findByAccountNo(inviteFriendRequest.getMobileNumber());
            friendInvite.setInvitorAccountId(new BigDecimal(tblAccount.getAccountId()));
            friendInvite.setCreatedate(new Date());
            friendInvite.setCreateuser(BigDecimal.ONE);
            TblFriendInvite tblFriendInvite = friendInviteRepo.save(friendInvite);
            tblFriendInvites.add(tblFriendInvite);
        }
        return tblFriendInvites;
    }
}
