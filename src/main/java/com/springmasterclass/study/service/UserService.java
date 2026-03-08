package com.springmasterclass.study.service;

import com.springmasterclass.study.dto.record.UserRequest;
import com.springmasterclass.study.dto.record.UserResponse;
import com.springmasterclass.study.dto.request.UserRq;
import com.springmasterclass.study.dto.response.UserRp;

import java.util.List;

public interface UserService {

    void create(UserRequest rq);

    void update(String id, UserRequest rq);

    List<UserResponse> index();

    void delete(String id);
}
