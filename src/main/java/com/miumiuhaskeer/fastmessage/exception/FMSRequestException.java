package com.miumiuhaskeer.fastmessage.exception;

import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;
import com.miumiuhaskeer.fastmessage.statistic.FMSRequest;

/**
 * Throws when any unexpected exception occurred
 * @see com.miumiuhaskeer.fastmessage.statistic.FMSRestTemplate#getForEntity(FMSRequest, Object)
 */
public class FMSRequestException extends RuntimeException {

    public FMSRequestException() {
        super(ErrorBundle.get("error.fmsRequestException.unexpected.message"));
    }
}
