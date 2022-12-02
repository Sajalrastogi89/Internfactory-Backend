package com.OurInternfactory.Services.Impl;

import com.OurInternfactory.Exceptions.Apiexception;
import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Category;
import com.OurInternfactory.Models.Internships;
import com.OurInternfactory.Models.Submission;
import com.OurInternfactory.Models.User;
import com.OurInternfactory.Payloads.*;
import com.OurInternfactory.Repositories.CategoryRepo;
import com.OurInternfactory.Repositories.InternshipRepo;
import com.OurInternfactory.Repositories.SubmissionRepo;
import com.OurInternfactory.Repositories.UserRepo;
import com.OurInternfactory.Services.InternshipServices;
import com.OurInternfactory.Services.SubmissionSucessService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InternshipServiceImpl implements InternshipServices {
    private final InternshipRepo internshipRepo;
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final SubmissionSucessService submissionSucessService;
    private final SubmissionRepo submissionRepo;
    public String getFirstNStrings(String str, int n) {
        String[] sArr = str.split(" ");
        String firstStrs = "";
        for(int i = 0; i < n; i++)
            firstStrs += sArr[i] + " ";
        return firstStrs.trim();
    }
    public InternshipServiceImpl(InternshipRepo internshipRepo, ModelMapper modelMapper, UserRepo userRepo, CategoryRepo categoryRepo, SubmissionRepo submissionRepo, SubmissionSucessService submissionSucessService) {
        this.internshipRepo = internshipRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.submissionRepo = submissionRepo;
        this.submissionSucessService = submissionSucessService;
    }
    @Override
    public InternshipsDto createInternship(InternshipAssessment internshipAssessment, Integer categoryId, String Email) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryID", categoryId));
        User user = this.userRepo.findByEmail(Email).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+Email, 0));
        Internships internships = this.modelMapper.map(internshipAssessment.getInternshipsDto(), Internships.class);
        internships.setDisplayName(getFirstNStrings(internships.getTitle(), 3));
        internships.setProvider(user.getFirstname());
        internships.setActive(true);
        internships.setImageUrl(user.getProfilePhoto());
        internships.setIssuedDate(new Date());
        internships.setCategory(category);
        internships.setUserProvider(user);
        category.getInternshipsList().add(internships);
        Submission createSubmission = new Submission();
        createSubmission.setQuestions(internshipAssessment.getQuestions());
        internships.submissionModel = createSubmission;
        Internships newInternship = this.internshipRepo.save(internships);
        return this.modelMapper.map(newInternship, InternshipsDto.class);
    }
    @Override
    public SubmissionDto applyForInternship(String Email, Integer internshipId, SubmissionDto submissionDto){
            User user = this.userRepo.findByEmail(Email).orElseThrow(() -> new ResourceNotFoundException("User", "Email :"+Email, 0));
            Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(()->new ResourceNotFoundException("Internship", "InternshipId", internshipId));
        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis = internships.getLastDate().getTime();
        if(otpRequestedTimeInMillis > currentTimeInMillis) {
            Submission submission = this.modelMapper.map(submissionDto, Submission.class);
            Category category = internships.getCategory();
            category.setCount(category.getCount() + 1);
            user.getInterships().add(internships);
            user.getSubmission().add(submission);
            internships.getUser().add(user);
            internships.getSubmissions().add(submission);
            submission.setInternships(internships);
            submission.setUser(user);
            this.categoryRepo.save(category);
            this.userRepo.save(user);
            this.internshipRepo.save(internships);
            this.submissionRepo.save(submission);
            this.submissionSucessService.applicationMessage(Email, internships.getTitle());
            return this.modelMapper.map(submission, SubmissionDto.class);
        }
        else{
            throw new Apiexception("The last date to apply internships has been passed out!!!!\nPlease apply any other internship!!!");
        }
    }
    @Override
    public SubmissionDto getSubmissionForm(Integer submissionId) {
        Submission submission = this.submissionRepo.findById(submissionId).orElseThrow(() -> new ResourceNotFoundException("Submission", "submissionId", submissionId));
        return this.modelMapper.map(submission, SubmissionDto.class);
    }
    @Override
    public String deleteSubmissionForm(Integer submissionId) {
        Submission submission = this.submissionRepo.findById(submissionId).orElseThrow(() -> new ResourceNotFoundException("Submission", "submissionId", submissionId));
        Internships internships = this.internshipRepo.findBySubmissions(submission);
        User user = submission.getUser();
        user.getInterships().remove(internships);
        user.getSubmission().remove(submission);
        internships.getUser().remove(user);
        internships.getSubmissions().remove(submission);
        Category category = internships.getCategory();
        if(category.getCount() != 0) {
            category.setCount(category.getCount() - 1);
            this.categoryRepo.save(category);
        }
        this.userRepo.save(user);
        this.internshipRepo.save(internships);
        this.submissionRepo.delete(submission);
        return "Your submission successfully deleted";
    }
    @Override
    public InternshipsDto updateInternship(InternshipsDto internshipsDto, Integer internshipId) {
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(()-> new ResourceNotFoundException("Internship", "InternshipId", internshipId));
        internships.setTitle(internshipsDto.getTitle());
        internships.setAbout(internshipsDto.getAbout());
        internships.setTenure(internshipsDto.getTenure());
        internships.setLastDate(internshipsDto.getLastDate());
        internships.setStipend(internshipsDto.getStipend());
        internships.setIssuedDate(new Date());
        internships.setImageUrl(internshipsDto.getImageUrl());
        this.internshipRepo.save(internships);
        return this.modelMapper.map(internships, InternshipsDto.class);
    }
    @Override
    public void deleteInternship(Integer internshipId) {
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(()-> new ResourceNotFoundException("Internship", "InternshipId", internshipId));
        this.submissionRepo.deleteAllByInternships(internships);
        this.internshipRepo.delete(internships);
    }
    @Override
    public InternshipResponse getAllInternships(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc")){
            sort = Sort.by(sortBy).ascending();
        }
        else{
            sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Internships> pageInternships = this.internshipRepo.findAll(p);
        List<Internships> allInternships = pageInternships.getContent();
        List<InternshipDisplayOnly> allInternshipsDto = allInternships.stream().map((internships) -> this.modelMapper.map(internships, InternshipDisplayOnly.class)).collect(Collectors.toList());
        return new InternshipResponse(allInternshipsDto, pageInternships.getNumber(), pageInternships.getSize(), pageInternships.getTotalPages(), pageInternships.getTotalElements(), pageInternships.isLast());
    }
    @Override
    public InternshipResponse getAllInternshipsHostedByUser(User user, Integer pageNumber, Integer pageSize, String sortBy, String sortDir){
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc")){
            sort = Sort.by(sortBy).ascending();
        }
        else{
            sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Internships> pageInternships = this.internshipRepo.findByUserProvider(user, p);
        List<Internships> allInternships = pageInternships.getContent();
        List<InternshipDisplayOnly> allInternshipsDto = allInternships.stream().map((internships) -> this.modelMapper.map(internships, InternshipDisplayOnly.class)).collect(Collectors.toList());
        return new InternshipResponse(allInternshipsDto, pageInternships.getNumber(), pageInternships.getSize(), pageInternships.getTotalPages(), pageInternships.getTotalElements(), pageInternships.isLast());
    }
    @Override
    public InternshipResponse getAllTrendingInternship(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc")){
            sort = Sort.by(Sort.Direction.ASC, "submissions");
        }
        else{
            sort = Sort.by(Sort.Direction.DESC, "submissions");
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Internships> pageInternships = this.internshipRepo.findAll(p);
        List<Internships> allInternships = pageInternships.getContent();
        List<InternshipDisplayOnly> allInternshipsDto =  allInternships.stream().map(this::InternshipToDto).collect(Collectors.toList());
        return new InternshipResponse(allInternshipsDto, pageInternships.getNumber(), pageInternships.getSize(), pageInternships.getTotalPages(), pageInternships.getTotalElements(), pageInternships.isLast());
    }
    @Override
    public InternshipsDto getSingleInternship(Integer internshipId) {
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(()->new ResourceNotFoundException("Internship", "internshipId", internshipId));
        return this.modelMapper.map(internships, InternshipsDto.class);
    }
    @Override
    public InternshipResponse getInternshipsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "Category Id", categoryId));
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc")){
            sort = Sort.by(sortBy).ascending();
        }
        else{
            sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Internships> pageInternships = this.internshipRepo.findByCategory(category, p);//.findByCategory(category);
        List<Internships> allInternships = pageInternships.getContent();
        List<InternshipDisplayOnly> allInternshipsDto = allInternships.stream().map((internships) -> this.modelMapper.map(internships, InternshipDisplayOnly.class)).collect(Collectors.toList());
        return new InternshipResponse(allInternshipsDto, pageInternships.getNumber(), pageInternships.getSize(), pageInternships.getTotalPages(), pageInternships.getTotalElements(), pageInternships.isLast());
    }

    @Override
    public SubmissionResponse getInternshipsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "userID", userId));
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc")){
            sort = Sort.by(sortBy).ascending();
        }
        else{
            sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Submission> pageInternships = this.submissionRepo.findByUser(user, p);
        List<Submission> allInternships = pageInternships.getContent();
        List<SubmissionDto> allInternshipsDto = allInternships.stream().map((internships) -> this.modelMapper.map(internships, SubmissionDto.class)).collect(Collectors.toList());
        return new SubmissionResponse(allInternshipsDto, pageInternships.getNumber(), pageInternships.getSize(), pageInternships.getTotalPages(), pageInternships.getTotalElements(), pageInternships.isLast());
    }
    @Override
    public InternshipResponse searchInternships(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir){
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc")){
            sort = Sort.by(sortBy).ascending();
        }
        else{
            sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Internships> pageInternships = this.internshipRepo.findByTitleContainingIgnoreCase(keyword, p);
        List<Internships> allInternships = pageInternships.getContent();
        List<InternshipDisplayOnly> allInternshipsDto = allInternships.stream().map((internships) -> this.modelMapper.map(internships, InternshipDisplayOnly.class)).collect(Collectors.toList());
        return new InternshipResponse(allInternshipsDto, pageInternships.getNumber(), pageInternships.getSize(), pageInternships.getTotalPages(), pageInternships.getTotalElements(), pageInternships.isLast());
    }
    @Override
    public AppliedUserResponse searchUserByInternship(Integer internshipId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(()->new ResourceNotFoundException("Internship", "internshipId", internshipId));
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc")){
            sort = Sort.by(sortBy).ascending();
        }
        else{
            sort = Sort.by(sortBy).descending();
        }
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> pageUser = this.userRepo.findByInterships(internships, p);
        List<User> allUsers = pageUser.getContent();
        List<ApppliedUserDto> allUserDto = allUsers.stream().map((internshipe) -> this.modelMapper.map(internshipe, ApppliedUserDto.class)).collect(Collectors.toList());
        return new AppliedUserResponse(allUserDto, pageUser.getNumber(), pageUser.getSize(), pageUser.getTotalPages(), pageUser.getTotalElements(), pageUser.isLast());
    }
    public InternshipDisplayOnly InternshipToDto(Internships internship){
        return this.modelMapper.map(internship, InternshipDisplayOnly.class);
    }
}
