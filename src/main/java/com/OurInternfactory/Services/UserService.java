package com.OurInternfactory.Services;
import com.OurInternfactory.Payloads.ForgetPassword;
import com.OurInternfactory.Payloads.UserDto;

import java.util.List;

public interface UserService {

    void registerNewUser(UserDto user, int otp);
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, Integer userId);


    boolean isOTPValid(String email);

    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void DeleteUser(Integer userId);
    void updateUserPass(ForgetPassword password);
    boolean emailExists(String email);
}
