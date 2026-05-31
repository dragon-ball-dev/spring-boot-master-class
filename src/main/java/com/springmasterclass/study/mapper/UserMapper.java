package com.springmasterclass.study.mapper;

import com.springmasterclass.study.dto.record.UserRequest;
import com.springmasterclass.study.dto.record.UserResponse;
import com.springmasterclass.study.entity.user.UserTest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponse toResponse(UserTest userTest);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    UserTest toEntity(UserRequest request);
}
