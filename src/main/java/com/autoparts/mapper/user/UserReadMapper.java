package com.autoparts.mapper.user;

import com.autoparts.dto.user.UserReadDto;
import com.autoparts.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserReadMapper {

    UserReadDto toDto(User user);
    User toEntity(UserReadDto userReadDto);
}
