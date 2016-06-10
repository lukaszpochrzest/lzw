package org.lzw.exception;

/**
 * Created by lukasz on 10.06.16.
 */
public class InvalidInputEncodedDataFileException extends Exception {

    public InvalidInputEncodedDataFileException() {
    }

    public InvalidInputEncodedDataFileException(String message) {
        super(message);
    }

    public InvalidInputEncodedDataFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputEncodedDataFileException(Throwable cause) {
        super(cause);
    }

    public InvalidInputEncodedDataFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
