package com.dfs.cardapi.controller;

import com.dfs.cardapi.dto.*;
import com.dfs.cardapi.service.CardApiService;
import com.dfs.cardapi.utils.AESencryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    private CardApiService cardApiService;

    @Autowired
    private AESencryption aesEncryption;

    @PostMapping("/new-request")
    public ResponseEntity<GenericResponse> newCardRequest(@RequestBody NewRequest request) {
        LoginResponse login = cardApiService.login(null);
        return ResponseEntity.ok(cardApiService.newCardRequest(request, login.getResponseBody().getToken()));
    }

    @PostMapping("/inquiry")
    public ResponseEntity<InquiryResponse> inquiry(@RequestBody InquiryRequest request) {
        LoginResponse login = cardApiService.login(null);
        return ResponseEntity.ok(cardApiService.inquiry(request, login.getResponseBody().getToken()));
    }

    @PostMapping("/change-pin")
    public ResponseEntity<GenericResponse> changePin(@RequestBody ChangePinRequest request) {
        request.setOldPin(aesEncryption.encryptwith256(request.getOldPin()));
        request.setNewPin(aesEncryption.encryptwith256(request.getNewPin()));
        request.setConfirmNewPin(aesEncryption.encryptwith256(request.getConfirmNewPin()));

        LoginResponse login = cardApiService.login(null);
        return ResponseEntity.ok(cardApiService.changePin(request, login.getResponseBody().getToken()));
    }

    @PostMapping("/generate-pin")
    public ResponseEntity<GenericResponse> generatePin(@RequestBody GeneratePinRequest request) {
        request.setPin(aesEncryption.encryptwith256(request.getPin()));
        request.setConfirmPin(aesEncryption.encryptwith256(request.getConfirmPin()));

        LoginResponse login = cardApiService.login(null);
        return ResponseEntity.ok(cardApiService.generatePin(request, login.getResponseBody().getToken()));
    }

    @PostMapping("/update-status")
    public ResponseEntity<GenericResponse> updateStatus(@RequestBody UpdateStatusRequest request) {
        LoginResponse login = cardApiService.login(null);
        return ResponseEntity.ok(cardApiService.updateStatus(request, login.getResponseBody().getToken()));
    }

    @PostMapping("/validate")
    public ResponseEntity<GenericResponse> validate(@RequestBody ValidateRequest request) {
        LoginResponse login = cardApiService.login(null);
        return ResponseEntity.ok(cardApiService.validate(request, login.getResponseBody().getToken()));
    }

    @PostMapping("/limit/validate")
    public ResponseEntity<LimitValidateResponse> validateLimit(@RequestBody LimitValidateRequest request) {
        LoginResponse login = cardApiService.login(null);
        return ResponseEntity.ok(cardApiService.validateLimit(request, login.getResponseBody().getToken()));
    }

    @PostMapping("/limit/fetch")
    public ResponseEntity<LimitFetchResponse> fetchLimit(@RequestBody LimitFetchRequest request) {
        LoginResponse login = cardApiService.login(null);
        return ResponseEntity.ok(cardApiService.fetchLimit(request, login.getResponseBody().getToken()));
    }

    @PostMapping("/spending-summary")
    public ResponseEntity<SpendingSummaryResponse> spendingSummary(@RequestBody SpendingSummaryRequest request) {
        LoginResponse login = cardApiService.login(null);
        return ResponseEntity.ok(cardApiService.spendingSummary(request, login.getResponseBody().getToken()));
    }

    @GetMapping("/lov/all")
    public ResponseEntity<LovResponse> getLov() {
        LoginResponse login = cardApiService.login(null);
        return ResponseEntity.ok(cardApiService.getLov(login.getResponseBody().getToken()));
    }
}
