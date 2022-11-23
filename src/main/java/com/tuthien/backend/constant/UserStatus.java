package com.tuthien.backend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    ACTIVE(1), INACTIVE(0);

    final Integer status;
}
