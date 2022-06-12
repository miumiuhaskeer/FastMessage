package com.miumiuhaskeer.fastmessage.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseEntityBuilder {

    private HttpStatus status = HttpStatus.OK;
    private String message = HttpStatus.OK.getReasonPhrase();

    public ResponseEntityBuilder status(HttpStatus httpStatus) {
        this.status = httpStatus;

        return this;
    }

    public ResponseEntityBuilder message(String message) {
        this.message = message;

        return this;
    }

    public ResponseEntity<SimpleResponse> create() {
        return ResponseEntity.status(status)
                .body(new SimpleResponse(status.value(), message));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static final class SimpleResponse {
        private int status;
        private String message;
    }
}
