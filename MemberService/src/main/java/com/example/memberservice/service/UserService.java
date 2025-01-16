package com.example.memberservice.service;

import com.example.memberservice.entity.UserEntity;
import com.example.memberservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserEntity registerUser(String email, String name) {
        UserEntity user = new UserEntity(email, name);
        return userRepository.save(user);
    }

    @Transactional
    public UserEntity modifyUser(Long id, String name) {
        UserEntity user = userRepository.findById(id).orElseThrow();
        user.modifyName(name);
        return user;
    }

    public UserEntity login(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }
}
