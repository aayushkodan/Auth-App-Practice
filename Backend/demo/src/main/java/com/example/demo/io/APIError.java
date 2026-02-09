package com.example.demo.io;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record APIError(
        int status,
        String message,
        String error,
        String path,
        OffsetDateTime timeStamp
) {

    public static APIError of(int status, String message, String error, String path) {
        return new APIError(status, message, error, path, OffsetDateTime.now(ZoneOffset.UTC));
    }
}