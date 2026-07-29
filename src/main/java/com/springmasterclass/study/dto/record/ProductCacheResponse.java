package com.springmasterclass.study.dto.record;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductCacheResponse(
        Long id,
        String name,
        BigDecimal price,
        String description,
        Integer stock,
        String cachedAt
) implements Serializable {
}
