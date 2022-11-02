package com.OurInternfactory.Controllers;
import com.OurInternfactory.Exceptions.Apiexception;
import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.User;
import com.OurInternfactory.Payloads.*;
import com.OurInternfactory.Repositories.UserRepo;
import com.OurInternfactory.Security.JwtAuthRequest;
import com.OurInternfactory.Services.JWTTokenGenerator;
import com.OurInternfactory.Services.OTPService;
import com.OurInternfactory.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    private static final long OTP_VALID_DURATION = 10 * 60 * 1000;
    private final UserRepo userRepo;
    private final UserService userService;
    private final JWTTokenGenerator jwtTokenGenerator;
    private final OTPService otpService;

    public AuthController(UserRepo userRepo, UserService userService, JWTTokenGenerator jwtTokenGenerator, OTPService otpService) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.otpService = otpService;
    }
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@Valid @RequestBody JwtAuthRequest request) {
        request.setUsername(request.getUsername().toLowerCase());
        User user = this.userRepo.findByEmail(request.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User", "Email: "+request.getUsername(), 0));
        if(user.isActive()) {
            System.out.println(request.getPassword() + "\n" +request.getUsername());
            JwtAuthResponse response = jwtTokenGenerator.getTokenGenerate(request.getUsername(), request.getPassword());
            return new ResponseEntity<>(response, OK);
        }
        else{
            throw new Apiexception("Please verify your email first");
        }
    }

    //SignUP
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto){
        userDto.setEmail(userDto.getEmail().toLowerCase());
        if(!userService.emailExists(userDto.getEmail())) {
            //write code for send otp to email....
            this.userService.registerNewUser(userDto, otpService.OTPRequest(userDto.getEmail()));
        }
        else{
            throw new Apiexception("User already exist with the entered email id");
        }
        return new ResponseEntity<>("OTP Sent Success on the entered Email", HttpStatus.CREATED);
    }
    @PostMapping("/forget")
    public ResponseEntity<String> sendOTP(@Valid @RequestBody ForgetEmail forgetEmail) {
        forgetEmail.setEmail(forgetEmail.getEmail().toLowerCase());
        if(userService.emailExists(forgetEmail.getEmail())){
            //write code for send otp to email....
            User user = this.userRepo.findByEmail(forgetEmail.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "email: "+forgetEmail.getEmail(), 0));
            user.setOtp(otpService.OTPRequest(forgetEmail.getEmail()));
            user.setOtpRequestedTime(new Date(System.currentTimeMillis()+OTP_VALID_DURATION));
            user.setActive(false);
            this.userRepo.save(user);
        }
        else{
            throw new Apiexception("User does not exist with the entered email id");
        }
        return new ResponseEntity<>("OTP Sent Success", OK);
    }
    @PostMapping("/verifyotp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody OtpDto otpDto) {
        otpDto.setEmail(otpDto.getEmail().toLowerCase());
        User userOTP = this.userRepo.findByEmail(otpDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+otpDto.getEmail(), 0));
        if(this.userService.isOTPValid(otpDto.getEmail()) && userOTP.getOtp()!=null) {
            if (userOTP.getOtp() == otpDto.getOne_time_password()) {
                userOTP.setActive(true);
                userOTP.setOtp(null);
                userOTP.setOtpRequestedTime(null);
                this.userRepo.save(userOTP);
                return new ResponseEntity<>("OTP Successfully Verified", OK);
            } else {
                throw new Apiexception("Invalid OTP!!");
            }
        }
        else{
            throw new Apiexception("INVALID ACTION!!!");
        }
    }
    @PostMapping("/resetpass")
    public ResponseEntity<String> resetPass(@Valid @RequestBody ForgetPassword forgetPassword){
        forgetPassword.setEmail(forgetPassword.getEmail().toLowerCase());
        User userRP = this.userRepo.findByEmail(forgetPassword.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+forgetPassword.getEmail(), 0));
        if(userRP.isActive()){
            if ((forgetPassword.getPassword()).equals(forgetPassword.getConformpassword())){
                this.userService.updateUserPass(forgetPassword);
            }
            else{
                throw new Apiexception("Please, check if the password and conform password fields matches");
            }
        }
        else{
            throw new Apiexception("Please Verify the OTP first");
        }
        return new ResponseEntity<>("Password Reset SUCCESS", OK);
    }

}
