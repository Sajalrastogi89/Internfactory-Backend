package com.Internfactory.Repository;

import com.Internfactory.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,String> {
}
