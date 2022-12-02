package com.OurInternfactory.Controllers;
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
import com.OurInternfactory.Security.JwtTokenHelper;
import com.OurInternfactory.Services.FileServices;
import com.OurInternfactory.Services.InternshipServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class InternshipController {
    private final InternshipServices internshipServices;
    private final InternshipRepo internshipRepo;
    private final FileServices fileServices;
    private final ModelMapper modelMapper;
    private final JwtTokenHelper jwtTokenHelper;
    private final UserRepo userRepo;
    private final SubmissionRepo submissionRepo;
    private final CategoryRepo categoryRepo;
    @Value("${project.image}")
    private String path;
    public InternshipController(InternshipServices internshipServices, InternshipRepo internshipRepo, FileServices fileServices, ModelMapper modelMapper, JwtTokenHelper jwtTokenHelper, UserRepo userRepo, SubmissionRepo submissionRepo, CategoryRepo categoryRepo) {
        this.internshipServices = internshipServices;
        this.internshipRepo = internshipRepo;
        this.fileServices = fileServices;
        this.modelMapper = modelMapper;
        this.jwtTokenHelper = jwtTokenHelper;
        this.userRepo = userRepo;
        this.submissionRepo = submissionRepo;
        this.categoryRepo = categoryRepo;
    }
//Get assessment assigned to an internship
    @GetMapping("/getAssessment/internship/{internshipId}")
    public ResponseEntity<?> getAssessment(@PathVariable Integer internshipId) {
        Internships internship = this.internshipRepo.findById(internshipId).orElseThrow(() -> new ResourceNotFoundException("Internship", "InternshipId: ", internshipId));
        SubmissionDto submission = this.modelMapper.map(internship.submissionModel, SubmissionDto.class);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }
//Apply internship can only be done by normal user
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PostMapping(path = "/internships/{internshipId}/apply")
    public ResponseEntity<?> applyInternship(@PathVariable Integer internshipId, @RequestBody SubmissionDto submissionDto, @RequestHeader("Authorization") String bearerToken){
        String Email= this.jwtTokenHelper.getUsernameFromToken(bearerToken.substring(7));
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(()-> new ResourceNotFoundException("Internship", "internshipId", internshipId));
        if (internships.isActive()){
            SubmissionDto submissionDto1 = this.internshipServices.applyForInternship(Email, internshipId, submissionDto);
            return new ResponseEntity<>(submissionDto1, HttpStatus.OK);
        }
        else{
            return  new ResponseEntity<>(new ApiResponse("Internship is not active right now", false), HttpStatus.BAD_REQUEST);
        }
    }
//View Submission Api
    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<SubmissionDto> getSubmission(@PathVariable Integer submissionId){
        SubmissionDto submissionDto = this.internshipServices.getSubmissionForm(submissionId);
        return new ResponseEntity<>(submissionDto, HttpStatus.OK);
    }
//Delete Submission API
    @DeleteMapping("/submission/{submissionId}")
    public ResponseEntity<ApiResponse> deleteSubmission(@PathVariable Integer submissionId, @RequestHeader("Authorization") String bearerToken){
        bearerToken = bearerToken.substring(7);
        String Email= this.jwtTokenHelper.getUsernameFromToken(bearerToken);
        User TokenUser = this.userRepo.findByEmail(Email).orElseThrow(()->new ResourceNotFoundException("User", "Email: "+Email, 0));
        Submission submission = this.submissionRepo.getById(submissionId);
        if(TokenUser.getSubmission().contains(submission)){
            String message = this.internshipServices.deleteSubmissionForm(submissionId);
            return new ResponseEntity<>(new ApiResponse(message, true), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ApiResponse("User not Authorized to perform the action", false), HttpStatus.FORBIDDEN);
        }
    }
//Get Internships that are applied by the user
    @PostMapping("/user/{userid}/internships")
    public ResponseEntity<SubmissionResponse> getInternshipsByUser(@PathVariable Integer userid, @RequestBody PageParam pageParam){
        SubmissionResponse internshipResponse  = this.internshipServices.getInternshipsByUser(userid, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);
    }
//Get all the internships
    @PostMapping("/internships")
    public ResponseEntity<InternshipResponse> getAllInternships(@RequestBody PageParam pageParam){
        InternshipResponse internshipResponse  = this.internshipServices.getAllInternships(pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);
    }
//Get the trending API
    @PostMapping("/trendingInternships")
    public ResponseEntity<?> getTrendingInternship (@RequestBody PageParam pageParam){
        return new ResponseEntity<>(internshipServices.getAllTrendingInternship(pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir()), HttpStatus.OK);
    }
//Create internship can be added either by the host or the Admin
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PostMapping("/category/{categoryid}/internships")
    public ResponseEntity<?> createInternship(@Valid @RequestBody InternshipAssessment internshipAssessment, @PathVariable Integer categoryid, @RequestHeader("Authorization") String bearerToken) {
        if(internshipAssessment.getInternshipsDto().getLastDate().getTime() > System.currentTimeMillis()){
            InternshipsDto newInternship = this.internshipServices.createInternship(internshipAssessment, categoryid, this.jwtTokenHelper.getUsernameFromToken(bearerToken.substring(7)));
            return new ResponseEntity<>(newInternship, HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(new ApiResponse("Please select a valid date!!", true), HttpStatus.BAD_REQUEST);
        }
    }
//Pause or resume an internship
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PutMapping("/internships/{internshipid}/pause")
    public ResponseEntity<?> pauseInternship(@PathVariable Integer internshipid, @RequestHeader("Authorization") String bearerToken){
        String Email= this.jwtTokenHelper.getUsernameFromToken(bearerToken.substring(7));
        User TokenUser = this.userRepo.findByEmail(Email).orElseThrow(()->new ResourceNotFoundException("User", "Email: "+Email, 0));
        Internships internships = this.internshipRepo.findById(internshipid).orElseThrow(() -> new ResourceNotFoundException("Internship", "internshipid", internshipid));
        if(Objects.equals(internships.getUserProvider(), TokenUser)) {
            if (internships.isActive()) {
                internships.setActive(false);
                this.internshipRepo.save(internships);
                return new ResponseEntity<>(new ApiResponse("Internship has been paused", true), HttpStatus.OK);
            } else {
                internships.setActive(true);
                this.internshipRepo.save(internships);
                return new ResponseEntity<>(new ApiResponse("Internship has been resumed", true), HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>(new ApiResponse("User not Authorized to perform the action", false), HttpStatus.FORBIDDEN);
        }
    }
//Get an internship using internship id
    @GetMapping("/getinternships/{internshipid}")
    public ResponseEntity<InternshipsDto> getInternshipById(@PathVariable Integer internshipid){
        InternshipsDto internshipsDto  = this.internshipServices.getSingleInternship(internshipid);
        return new ResponseEntity<>(internshipsDto, HttpStatus.OK);
    }
//Delete a internship
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @DeleteMapping("/internships/{internshipId}")
    public ResponseEntity<?> deleteInternship(@PathVariable Integer internshipId, @RequestHeader("Authorization") String bearerToken){
        String Email= this.jwtTokenHelper.getUsernameFromToken(bearerToken.substring(7));
        User TokenUser = this.userRepo.findByEmail(Email).orElseThrow(()->new ResourceNotFoundException("User", "Email: "+Email, 0));
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(() -> new ResourceNotFoundException("Internship", "internshipid", internshipId));
        if(Objects.equals(internships.getUserProvider(), TokenUser)) {
            Category category = internships.getCategory();
            category.getInternshipsList().remove(internships);
            this.categoryRepo.save(category);
            this.internshipServices.deleteInternship(internshipId);
            return new ResponseEntity<>(new ApiResponse("Internship is Successfully Deleted!!", true), HttpStatus.FORBIDDEN);
        }
        else{
            return new ResponseEntity<>(new ApiResponse("User not Authorized to perform the action", false), HttpStatus.FORBIDDEN);
        }
    }
//Update an internship added by the host
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PutMapping("/internships/{internshipId}")
    public ResponseEntity<?> updateInternship(@PathVariable Integer internshipId, @RequestBody InternshipsDto internshipsDto, @RequestHeader("Authorization") String bearerToken){
        String Email= this.jwtTokenHelper.getUsernameFromToken(bearerToken.substring(7));
        User TokenUser = this.userRepo.findByEmail(Email).orElseThrow(()->new ResourceNotFoundException("User", "Email: "+Email, 0));
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(() -> new ResourceNotFoundException("Internship", "internshipid", internshipId));
        if(Objects.equals(internships.getUserProvider(), TokenUser)) {
            if (internshipsDto.getLastDate().getTime() > System.currentTimeMillis()) {
                InternshipsDto updatedInternshipDto = this.internshipServices.updateInternship(internshipsDto, internshipId);
                return new ResponseEntity<>(updatedInternshipDto, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(new ApiResponse("Please select a valid date!!", true), HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>(new ApiResponse("User not Authorized to perform the action", false), HttpStatus.FORBIDDEN);
        }
    }
//Added the photo to the internship
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PostMapping("/internships/{internshipId}/setimage")
    public ResponseEntity<?> setIntershipImage(@RequestParam("image") MultipartFile image, @PathVariable Integer internshipId, @RequestHeader("Authorization") String bearerToken){
        String Email= this.jwtTokenHelper.getUsernameFromToken(bearerToken.substring(7));
        User TokenUser = this.userRepo.findByEmail(Email).orElseThrow(()->new ResourceNotFoundException("User", "Email: "+Email, 0));
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(() -> new ResourceNotFoundException("Internship", "internshipid", internshipId));
        if(Objects.equals(internships.getUserProvider(), TokenUser)) {
            String filename = null;
            if (image.getContentType().equals("image/png")|| image.getContentType().equals("image/jpg")|| image.getContentType().equals("image/jpeg")) {
                try {
                    filename = this.fileServices.uploadImage(path, image);
                    internships.setImageUrl(filename);
                    this.internshipRepo.save(internships);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(new FileDto(filename, "Image not uploaded, Server error !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<>(new FileDto(filename, "Image is Successfully Uploaded !!!"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new FileDto(filename, "File is not of image type(JPEG/ JPG or PNG)!!!"), HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>(new ApiResponse("User not Authorized to perform the action", false), HttpStatus.FORBIDDEN);
        }
    }
//Get a list of all the internship  in a particular category
    @PostMapping("/category/{categoryid}/allinternships")
    public  ResponseEntity<InternshipResponse> getInternshipsByCategory(@PathVariable("categoryid") Integer categoryid, @RequestBody PageParam pageParam){
        InternshipResponse internshipResponse = this.internshipServices.getInternshipsByCategory(categoryid, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);
    }
//Get a list of all the internships Hosted By a User Host
    @PostMapping("internshipsHosted")
    public  ResponseEntity<InternshipResponse> getInternshipsByHostedByUser(@RequestBody PageParam pageParam, @RequestHeader("Authorization") String bearerToken){
        String Email= this.jwtTokenHelper.getUsernameFromToken(bearerToken.substring(7));
        User TokenUser = this.userRepo.findByEmail(Email).orElseThrow(()->new ResourceNotFoundException("User", "Email: "+Email, 0));
        InternshipResponse internshipResponse = this.internshipServices.getAllInternshipsHostedByUser(TokenUser, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);
    }
//Search internship using the title or the keyword contained by the title
    @PostMapping("/internships/search/{keywords}")
    public  ResponseEntity<InternshipResponse> searchInternshipByTitle(@PathVariable("keywords") String keywords, @RequestBody PageParam pageParam){
        InternshipResponse internshipResponse = this.internshipServices.searchInternships(keywords, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);

    }
//Get a list of all the user applicant of a particular internship
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PostMapping("/internships/appliedUser/{internnshipId}")
    public ResponseEntity<AppliedUserResponse> getAppliedUser(@PathVariable("internnshipId") Integer internnshipId, @RequestBody PageParam pageParam){
        AppliedUserResponse appliedUserResponse = this.internshipServices.searchUserByInternship(internnshipId, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(appliedUserResponse, HttpStatus.OK);
    }
}
