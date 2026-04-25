package com.springmasterclass.study.mapper;

import com.springmasterclass.study.dto.record.CategoryRequest;
import com.springmasterclass.study.dto.record.ProductRequest;
import com.springmasterclass.study.entity.product.Category;
import com.springmasterclass.study.entity.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryRequest categoryRequest);

    ProductRequest toProductDto(Product product);
    Product toProductEntity(ProductRequest productRequest);
}
