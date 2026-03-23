package com.springmasterclass.study.service;

import com.springmasterclass.study.dto.record.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponse> getAllProducts(String name, String category, Pageable pageable);
}
