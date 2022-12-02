package com.OurInternfactory.Services;

import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Resume;
import com.OurInternfactory.Models.User;
import com.OurInternfactory.Payloads.ResumeDTO;
import com.OurInternfactory.Repositories.ResumeRepo;
import com.OurInternfactory.Repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
@Service
public class ResumeService {
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final ResumeRepo resumeRepo;
    public ResumeService(ModelMapper modelMapper, ResumeRepo resumeRepo, UserRepo userRepo) {
        this.modelMapper = modelMapper;
        this.resumeRepo = resumeRepo;
        this.userRepo = userRepo;
    }
    public ResumeDTO getUserResume(String Email){
        User user = this.userRepo.findByEmail(Email).orElseThrow(() -> new ResourceNotFoundException("User", "Email: "+Email, 0));
        Resume resume = this.resumeRepo.findByUser(user);
        return this.modelMapper.map(resume, ResumeDTO.class);
    }
    public ResumeDTO setUserResume(String Email, ResumeDTO resumeDTO){
        User user = this.userRepo.findByEmail(Email).orElseThrow(() -> new ResourceNotFoundException("User", "Email: "+Email, 0));
        if(this.resumeRepo.existsResumeByUser(user)){
            Resume resume = this.resumeRepo.findByUser(user);
            resume.setFullName(resumeDTO.getFullName());
            resume.setEducation(resumeDTO.getEducation());
            resume.setInternships(resumeDTO.getInternships());
            resume.setTrainingCourses(resumeDTO.getTrainingCourses());
            resume.setProjects(resumeDTO.getProjects());
            resume.setSkills(resumeDTO.getSkills());
            resume.setPortfolio(resumeDTO.getPortfolio());
            resume.setUser(user);
            user.setResume(resume);
            this.resumeRepo.save(resume);
            this.userRepo.save(user);
            resumeDTO.setId(resume.getId());
        }
        else{
            Resume resume = this.modelMapper.map(resumeDTO, Resume.class);
            resume.setUser(user);
            user.setResume(resume);
            this.resumeRepo.save(resume);
            this.userRepo.save(user);
            resumeDTO.setId(resume.getId());
        }
        return resumeDTO;
    }
    public String deleteUserResume(String Email){
        User user = this.userRepo.findByEmail(Email).orElseThrow(() -> new ResourceNotFoundException("User", "Email: "+Email, 0));
        Resume resume = this.resumeRepo.findByUser(user);
        user.setResume(null);
        this.userRepo.save(user);
        this.resumeRepo.delete(resume);
        return "User Resume Deleted SucessFullly";
    }
}