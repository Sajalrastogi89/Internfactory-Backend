package com.OurInternfactory.Controllers;

import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Internships;
import com.OurInternfactory.Payloads.ApiResponse;
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
import java.util.List;

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
}
