package com.dfs.switchgateway.service;

import com.dfs.switchgateway.dto.EchoDto;
import com.dfs.switchgateway.dto.IbftAdviceRqst;
import com.dfs.switchgateway.dto.TSDtos.BillRqst;
import com.dfs.switchgateway.dto.TSDtos.OTIBFTTitleFetchRequest;
import com.dfs.switchgateway.dto.TSDtos.ResponseDto;
import com.dfs.switchgateway.dto.TSDtos.responseDto.BillResponse;
import com.dfs.switchgateway.dto.TSDtos.responseDto.EchoResponse;
import com.dfs.switchgateway.dto.TSDtos.responseDto.IBFTAdviceResponse;
import com.dfs.switchgateway.dto.TSDtos.responseDto.IBFTTitleFetchResponse;

/**
 * Outgoing routes towards the payment switch.
 *
 * Deliberately narrow: one method per 1LINK message pair. Anything that looks like a business
 * decision belongs in the transaction layer, not behind this interface.
 */
public interface SwitchRouteService {

    /** 0800 / 0810 - echo, sign-on and sign-off (1LINK spec 11.27). */
    ResponseDto<EchoResponse> networkManagement(EchoDto request);

    /** 0200 / 0210 with processing code 620000 - Account Title Inquiry (1LINK spec 11.13). */
    ResponseDto<IBFTTitleFetchResponse> titleFetch(OTIBFTTitleFetchRequest request) throws Exception;

    /** 0220 / 0230 with processing code 480000 - IBFT Advice (1LINK spec 11.10). */
    ResponseDto<IBFTAdviceResponse> advice(IbftAdviceRqst request) throws Exception;

    /** 0200 / 0210 Utility Bill Inquiry - asks the biller what is due, and moves no money. */
    ResponseDto<BillResponse> billInquiry(BillRqst request) throws Exception;

    /** 0200 / 0210 Utility Bill Payment - tells the biller the bill has been settled. */
    ResponseDto<BillResponse> billPayment(BillRqst request) throws Exception;
}
