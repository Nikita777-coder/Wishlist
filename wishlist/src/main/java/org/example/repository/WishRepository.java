package org.example.repository;

import org.example.entity.UserEntity;
import org.example.entity.WishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, UUID> {
    Optional<WishEntity> findByUserAndName(UserEntity user, String name);
    List<WishEntity> findAllByUser_Id(UUID id);
    Optional<WishEntity> findByIdAndUser(UUID id, UserEntity user);
}
