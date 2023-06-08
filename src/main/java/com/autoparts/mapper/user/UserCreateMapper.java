package com.autoparts.mapper.user;

import com.autoparts.dto.user.UserCreateDto;
import com.autoparts.dto.user.UserUpdateDto;
import com.autoparts.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserCreateMapper {

    UserCreateDto toDto(User user);
    User toEntity(UserCreateDto userCreateDto);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "apiToken", ignore = true)
    void update(@MappingTarget User entity, UserUpdateDto updateEntity);
}
