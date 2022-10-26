package com.Internfactory.Services;

import com.Internfactory.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service // Helps to autowired at-service helps to amke a component who's object can be autowired
public class UserService {
    List <User> list = new ArrayList<>();  //This list will store the user data

    // Fake data is created means our data is not connected with the database
    public UserService() {
        list.add(new User("XYZ@gmail.com", "Password"));
    }

    //Get All user
    public List<User> getAllUsers(){
        return this .list;  /// This will return all the entries in the list till now
    }

    //Get the single user
    public User getUser(String username){
        return this.list.stream().filter((user) -> user.getEmail().equals(username)).findAny().orElse(null);
    }  // This simply mean that if the username matches with the name entered than it will return it and filtered out it out of the array list

    //add new user
    public User addUser(User user){
        this.list.add(user);
        return user;
    }

}

