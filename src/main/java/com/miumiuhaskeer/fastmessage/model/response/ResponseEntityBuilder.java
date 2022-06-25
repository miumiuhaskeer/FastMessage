package com.miumiuhaskeer.fastmessage.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// TODO Change to correct builder with default object (ok, bad_request)
public final class ResponseEntityBuilder {

    private HttpStatus status = HttpStatus.OK;
    private String message = HttpStatus.OK.getReasonPhrase();

    /**
     * Set http status for response entity (default OK)
     *
     * @param httpStatus response status
     * @return object builder
     */
    public ResponseEntityBuilder status(HttpStatus httpStatus) {
        this.status = httpStatus;

        return this;
    }

    /**
     * Set message for response entity (default message for OK http status)
     *
     * @param message description for http status
     * @return object builder
     */
    public ResponseEntityBuilder message(String message) {
        this.message = message;

        return this;
    }

    /**
     * Finish creating of response object. Default status OK
     *
     * @return response entity object with description for http status
     */
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
