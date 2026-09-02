package com.mfs.pricingprofile.service.abstracts;

import com.mfs.pricingprofile.model.*;

import java.util.List;

public interface AbstractService {
	TblSmsMessage saveSmSMessage(TblSmsMessage tblSmsMessage);

	List<TblSmsMessageTemplate> getMessageTemplate(long transId);

	List<TblSmsMessage> getSmsMessageList();

	TblAccount findAgentById(long userId);

	TblAgent getAgentById(long userId);

}
