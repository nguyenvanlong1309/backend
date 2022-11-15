package com.tuthien.backend.controller;

import com.tuthien.backend.model.UserModel;
import com.tuthien.backend.model.UserResponse;
import com.tuthien.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity updateInfo(@RequestBody UserModel userModel) {
        UserResponse userResponse = this.userService.updateInfo(userModel);
        return ResponseEntity.ok(userResponse);
    }
}
