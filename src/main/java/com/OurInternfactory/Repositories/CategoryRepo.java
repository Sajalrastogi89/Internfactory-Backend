package com.OurInternfactory.Repositories;

import com.OurInternfactory.Models.Category;
import com.OurInternfactory.Models.Internships;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Integer> {
//    Category findByInternships(Internships internships);
}
