package com.OurInternfactory.Services;

import com.OurInternfactory.Models.Internships;
import com.OurInternfactory.Payloads.InternshipResponse;
import com.OurInternfactory.Payloads.InternshipsDto;

import java.util.List;

public interface InternshipServices {
    InternshipsDto createInternship(InternshipsDto internshipsDto, Integer categoryId);

    String applyForInternship(String email, String Title);

    InternshipsDto updateInternship(InternshipsDto internshipsDto, Integer internshipId);

    void deleteInternship(Integer internshipId);

    InternshipResponse getAllInternships(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    InternshipsDto getSingleInternship(Integer internshipId);

    InternshipResponse getInternshipsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    InternshipResponse getInternshipsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    InternshipResponse searchInternships(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    //Search Internships
}
