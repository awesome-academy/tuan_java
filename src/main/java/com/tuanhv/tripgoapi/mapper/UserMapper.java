package com.tuanhv.tripgoapi.mapper;

import com.tuanhv.tripgoapi.dto.response.UserResponse;
import com.tuanhv.tripgoapi.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}
