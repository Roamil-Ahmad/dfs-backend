package com.mfs.pricingprofile.service.abstracts;

import com.mfs.pricingprofile.model.*;
import com.mfs.pricingprofile.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AbstractServiceImpl implements AbstractService {

    @Autowired
    private TblSmsMessageRepo tblSmsMessageRepo;

    @Autowired
    private TblSmsMessageTemplateRepo tblSmsMessageTemplateRepo;

    @Autowired
    private TblAccountRepo tblAccountRepo;

    @Autowired
    private TblAgentRepo tblAgentRepo;

    @Override
    public TblSmsMessage saveSmSMessage(TblSmsMessage tblSmsMessage) {
        return tblSmsMessageRepo.save(tblSmsMessage);
    }

    @Override
    public List<TblSmsMessageTemplate> getMessageTemplate(long transId) {
        return tblSmsMessageTemplateRepo.getMessageTemplate(transId);
    }

    @Override
    public List<TblSmsMessage> getSmsMessageList() {
        return tblSmsMessageRepo.getSmsMessageList();
    }

    @Override
    public TblAccount findAgentById(long userId) {
        return tblAccountRepo.searchAgentById(userId);
    }

    @Override
    public TblAgent getAgentById(long userId) {
        return tblAgentRepo.findById(userId).orElse(null);
    }

}
