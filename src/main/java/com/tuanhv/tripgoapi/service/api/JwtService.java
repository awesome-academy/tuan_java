package com.tuanhv.tripgoapi.service.api;

import com.tuanhv.tripgoapi.entity.User;

public interface JwtService {

    String generateToken(User user);
}
