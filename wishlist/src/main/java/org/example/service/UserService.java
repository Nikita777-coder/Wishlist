package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.dto.user.UpdatePasswordData;
import org.example.entity.UserEntity;
import org.example.entity.userattributes.Role;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public UserDetails createUser(UserEntity entity) {
        if (userRepository.findByEmail(entity.getEmail()).isPresent()) {
            throw new IllegalArgumentException("user with this email exists");
        }
        entity.setRole(Role.USER);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        return userRepository.save(entity);
    }
    public UserDetails getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updatePassword(UserDetails currentUserDetails,
                               UpdatePasswordData updatePasswordData) {
        UserEntity currentUser = getUserByEmail(currentUserDetails.getUsername());

        if (!passwordEncoder.matches(updatePasswordData.getOldPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("Old password is not correct!");
        }

        currentUser.setPassword(passwordEncoder.encode(updatePasswordData.getNewPassword()));
        userRepository.save(currentUser);
    }

    private UserEntity getUserByEmail(String email) {
        Optional<UserEntity> currentUser = userRepository.findByEmail(email);

        if (currentUser.isEmpty()) {
            throw new RuntimeException("get current user error!");
        }

        return currentUser.get();
    }
}
