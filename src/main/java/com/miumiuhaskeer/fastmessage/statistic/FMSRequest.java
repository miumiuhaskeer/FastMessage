package com.miumiuhaskeer.fastmessage.statistic;

import com.miumiuhaskeer.fastmessage.model.response.fms.FindMessagesResponse;
import com.miumiuhaskeer.fastmessage.model.response.fms.FindUserInfoResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Class that contain constants for FastMessage Statistic service requests
 *
 * @param <R> return type
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class FMSRequest<R> {

    public static final FMSRequest<FindMessagesResponse> FIND_MESSAGES = new FMSRequest<>(
            "/find/messages", FindMessagesResponse.class
    );

    public static final FMSRequest<FindUserInfoResponse> FIND_USER_INFO = new FMSRequest<>(
            "/find/user/info", FindUserInfoResponse.class
    );

    public static final FMSRequest<String> HEALTH = new FMSRequest<>(
            "/health", String.class
    );

    /**
     * Path without base url
     */
    private final String requestPath;
    private final Class<R> returnType;
}
