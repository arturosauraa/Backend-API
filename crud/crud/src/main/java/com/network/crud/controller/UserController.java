package com.network.crud.controller;

import com.network.crud.entity.AppUser;
import com.network.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //Add a user to the database
    @PostMapping
    public AppUser addUser(@RequestBody AppUser appUser) {
        return userService.save(appUser);
    }

    //Retrieves a user from the database
    //To identify the user that you want to retrieve, you must know its "id"
    @GetMapping(path = "/{id}")
    public AppUser getAppUser(@PathVariable int id) {
        return userService.getAppUser(id);
    }

    //Updates a user from the database
    //To identify the user that you want to update, you must know its "id"
    @PutMapping
    public AppUser updateAppUser(@RequestBody AppUser appUser) {
        return userService.update(appUser);
    }

    //Deletes a user from the database
    //To identify the user that you want to delete, you must know its "id"
    @DeleteMapping(path = "/{id}")
    public String deleteAppUser(@PathVariable int id) {
        return userService.delete(id);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
