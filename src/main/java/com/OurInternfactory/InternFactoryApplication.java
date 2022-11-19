package com.OurInternfactory;

import com.OurInternfactory.Configs.AppConstants;
import com.OurInternfactory.Models.Role;
import com.OurInternfactory.Repositories.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
@ComponentScan({ "com.OurInternfactory.*" })
public class InternFactoryApplication implements CommandLineRunner{
    private final RoleRepo roleRepo;

    public InternFactoryApplication(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(InternFactoryApplication.class, args);
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Override
    public void run(String... args){
        try{
            Role role1 = new Role();
            role1.setId(AppConstants.ROLE_ADMIN);
            role1.setName("ROLE_ADMIN");

            Role role2 = new Role();
            role2.setId(AppConstants.ROLE_NORMAL);
            role2.setName("ROLE_NORMAL");

            Role role3 = new Role();
            role3.setId(AppConstants.ROLE_HOST);
            role3.setName("ROLE_HOST");

            List<Role> roles= List.of(role1, role2, role3);
            this.roleRepo.saveAll(roles);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}