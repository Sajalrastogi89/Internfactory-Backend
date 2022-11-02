package com.OurInternfactory.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeatureController {

    @PostMapping("/AddCategory")
    public ResponseEntity<String> Category(){

        return ResponseEntity.ok("Done");
    }




}
