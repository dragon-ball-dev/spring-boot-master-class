package com.springmasterclass.study.service;

import com.springmasterclass.study.dto.request.UserRq;
import com.springmasterclass.study.dto.response.UserRp;

import java.util.List;

public interface UserService {

    void create(UserRq rq);

    void update(String id, UserRq rq);

    List<UserRp> index();

    void delete(String id);
}
