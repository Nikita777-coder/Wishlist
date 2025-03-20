package org.example.mapper;

import org.example.dto.auth.SignUpRequest;
import org.example.entity.UserEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity signUpDtoToUserEntity(SignUpRequest request);
}
