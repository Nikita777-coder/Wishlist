package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.user.UpdateUserData;
import org.example.dto.user.UserData;
import org.example.entity.UserEntity;
import org.example.entity.userattributes.Role;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        return userRepository.save(entity);
    }
    public UserDetails getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public UserData getUser(UserDetails userDetails) {
        return userMapper.userEntityToUserData(getUserById(UUID.fromString(userDetails.getUsername())));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void update(UserDetails currentUserDetails,
                               UpdateUserData updateUserData) {
        UserEntity currentUser = getUserById(UUID.fromString(currentUserDetails.getUsername()));

        if (updateUserData.getNewPassword() != null && !updateUserData.getNewPassword().isBlank()) {
            checkUserOldPassword(updateUserData.getOldPassword(), currentUser.getPassword());
            currentUser.setPassword(passwordEncoder.encode(updateUserData.getNewPassword()));
        }

        if (updateUserData.getName() != null && !updateUserData.getName().isBlank()) {
            updateUserData.setName(updateUserData.getName());
            currentUser.setName(updateUserData.getName());
        }

        userRepository.save(currentUser);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void delete(UserDetails userDetails) {
        userRepository.delete(getUserById(UUID.fromString(userDetails.getUsername())));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void delete(UserDetails userDetails, UUID id) {
        checkUserRolesInList(userDetails, List.of(Role.ADMIN));
        userRepository.delete(getUserById(id));
    }
    public List<UserData> getAllUsers(UserDetails userDetails) {
        checkUserRolesInList(userDetails, List.of(Role.USER, Role.ADMIN));

        List<Role> userDataGetting = new ArrayList<>();
        userDataGetting.add(Role.USER);

        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + Role.ADMIN))) {
            userDataGetting.add(Role.ADMIN);
        }

        return userMapper.userEntiiesToUserDatas(userRepository.findAllByRoleIn(userDataGetting));
    }
    private UserEntity getUserById(UUID id) {
        Optional<UserEntity> currentUser = userRepository.findById(id);

        if (currentUser.isEmpty()) {
            throw new RuntimeException("get current user error!");
        }

        return currentUser.get();
    }
    private void checkUserOldPassword(String oldPassword, String currentUserPassword) {
        if (!passwordEncoder.matches(oldPassword, currentUserPassword)) {
            throw new IllegalArgumentException("Old password is not correct!");
        }
    }
    private void checkUserRolesInList(UserDetails userDetails, List<Role> checkedRoles) {
        for (Role role: checkedRoles) {
            if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role))) {
                return;
            }
        }

        throw new AccessDeniedException("access denied");
    }
}
