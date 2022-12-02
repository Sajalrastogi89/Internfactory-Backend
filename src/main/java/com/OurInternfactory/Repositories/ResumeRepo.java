package com.OurInternfactory.Repositories;

import com.OurInternfactory.Models.Resume;
import com.OurInternfactory.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepo extends JpaRepository<Resume, Integer> {
    Resume findByUser(User user);
    Boolean existsResumeByUser(User User);
}
