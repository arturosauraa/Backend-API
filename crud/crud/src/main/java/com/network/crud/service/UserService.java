package com.network.crud.service;

import com.network.crud.entity.AppUser;
import com.network.crud.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public AppUser save(AppUser appUser) {
        return userRepo.save(appUser);
    }

    //Retrieves a user by using its id
    public AppUser getAppUser(int id) {
        return userRepo.findById(id).get();
    }

    //Updates the user by using its id, so it can change the name, password or email
    public AppUser update(AppUser appUser) {
        AppUser appUser1 = userRepo.findById(appUser.getId()).get();
        appUser1.setName(appUser.getName());
        appUser1.setPassword(appUser.getPassword());
        appUser1.setEmail(appUser.getEmail());

        return userRepo.save(appUser1);

    }

    //uses the id to delete a user
    public String delete(int id) {
        userRepo.deleteById(id);
        return "Entity deleted" + id;
    }

    public UserRepo getUserRepo() {
        return userRepo;
    }

    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
}
