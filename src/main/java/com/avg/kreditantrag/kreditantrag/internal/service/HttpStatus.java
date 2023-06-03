package com.avg.kreditantrag.kreditantrag.internal.service;

public enum HttpStatus {
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    CONFLICT(409),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int code;
    HttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
