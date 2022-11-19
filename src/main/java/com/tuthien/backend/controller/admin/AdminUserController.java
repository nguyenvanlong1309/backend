package com.tuthien.backend.controller.admin;

import com.tuthien.backend.constant.UserStatus;
import com.tuthien.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity findAll() {
        return ResponseEntity.ok(this.userService.findAll());
    }

    @GetMapping("/lock/{username}")
    public ResponseEntity lockUser(@PathVariable String username) {
        return ResponseEntity.ok(this.userService.changeStatusUser(username, UserStatus.INACTIVE));
    }

    @GetMapping("/unlock/{username}")
    public ResponseEntity unlockUser(@PathVariable String username) {
        return ResponseEntity.ok(this.userService.changeStatusUser(username, UserStatus.ACTIVE));
    }
}