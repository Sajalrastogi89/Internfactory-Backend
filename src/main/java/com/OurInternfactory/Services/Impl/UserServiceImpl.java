package com.OurInternfactory.Services.Impl;

import com.OurInternfactory.Configs.AppConstants;
import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Category;
import com.OurInternfactory.Models.Resume;
import com.OurInternfactory.Models.Role;
import com.OurInternfactory.Models.User;
import com.OurInternfactory.Payloads.*;
import com.OurInternfactory.Repositories.CategoryRepo;
import com.OurInternfactory.Repositories.ResumeRepo;
import com.OurInternfactory.Repositories.RoleRepo;
import com.OurInternfactory.Repositories.UserRepo;
import com.OurInternfactory.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final long OTP_VALID_DURATION = 10 * 60 * 1000;
    private final CategoryRepo catRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categRepo;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final ResumeRepo resumeRepo;
    private final RoleRepo roleRepo;

    public UserServiceImpl(CategoryRepo catRepo, UserRepo userRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepo roleRepo, CategoryRepo categRepo, ResumeRepo resumeRepo) {
        this.catRepo = catRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.categRepo = categRepo;
        this.resumeRepo = resumeRepo;
    }

    @Override
    public void registerNewUser(UserDto userDto, int otp) {
        User user =this.modelMapper.map(userDto, User.class);
        //encoded the password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setOtp(otp);
        user.setActive(false);
        user.setProfilePhoto("default.png");
        user.setOtpRequestedTime(new Date(System.currentTimeMillis()+OTP_VALID_DURATION));
        Role role = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
        Resume resume = new Resume();
        resume.setUser(user);
        resumeRepo.save(resume);
        user.setResume(resume);
        user.getRoles().add(role);
        this.userRepo.save(user);
    }
    @Override
    public void registerNewHost(RegisterHost userDto, int otp){
        User user = new User();
        user.setFirstname(userDto.getCompanyEmail().substring(0, userDto.getCompanyEmail().indexOf("@")));
        user.setEmail(userDto.getCompanyEmail());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        //encoded the password
        user.setOtp(otp);
        user.setActive(false);
        user.setProfilePhoto("default.png");
        user.setOtpRequestedTime(new Date(System.currentTimeMillis()+OTP_VALID_DURATION));
        //roles
        Role role = this.roleRepo.findById(AppConstants.ROLE_HOST).get();
//        Resume resume = new Resume();
//        resume.setUser(user);
//        resumeRepo.save(resume);
//        user.setResume(resume);
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
    public String updateUserProfile(EditUserDto editUserDto){
        User userUpdate = this.userRepo.findByEmail(editUserDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+editUserDto.getEmail(), 0));
        userUpdate.setFirstname(editUserDto.getFirstname());
        userUpdate.setLastname(editUserDto.getLastname());
        editUserDto.setNewemail(editUserDto.getNewemail().toLowerCase());
        userUpdate.setEmail(editUserDto.getNewemail());
        userUpdate.setGender(editUserDto.getGender());
        userUpdate.setPhoneNumber(editUserDto.getPhoneNumber());
        this.userRepo.save(userUpdate);
        return "User Updated Successfully!!!";
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
        user.getRoles().clear();
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

    @Override
    public List<CategoryDTO> getAllCategory() {
        List<Category> cat = this.categRepo.findAll();
        return cat.stream().map(this::CategoryToDto).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> getAllTrendingCategory() {
        List<Category> cat = this.categRepo.findAll(Sort.by(Sort.Direction.DESC,"count"));
        return cat.stream().map(this::CategoryToDto).collect(Collectors.toList());

    }

    public User DtoToUser(UserDto userdto) {
        return this.modelMapper.map(userdto, User.class);
    }
    public UserDto UserToDto(User user){return this.modelMapper.map(user, UserDto.class);}
    public CategoryDTO CategoryToDto(Category category){
        return this.modelMapper.map(category, CategoryDTO.class);
    }

}
