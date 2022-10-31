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
//		System.out.println(this.passwordEncoder.encode("sajalRastogi@123"));
        try{
            Role role1 = new Role();
            role1.setId(AppConstants.ADMIN_USER);
            role1.setName("ADMIN_USER");

            Role role2 = new Role();
            role2.setId(AppConstants.NORMAL_USER);
            role2.setName("NORMAL_USER");

            List<Role> roles= List.of(role1, role2);
            this.roleRepo.saveAll(roles);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
