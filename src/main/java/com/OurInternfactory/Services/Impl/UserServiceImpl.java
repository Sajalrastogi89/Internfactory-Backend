package com.OurInternfactory.Services.Impl;

import com.OurInternfactory.Configs.AppConstants;
import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Role;
import com.OurInternfactory.Models.User;
import com.OurInternfactory.Payloads.CVGenerator;
import com.OurInternfactory.Payloads.ForgetPassword;
import com.OurInternfactory.Payloads.UserDto;
import com.OurInternfactory.Repositories.RoleRepo;
import com.OurInternfactory.Repositories.UserRepo;
import com.OurInternfactory.Services.UserService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final long OTP_VALID_DURATION = 10 * 60 * 1000;
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


    @Override
    public void export(HttpServletResponse response,CVGenerator cvData) throws IOException {
        System.out.println(cvData.getName()+cvData.getSkill1());
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle= FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph=new Paragraph(cvData.getName()+"\n",fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);


        Font fontParagraph=FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(10);
        Font fontParagraph1=FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph1.setSize(12);
        Font fontParagraph2=FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph2  .setSize(15);
//Skills
        Paragraph paragraph2=new Paragraph("SKILLS\n",fontParagraph);
        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);
        Paragraph paragraph3=new Paragraph(cvData.getSkill1()+"\n"+cvData.getSkill2()+"\n"+cvData.getSkill3()+"\n",fontParagraph1);
        paragraph3.setAlignment(Paragraph.ALIGN_LEFT);

     //Projects

        Paragraph paragraph4=new Paragraph("PROJECTS\n",fontParagraph);
        paragraph4.setAlignment(Paragraph.ALIGN_LEFT);
//Project1
        Paragraph paragraph5=new Paragraph(cvData.getProject1Heading(),fontParagraph2);
        paragraph5.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph paragraph6=new Paragraph(cvData.getProject1Description(),fontParagraph1);
        paragraph6.setAlignment(Paragraph.ALIGN_LEFT);
        //Project2


        Paragraph paragraph8=new Paragraph(cvData.getProject2Heading(),fontParagraph2);
        paragraph8.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph paragraph9=new Paragraph(cvData.getProject2Description(),fontParagraph1);
        paragraph9.setAlignment(Paragraph.ALIGN_LEFT);




        //Experiences
        Paragraph paragraph80=new Paragraph("EXPERIENCE\n",fontParagraph);
        paragraph80.setAlignment(Paragraph.ALIGN_LEFT);
        //Experience1
        Paragraph paragraph12=new Paragraph(cvData.getExperience1(),fontParagraph2);
        paragraph12.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph paragraph13=new Paragraph(cvData.getExperience1role(),fontParagraph1);
        paragraph13.setAlignment(Paragraph.ALIGN_LEFT);

        //Experience2
        Paragraph paragraph14=new Paragraph(cvData.getExperience2(),fontParagraph2);
        paragraph14.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph paragraph15=new Paragraph(cvData.getExperience2role(),fontParagraph1);
        paragraph15.setAlignment(Paragraph.ALIGN_LEFT);
        //Experience3



        //Awards
        Paragraph paragraph90=new Paragraph("AWARDS\n",fontParagraph);
        paragraph90.setAlignment(Paragraph.ALIGN_LEFT);
        //Award1
        Paragraph paragraph18=new Paragraph(cvData.getAward1(),fontParagraph1);
        paragraph18.setAlignment(Paragraph.ALIGN_LEFT);

        //Award2
        Paragraph paragraph19=new Paragraph(cvData.getAward2(),fontParagraph1);
        paragraph19.setAlignment(Paragraph.ALIGN_LEFT);





        //Marks
        Paragraph paragraph21=new Paragraph("PERCENTAGE\n",fontParagraph);
        paragraph21.setAlignment(Paragraph.ALIGN_LEFT);
//University
        Paragraph paragraph22=new Paragraph("University Percentage:"+cvData.getUniversityMarks(),fontParagraph);
        paragraph22.setAlignment(Paragraph.ALIGN_LEFT);
        //12th
        Paragraph paragraph23=new Paragraph("12th Percentage:"+cvData.getTwelth_marks(),fontParagraph);
        paragraph23.setAlignment(Paragraph.ALIGN_LEFT);
        //10th
        Paragraph paragraph24=new Paragraph("10th Percentage:"+cvData.getTenth_marks(),fontParagraph);
        paragraph24.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(paragraph);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph6);
        document.add(paragraph8);
        document.add(paragraph9);

        document.add(paragraph80);
        document.add(paragraph12);
        document.add(paragraph13);
        document.add(paragraph14);
        document.add(paragraph15);

        document.add(paragraph90);
        document.add(paragraph18);
        document.add(paragraph19);

        document.add(paragraph21);
        document.add(paragraph22);
        document.add(paragraph23);
        document.add(paragraph24);


        document.close();


    }



    public User DtoToUser(UserDto userdto) {
        return this.modelMapper.map(userdto, User.class);
    }
    public UserDto UserToDto(User user){
        return this.modelMapper.map(user, UserDto.class);
    }
}
