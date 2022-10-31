package com.OurInternfactory.Services.Impl;

import com.OurInternfactory.Configs.AppConstants;
import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Role;
import com.OurInternfactory.Models.User;
import com.OurInternfactory.Payloads.ForgetPassword;
import com.OurInternfactory.Payloads.UserDto;
import com.OurInternfactory.Repositories.RoleRepo;
import com.OurInternfactory.Repositories.UserRepo;
import com.OurInternfactory.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;
    private final UserRepo userRepo;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepo roleRepo;

    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    @Override
    public void registerNewUser(UserDto userDto, int otp) {
        User user =this.modelMapper.map(userDto, User.class);
        //encoded the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setOtp(otp);
        user.setActive(false);
        user.setOtpRequestedTime(new Date(System.currentTimeMillis()+OTP_VALID_DURATION));
        //roles
        Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
        user.getRoles().add(role);
        this.userRepo.save(user);
    }
    @Override
    public UserDto createUser(UserDto userDto) {
        User user  = this.DtoToUser(userDto);
        User savedUser = this.userRepo.save(user);
        return this.UserToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        User updatedUser = this.userRepo.save(user);
        return this.UserToDto(updatedUser);
    }

    @Override
    public boolean isOTPValid(String email) {
        User userOTP = this.userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+email, 0));
        if (userOTP.getOtp() == null) {
            return false;
        }

        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis = userOTP.getOtpRequestedTime().getTime();

        if (otpRequestedTimeInMillis < currentTimeInMillis) {
            // OTP expires
            return false;
        }

        return true;
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));
        return this.UserToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();
        return users.stream().map(this::UserToDto).collect(Collectors.toList());
    }

    @Override
    public void DeleteUser(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "ID", userId));
        this.userRepo.delete(user);
    }
    @Override
    public void updateUserPass(ForgetPassword password) {
        User user = this.userRepo.findByEmail(password.getEmail()).orElseThrow(()-> new ResourceNotFoundException("User", "Email :"+password.getEmail(), 0));
        user.setPassword(this.passwordEncoder.encode(password.getPassword()));
        this.userRepo.save(user);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    public User DtoToUser(UserDto userdto) {
        return this.modelMapper.map(userdto, User.class);
    }
    public UserDto UserToDto(User user){
        return this.modelMapper.map(user, UserDto.class);
    }
}
