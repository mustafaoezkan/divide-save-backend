package com.divideandsave.backend.service;

import com.divideandsave.backend.dto.request.UpdatePasswordRequest;
import com.divideandsave.backend.dto.request.UserRegisterRequest;
import com.divideandsave.backend.dto.response.UserResponse;
import com.divideandsave.backend.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    UserResponse registerUser(UserRegisterRequest request);

    void updatePassword(UpdatePasswordRequest request);

    void deleteAccount();
}
