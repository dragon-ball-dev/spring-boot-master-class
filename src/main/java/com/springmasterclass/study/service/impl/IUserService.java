package com.springmasterclass.study.service.impl;

import com.springmasterclass.study.dto.request.UserRq;
import com.springmasterclass.study.dto.response.UserRp;
import com.springmasterclass.study.entity.User;
import com.springmasterclass.study.repository.UserRepository;
import com.springmasterclass.study.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public void create(UserRq rq) {
        userRepository.save(mapToEntity(rq));
    }

    @Override
    public void update(String id, UserRq rq) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not exist!");
        }

        User userUpdate = user.get();
        userUpdate.setName(rq.getName());
        userUpdate.setPhone(rq.getPhone());
        userUpdate.setEmail(rq.getEmail());
        userUpdate.setPassword(rq.getPassword());
        userUpdate.setUsername(rq.getUsername());
        userRepository.save(userUpdate);
    }

    @Override
    public List<UserRp> index() {
        List<User> list = userRepository.findAll();
        return list.stream().map(this::mapToResponse).toList();
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
        return UserRp.builder()
                .id(user.getId())
                .name(user.getName())
                .address(user.getAddress())
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .build();
    }
}
