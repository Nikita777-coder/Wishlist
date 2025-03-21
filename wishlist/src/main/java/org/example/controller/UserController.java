package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.user.UpdateUserData;
import org.example.dto.user.UserData;
import org.example.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    public UserData getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUser(userDetails);
    }
    @GetMapping("/all")
    public List<UserData> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getAllUsers(userDetails);
    }
    @PatchMapping
    public String updateUser(@AuthenticationPrincipal UserDetails userDetails,
                               @RequestBody UpdateUserData userData) {
        userService.update(userDetails, userData);
        return "Success";
    }
    @DeleteMapping
    public void delete(@AuthenticationPrincipal UserDetails userDetails) {
        userService.delete(userDetails);
    }

    @DeleteMapping("/{user-id}")
    public void deleteRandomUser(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable("user-id") UUID userId) {
        userService.delete(userDetails, userId);
    }
}
