package com.OurInternfactory.Controllers;

import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Internships;
import com.OurInternfactory.Models.User;
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
        return new ResponseEntity<InternshipsDto>(newInternship, HttpStatus.CREATED);
    }
    @PostMapping("/user/{userEmail}/internships/apply")
    public ResponseEntity<ApiResponse> applyInternship(@PathVariable String userEmail, @RequestBody ApplyInternship applyInternship){
        String message = this.internshipServices.applyForInternship(userEmail, applyInternship.getTitle());
        ApiResponse apiResponse = new ApiResponse(message, true);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/user/{userid}/internships")
    public ResponseEntity<InternshipResponse> getInternshipsByUser(@PathVariable Integer userid,
                                                                   @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                   @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                   @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                                   @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        InternshipResponse internshipResponse  = this.internshipServices.getInternshipsByUser(userid, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<InternshipResponse>(internshipResponse, HttpStatus.OK);
    }
    @GetMapping("/internships")
    public ResponseEntity<InternshipResponse> getAllInternships(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){

        InternshipResponse internshipResponse  = this.internshipServices.getAllInternships(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<InternshipResponse>(internshipResponse, HttpStatus.OK);
    }
    @GetMapping("/internships/{internshipid}")
    public ResponseEntity<InternshipsDto> getiInternshipById(@PathVariable Integer internshipid){
        InternshipsDto internshipsDto  = this.internshipServices.getSingleInternship(internshipid);
        return new ResponseEntity<InternshipsDto>(internshipsDto, HttpStatus.OK);
    }
    @DeleteMapping("/internships/{internshipId}")
    public ApiResponse deleteInternship(@PathVariable Integer internshipId){
        this.internshipServices.deleteInternship(internshipId);
        return new ApiResponse("Internship is Successfully Deleted!!", true);
    }
    @PutMapping("/internships/{internshipId}")
    public ResponseEntity<InternshipsDto> updateInternship(@PathVariable Integer internshipId, @RequestBody InternshipsDto internshipsDto){
        InternshipsDto updatedInternshipDto = this.internshipServices.updateInternship(internshipsDto, internshipId);
        return new ResponseEntity<InternshipsDto>(updatedInternshipDto, HttpStatus.OK);
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

    @GetMapping("/category/{categoryid}/internships")
    public  ResponseEntity<InternshipResponse> getInternshipsByCategory(@PathVariable Integer categoryid,
                                                                        @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                        @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                        @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                                        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        InternshipResponse internshipResponse = this.internshipServices.getInternshipsByCategory(categoryid, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<InternshipResponse>(internshipResponse, HttpStatus.OK);
    }

    @GetMapping("/internships/search/{keywords}")
    public  ResponseEntity<InternshipResponse> searchInternshipByTitle(@PathVariable("keywords") String keywords,
                                                                       @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                       @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                                       @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        InternshipResponse internshipResponse = this.internshipServices.searchInternships(keywords, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<InternshipResponse>(internshipResponse, HttpStatus.OK);

    }

    @GetMapping(value = "/internships/image/{internshipId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getInternshipPhoto(@PathVariable Integer internshipId, HttpServletResponse response) throws IOException {
        Internships internships = this.internshipRepo.findById(internshipId).orElseThrow(() -> new ResourceNotFoundException("Internship", "InternshipID :"+internshipId, 0));
        InputStream resource = this.fileServices.getImage(path, internships.getImageUrl());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
