package com.OurInternfactory.Repositories;

import com.OurInternfactory.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Integer> {
}
