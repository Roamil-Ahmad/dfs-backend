package com.dfs.cardapi.controller;

import com.dfs.cardapi.dto.LoginRequest;
import com.dfs.cardapi.dto.LoginResponse;
import com.dfs.cardapi.service.CardApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CardApiService cardApiService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(cardApiService.login(request));
    }
}
