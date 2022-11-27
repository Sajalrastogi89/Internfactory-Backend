package com.OurInternfactory.Services;
import com.OurInternfactory.Payloads.*;

import java.util.List;

public interface UserService {

    void registerNewUser(UserDto user, int otp);
    void registerNewHost(RegisterHost user, int otp);
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

    List<InternshipsDto> getAllTrendingInternship();
//    void export(HttpServletResponse response, CVGenerator cvData) throws IOException;
}
