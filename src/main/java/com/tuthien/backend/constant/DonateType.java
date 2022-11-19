package com.tuthien.backend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DonateType {

    PERSONAL(0), BUSINESS(1);

    final int type;
}
