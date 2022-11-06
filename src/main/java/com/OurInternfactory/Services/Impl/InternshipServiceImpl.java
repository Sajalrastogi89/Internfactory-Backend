package com.OurInternfactory.Services.Impl;

import com.OurInternfactory.Exceptions.ResourceNotFoundException;
import com.OurInternfactory.Models.Category;
import com.OurInternfactory.Models.Internships;
import com.OurInternfactory.Models.User;
import com.OurInternfactory.Payloads.InternshipResponse;
import com.OurInternfactory.Payloads.InternshipsDto;
import com.OurInternfactory.Repositories.CategoryRepo;
import com.OurInternfactory.Repositories.InternshipRepo;
import com.OurInternfactory.Repositories.UserRepo;
import com.OurInternfactory.Services.InternshipServices;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InternshipServiceImpl implements InternshipServices {
    private final InternshipRepo internshipRepo;
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;

    private final CategoryRepo categoryRepo;

    public InternshipServiceImpl(InternshipRepo internshipRepo, ModelMapper modelMapper, UserRepo userRepo, CategoryRepo categoryRepo) {
        this.internshipRepo = internshipRepo;
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public InternshipsDto createInternship(InternshipsDto internshipsDto, Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryID", categoryId));
        Internships internships = this.modelMapper.map(internshipsDto, Internships.class);
        internships.setImageUrl("default.png");
        internships.setIssuedDate(new Date());
        internships.setCategory(category);
        category.getInternshipsList().add(internships);
        Internships newInternship = this.internshipRepo.save(internships);
        return this.modelMapper.map(newInternship, InternshipsDto.class);
    }
}
