package com.OurInternfactory.Services;

import com.OurInternfactory.Models.User;
import com.OurInternfactory.Payloads.*;

import java.util.List;

public interface InternshipServices {
    InternshipsDto createInternship(InternshipAssessment internshipAssessment, Integer categoryId, String hostEmail);

    SubmissionDto applyForInternship(String Email, Integer internshipId, SubmissionDto submissionDto);
    SubmissionDto getSubmissionForm(Integer submissionId);
    String deleteSubmissionForm(Integer submissionId);

    InternshipsDto updateInternship(InternshipsDto internshipsDto, Integer internshipId);

    void deleteInternship(Integer internshipId);

    InternshipResponse getAllInternships(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    InternshipResponse getAllInternshipsHostedByUser(User user, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    InternshipResponse getAllTrendingInternship(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    InternshipsDto getSingleInternship(Integer internshipId);

    InternshipResponse getInternshipsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    SubmissionResponse getInternshipsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    InternshipResponse searchInternships(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
   // InternshipResponse searchInternshipsJPA(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    AppliedUserResponse searchUserByInternship(Integer internshipId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    //Search Internships
}
