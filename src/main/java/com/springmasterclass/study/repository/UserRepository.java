package com.springmasterclass.study.repository;

import com.springmasterclass.study.entity.user.UserTest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("userRepositoryTest")
public interface UserRepository extends JpaRepository<UserTest, String> {

    void deleteById(String id);
}
