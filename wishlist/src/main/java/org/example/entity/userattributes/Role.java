package org.example.entity.userattributes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static org.example.entity.userattributes.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_CREATE,
                    ADMIN_DELETE,
                    USER_READ_RECORDS,
                    USER_UPDATE,
                    USER_CREATE_RECORD,
                    USER_DELETE_RECORD
            )
    ),
    USER(
            Set.of(
                    USER_READ_RECORDS,
                    USER_UPDATE,
                    USER_CREATE_RECORD,
                    USER_DELETE_RECORD
            )
    );

    private final Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
