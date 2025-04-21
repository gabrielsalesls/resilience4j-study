package io.gabrielsalesls.example.exception;

public class ExternalServiceUnavailableException extends RuntimeException {

    private final String externalService;
    private final int statusCode;

    public ExternalServiceUnavailableException(String externalService, String message, int statusCode) {
        super(message);
        this.externalService = externalService;
        this.statusCode = statusCode;
    }

    public String getExternalService() {
        return externalService;
    }

    public int getStatusCode() {
        return statusCode;
    }
}