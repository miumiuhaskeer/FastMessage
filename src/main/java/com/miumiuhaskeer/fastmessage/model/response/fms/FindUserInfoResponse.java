package com.miumiuhaskeer.fastmessage.model.response.fms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FindUserInfoResponse {

    private final List<UserInfo> messages;

    // TODO change to public
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static final class UserInfo {
        private Long userId;
        private String email;
        private Integer messageCount;
        private Integer chatCount;
        private LocalDateTime creationDateTime;
    }
}
