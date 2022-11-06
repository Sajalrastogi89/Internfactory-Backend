package com.OurInternfactory.Controllers;

import com.OurInternfactory.Payloads.CategoryDTO;
import com.OurInternfactory.Repositories.UserRepo;
import com.OurInternfactory.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final UserRepo userRepo;
    private final UserService userService;

    public CategoryController(UserRepo userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @GetMapping(value="/getCategory")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        return ResponseEntity.ok(this.userService.getAllCategory());

    }
    @GetMapping(value="/getTrending")
    public ResponseEntity<List<CategoryDTO>> getAllTrendingCategories(){
        System.out.println(1);
        return ResponseEntity.ok(this.userService.getAllTrendingCategory());

    }
    @PostMapping( "/category")
    public ResponseEntity<CategoryDTO> createUser(@RequestBody CategoryDTO catDTO){
        System.out.println(catDTO.getCategoryName());
        CategoryDTO createdUserDto = this.userService.AddData(catDTO);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }
}
