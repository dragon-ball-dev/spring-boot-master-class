package com.springmasterclass.study.dto.record;

import java.io.Serializable;

public record CacheProtectionResponse(
        boolean success,
        String message,
        Object data,
        String protectionMechanism,
        long executionTimeMs
) implements Serializable {}
