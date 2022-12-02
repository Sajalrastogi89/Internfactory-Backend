package com.OurInternfactory.Repositories;

import com.OurInternfactory.Models.Internships;
import com.OurInternfactory.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);
    Page<User> findByInterships(Internships internships, Pageable pageable);
    List<User> findByInterships(Internships internships);
}
