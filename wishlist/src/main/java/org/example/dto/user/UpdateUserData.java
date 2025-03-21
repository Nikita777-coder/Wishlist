package org.example.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserData {
    private String name;
    private String oldPassword;
    private String newPassword;
}
