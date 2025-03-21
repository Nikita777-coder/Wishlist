package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.wish.WishData;
import org.example.entity.UserEntity;
import org.example.entity.WishEntity;
import org.example.entity.userattributes.Role;
import org.example.mapper.WishMapper;
import org.example.repository.WishRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishService {
    private final WishMapper wishMapper;
    private final WishRepository wishRepository;
    private final UserService userService;
    public WishData addUserWish(UserDetails userDetails, WishData wishData) {
        var user = userService.getUser(UUID.fromString(userDetails.getUsername()));

        checkUserWish(user, wishData.getName());

        WishEntity newWish = wishMapper.wishDataToWishEntity(wishData);
        newWish.setUser(user);
        return wishMapper.wishEntityToWishData(wishRepository.save(newWish));
    }
    public List<WishData> getUserWishList(UserDetails userDetails) {
        return wishMapper.wishEntitiesToWishDatas(wishRepository.findAllByUser_Id(UUID.fromString(userDetails.getUsername())));
    }
    public WishData updateWish(UserDetails userDetails, WishData updateData) {
        var updatedWish = wishRepository.findById(updateData.getId());

        if (updatedWish.isEmpty()) {
            throw new IllegalArgumentException("wish not found");
        }

        var user = userService.getUser(UUID.fromString(userDetails.getUsername()));
        if (!updatedWish.get().getUser().getId().equals(UUID.fromString(user.getUsername())) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("access deny");
        }

        WishEntity updatedWishE = wishMapper.wishDataToWishEntity(updateData);
        updatedWishE.setId(updatedWish.get().getId());
        updatedWishE.setUser(user);

        return wishMapper.wishEntityToWishData(wishRepository.save(updatedWishE));
    }
    public void deleteWish(UserDetails userDetails, UUID wishId) {
        var user = userService.getUser(UUID.fromString(userDetails.getUsername()));
        var wish = wishRepository.findByIdAndUser(wishId, user);

        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + Role.ADMIN)) &&
                wish.isEmpty()) {
            throw new AccessDeniedException("access denied");
        }

        wishRepository.delete(wish.get());
    }
    private void checkUserWish(UserEntity user, String name) {
        if (wishRepository.findByUserAndName(user, name).isPresent()) {
            throw new IllegalArgumentException("user has already indicated wish");
        }
    }
}
