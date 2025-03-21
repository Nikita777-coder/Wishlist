package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.wish.WishData;
import org.example.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/wish")
@RequiredArgsConstructor
public class WishController {
    private final WishService wishService;
    @PostMapping
    public WishData addWish(@AuthenticationPrincipal UserDetails userDetails,
                              @Valid @RequestBody WishData wishData) {
        return wishService.addUserWish(userDetails, wishData);
    }
    @GetMapping("/all")
    public List<WishData> getAllWishes(@AuthenticationPrincipal UserDetails userDetails) {
        return wishService.getUserWishList(userDetails);
    }
    @PutMapping
    public WishData updateWish(@AuthenticationPrincipal UserDetails userDetails,
                                 @Valid @RequestBody WishData updateWishData) {
        return wishService.updateWish(userDetails, updateWishData);
    }
    @DeleteMapping
    public String deleteWish(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam("wish-id") UUID wishId) {
        wishService.deleteWish(userDetails, wishId);
        return "Success";
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
