package com.OurInternfactory.Controllers;

import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Internships;
import com.OurInternfactory.Payloads.*;
import com.OurInternfactory.Repositories.InternshipRepo;
import com.OurInternfactory.Services.FileServices;
import com.OurInternfactory.Services.InternshipServices;
import com.OurInternfactory.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InternshipController {
    private final InternshipServices internshipServices;
    private final InternshipRepo internshipRepo;
    private final FileServices fileServices;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @Value("${project.image}")
    private String path;
    public InternshipController(InternshipServices internshipServices, InternshipRepo internshipRepo, FileServices fileServices, ModelMapper modelMapper, UserService userService) {
        this.internshipServices = internshipServices;
        this.internshipRepo = internshipRepo;
        this.fileServices = fileServices;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }



    //Create internship can be added either by the host or the Admin
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PostMapping("/category/{categoryid}/host/{hostEmail}/internships")
    public ResponseEntity<InternshipsDto> createInternship(@RequestBody InternshipAssessment internshipAssessment, @PathVariable Integer categoryid, @PathVariable String hostEmail){
        InternshipsDto newInternship = this.internshipServices.createInternship(internshipAssessment, categoryid, hostEmail);
        return new ResponseEntity<>(newInternship, HttpStatus.CREATED);
    }



    @GetMapping("/getAssessment/internhip/{internshipid}")
    public ResponseEntity<?> getAssessment(@PathVariable Integer internshipid){
        Internships internship = this.internshipRepo.findById(internshipid).orElseThrow(() -> new ResourceNotFoundException("Internship", "InternshipId: ", internshipid));
        SubmissionDto submission = this.modelMapper.map(internship.submissionModel, SubmissionDto.class);
        return new ResponseEntity<SubmissionDto>(submission, HttpStatus.OK);
    }

    //Apply Internship can only be done by normal user
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PostMapping("/user/{Email}/internships/{internshipid}/apply")
    public ResponseEntity<?> applyInternship(@PathVariable String Email, @PathVariable Integer internshipid, @RequestBody SubmissionDto submissionDto) {
        Internships internships = this.internshipRepo.findById(internshipid).orElseThrow(()-> new ResourceNotFoundException("Internship", "internshipid", internshipid));
        if (internships.isActive()){
            SubmissionDto submissionDto1 = this.internshipServices.applyForInternship(Email, internshipid, submissionDto);
            return new ResponseEntity<>(submissionDto1, HttpStatus.OK);
        }
        else{
            return  new ResponseEntity<>(new ApiResponse("Internship is not active right now", false), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PutMapping("/internships/{internshipid}/pause")
    public ResponseEntity<?> pauseInternship(@PathVariable Integer internshipid){
        Internships internships = this.internshipRepo.findById(internshipid).orElseThrow(()-> new ResourceNotFoundException("Internship", "internshipid", internshipid));
        if(internships.isActive()){
            internships.setActive(false);
            this.internshipRepo.save(internships);
            return  new ResponseEntity<>(new ApiResponse("Internship has been paused", true), HttpStatus.OK);
        }
        else{
            internships.setActive(true);
            this.internshipRepo.save(internships);
            return  new ResponseEntity<>(new ApiResponse("Internship has been resumed", true), HttpStatus.OK);
        }
    }

    @GetMapping("/trendingInternships")
    public ResponseEntity<List<InternshipsDto>> getTrendingInternship (){
        ResponseEntity<List<InternshipsDto>> listResponseEntity = new ResponseEntity<>(userService.getAllTrendingInternship(), HttpStatus.OK);
        return listResponseEntity;
    }

    //View Submission Api
    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<SubmissionDto> getSubmission(@PathVariable Integer submissionId){
        SubmissionDto submissionDto = this.internshipServices.getSubmissionForm(submissionId);
        return new ResponseEntity<>(submissionDto, HttpStatus.OK);
    }



    //Delete Submisssion API
    @DeleteMapping("/submission/{submissionId}")
    public ResponseEntity<ApiResponse> deleteSubmission(@PathVariable Integer submissionId){
        String message = this.internshipServices.deleteSubmissionForm(submissionId);
        return new ResponseEntity<>(new ApiResponse(message, true), HttpStatus.OK);
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



    //Get a internship using internship Id
    @GetMapping("/getinternships/{internshipid}")
    public ResponseEntity<InternshipsDto> getiInternshipById(@PathVariable Integer internshipid){
        InternshipsDto internshipsDto  = this.internshipServices.getSingleInternship(internshipid);
        return new ResponseEntity<>(internshipsDto, HttpStatus.OK);
    }



    //Delete a internship
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @DeleteMapping("/internships/{internshipId}")
    public ApiResponse deleteInternship(@PathVariable Integer internshipId){
        this.internshipServices.deleteInternship(internshipId);
        return new ApiResponse("Internship is Successfully Deleted!!", true);
    }



    //Update an internship added by the host
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PutMapping("/internships/{internshipId}")
    public ResponseEntity<InternshipsDto> updateInternship(@PathVariable Integer internshipId, @RequestBody InternshipsDto internshipsDto){
        InternshipsDto updatedInternshipDto = this.internshipServices.updateInternship(internshipsDto, internshipId);
        return new ResponseEntity<>(updatedInternshipDto, HttpStatus.OK);
    }



    //Added the photo to the internship
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PostMapping("/internships/{internshipId}/setimage")
    public ResponseEntity<FileDto> setIntershipImage(@RequestParam("image") MultipartFile image, @PathVariable Integer internshipId){
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(()-> new ResourceNotFoundException("Internship", "Intership Id", internshipId));
        String filename = null;
        if (image.getContentType().equals("image/png")
                || image.getContentType().equals("image/jpg")
                || image.getContentType().equals("image/jpeg")) {
        try {
            filename = this.fileServices.uploadImage(path, image);
            internships.setImageUrl(filename);
            this.internshipRepo.save(internships);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileDto(filename, "Image not uploaded, Server error !!!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new FileDto(filename, "Image is Successfully Uploaded !!!"), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new FileDto(filename, "File is not of image type(JPEG/ JPG or PNG)!!!"), HttpStatus.BAD_REQUEST);
        }
    }



    //Get a list of all the internship  in a particular category
    @PostMapping("/category/{categoryid}/allinternships")
    public  ResponseEntity<InternshipResponse> getInternshipsByCategory(@PathVariable("categoryid") Integer categoryid, @RequestBody PageParam pageParam){
        InternshipResponse internshipResponse = this.internshipServices.getInternshipsByCategory(categoryid, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);
    }



    //Search internship using the title or the keyword contained by the title
    @PostMapping("/internships/search/{keywords}")
    public  ResponseEntity<InternshipResponse> searchInternshipByTitle(@PathVariable("keywords") String keywords, @RequestBody PageParam pageParam){
        InternshipResponse internshipResponse = this.internshipServices.searchInternships(keywords, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);

    }
//    @PostMapping("/internships/searchJPA/{keywords}")
//    public  ResponseEntity<InternshipResponse> searchInternshipByTitleQUERY(@PathVariable("keywords") String keywords, @RequestBody PageParam pageParam){
//        InternshipResponse internshipResponse = this.internshipServices.searchInternshipsJPA(keywords, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
//        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);
//
//    }



    //Get a list of all the user applicant of a particular internship
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    @PostMapping("/internships/appliedUser/{internnshipId}")
    public ResponseEntity<AppliedUserResponse> getAppliedUser(@PathVariable("internnshipId") Integer internnshipId, @RequestBody PageParam pageParam){
        AppliedUserResponse appliedUserResponse = this.internshipServices.searchUserByInternship(internnshipId, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(appliedUserResponse, HttpStatus.OK);
    }
}
