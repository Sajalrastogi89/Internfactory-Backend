package com.OurInternfactory.Controllers;

import com.OurInternfactory.Payloads.CategoryDTO;
import com.OurInternfactory.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final UserService userService;
    public CategoryController(UserService userService) {
        this.userService = userService;
    }
    // Get ALL categories on the home page
    @GetMapping(value="/getCategory")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        return ResponseEntity.ok(this.userService.getAllCategory());
    }



    //Get trending categories
    @GetMapping(value="/getTrending")
    public ResponseEntity<List<CategoryDTO>> getAllTrendingCategories(){
        return ResponseEntity.ok(this.userService.getAllTrendingCategory());
    }
}