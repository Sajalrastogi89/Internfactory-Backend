package com.OurInternfactory.Repositories;

import com.OurInternfactory.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository <Role, Integer>{
}
