package com.springmasterclass.study.service.impl;

import com.springmasterclass.study.dto.record.UserRequest;
import com.springmasterclass.study.dto.record.UserResponse;
import com.springmasterclass.study.dto.request.UserRq;
import com.springmasterclass.study.dto.response.UserRp;
import com.springmasterclass.study.entity.user.UserTest;
import com.springmasterclass.study.mapper.UserMapper;
import com.springmasterclass.study.repository.UserRepository;
import com.springmasterclass.study.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IUserService implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;

    @Override
    public void create(UserRequest rq) {
        userRepository.save(mapToEntity(rq));
    }

    @Override
    public void update(String id, UserRequest rq) {
        Optional<UserTest> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not exist!");
        }

        UserTest userTestUpdate = user.get();
        userTestUpdate.setName(rq.name());
        userTestUpdate.setPhone(rq.phone());
        userTestUpdate.setEmail(rq.email());
        userTestUpdate.setPassword(rq.password());
        userTestUpdate.setUsername(rq.username());
        userRepository.save(userTestUpdate);
    }

    @Override
    public List<UserResponse> index() {
        List<UserTest> list = userRepository.findAll();
        return list.stream().map(this::mapToResponseOfMapStruct).toList();
    }

    @Override
    public void delete(String id) {
        Optional<UserTest> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not exist!");
        }
        userRepository.delete(user.get());
    }

    private UserTest mapToEntity(UserRq rq) {
        return UserTest.builder()
                .name(rq.getName())
                .address(rq.getAddress())
                .email(rq.getEmail())
                .password(rq.getPassword())
                .username(rq.getUsername())
                .phone(rq.getPhone())
                .build();
    }

    private UserRp mapToResponse(UserTest userTest) {
//        UserRp userRp = modelMapper.map(user, UserRp.class);
        return UserRp.builder()
                .id(userTest.getId())
                .name(userTest.getName())
                .address(userTest.getAddress())
                .email(userTest.getEmail())
                .username(userTest.getUsername())
                .phone(userTest.getPhone())
                .build();
    }

    private UserTest mapToEntity(UserRequest userRequest) {
        return userMapper.toEntity(userRequest);
    }

    private UserResponse mapToResponseOfMapStruct(UserTest userTest){
        return userMapper.toResponse(userTest);
    }
}
