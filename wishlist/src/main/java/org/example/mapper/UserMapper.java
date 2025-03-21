package org.example.mapper;

import org.example.dto.auth.SignUpRequest;
import org.example.dto.user.UserData;
import org.example.entity.UserEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity signUpDtoToUserEntity(SignUpRequest request);
    UserData userEntityToUserData(UserEntity userEntity);
    List<UserData> userEntiiesToUserDatas(List<UserEntity> userEntities);
}
