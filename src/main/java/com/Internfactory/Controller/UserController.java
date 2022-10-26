package com.Internfactory.Controller;

import com.Internfactory.Repository.UserRepository;
import com.Internfactory.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController
{

    @Autowired
    UserRepository operate;
    User para;

    @PostMapping(path = "/insert")

    public String Insert(@Valid User param){
       String Email= param.getEmail().toLowerCase();
       param.setEmail(Email);
       boolean result= (operate.findById(Email).isEmpty());
       System.out.println(result);
        if(result){
            operate.save(param);
            return "inserted";
        }

        return "Already present";
    }
    @GetMapping(path = "/read/{Email}/{password}")

    public String readById(@PathVariable("Email") String email,@PathVariable("password") String Password) {

        Optional<User> a = operate.findById(email);
        para = a.get();
        if (para.getPassword().equals(Password))
            return "Found";
        else
            return "not found";
    }
}
