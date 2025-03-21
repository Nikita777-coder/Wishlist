package org.example.repository;

import org.example.entity.UserEntity;
import org.example.entity.userattributes.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findAllByRoleIn(List<Role> roles);
}
