package com.OurInternfactory.Controllers;

import com.OurInternfactory.Payloads.ApiResponse;
import com.OurInternfactory.Payloads.EditResumeDto;
import com.OurInternfactory.Payloads.ForgetEmail;
import com.OurInternfactory.Payloads.ResumeDTO;
import com.OurInternfactory.Services.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/getResume")
    public ResponseEntity<ResumeDTO> getResume(@RequestBody ForgetEmail forgetEmail){
        ResumeDTO resumeDTO = this.resumeService.getUserResume(forgetEmail.getEmail());
        return new ResponseEntity<>(resumeDTO, HttpStatus.OK);
    }

    @PutMapping("/editResume")
    public ResponseEntity<ResumeDTO> setResume(@RequestBody EditResumeDto editResumeDto){
        ResumeDTO UpdatedResume = this.resumeService.setUserResume(editResumeDto.getEmail(), editResumeDto.getResumeDTO());
        return new ResponseEntity<>(UpdatedResume, HttpStatus.OK);
    }
    @DeleteMapping("/deleteResume")
    public ResponseEntity<ApiResponse> deleteResume(@RequestBody ForgetEmail forgetEmail){
        String message = this.resumeService.deleteUserResume(forgetEmail.getEmail());
        return new ResponseEntity<>(new ApiResponse(message, true), HttpStatus.OK);
    }

}
