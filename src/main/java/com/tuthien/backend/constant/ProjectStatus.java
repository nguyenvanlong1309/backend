package com.tuthien.backend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProjectStatus {

    PENDING(0, "Chờ duyệt"),
    DONE(1, "Đã hoàn thành"),
    DOING(2, "Đang vận động"),
    EXPIRED(3, "Sắp hết hạn"),
    CANCEL(4, "Hủy bỏ"),
    LOCKED(5, "Khóa");

    final int status;
    final String desc;

    public static ProjectStatus fromValue(int status) {
        return Arrays.stream(values())
                .filter(_e -> _e.status == status)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Trạng thái không hợp lệ"));
    }
}
