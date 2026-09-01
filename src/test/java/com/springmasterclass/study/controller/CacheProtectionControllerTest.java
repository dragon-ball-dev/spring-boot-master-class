package com.springmasterclass.study.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CacheProtectionControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("1. Bloom Filter: Chặn ngay ID 9999 không tồn tại (Chống Cache Penetration)")
    void testBloomFilterBlocked() throws Exception {
        mockMvc.perform(get("/api/v1/cache-protection/bloom-filter/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.success").value(false))
                .andExpect(jsonPath("$.result.protectionMechanism").value("BLOOM_FILTER_BLOCKED"))
                .andExpect(jsonPath("$.result.data").doesNotExist());
    }

    @Test
    @DisplayName("2. Bloom Filter: ID 101 hợp lệ nạp DB và Cache thành công")
    void testBloomFilterAllowed() throws Exception {
        mockMvc.perform(get("/api/v1/cache-protection/bloom-filter/101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.success").value(true))
                .andExpect(jsonPath("$.result.protectionMechanism", anyOf(
                        is("DATABASE_QUERY_&_CACHE_POPULATED"),
                        is("REDIS_CACHE_HIT")
                )))
                .andExpect(jsonPath("$.result.data.id").value(101));
    }

    @Test
    @DisplayName("3. Random Jitter TTL: Lưu cache với thời gian ngẫu nhiên (Chống Cache Avalanche)")
    void testRandomJitterTTL() throws Exception {
        mockMvc.perform(post("/api/v1/cache-protection/jitter-ttl/101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.success").value(true))
                .andExpect(jsonPath("$.result.protectionMechanism", containsString("RANDOM_JITTER_TTL_APPLIED")))
                .andExpect(jsonPath("$.result.data.id").value(101));
    }

    @Test
    @DisplayName("4. Anti-Stampede Lock: Truy vấn Hot Key an toàn (Chống Cache Stampede)")
    void testAntiStampedeLock() throws Exception {
        mockMvc.perform(get("/api/v1/cache-protection/stampede-lock/101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.success").value(true))
                .andExpect(jsonPath("$.result.protectionMechanism", anyOf(
                        is("STAMPEDE_SINGLE_DB_QUERY_SUCCESS"),
                        is("HOT_KEY_CACHE_HIT"),
                        is("DOUBLE_CHECK_CACHE_HIT"),
                        is("STAMPEDE_RETRY_CACHE_HIT")
                )))
                .andExpect(jsonPath("$.result.data.id").value(101));
    }
}
