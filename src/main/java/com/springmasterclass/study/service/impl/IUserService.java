package com.springmasterclass.study.service.impl;

import com.springmasterclass.study.dto.record.UserRequest;
import com.springmasterclass.study.dto.record.UserResponse;
import com.springmasterclass.study.dto.request.UserRq;
import com.springmasterclass.study.dto.response.UserRp;
import com.springmasterclass.study.entity.user.User;
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
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not exist!");
        }

        User userUpdate = user.get();
        userUpdate.setName(rq.name());
        userUpdate.setPhone(rq.phone());
        userUpdate.setEmail(rq.email());
        userUpdate.setPassword(rq.password());
        userUpdate.setUsername(rq.username());
        userRepository.save(userUpdate);
    }

    @Override
    public List<UserResponse> index() {
        List<User> list = userRepository.findAll();
        return list.stream().map(this::mapToResponseOfMapStruct).toList();
    }

    @Override
    public void delete(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not exist!");
        }
        userRepository.delete(user.get());
    }

    private User mapToEntity(UserRq rq) {
        return User.builder()
                .name(rq.getName())
                .address(rq.getAddress())
                .email(rq.getEmail())
                .password(rq.getPassword())
                .username(rq.getUsername())
                .phone(rq.getPhone())
                .build();
    }

    private UserRp mapToResponse(User user) {
//        UserRp userRp = modelMapper.map(user, UserRp.class);
        return UserRp.builder()
                .id(user.getId())
                .name(user.getName())
                .address(user.getAddress())
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .build();
    }

    private User mapToEntity(UserRequest userRequest) {
        return userMapper.toEntity(userRequest);
    }

    private UserResponse mapToResponseOfMapStruct(User user){
        return userMapper.toResponse(user);
    }
}
