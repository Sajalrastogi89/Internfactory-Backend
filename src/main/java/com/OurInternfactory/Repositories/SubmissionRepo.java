package com.OurInternfactory.Repositories;

import com.OurInternfactory.Models.Internships;
import com.OurInternfactory.Models.Submission;
import com.OurInternfactory.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepo extends JpaRepository<Submission, Integer> {
    Page<Submission> findByUser(User user, Pageable pageable);
    void deleteAllByInternships(Internships internships);
}
