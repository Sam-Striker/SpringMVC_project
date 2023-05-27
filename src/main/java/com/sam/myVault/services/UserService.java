package com.sam.myVault.services;

import com.sam.myVault.dto.UserDto;
import com.sam.myVault.entities.User;
public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);
}
