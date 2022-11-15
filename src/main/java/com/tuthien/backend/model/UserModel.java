package com.tuthien.backend.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserModel {

    private String id;

    @NotBlank(message = "Tên đăng nhập không được trống")
    private String username;

    @NotBlank(message = "Họ và tên không được trống")
    private String fullName;

    @NotBlank(message = "Mật khẩu không được tróng")
    private String password;

    private String role = "USER";

    private String phone;
    private String address;
    private String email;

    private Integer status;
}
