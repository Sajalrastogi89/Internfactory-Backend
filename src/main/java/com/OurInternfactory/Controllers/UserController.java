package com.OurInternfactory.Controllers;

import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.User;
import com.OurInternfactory.Payloads.*;
import com.OurInternfactory.Repositories.UserRepo;
import com.OurInternfactory.Services.FileServices;
import com.OurInternfactory.Services.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final FileServices fileServices;
    @Value("${project.image}")
    private String path;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    public UserController(UserService userService, FileServices fileServices, UserRepo userRepo, ModelMapper modelMapper) {
        this.userService = userService;
        this.fileServices = fileServices;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }
    //POST -create user
    @PostMapping( "/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto createdUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }
    //PUT -update user
    @PutMapping("/{userid}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userid") Integer userId){
        UserDto updatedUser= this.userService.updateUser(userDto, userId);
        return ResponseEntity.ok(updatedUser);
    }
    //Delete - delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer userid){
        this.userService.DeleteUser(userid);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
    }
    //GET - user get
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(this.userService.getAllUsers());
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable("userId") Integer userId){
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }
    @PutMapping("/editUserInfo")
    public ResponseEntity<ApiResponse> updateProfile(@Valid @RequestBody EditUserDto editUserDto){
        String message = this.userService.updateUserProfile(editUserDto);
        ApiResponse apiResponse = new ApiResponse(message, true);
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/getUserInfo")
    public ResponseEntity<GetProfileResponse> getUserInfo(@RequestBody ForgetEmail forgetEmail) {
        forgetEmail.setEmail(forgetEmail.getEmail().toLowerCase());
        String finalEmail = forgetEmail.getEmail();
        User user = this.userRepo.findByEmail(finalEmail).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+ finalEmail, 0));
        GetProfileResponse editUserDto = new GetProfileResponse(user.getId(), user.getProfilePhoto(), user.getFirstname(), user.getLastname(), user.getGender(), user.getEmail(), user.getPhoneNumber());
        return new ResponseEntity<GetProfileResponse>(editUserDto, HttpStatus.OK);
    }
    @PostMapping("/setprofilephoto/{userEmail}")
    public ResponseEntity<FileDto> settProfileImage(
            @RequestParam("image") MultipartFile image, @PathVariable String userEmail
    ){
        User user = this.userRepo.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+userEmail, 0));
        String filename = null;
        try {
            filename = this.fileServices.uploadImage(path, image);
            user.setProfilePhoto(filename);
            this.userRepo.save(user);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileDto(filename, "Image not uploaded, Server error !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new FileDto(filename, "Image is Successfully Uploaded !!!"), HttpStatus.OK);
    }
    @GetMapping(value = "/getprofilephoto", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getProfileImage(@RequestBody ForgetEmail forgetEmail, HttpServletResponse response) throws IOException {
        User user = this.userRepo.findByEmail(forgetEmail.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+forgetEmail.getEmail(), 0));
        InputStream resource = this.fileServices.getImage(path, user.getProfilePhoto());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
