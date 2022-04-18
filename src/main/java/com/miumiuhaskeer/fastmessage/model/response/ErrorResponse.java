package com.miumiuhaskeer.fastmessage.model.response;

import com.miumiuhaskeer.fastmessage.model.SimpleError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ErrorResponse {

    private final int status;
    private final String message;
    private final List<SimpleError> errors;

//    public static <T> ResponseEntity<T> fromException(Exception exception) {
//
//    }

    public static class Builder {

        private int status;
        private final List<SimpleError> errors = new ArrayList<>();

        public void addError(Exception exception) {
            errors.add(SimpleError.fromException(exception));
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public ErrorResponse build() {
            StringBuilder message = new StringBuilder();

            for (SimpleError error: errors) {
                // TODO change string to property constant
                message.append(error.getMessage()).append(", ");
            }

            return new ErrorResponse(
                    status,
                    message.toString(),
                    errors
            );
        }
    }
}
