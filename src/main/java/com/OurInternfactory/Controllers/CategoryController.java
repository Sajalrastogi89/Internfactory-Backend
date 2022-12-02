package com.OurInternfactory.Controllers;

import com.OurInternfactory.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final UserService userService;
    public CategoryController(UserService userService) {
        this.userService = userService;
    }
//Get trending categories
    @GetMapping(value="/getTrending")
    public ResponseEntity<?> getAllTrendingCategories(){
        return ResponseEntity.ok(this.userService.getAllTrendingCategory());
    }
// Get ALL categories on the home page
    @GetMapping(value="/getCategory")
    public ResponseEntity<?> getAllCategories(){
        return ResponseEntity.ok(this.userService.getAllCategory());
    }
}
