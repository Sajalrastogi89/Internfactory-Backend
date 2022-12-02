package com.OurInternfactory.Controllers;

import com.OurInternfactory.Payloads.ApiResponse;
import com.OurInternfactory.Payloads.ResumeDTO;
import com.OurInternfactory.Security.JwtTokenHelper;
import com.OurInternfactory.Services.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ResumeController {
    private final ResumeService resumeService;
    private final JwtTokenHelper jwtTokenHelper;
    public ResumeController(ResumeService resumeService, JwtTokenHelper jwtTokenHelper) {
        this.resumeService = resumeService;
        this.jwtTokenHelper = jwtTokenHelper;
    }
//get the resume using the email
    @GetMapping("/getResume")
    public ResponseEntity<?> getResume(@RequestHeader("Authorization") String bearerToken){
        try {
            ResumeDTO resumeDTO = this.resumeService.getUserResume(this.jwtTokenHelper.getUsernameFromToken(bearerToken.substring(7)));
            return new ResponseEntity<>(resumeDTO, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(new ApiResponse("No Resume Found", false), HttpStatus.NOT_FOUND);
        }
    }
//Edit the resume by the user
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @PostMapping("/editResume")
    public ResponseEntity<ResumeDTO> setResume(@RequestBody ResumeDTO editResumeDto, @RequestHeader("Authorization") String bearerToken) {
        ResumeDTO UpdatedResume = this.resumeService.setUserResume(this.jwtTokenHelper.getUsernameFromToken(bearerToken.substring(7)), editResumeDto);
        return new ResponseEntity<>(UpdatedResume, HttpStatus.OK);
    }
//Delete the resume by the user
    @PreAuthorize("hasAnyRole('NORMAL', 'ADMIN')")
    @DeleteMapping("/deleteResume")
    public ResponseEntity<ApiResponse> deleteResume(@RequestHeader("Authorization") String bearerToken) {
        String message = this.resumeService.deleteUserResume(this.jwtTokenHelper.getUsernameFromToken(bearerToken.substring(7)));
        return new ResponseEntity<>(new ApiResponse(message, true), HttpStatus.OK);
    }
}
