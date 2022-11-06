package com.OurInternfactory.Services;
import com.OurInternfactory.Payloads.EditUserDto;
import com.OurInternfactory.Payloads.ForgetPassword;
import com.OurInternfactory.Payloads.UserDto;
import com.OurInternfactory.Payloads.CategoryDTO;

import java.util.List;

public interface UserService {

    void registerNewUser(UserDto user, int otp);
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, Integer userId);
    String updateUserProfile(EditUserDto editUserDto);
    boolean isOTPValid(String email);

    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void DeleteUser(Integer userId);
    void updateUserPass(ForgetPassword password);
    boolean emailExists(String email);
    List<CategoryDTO> getAllCategory();
    List<CategoryDTO> getAllTrendingCategory();
    CategoryDTO AddData(CategoryDTO catDTO);
}
