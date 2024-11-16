package com.divideandsave.backend.service;

import com.divideandsave.backend.dto.request.UserRegisterRequest;
import com.divideandsave.backend.entity.User;

import java.util.List;

public interface UserService {

    User registerUser(UserRegisterRequest request);

    User getUserByEmail(String email);

    List<User> getAllUsers();
}
