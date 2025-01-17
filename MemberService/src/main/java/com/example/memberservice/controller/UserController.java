package com.example.memberservice.controller;

import com.example.memberservice.dto.ModifyUserDTO;
import com.example.memberservice.dto.RegisterUserDTO;
import com.example.memberservice.dto.UserResponse;
import com.example.memberservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody RegisterUserDTO dto) {
        return new UserResponse(userService.registerUser(dto.email(), dto.name()));
    }

    @PutMapping("/{id}/modify")
    public UserResponse modifyUser(@PathVariable("id") Long id, @RequestBody ModifyUserDTO dto) {
        return new UserResponse(userService.modifyUser(id, dto.name()));
    }

    @GetMapping("/login")
    public UserResponse modifyUser(@RequestParam("email") String email) {
        return new UserResponse(userService.login(email));
    }
}
