package com.tuthien.backend.model;

import lombok.Data;

@Data
public class ChangePasswordModel {

    private String currentPassword;
    private String newPassword;
}
