package com.example.shopease.mapper;

import com.example.shopease.dto.UserDto;
import com.example.shopease.dto.UserSaveRequestDto;
import com.example.shopease.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity (UserDto dto);

    UserDto toDto (User entity);

    User toEntityFromSaveRequestDto (UserSaveRequestDto dto);
}
