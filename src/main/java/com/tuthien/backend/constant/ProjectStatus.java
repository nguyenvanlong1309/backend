package com.tuthien.backend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProjectStatus {

    PENDING(-1, "Chờ duyệt"),
    INDEFINITE(0, "Không thời hạn"),
    DOING(2, "Đang vận động"),
    DONE(1, "Đã hoàn thành");

    final int status;
    final String desc;

    public static ProjectStatus fromValue(int status) {
        return Arrays.stream(values())
                .filter(_e -> _e.status == status)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Trạng thái không hợp lệ"));
    }
}
