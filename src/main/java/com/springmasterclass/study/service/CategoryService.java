package com.springmasterclass.study.service;

import com.springmasterclass.study.dto.record.CategoryRequest;
import com.springmasterclass.study.entity.Category;

public interface CategoryService {
    Category createCategory(CategoryRequest rq);
    Category updateCategory(Long catId,CategoryRequest rq);
}
