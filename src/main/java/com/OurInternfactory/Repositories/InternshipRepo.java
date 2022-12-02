package com.OurInternfactory.Repositories;

import com.OurInternfactory.Models.Category;
import com.OurInternfactory.Models.Internships;
import com.OurInternfactory.Models.Submission;
import com.OurInternfactory.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipRepo extends JpaRepository<Internships, Integer> {
//    List<Internships> findByUser(User user);
    Page<Internships> findByCategory(Category category, Pageable pageable);
    Page<Internships> findByUserProvider(User user, Pageable pageable);
    Page<Internships> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Internships findBySubmissions(Submission submission);
}
