package org.example.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.userattributes.Role;

import java.util.UUID;

@Getter
@Setter
public class UserData {
    private UUID id;
    private String name;
    private Role role;
}
