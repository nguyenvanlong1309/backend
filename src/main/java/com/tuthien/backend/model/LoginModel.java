package com.tuthien.backend.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginModel {

    @NotBlank(message = "Tên đăng nhập không được trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được trống")
    private String password;
}
