package com.springmasterclass.study.controller;

import com.springmasterclass.study.common.ApiResponse;
import com.springmasterclass.study.common.BaseController;
import com.springmasterclass.study.dto.record.CategoryRequest;
import com.springmasterclass.study.entity.product.Category;
import com.springmasterclass.study.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController extends BaseController {
    private final CategoryService categoryService;

    // 1. Tạo mới category kèm danh sách product
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody CategoryRequest rq) {
        Category created = categoryService.createCategory(rq);
        return new ResponseEntity<>(createSuccessResponse(created), HttpStatus.CREATED);
    }

    // 2. Cập nhật category (thêm/sửa/xóa product)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest dto) {
        Category updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(createSuccessResponse(updated));
    }
}
