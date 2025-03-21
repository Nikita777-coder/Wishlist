package org.example.mapper;

import org.example.dto.wish.WishData;
import org.example.entity.WishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WishMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    WishEntity wishDataToWishEntity(WishData wishData);
    WishData wishEntityToWishData(WishEntity wishData);
    List<WishData> wishEntitiesToWishDatas(List<WishEntity> wishEntities);
}
