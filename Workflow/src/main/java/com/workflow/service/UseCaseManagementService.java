package com.workflow.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.workflow.dto.Request;
import com.workflow.dto.Response;
import com.workflow.dto.UseCaseRequest;
import com.workflow.dto.UseCaseSearch;
import com.workflow.modal.LkpMcFormName;
import com.workflow.modal.TblMcConfig;
import com.workflow.modal.TblMcConfigDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface UseCaseManagementService {
    TblMcConfig getUseCaseById(long useCaseId);

    TblMcConfig saveusecase(UseCaseRequest useCaseRequest, BigDecimal userId);

    TblMcConfig updateusecase(UseCaseRequest useCaseRequest, BigDecimal userId);

    TblMcConfig inactiveusecase(UseCaseRequest useCaseRequest, BigDecimal valueOf);

    BigDecimal checkUseCaseExistance(String useCaseName, String formName, String requestType);

    ResponseEntity<Response> processSaveUseCase(HttpServletRequest request, UseCaseRequest jsonRequest) throws JsonProcessingException;

    ResponseEntity<Response> processUpdateUseCase(HttpServletRequest request, UseCaseRequest useCaseRequest) throws JsonProcessingException;

    ResponseEntity<Response> processInactiveUseCase(HttpServletRequest request, UseCaseRequest useCaseRequest, BigDecimal loggedUserDetail) throws JsonProcessingException;

    ResponseEntity<Response> getAllUseCasesByFilter(UseCaseSearch useCaseSearch);

    List<TblMcConfigDetail> getMcConfigDetailByMcConfigId(long mcConfigId);

    List<LkpMcFormName> getprocessname();
}
