package com.nearsoft.farandula;

public class FarandulaException extends Exception {

    private final ErrorType _errorType;

    public FarandulaException(ErrorType errorType, String message) {
        super(message);
        _errorType = errorType;
    }

    public FarandulaException(Throwable cause, ErrorType errorType) {
        super(cause);
        _errorType = errorType;
    }

    public FarandulaException(Throwable cause, ErrorType errorType, String message) {
        super(message, cause);
        _errorType = errorType;
    }

    @Override
    public String toString() {
        return String.format("ErrorType: [%s]\n%s", _errorType, super.toString());
    }
}
