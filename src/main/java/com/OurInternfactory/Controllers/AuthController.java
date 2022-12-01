package com.OurInternfactory.Controllers;
import com.OurInternfactory.Configs.AppConstants;
import com.OurInternfactory.Exceptions.Apiexception;
import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Role;
import com.OurInternfactory.Models.User;
import com.OurInternfactory.Payloads.*;
import com.OurInternfactory.Repositories.RoleRepo;
import com.OurInternfactory.Repositories.UserRepo;
import com.OurInternfactory.Security.JwtAuthRequest;
import com.OurInternfactory.Services.JWTTokenGenerator;
import com.OurInternfactory.Services.OTPService;
import com.OurInternfactory.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;
import java.util.Objects;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path ="/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private static final long OTP_VALID_DURATION = 10 * 60 * 1000;
    private final UserRepo userRepo;
    private final UserService userService;
    private final JWTTokenGenerator jwtTokenGenerator;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final RoleRepo roleRepo;
    public AuthController(UserRepo userRepo, UserService userService, JWTTokenGenerator jwtTokenGenerator, OTPService otpService, PasswordEncoder passwordEncoder, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }
// User as well as the host login API and          -------------------------/TOKEN GENERATOR/-----------------------
    @PostMapping("/login")
    public ResponseEntity<?> createToken(@Valid @RequestBody JwtAuthRequest request) {
        request.setEmail(request.getEmail().trim().toLowerCase());
        User user = this.userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email: "+request.getEmail(), 0));
        if(!user.getTwoStepVerification() || user.isActiveTwoStep()){
            user.setTwoStepVerification(false);
            user.setActiveTwoStep(false);
            this.userRepo.save(user);
            if (user.isActive()){
                JwtAuthResponse response = jwtTokenGenerator.getTokenGenerate(request.getEmail(), request.getPassword());
                response.setFirstname(user.getFirstname());
                response.setLastname(user.getLastname());
                return new ResponseEntity<>(response, OK);
            } else {
                return new ResponseEntity<>(new ApiResponse("Please verify your email first", false), HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else{
       /// code to send the OTP on mobile number;
            user.setOtp(otpService.OTPRequestMobile(user.getPhoneNumber()));
            user.setOtpRequestedTime(new Date(System.currentTimeMillis()+OTP_VALID_DURATION));
            this.userRepo.save(user);
            return new ResponseEntity<>(new ApiResponse("OTP has been successfully sent on the registered mobile number!!", true), HttpStatus.CONTINUE);
        }
    }
//SignUP API for user
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto){
        userDto.setFirstname(userDto.getFirstname().trim());
        userDto.setLastname(userDto.getLastname().trim());
        userDto.setEmail(userDto.getEmail().trim().toLowerCase());
        if(userService.emailExists(userDto.getEmail())){
            return getResponseEntity(userDto);
        }
        else{
            this.userService.registerNewUser(userDto, otpService.OTPRequest(userDto.getEmail()));
            return new ResponseEntity<>(new ApiResponse("OTP Sent Success on the entered Email", true), HttpStatus.CREATED);
        }
    }
//Signup API for Host
    @PostMapping("/signupHost")
    public ResponseEntity<?> registerHost(@Valid @RequestBody RegisterHost registerNewHost) {
        registerNewHost.setCompanyEmail(registerNewHost.getCompanyEmail().trim().toLowerCase());
        if (userService.emailExists(registerNewHost.getCompanyEmail())){
            return getResponseEntityHOST(registerNewHost);
        }
        else {
            this.userService.registerNewHost(registerNewHost, otpService.OTPRequest(registerNewHost.getCompanyEmail()));
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setSuccess(true);
            apiResponse.setMessage("OTP Sent Success on the entered Email");
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
    }
//Forget Password and otp generator API
    @PostMapping("/forget")
    public ResponseEntity<?> sendOTP(@Valid @RequestBody ForgetEmail forgetEmail) {
        forgetEmail.setEmail(forgetEmail.getEmail().trim().toLowerCase());
        if(userService.emailExists(forgetEmail.getEmail())){
            //write code for send otp to email....
            User user = this.userRepo.findByEmail(forgetEmail.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "email: "+forgetEmail.getEmail(), 0));
            user.setOtp(otpService.OTPRequest(forgetEmail.getEmail()));
            user.setOtpRequestedTime(new Date(System.currentTimeMillis()+OTP_VALID_DURATION));
            this.userRepo.save(user);
        }
        else{
            return new ResponseEntity<>(new ApiResponse("User does not exist with the entered email id", false), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse("OTP Sent Success", true), OK);
    }
//Verify OTP for activation of user/host account
    @PostMapping("/verifyotp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpDto otpDto) {
        otpDto.setEmail(otpDto.getEmail().trim().toLowerCase());
        User userOTP = this.userRepo.findByEmail(otpDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+otpDto.getEmail(), 0));
        if(this.userService.isOTPValid(otpDto.getEmail()) && userOTP.getOtp()!=null) {
            if (userOTP.getOtp() == otpDto.getOne_time_password()) {
                userOTP.setActive(true);
                userOTP.setActiveTwoStep(true);
                userOTP.setOtp(null);
                userOTP.setOtpRequestedTime(null);
                this.userRepo.save(userOTP);
                return new ResponseEntity<>(new ApiResponse("OTP Successfully Verified", true), OK);
            }
            else {
                return new ResponseEntity<>(new ApiResponse("Invalid OTP!!", false), HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else{
            throw new Apiexception("INVALID ACTION!!!");
        }
    }
//Verify OTP for Password Change
    @PostMapping("/verifyPassOtp")
    public ResponseEntity<?> verifyOtpPassChange(@Valid @RequestBody OtpDto otpDto) {
        otpDto.setEmail(otpDto.getEmail().trim().toLowerCase());
        User userOTP = this.userRepo.findByEmail(otpDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+otpDto.getEmail(), 0));
        if(this.userService.isOTPValid(otpDto.getEmail()) && userOTP.getOtp()!=null && userOTP.isActive()){
            if (userOTP.getOtp() == otpDto.getOne_time_password()){
                userOTP.setActive(true);
                userOTP.setActiveTwoStep(true);
                this.userRepo.save(userOTP);
                return new ResponseEntity<>(new ApiResponse("OTP Successfully Verified", true), OK);
            }
            else {
                return new ResponseEntity<>(new ApiResponse("Invalid OTP!!", false), HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else{
            throw new Apiexception("INVALID ACTION!!!");
        }
    }
//Reset Password OTP to change the password
    @PostMapping("/resetpass")
    public ResponseEntity<?> resetPass(@Valid @RequestBody ForgetPassword forgetPassword){
        forgetPassword.setEmail(forgetPassword.getEmail().trim().toLowerCase());
        User userRP = this.userRepo.findByEmail(forgetPassword.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+forgetPassword.getEmail(), 0));
        System.out.println("\n\n\n"+forgetPassword.getOtp()+"\n\n\n"+userRP.getOtp()+"\n\n\n");
        if (userRP.getOtp() != null) {
            if(Objects.equals(userRP.getOtp(), forgetPassword.getOtp())){
                if (this.userService.isOTPValid(forgetPassword.getEmail())) {
                    this.userService.updateUserPass(forgetPassword);
                    return new ResponseEntity<>(new ApiResponse("Password Reset SUCCESS", true), OK);
                } else {
                    return new ResponseEntity<>(new ApiResponse("Session Expired...........", false), HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return new ResponseEntity<>(new ApiResponse("Invalid OTP!!!", false), HttpStatus.FORBIDDEN);
            }
        }
        else{
                return new ResponseEntity<>(new ApiResponse("Invalid Action!!", false), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    private ResponseEntity<?> getResponseEntityHOST(@RequestBody @Valid RegisterHost userDto) {
        User user = this.userRepo.findByEmail(userDto.getCompanyEmail()).orElseThrow(()->new ResourceNotFoundException("User", "Email: "+userDto.getCompanyEmail(), 0));
        if(user.isActive()){
            return new ResponseEntity<>(new ApiResponse("User already exist with the entered email id", false), HttpStatus.CONFLICT);
        }
        else{
            user.setFirstname(userDto.getCompanyEmail().substring(0, userDto.getCompanyEmail().indexOf("@")));
            user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
            Role oldRole = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
            if(user.getRoles().contains(oldRole)){
                Role newRole = this.roleRepo.findById(AppConstants.ROLE_HOST).get();
                user.getRoles().add(newRole);
            }
            this.userRepo.save(user);
            return sendOTP(new ForgetEmail(userDto.getCompanyEmail()));
        }
    }
    private ResponseEntity<?> getResponseEntity(@RequestBody @Valid UserDto userDto) {
        User user = this.userRepo.findByEmail(userDto.getEmail()).orElseThrow(()->new ResourceNotFoundException("User", "Email: "+userDto.getEmail(), 0));
        if(user.isActive()){
            return new ResponseEntity<>(new ApiResponse("User already exist with the entered email id", false), HttpStatus.CONFLICT);
        }
        else{
            user.setFirstname(userDto.getFirstname());
            user.setLastname(userDto.getLastname());
            user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
            Role oldRole = this.roleRepo.findById(AppConstants.ROLE_HOST).get();
            if(user.getRoles().contains(oldRole)){
                Role newRole = this.roleRepo.findById(AppConstants.ROLE_NORMAL).get();
                user.getRoles().add(newRole);
            }
            this.userRepo.save(user);
            return sendOTP(new ForgetEmail(userDto.getEmail()));
        }
    }
//    @GetMapping("/pdf/generate")
//    public void generateCV(HttpServletResponse response, @RequestBody CVGenerator cvData) throws IOException {
//        response.setContentType("application/pdf");
//        String headerKey="Content-Disposition";
//        String headerValue="attachment; filename=GeneratedCV.pdf";
//        response.setHeader(headerKey,headerValue);
//        this.userService.export(response,cvData);
//    }
}
