package com.OurInternfactory.Controllers;

import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Internships;
import com.OurInternfactory.Payloads.*;
import com.OurInternfactory.Repositories.InternshipRepo;
import com.OurInternfactory.Services.FileServices;
import com.OurInternfactory.Services.InternshipServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
public class InternshipController {
    private final InternshipServices internshipServices;
    private final InternshipRepo internshipRepo;
    private final FileServices fileServices;
    @Value("${project.image}")
    private String path;
    public InternshipController(InternshipServices internshipServices, InternshipRepo internshipRepo, FileServices fileServices) {
        this.internshipServices = internshipServices;
        this.internshipRepo = internshipRepo;
        this.fileServices = fileServices;
    }
    //    create
//    @PreAuthorize("hasRole('HOST')")
    @PostMapping("/category/{categoryid}/internships")
    public ResponseEntity<InternshipsDto> createInternship(@RequestBody InternshipsDto internshipsDto, @PathVariable Integer categoryid){
        InternshipsDto newInternship = this.internshipServices.createInternship(internshipsDto, categoryid);
        return new ResponseEntity<>(newInternship, HttpStatus.CREATED);
    }
    @PostMapping("/user/{Email}/internships/{internshipid}/apply")
    public ResponseEntity<SubmissionDto> applyInternship(@PathVariable String Email, @PathVariable Integer internshipid, @RequestBody SubmissionDto submissionDto){
        SubmissionDto submissionDto1 = this.internshipServices.applyForInternship(Email, internshipid, submissionDto);
        return new ResponseEntity<>(submissionDto1, HttpStatus.OK);
    }

    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<SubmissionDto> getSubmission(@PathVariable Integer submissionId){
        SubmissionDto submissionDto = this.internshipServices.getSubmissionForm(submissionId);
        return new ResponseEntity<>(submissionDto, HttpStatus.OK);
    }
    @DeleteMapping("/submission/{submissionId}")
    public ResponseEntity<ApiResponse> deleteSubmission(@PathVariable Integer submissionId){
        String message = this.internshipServices.deleteSubmissionForm(submissionId);
        return new ResponseEntity<>(new ApiResponse(message, true), HttpStatus.OK);
    }

    @PostMapping("/user/{userid}/internships")
    public ResponseEntity<SubmissionResponse> getInternshipsByUser(@PathVariable Integer userid, @RequestBody PageParam pageParam){
        SubmissionResponse internshipResponse  = this.internshipServices.getInternshipsByUser(userid, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);
    }
    @PostMapping("/internships")
    public ResponseEntity<InternshipResponse> getAllInternships(@RequestBody PageParam pageParam){

        InternshipResponse internshipResponse  = this.internshipServices.getAllInternships(pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);
    }
    @GetMapping("/internships/{internshipid}")
    public ResponseEntity<InternshipsDto> getiInternshipById(@PathVariable Integer internshipid){
        InternshipsDto internshipsDto  = this.internshipServices.getSingleInternship(internshipid);
        return new ResponseEntity<>(internshipsDto, HttpStatus.OK);
    }
    @DeleteMapping("/internships/{internshipId}")
    public ApiResponse deleteInternship(@PathVariable Integer internshipId){
        this.internshipServices.deleteInternship(internshipId);
        return new ApiResponse("Internship is Successfully Deleted!!", true);
    }
    @PutMapping("/internships/{internshipId}")
    public ResponseEntity<InternshipsDto> updateInternship(@PathVariable Integer internshipId, @RequestBody InternshipsDto internshipsDto){
        InternshipsDto updatedInternshipDto = this.internshipServices.updateInternship(internshipsDto, internshipId);
        return new ResponseEntity<>(updatedInternshipDto, HttpStatus.OK);
    }

    @PostMapping("/internships/{internshipId}/setimage")
    public ResponseEntity<FileDto> setIntershipImage(@RequestParam("image") MultipartFile image, @PathVariable Integer internshipId){
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(()-> new ResourceNotFoundException("Internship", "Intership Id", internshipId));
        String filename = null;
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

    @PostMapping("/category/{categoryid}/allinternships")
    public  ResponseEntity<InternshipResponse> getInternshipsByCategory(@PathVariable("categoryid") Integer categoryid, @RequestBody PageParam pageParam){
        InternshipResponse internshipResponse = this.internshipServices.getInternshipsByCategory(categoryid, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);
    }

    @PostMapping("/internships/search/{keywords}")
    public  ResponseEntity<InternshipResponse> searchInternshipByTitle(@PathVariable("keywords") String keywords, @RequestBody PageParam pageParam){
        InternshipResponse internshipResponse = this.internshipServices.searchInternships(keywords, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(internshipResponse, HttpStatus.OK);

    }
    @PostMapping("/internships/appliedUser/{internnshipId}")
    public ResponseEntity<AppliedUserResponse> getAppliedUser(@PathVariable("internnshipId") Integer internnshipId, @RequestBody PageParam pageParam){
        AppliedUserResponse appliedUserResponse = this.internshipServices.searchUserByInternship(internnshipId, pageParam.getPageNumber(), pageParam.getPageSize(), pageParam.getSortBy(), pageParam.getSortDir());
        return new ResponseEntity<>(appliedUserResponse, HttpStatus.OK);
    }
}
