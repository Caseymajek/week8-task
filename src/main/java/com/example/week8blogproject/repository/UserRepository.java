package com.example.week8blogproject.repository;

import com.example.week8blogproject.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {

    List<Users> findByPublished (boolean published);

    List<Users> findByTitleIgnoreCaseStartingWith(String title);
}
