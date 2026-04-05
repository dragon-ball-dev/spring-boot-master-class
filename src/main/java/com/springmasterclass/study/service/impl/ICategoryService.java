package com.springmasterclass.study.service.impl;

import com.springmasterclass.study.dto.record.CategoryRequest;
import com.springmasterclass.study.dto.record.ProductRequest;
import com.springmasterclass.study.entity.Category;
import com.springmasterclass.study.entity.Product;
import com.springmasterclass.study.mapper.CategoryMapper;
import com.springmasterclass.study.repository.CategoryRepository;
import com.springmasterclass.study.repository.ProductRepository;
import com.springmasterclass.study.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ICategoryService implements CategoryService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category createCategory(CategoryRequest rq) {
        Category category = categoryMapper.toEntity(rq);

        // Xử lý logic 1 category kèm theo một danh sách sản phẩm
        if (Objects.nonNull(rq.products())) {
            for (ProductRequest product : rq.products()) {
                Product pro = categoryMapper.toProductEntity(product);
                category.addProduct(pro);
            }
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long catId, CategoryRequest rq) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new RuntimeException("Category not found!"));

        category.setName(rq.name());

        // Xử lý product cũ như nào
        List<Long> newProductIds = Objects.nonNull(rq.products()) ?
        rq.products().stream().map(ProductRequest::id).filter(Objects::nonNull).toList()
        : List.of();

        // Xóa các product mà không còn nằm trong thằng dto hay request mới nữa
        category.getProducts().removeIf(product -> !newProductIds.contains(product.getId()));

        if (Objects.nonNull(rq.products())) {
            for (ProductRequest pDto : rq.products()) {
                if (pDto.id() == null) {
                    Product newProduct = categoryMapper.toProductEntity(pDto);
                    category.addProduct(newProduct);
                } else {
                    Product existing = category.getProducts().stream()
                            .filter(product -> product.getId().equals(pDto.id()))
                            .findFirst()
                            .orElse(null);
                    if (existing != null) {
                        existing.setName(pDto.name());
                        existing.setPrice(pDto.price());
                    }
                }
            }
        }
        return categoryRepository.save(category);
    }
}
