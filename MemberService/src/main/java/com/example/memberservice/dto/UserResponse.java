package com.example.memberservice.dto;

import com.example.memberservice.entity.UserEntity;

public record UserResponse(Long id, String email, String name) {
    public UserResponse(UserEntity user) {
        this(user.getId(), user.getEmail(), user.getName());
    }
}
