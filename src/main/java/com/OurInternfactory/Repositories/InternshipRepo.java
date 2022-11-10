package com.OurInternfactory.Repositories;

import com.OurInternfactory.Models.Category;
import com.OurInternfactory.Models.Internships;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipRepo extends JpaRepository<Internships, Integer> {
//    List<Internships> findByUser(User user);
    Page<Internships> findByCategory(Category category, Pageable pageable);
    Page<Internships> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Internships findByTitle(String title);
}
