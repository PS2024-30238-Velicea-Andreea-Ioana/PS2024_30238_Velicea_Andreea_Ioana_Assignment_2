package com.example.untoldpsproject.repositories;

import com.example.untoldpsproject.dtos.UserDto;
import com.example.untoldpsproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
